package com.platform.controller;

import com.platform.entity.UserEntity;
import com.platform.service.impl.TixianshenqingImpl;
import com.platform.utils.R;
import com.platform.utils.excel.ExcelExport;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("tixianshenqing")
public class TixianshenqingController {

    @Autowired
    private TixianshenqingImpl tixianshenqing;

    /**
     * 查看列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        return R.ok().put("page", tixianshenqing.queryList(params));
    }

    /**
     * 查看信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id) {

        Map map = tixianshenqing.info(id);

        return R.ok().put("tixianshenqing", map);
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody Map map) {
        return tixianshenqing.update(map);
    }

    /**
     * 修改
     */
    @RequestMapping("/successPay")
    public R successPay(@RequestBody Map map) {
        return tixianshenqing.successPay(map);
    }

    /**
     * 导出会员
     */
    @RequestMapping("/export")
    public R export(@RequestParam Map<String, Object> params, HttpServletResponse response) {

        List<Map<String, Object>> listData = tixianshenqing.queryListNonePage(params);

        ExcelExport ee = new ExcelExport("提现申请");

        String[] header = new String[]{"id","会员昵称","手机号", "申请提现的金额", "申请提现日期(yyyy-mm-dd)", "银行户名","银行账号","开户行","提现状态(成功,失败,申请中)","备注"};

        List<Map<String, Object>> list = new ArrayList<>();

        if (listData != null && listData.size() != 0) {
            for (Map<String,Object> mapData: listData) {
                LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                map.put("id", mapData.get("id"));
                map.put("nick_name", mapData.get("nick_name"));
                map.put("mobile", mapData.get("mobile"));
                map.put("apply_withdraw_money", mapData.get("apply_withdraw_money"));
                map.put("apply_date", mapData.get("apply_date"));
                map.put("bank_account_name", mapData.get("bank_account_name"));
                map.put("bank_account", mapData.get("bank_account"));
                map.put("bank_name", mapData.get("bank_name"));
                map.put("state", mapData.get("state"));
                map.put("remark", mapData.get("remark"));
                list.add(map);
            }
        }

        ee.addSheetByMap("提现申请", list, header);
        ee.export(response);
        return R.ok();
    }
}
