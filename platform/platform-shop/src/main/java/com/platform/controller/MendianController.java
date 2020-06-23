package com.platform.controller;

import com.platform.entity.AttributeCategoryEntity;
import com.platform.entity.MendianEntity;
import com.platform.service.MendianService;
import com.platform.service.impl.MendianServiceImpl;
import com.platform.utils.PageUtils;
import com.platform.utils.Query;
import com.platform.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("mendian")
public class MendianController {

    @Autowired
    private MendianServiceImpl mendianService;

    /**
     * 查看列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) throws Exception {
        //查询列表数据
        return mendianService.queryList(params);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody MendianEntity mendianEntity) throws Exception {
        return mendianService.save(mendianEntity);
    }

    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id) throws Exception {
        return mendianService.info(id);
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody MendianEntity MendianEntity) throws Exception {
        return mendianService.update(MendianEntity);
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[]ids) throws Exception{
        mendianService.deleteBatch(ids);
        return R.ok();
    }
}
