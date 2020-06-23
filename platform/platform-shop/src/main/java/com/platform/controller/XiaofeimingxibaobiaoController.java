package com.platform.controller;

import com.platform.service.impl.HuiyuanmingxibaobiaoServiceImpl;
import com.platform.service.impl.XiaofeimingxibaobiaoServiceImpl;
import com.platform.utils.PageUtils;
import com.platform.utils.Query;
import com.platform.utils.R;
import com.platform.utils.excel.ExcelExport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("xiaofeimingxibaobiao")
public class XiaofeimingxibaobiaoController {

    @Autowired
    private XiaofeimingxibaobiaoServiceImpl xiaofeimingxibaobiaoService;
    /**
     * 查看列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {

        return R.ok().put("page", xiaofeimingxibaobiaoService.queryList(params));

    }

    @RequestMapping("/export")
    public R export(@RequestParam Map<String, Object> params, HttpServletResponse response) {

        List<Map<String, Object>> listData = xiaofeimingxibaobiaoService.queryListNonePage(params);

        ExcelExport ee = new ExcelExport("消费明细报表");

        String[] header = new String[]{"id","董事","合伙人", "代理商", "直推人", "会员姓名","会员手机号","产品名称","产品价格","产品数量","支付总价格","收货人姓名","收货人手机号","收货人地址","购买日期"};

        List<Map<String, Object>> list = new ArrayList<>();

        if (listData != null && listData.size() != 0) {
            for (Map<String,Object> mapData: listData) {
                LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                map.put("id", mapData.get("id"));
                map.put("dongshi_nickname", mapData.get("dongshi_nickname"));
                map.put("hehuoren_nickname", mapData.get("hehuoren_nickname"));
                map.put("daili_nickname", mapData.get("daili_nickname"));
                map.put("huiyuanzhishu_nickname", mapData.get("huiyuanzhishu_nickname"));
                map.put("huiyuan_nickname", mapData.get("huiyuan_nickname"));
                map.put("mobile", mapData.get("mobile"));
                map.put("goods_name", mapData.get("goods_name"));
                map.put("goods_price", mapData.get("goods_price"));
                map.put("goods_num", mapData.get("goods_num"));
                map.put("goods_total_pay", mapData.get("goods_total_pay"));
                map.put("shouhuo_name", mapData.get("shouhuo_name"));
                map.put("shouhuo_tel", mapData.get("shouhuo_tel"));
                map.put("shouhuo_address", mapData.get("shouhuo_address"));
                map.put("consumer_time", String.valueOf(mapData.get("consumer_time")).substring(0,10));
                list.add(map);
            }
        }

        ee.addSheetByMap("消费明细报表", list, header);
        ee.export(response);
        return R.ok();
    }
}
