package com.platform.service.impl;

import com.platform.entity.SysUserEntity;
import com.platform.utils.PageUtils;
import com.platform.utils.ShiroUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class XiaofeimingxibaobiaoServiceImpl extends BasicSerivce{

    private String QuerySql(){
        SysUserEntity sysUserEntity = ShiroUtils.getUserEntity();

        String where = "";
        if(sysUserEntity.getUserId()==1){
            where = " where 1=1 ";
        }else{
            where = " and t.dongshi_belong="+sysUserEntity.getUserId();
        }

        String sql = "select " +
                " id,huiyuan_nickname," +
                " hehuoren_nickname," +
                " huiyuanzhishu_nickname," +
                " dongshi_nickname," +
                " daili_nickname," +
                " user_level_id,mobile,goods_id,goods_name,goods_price,goods_num,goods_total_pay,shouhuo_address,shouhuo_tel,shouhuo_name," +
                " consumer_time" +
                " from consumer_record tt "+where;
        return sql;
    }

    public PageUtils queryList(Map<String, Object> params) {
        return SqlQuery(QuerySql(),params);
    }

    public List<Map<String,Object>> queryListNonePage(Map params){
        return SqlQueryNonePage(QuerySql(),params);
    }

}
