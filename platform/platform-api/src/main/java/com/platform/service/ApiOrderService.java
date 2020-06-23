package com.platform.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.platform.util.ApiBaseAction;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.platform.cache.J2CacheUtils;
import com.platform.dao.ApiAddressMapper;
import com.platform.dao.ApiCartMapper;
import com.platform.dao.ApiCouponMapper;
import com.platform.dao.ApiOrderGoodsMapper;
import com.platform.dao.ApiOrderMapper;
import com.platform.entity.AddressVo;
import com.platform.entity.BuyGoodsVo;
import com.platform.entity.CartVo;
import com.platform.entity.CouponVo;
import com.platform.entity.OrderGoodsVo;
import com.platform.entity.OrderVo;
import com.platform.entity.ProductVo;
import com.platform.entity.UserVo;
import com.platform.util.CommonUtil;

import javax.sql.DataSource;


@Service
public class ApiOrderService extends ApiBaseAction {
    @Autowired
    private ApiOrderMapper orderDao;
    @Autowired
    private ApiAddressMapper apiAddressMapper;
    @Autowired
    private ApiCartMapper apiCartMapper;
    @Autowired
    private ApiCouponMapper apiCouponMapper;
    @Autowired
    private ApiOrderMapper apiOrderMapper;
    @Autowired
    private ApiOrderGoodsMapper apiOrderGoodsMapper;
    @Autowired
    private ApiProductService productService;

    public OrderVo queryObjectByOrderSn(String orderSn) {
        return orderDao.queryObjectByOrderSn(orderSn);
    }

    public OrderVo queryObject(Integer id) {
        return orderDao.queryObject(id);
    }


    public List<OrderVo> queryList(Map<String, Object> map) {
        return orderDao.queryList(map);
    }


    public int queryTotal(Map<String, Object> map) {
        return orderDao.queryTotal(map);
    }


    public void save(OrderVo order) {
        orderDao.save(order);
    }


    public int update(OrderVo order) {
        return orderDao.update(order);
    }


    public void delete(Integer id) {
        orderDao.delete(id);
    }


    public void deleteBatch(Integer[] ids) {
        orderDao.deleteBatch(ids);
    }


