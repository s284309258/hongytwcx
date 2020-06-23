package com.platform.controller;

import com.platform.entity.OrderEntity;
import com.platform.service.OrderService;
import com.platform.util.wechat.WechatRefundApiResult;
import com.platform.util.wechat.WechatUtil;
import com.platform.utils.PageUtils;
import com.platform.utils.Query;
import com.platform.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.RoundingMode;
import java.util.List;
import java.util.Map;


/**
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-08-13 10:41:09
 */
@RestController
@RequestMapping("order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("order:list")
    public R list(@RequestParam Map<String, Object> params) {
        // 查询列表数据
        Query query = new Query(params);

        List<OrderEntity> orderList = orderService.queryList(query);
        int total = orderService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(orderList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("order:info")
    public R info(@PathVariable("id") Integer id) {
        OrderEntity order = orderService.queryObject(id);

        return R.ok().put("order", order);
    }

    /**
     * 信息
     */
    @RequestMapping("/refund/{id}")
    public R refund(@PathVariable("id") Integer id) {
        OrderEntity orderInfo = orderService.queryObject(id);

        if (null == orderInfo) {
            return R.ok("订单已取消");
        }

        if (orderInfo.getOrderStatus() == 401 || orderInfo.getOrderStatus() == 402) {
            return R.ok("订单已退款");
        }

        if (orderInfo.getPayStatus() != 2) {
            return R.ok("订单未付款，不能退款");
        }

        WechatRefundApiResult result = WechatUtil.wxRefund(orderInfo.getOrderSn(),
                Double.parseDouble(orderInfo.getOrderPrice().setScale(2,RoundingMode.DOWN).toString()),
                Double.parseDouble(orderInfo.getOrderPrice().setScale(2,RoundingMode.DOWN).toString()));
        if (result.getResult_code().equals("SUCCESS")) {
            if (orderInfo.getOrderStatus() == 201) {
                orderInfo.setOrderStatus(401);
            } else if (orderInfo.getOrderStatus() == 300) {
                orderInfo.setOrderStatus(402);
            } else if(orderInfo.getOrderStatus()==109){
                orderInfo.setOrderStatus(110);
            } else if(orderInfo.getOrderStatus() == 108){
                orderInfo.setOrderStatus(110);
            } else if(orderInfo.getOrderStatus() == 107){
                orderInfo.setOrderStatus(110);
            }
            orderService.update(orderInfo);
            return R.ok("退款成功");
        } else {
            return R.ok("退款失败");
        }
//        return R.ok().put("order", orderInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("order:save")
    public R save(@RequestBody OrderEntity order) {
        orderService.save(order);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("order:update")
    public R update(@RequestBody OrderEntity order) {
        orderService.update(order);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("order:delete")
    public R delete(@RequestBody Integer[] ids) {
        orderService.deleteBatch(ids);

        return R.ok();
    }

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    public R queryAll(@RequestParam Map<String, Object> params) {

        List<OrderEntity> list = orderService.queryList(params);

        return R.ok().put("list", list);
    }

    /**
     * 总计
     */
    @RequestMapping("/queryTotal")
    public R queryTotal(@RequestParam Map<String, Object> params) {
        int sum = orderService.queryTotal(params);

        return R.ok().put("sum", sum);
    }

    /**
     * 确定收货
     *
     * @param id
     * @return
     */
    @RequestMapping("/confirm")
    @RequiresPermissions("order:confirm")
    public R confirm(@RequestBody Integer id) {
        orderService.confirm(id);

        return R.ok();
    }

    /**
     * 发货
     *
     * @param order
     * @return
     */
    @RequestMapping("/sendGoods")
    @RequiresPermissions("order:sendGoods")
    public R sendGoods(@RequestBody OrderEntity order) {
        orderService.sendGoods(order);

        return R.ok();
    }
}
