package com.platform.controller;

import com.platform.service.impl.HuiyuanmingxibaobiaoServiceImpl;
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
@RequestMapping("huiyuanmingxibaobiao")
public class HuiyuanmingxibaobiaoController {

    @Autowired
    private HuiyuanmingxibaobiaoServiceImpl huiyuanmingxibaobiaoService;
    /**
     * 查看列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {

        return R.ok().put("page", huiyuanmingxibaobiaoService.queryList(params));

    }

    @RequestMapping("/export")
    public R export(@RequestParam Map<String, Object> params, HttpServletResponse response) {

        List<Map<String, Object>> listData = huiyuanmingxibaobiaoService.queryListNonePage(params);

        ExcelExport ee = new ExcelExport("会员报表");

        String[] header = new String[]{"id","董事","合伙人", "代理商", "直推人", "会员姓名","会员手机号","注册日期","银行账户名","银行账号","开户行"};

        List<Map<String, Object>> list = new ArrayList<>();

        if (listData != null && listData.size() != 0) {
            for (Map<String,Object> mapData: listData) {
                LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                map.put("id", mapData.get("id"));
                map.put("dongshi", mapData.get("dongshi"));
                map.put("hehuoren", mapData.get("hehuoren"));
                map.put("dailishang", mapData.get("dailishang"));
                map.put("zhituiren", mapData.get("zhituiren"));
                map.put("nickname", mapData.get("nickname"));
                map.put("mobile", mapData.get("mobile"));
                map.put("register_time", String.valueOf(mapData.get("register_time")).substring(0,10));
                map.put("bank_account_name", mapData.get("bank_account_name"));
                map.put("bank_account", mapData.get("bank_account"));
                map.put("bank_name", mapData.get("bank_name"));
                list.add(map);
            }
        }

        ee.addSheetByMap("会员报表", list, header);
        ee.export(response);
        return R.ok();
    }
}