    @Transactional
    public Map<String, Object> submit(JSONObject jsonParam, UserVo loginUser) {
        Map<String, Object> resultObj = new HashMap<String, Object>();

        Integer couponId = null;//jsonParam.getInteger("couponId");
        //如果是拼单type=2
        String type = jsonParam.getString("type");
        String postscript = jsonParam.getString("postscript");
//        AddressVo addressVo = jsonParam.getObject("checkedAddress",AddressVo.class);
        String currentId = jsonParam.getString("currentId");
        String note = jsonParam.getString("note");

//        AddressVo addressVo = apiAddressMapper.queryObject(jsonParam.getInteger("addressId"));


        BigDecimal freightPrice = new BigDecimal(0.00);

        // * 获取要购买的商品
        List<CartVo> checkedGoodsList = new ArrayList<>();
        BigDecimal goodsTotalPrice;
        if (type.equals("cart")) {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("user_id", loginUser.getUserId());
            param.put("session_id", 1);
            param.put("checked", 1);
            checkedGoodsList = apiCartMapper.queryList(param);
            if (null == checkedGoodsList) {
                resultObj.put("errno", 400);
                resultObj.put("errmsg", "请选择商品");
                return resultObj;
            }
            //统计商品总价
            goodsTotalPrice = new BigDecimal(0.00);
            for (CartVo cartItem : checkedGoodsList) {
                //取得规格的信息,判断规格库存
                ProductVo productInfo3 = productService.queryObject(cartItem.getProduct_id());
                if (null == productInfo3 || productInfo3.getGoods_number() < cartItem.getNumber()) {
                    HashMap hashMap = new HashMap<>();
                    hashMap.put("errno","400");
                    hashMap.put("errmsg",cartItem.getGoods_name()+"库存不足");
                    return hashMap;
                }
                if("0".equals(productInfo3.getIs_on_sale())){
                    HashMap hashMap = new HashMap<>();
                    hashMap.put("errno","400");
                    hashMap.put("errmsg",cartItem.getGoods_name()+"已下级");
                    return hashMap;
                }
                goodsTotalPrice = goodsTotalPrice.add(cartItem.getRetail_price().multiply(new BigDecimal(cartItem.getNumber())));
            }
        } else {
            BuyGoodsVo goodsVo = (BuyGoodsVo) J2CacheUtils.get(J2CacheUtils.SHOP_CACHE_NAME, "goods" + loginUser.getUserId());
            ProductVo productInfo = productService.queryObject(goodsVo.getProductId());
            //取得规格的信息,判断规格库存
            if (null == productInfo || productInfo.getGoods_number() < goodsVo.getNumber()) {
                HashMap hashMap = new HashMap<>();
                hashMap.put("errno","400");
                hashMap.put("errmsg",productInfo.getGoods_name()+"库存不足");
                return hashMap;
            }
            if("0".equals(productInfo.getIs_on_sale())){
                HashMap hashMap = new HashMap<>();
                hashMap.put("errno","400");
                hashMap.put("errmsg",productInfo.getGoods_name()+"已下架");
                return hashMap;
            }
            //计算订单的费用
            //商品总价
            goodsTotalPrice = productInfo.getRetail_price().multiply(new BigDecimal(goodsVo.getNumber()));

            CartVo cartVo = new CartVo();
            BeanUtils.copyProperties(productInfo, cartVo);
            cartVo.setNumber(goodsVo.getNumber());
            cartVo.setProduct_id(goodsVo.getProductId());
            checkedGoodsList.add(cartVo);
        }

        //校验商品库存begin
//        for(CartVo cartVo : checkedGoodsList){
//            cartVo.getGoods_id();
//            cartVo.getNumber();
//        }
        //校验商品库存end


        //获取订单使用的优惠券
        BigDecimal couponPrice = new BigDecimal(0.00);
        CouponVo couponVo = null;
        if (couponId != null && couponId != 0) {
            couponVo = apiCouponMapper.getUserCoupon(couponId);
            if (couponVo != null && couponVo.getCoupon_status() == 1) {
                couponPrice = couponVo.getType_money();
            }
        }


        if(goodsTotalPrice.compareTo(new BigDecimal(200.00))<0){
            freightPrice=new BigDecimal(10.00);
        }

        //订单价格计算
        BigDecimal orderTotalPrice = goodsTotalPrice.add(freightPrice); //订单的总价

        BigDecimal actualPrice = orderTotalPrice.subtract(couponPrice);  //减去其它支付的金额后，要实际支付的金额


        //
        OrderVo orderInfo = new OrderVo();
        String prefix = "";
        if ("cart".equals(type)){
            prefix="YTC";
        } else if ("buy".equals(type)){
            prefix="YTB";
        } else if ("2".equals(type)){
            prefix="YTP";
        }else{
            prefix="YT";
        }

        orderInfo.setOrder_sn(prefix+loginUser.getUserId()+"D"+CommonUtil.generateOrderNumber());
        orderInfo.setUser_id(loginUser.getUserId());
        orderInfo.setTake_way(currentId);
        //收货地址和运费
        if("1".equals(currentId)){
            orderInfo.setConsignee(jsonParam.getString("name"));
            orderInfo.setMobile(jsonParam.getString("mobile"));
            orderInfo.setCountry("到店自提");
            orderInfo.setProvince("到店自提");
            orderInfo.setCity("到店自提");
            orderInfo.setDistrict("到店自提");
            orderInfo.setAddress("深圳市罗湖区深南东路中建大厦1712");
        }else{
            orderInfo.setConsignee(jsonParam.getString("name"));
            orderInfo.setMobile(jsonParam.getString("mobile"));
            orderInfo.setCountry(jsonParam.getString("中国"));
            orderInfo.setProvince("");
            orderInfo.setCity("");
            orderInfo.setDistrict("");
            orderInfo.setAddress(jsonParam.getString("address"));
        }
        //
        orderInfo.setFreight_price(freightPrice.intValue());
        //留言
        orderInfo.setPostscript(postscript);
        //使用的优惠券
        orderInfo.setCoupon_id(couponId);
        orderInfo.setCoupon_price(couponPrice);
        orderInfo.setAdd_time(new Date());
        orderInfo.setGoods_price(goodsTotalPrice);
        orderInfo.setOrder_price(orderTotalPrice);
        orderInfo.setActual_price(actualPrice);
        // 待付款
        orderInfo.setOrder_status(0);
        orderInfo.setShipping_status(0);
        orderInfo.setPay_status(0);
        orderInfo.setShipping_id(0);
        orderInfo.setShipping_fee(new BigDecimal(0));
        orderInfo.setIntegral(0);
        orderInfo.setIntegral_money(new BigDecimal(0));
        if (type.equals("cart")) {
            orderInfo.setOrder_type("1");
        } else {
            if("2".equals(type)){
                orderInfo.setOrder_type("2");
            }else{
                orderInfo.setOrder_type("4");
            }
        }

        orderInfo.setNote(note);
        //开启事务，插入订单信息和订单商品
        apiOrderMapper.save(orderInfo);
        if (null == orderInfo.getId()) {
            resultObj.put("errno", 1);
            resultObj.put("errmsg", "订单提交失败");
            return resultObj;
        }
        //统计商品总价
        List<OrderGoodsVo> orderGoodsData = new ArrayList<OrderGoodsVo>();
        for (CartVo goodsItem : checkedGoodsList) {
            OrderGoodsVo orderGoodsVo = new OrderGoodsVo();
            orderGoodsVo.setOrder_id(orderInfo.getId());
            orderGoodsVo.setGoods_id(goodsItem.getGoods_id());
            orderGoodsVo.setGoods_sn(String.valueOf(goodsItem.getGoods_id()));
            orderGoodsVo.setProduct_id(goodsItem.getProduct_id());
            orderGoodsVo.setGoods_name(goodsItem.getGoods_name());
            orderGoodsVo.setList_pic_url(goodsItem.getList_pic_url());
            orderGoodsVo.setMarket_price(goodsItem.getMarket_price());
            orderGoodsVo.setRetail_price(goodsItem.getRetail_price());
            orderGoodsVo.setNumber(goodsItem.getNumber());
            orderGoodsVo.setGoods_specifition_name_value(goodsItem.getGoods_specifition_name_value());
            orderGoodsVo.setGoods_specifition_ids(goodsItem.getGoods_specifition_ids());
            orderGoodsData.add(orderGoodsVo);
            apiOrderGoodsMapper.save(orderGoodsVo);
        }

        //清空已购买的商品
        apiCartMapper.deleteByCart(loginUser.getUserId(), 1, 1);
        resultObj.put("errno", 0);
        resultObj.put("errmsg", "订单提交成功");
        //
        Map<String, OrderVo> orderInfoMap = new HashMap<String, OrderVo>();
        orderInfoMap.put("orderInfo", orderInfo);
        //
        resultObj.put("data", orderInfoMap);
        // 优惠券标记已用
        if (couponVo != null && couponVo.getCoupon_status() == 1) {
            couponVo.setCoupon_status(2);
            apiCouponMapper.updateUserCoupon(couponVo);
        }

        return resultObj;
    }

}
