package com.platform.controller;

import com.platform.service.impl.HuiyuanyinhangkaImpl;
import com.platform.utils.R;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("huiyuanyinhangka")
public class HuiyuanyinhangkaController {

    @Autowired
    private HuiyuanyinhangkaImpl huiyuanyinhangka;

    /**
     * 查看列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {

        return R.ok().put("page", huiyuanyinhangka.queryList(params));

    }


    /**
     * 查看信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id) {

        Map userLevel = huiyuanyinhangka.info(id);

        return R.ok().put("huiyuanyinhangka", userLevel);
    }

    /**
     * 保存
     */
//    @RequestMapping("/save")
//    public R save(@RequestBody Map map) {
//        huiyuanyinhangka.save(map);
//
//        return R.ok();
//    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody Map map) {
        return huiyuanyinhangka.update(map);
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids) {
        return huiyuanyinhangka.delete(ids);
    }

}
