package com.platform.service;

import com.platform.dao.ApiOrderGoodsMapper;
import com.platform.entity.OrderGoodsVo;
import com.platform.entity.ProductVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class ApiOrderGoodsService {
    @Autowired
    private ApiOrderGoodsMapper orderGoodsDao;

    @Autowired
    private ApiProductService productService;


    public OrderGoodsVo queryObject(Integer id) {
        return orderGoodsDao.queryObject(id);
    }


    public List<OrderGoodsVo> queryList(Map<String, Object> map) {
        List<OrderGoodsVo> OrderGoodsVos = orderGoodsDao.queryList(map);
        for(OrderGoodsVo vo : OrderGoodsVos){
            ProductVo productInfo = productService.queryObject(vo.getProduct_id());
            if(productInfo!=null && null!=productInfo.getGoods_specification_ids() && !"".equals(productInfo.getGoods_specification_ids())){
                String picture = productService.getSpecificationPicture(productInfo.getGoods_specification_ids(),String.valueOf(productInfo.getGoods_id()));
                vo.setList_pic_url(picture);
            }
        }
        return OrderGoodsVos;
    }


    public int queryTotal(Map<String, Object> map) {
        return orderGoodsDao.queryTotal(map);
    }


    public void save(OrderGoodsVo order) {
        orderGoodsDao.save(order);
    }


    public void update(OrderGoodsVo order) {
        orderGoodsDao.update(order);
    }


    public void delete(Integer id) {
        orderGoodsDao.delete(id);
    }


    public void deleteBatch(Integer[] ids) {
        orderGoodsDao.deleteBatch(ids);
    }

}
