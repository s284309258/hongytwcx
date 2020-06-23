package com.platform.service;

import com.alibaba.fastjson.JSONObject;
import com.platform.dao.ApiOrderMapper;
import com.platform.entity.OrderVo;
import com.platform.entity.UserVo;
import com.platform.util.AESUtils;
import com.platform.util.ApiBaseAction;
import com.platform.util.CommonUtil;
import com.platform.util.Sqlca;
import com.platform.util.wechat.WechatRefundApiResult;
import com.platform.util.wechat.WechatUtil;
import com.platform.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

@Service
public class ApiMyCenterService extends ApiBaseAction {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ApiOrderService orderService;

    @Autowired
    private ApiOrderGoodsService orderGoodsService;

    @Autowired
    private ApiUserService userService;

    @Autowired
    private ApiOrderMapper apiOrderMapper;

    public Object getMyCenterInfo(Long user_id){
        Map<String, Object> resultObj = new HashMap<String, Object>();

        try{
            ArrayList bankInfo = Sqlca.getArrayListFromMap("select * from bank_account where user_id="+user_id,dataSource.getConnection());
            if(bankInfo.size()>0){
                resultObj.put("bankInfo",bankInfo.get(0));
            }

            ArrayList<Map<String,Object>> levelInfo = Sqlca.getArrayListFromMap("select t.mobile,t.user_level_id," +
                    " (case when user_level_id=1 then '懂事' when user_level_id=2 then '合伙人' when user_level_id=3 then '代理' when user_level_id=4 then '会员' end) as user_level_name," +
                    " t.task_num as currentLevel,tt.task_num as maxLevel,t.nickname,ttt.balance," +
                    " ttt.withdraw_money,total_money,t.avatar from nideshop_user t,nideshop_user_level tt,bank_account ttt " +
                    " where t.user_level_id=tt.id and t.id=ttt.user_id and t.id="+user_id,dataSource.getConnection());
            if(levelInfo.size()>0){
                Map<String,Object> obj = levelInfo.get(0);
                resultObj.put("levelInfo",obj);
                if("4".equals(obj.get("user_level_id"))){
                    String cnt = Sqlca.getString(dataSource.getConnection(),"select count(*) as cnt from nideshop_user where huiyuan_belong="+user_id);
                    resultObj.put("teamScale",cnt);
                    String saleScale = Sqlca.getString(dataSource.getConnection(),"select IFNULL(sum(goods_total_pay),0) as saleScale from consumer_record where huiyuanzhishu_belong="+user_id);
                    resultObj.put("saleScale",saleScale);
                }else if("3".equals(obj.get("user_level_id"))){
                    String cnt = Sqlca.getString(dataSource.getConnection(),"select count(*) as cnt from nideshop_user where daili_belong="+user_id);
                    resultObj.put("teamScale",cnt);
                    String saleScale = Sqlca.getString(dataSource.getConnection(),"select IFNULL(sum(goods_total_pay),0) as saleScale from consumer_record where daili_belong="+user_id+
                            " or huiyuanzhishu_belong="+user_id);
                    resultObj.put("saleScale",saleScale);
                }else if("2".equals(obj.get("user_level_id"))){
                    String cnt = Sqlca.getString(dataSource.getConnection(),"select count(*) as cnt from nideshop_user where hehuoren_belong="+user_id);
                    resultObj.put("teamScale",cnt);
                    String saleScale = Sqlca.getString(dataSource.getConnection(),"select IFNULL(sum(goods_total_pay),0) as saleScale from consumer_record where hehuoren_belong="+user_id+
                            " or huiyuanzhishu_belong="+user_id);
                    resultObj.put("saleScale",saleScale);
                }else if("1".equals(obj.get("user_level_id"))){
                    String cnt = Sqlca.getString(dataSource.getConnection(),"select count(*) as cnt from nideshop_user where dongshi_belong="+user_id);
                    resultObj.put("teamScale",cnt);
                    String saleScale = Sqlca.getString(dataSource.getConnection(),"select IFNULL(sum(goods_total_pay),0) as saleScale from consumer_record where dongshi_belong="+user_id+
                            " or huiyuanzhishu_belong="+user_id);
                    resultObj.put("saleScale",saleScale);
                }

                ArrayList<Map<String,Object>> userBelong = Sqlca.getArrayListFromMap("select *,(case when user_level_id=1 then '懂事' when user_level_id=2 then '合伙人' when user_level_id=3 then '代理' when user_level_id=4 then '会员' end) as user_level_name" +
                        " from nideshop_user where reference_id="+user_id,dataSource.getConnection());
                resultObj.put("userBelong",userBelong);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return resultObj;
    }

    public Object consumerRecordByUserID(UserVo loginUser,Map<String,Object> map){
        Map<String, Object> resultObj = new HashMap<String, Object>();
        try{
            ArrayList<Map<String,Object>> arrayList = Sqlca.getArrayListFromMap("select * from consumer_record where huiyuan_id="+map.get("id"),dataSource.getConnection());
            resultObj.put("consumerRecord",arrayList);
        }catch (Exception e){
            e.printStackTrace();
        }

        return resultObj;
    }

    public Object bankInfoByID(UserVo loginUser,Map<String,Object> map){
        Map<String, Object> resultObj = new HashMap<String, Object>();
        try{
            ArrayList<Map<String,Object>> arrayList = Sqlca.getArrayListFromMap("select * from bank_account where user_id="+map.get("id"),dataSource.getConnection());
            resultObj.put("bankInfo",arrayList);
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultObj;
    }

    public String withdrawApply(UserVo loginUser,Map<String,Object> map){
        try{
            ArrayList<Map<String,Object>> arrayList =  Sqlca.getArrayListFromMap("select * from bank_account where user_id="+loginUser.getUserId(),dataSource.getConnection());

            if(arrayList.size()>0){
                Map mm = arrayList.get(0);
                double balance = Double.parseDouble(mm.get("balance").toString());
                double apply_withdraw_money = Double.parseDouble(map.get("apply_withdraw_money").toString());
                if(apply_withdraw_money==0){
                    return "申请提交金额必须大于0元";
                }

                if(apply_withdraw_money>balance){
                    return "申请提交金额不能大于余额";
                }

                String applyCnt = Sqlca.getString(dataSource.getConnection(),"select count(*) as cnt from withdraw_apply where state='applying' and user_id="+loginUser.getUserId());

                if(Integer.parseInt(applyCnt)>0){
                    return "已经有一笔待处理的申请";
                }

                Sqlca.updateObject(dataSource.getConnection(),"update nideshop_user set mobile=? where id="+loginUser.getUserId(),new String[]{
                        map.get("mobile").toString()
                });

                Sqlca.updateObject(dataSource.getConnection(),"update bank_account set mobile=?,bank_account_name=?,bank_account=?,bank_name=? where user_id="+loginUser.getUserId(),
                        new String[]{map.get("mobile").toString(),map.get("bank_account_name").toString(),map.get("bank_account").toString(),map.get("bank_name").toString()});

                Sqlca.updateObject(dataSource.getConnection(),"insert into withdraw_apply(user_id,bank_account_id,apply_withdraw_money,state,apply_date) " +
                        "values("+loginUser.getUserId()+",?,?,'applying',CURRENT_DATE)",new String[]{mm.get("id").toString(),String.valueOf(apply_withdraw_money)});
            }else{
                return "未授权登陆用户";
            }
        }catch (Exception e){
            e.printStackTrace();
            return "fail";
        }
        return "success";
    }

    public Object prePay(UserVo loginUser, Integer orderId){
        //
        OrderVo orderInfo = orderService.queryObject(orderId);

        if (null == orderInfo) {
            return toResponsObject(400, "订单已取消", "");
        }

        if (orderInfo.getPay_status() != 0) {
            return toResponsObject(400, "订单未付款，请重新下单", "");
        }

        String nonceStr = CharUtil.getRandomString(32);

        //https://pay.weixin.qq.com/wiki/doc/api/wxa/wxa_api.php?chapter=7_7&index=3
        Map<Object, Object> resultObj = new TreeMap();

        try {
            Map<Object, Object> parame = new TreeMap<Object, Object>();
            parame.put("appid", ResourceUtil.getConfigByName("wx.appId"));
            // 商家账号。
            parame.put("mch_id", ResourceUtil.getConfigByName("wx.mchId"));
            String randomStr = CharUtil.getRandomNum(18).toUpperCase();
            // 随机字符串
            parame.put("nonce_str", randomStr);
            // 商户订单编号
            parame.put("out_trade_no", orderInfo.getOrder_sn());
            Map orderGoodsParam = new HashMap();
            orderGoodsParam.put("order_id", orderId);
            // 商品描述
            parame.put("body", "红樱桃商城购买会员");
            //支付金额
            parame.put("total_fee", orderInfo.getActual_price().multiply(new BigDecimal(100)).intValue());
            // 回调地址  zfpay.mylcwl.com:28080
            parame.put("notify_url", "https://www.mtmzg.cn/api/center/notify");
            //parame.put("notify_url", "https://zfpay.mylcwl.com:1443/platform/api/center/notify");
            // 交易类型APP
            parame.put("trade_type", ResourceUtil.getConfigByName("wx.tradeType"));
            parame.put("spbill_create_ip", getClientIp());
            parame.put("openid", loginUser.getWeixin_openid());
            String sign = WechatUtil.arraySign(parame, ResourceUtil.getConfigByName("wx.paySignKey"));
            // 数字签证
            parame.put("sign", sign);

            String xml = MapUtils.convertMap2Xml(parame);
            logger.info("xml:" + xml);
            Map<String, Object> resultUn = XmlUtil.xmlStrToMap(WechatUtil.requestOnce(ResourceUtil.getConfigByName("wx.uniformorder"), xml));
            // 响应报文
            String return_code = MapUtils.getString("return_code", resultUn);
            String return_msg = MapUtils.getString("return_msg", resultUn);
            //
            if (return_code.equalsIgnoreCase("FAIL")) {
                return toResponsFail("支付失败," + return_msg);
            } else if (return_code.equalsIgnoreCase("SUCCESS")) {
                // 返回数据
                String result_code = MapUtils.getString("result_code", resultUn);
                String err_code_des = MapUtils.getString("err_code_des", resultUn);
                if (result_code.equalsIgnoreCase("FAIL")) {
                    return toResponsFail("支付失败," + err_code_des);
                } else if (result_code.equalsIgnoreCase("SUCCESS")) {
                    String prepay_id = MapUtils.getString("prepay_id", resultUn);
                    // 先生成paySign 参考https://pay.weixin.qq.com/wiki/doc/api/wxa/wxa_api.php?chapter=7_7&index=5
                    resultObj.put("appId", ResourceUtil.getConfigByName("wx.appId"));
                    resultObj.put("timeStamp", DateUtils.timeToStr(System.currentTimeMillis() / 1000, DateUtils.DATE_TIME_PATTERN));
                    resultObj.put("nonceStr", nonceStr);
                    resultObj.put("package", "prepay_id=" + prepay_id);
                    resultObj.put("signType", "MD5");
                    String paySign = WechatUtil.arraySign(resultObj, ResourceUtil.getConfigByName("wx.paySignKey"));
                    resultObj.put("paySign", paySign);
                    // 业务处理
                    orderInfo.setPay_id(prepay_id);
                    // 付款中
                    orderInfo.setPay_status(1);
                    orderService.update(orderInfo);
                    return toResponsObject(0, "微信统一订单下单成功", resultObj);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return toResponsFail("下单失败,error=" + e.getMessage());
        }
        return toResponsFail("下单失败");
    }


    public Object upGrade(UserVo loginUser){
        Map<String,Object> hashMap = new HashMap<>();
        try{
            OrderVo orderInfo = new OrderVo();
            String order_sn = "YTS"+loginUser.getUserId()+"D"+CommonUtil.generateOrderNumber();
            orderInfo.setOrder_sn(order_sn);
            orderInfo.setOrder_status(0);
            orderInfo.setShipping_status(0);
            orderInfo.setUser_id(loginUser.getUserId());
            orderInfo.setOrder_type("9");
            orderInfo.setPay_status(0);

            if(loginUser.getUser_level_id()==4){
                orderInfo.setConsignee("代理");
                hashMap.put("level","代理");
                String upgrade = Sqlca.getString(dataSource.getConnection(),"select upgrade from nideshop_user_level where id="+loginUser.getUser_level_id());
                orderInfo.setOrder_price(new BigDecimal(Integer.parseInt(upgrade)));
                orderInfo.setActual_price(new BigDecimal(Integer.parseInt(upgrade)));
                hashMap.put("money",new BigDecimal(Integer.parseInt(upgrade)));
            }else if(loginUser.getUser_level_id()==3){
                orderInfo.setConsignee("合伙人");
                hashMap.put("level","合伙人");
                String upgrade = Sqlca.getString(dataSource.getConnection(),"select upgrade from nideshop_user_level where id="+loginUser.getUser_level_id());
                orderInfo.setOrder_price(new BigDecimal(Integer.parseInt(upgrade)));
                orderInfo.setActual_price(new BigDecimal(Integer.parseInt(upgrade)));
                hashMap.put("money",new BigDecimal(Integer.parseInt(upgrade)));
            }else if(loginUser.getUser_level_id()==2){
                return toResponsFail("已经是合伙人不能直接升级懂事,请联系客服");
            }else{
                return toResponsFail("已经是懂事不能升级,请联系客服");
            }

            apiOrderMapper.save(orderInfo);

            String orderId = Sqlca.getString(dataSource.getConnection(),"select id from nideshop_order where order_sn='"+order_sn+"'");

            orderInfo.setId(Integer.parseInt(orderId));

            hashMap.put("id",orderId);

        }catch (Exception e){
            e.printStackTrace();
            toResponsFail("下单失败"+e.getMessage());
        }
        return toResponsSuccess(hashMap);
    }

    public void notify(HttpServletRequest request, HttpServletResponse response){
        try{
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            response.setHeader("Access-Control-Allow-Origin", "*");
            InputStream in = request.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.close();
            in.close();
            //xml数据
            String reponseXml = new String(out.toByteArray(), "utf-8");
            logger.info("29866666666666666666666666*********************"+reponseXml);
            WechatRefundApiResult result = (WechatRefundApiResult) XmlUtil.xmlStrToBean(reponseXml, WechatRefundApiResult.class);
            String result_code = result.getResult_code();
            if(result_code.equalsIgnoreCase("SUCCESS")){
                //订单编号
                String out_trade_no = result.getOut_trade_no();
                logger.error("订单" + out_trade_no + "支付成功");
                String user_id = Sqlca.getString(dataSource.getConnection(),"select user_id from nideshop_order where order_sn='"+out_trade_no+"'");

                UserVo userVo = userService.queryObject(Long.parseLong(user_id));

                OrderVo orderInfo = orderService.queryObjectByOrderSn(out_trade_no);

                logger.error("userVo：getUser_level_id========"+userVo.getUser_level_id());
                if(userVo.getUser_level_id()!=1){
                    //级别+1
                    if(userVo.getUser_level_id()==4){
                        userVo.setUser_level_id(3);
                        Sqlca.updateObject(dataSource.getConnection(),"update nideshop_user set user_level_id=3,task_num=0 where id="+userVo.getUserId(),new String[]{});
                        userService.updateLevelUnderUserDaili(userVo.getUserId());
                    }else if(userVo.getUser_level_id()==3){
                        userVo.setUser_level_id(2);
                        Sqlca.updateObject(dataSource.getConnection(),"update nideshop_user set user_level_id=2,task_num=0 where id="+userVo.getUserId(),new String[]{});
                        userService.updateLevelUnderUserHuoheren(userVo.getUserId());
                    }
                }

                orderInfo.setPay_status(2);
                orderInfo.setOrder_status(201);
                orderInfo.setShipping_status(0);
                orderInfo.setPay_time(new Date());
                orderService.update(orderInfo);

            }else if(result_code.equalsIgnoreCase("FAIL")){
                response.getWriter().write(setXml("SUCCESS", "OK"));
            }

            response.getWriter().write(setXml("SUCCESS", "OK"));

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Object getPhoneNumber(UserVo userVo,Map<String,Object> map){
        try{
            String uid = String.valueOf(map.get("uid"));
            String encryptedData = String.valueOf(map.get("encryptedData"));
            String iv = String.valueOf(map.get("iv"));
            String session_key = Sqlca.getString(dataSource.getConnection(),"select password from nideshop_user where id="+uid);

            String json = AESUtils.Decrypt(encryptedData,session_key,iv);
            JSONObject jsonObject = JSONObject.parseObject(json);
            String phoneNumber = String.valueOf(jsonObject.get("phoneNumber"));
            if(phoneNumber!=null && !"null".equals(phoneNumber)){
                Sqlca.updateObject(dataSource.getConnection(),"update nideshop_user set mobile='"+phoneNumber+"' where id="+uid,new String[]{});
            }
            return toResponsSuccess(jsonObject);
        }catch (Exception e){
            e.printStackTrace();
            return toResponsFail("授权失败");
        }

    }


    public Object getGoodsSpecification(UserVo userVo,Map<String,Object> map){
        Map<String, Object> resultObj = new HashMap<String, Object>();

        try{
            String sql = "select * from nideshop_product where goods_id="+map.get("goodsId")+" and goods_specification_ids like '%"+map.get("specifId")+"%' " +
                    " or goods_specification_ids like '%"+map.get("specifId")+"%'";
            List<Map<String,Object>> list = Sqlca.getArrayListFromMap(sql,dataSource.getConnection());
            if(list.size()>0){
                resultObj.put("price",list.get(0));
            }

            String sql1 = "select * from nideshop_goods_specification where id='"+map.get("colorId")+"' or id='"+map.get("specifId")+"'";
            List<Map<String,Object>> list1 = Sqlca.getArrayListFromMap(sql1,dataSource.getConnection());
            if (list1.size()>0){
                resultObj.put("color",list1.get(0));
                if(list1.size()>1){
                    for(Map<String,Object> mmm : list1){
                        if(null!=mmm.get("pic_url") && !"".equals(mmm.get("pic_url"))){
                            resultObj.put("color",mmm);
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return toResponsFail("系统异常");
        }
        return toResponsSuccess(resultObj);
    }

    //返回微信服务
    public static String setXml(String return_code, String return_msg) {
        return "<xml><return_code><![CDATA[" + return_code + "]]></return_code><return_msg><![CDATA[" + return_msg + "]]></return_msg></xml>";
    }
}
