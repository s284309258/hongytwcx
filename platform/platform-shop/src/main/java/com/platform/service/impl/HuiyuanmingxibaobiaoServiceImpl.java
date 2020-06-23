package com.platform.service.impl;

import com.platform.entity.SysUserEntity;
import com.platform.utils.PageUtils;
import com.platform.utils.ShiroUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class HuiyuanmingxibaobiaoServiceImpl extends BasicSerivce{

    private String QuerySql(){
        //查询列表数据
        SysUserEntity sysUserEntity = ShiroUtils.getUserEntity();
        String where = "";
        if(sysUserEntity.getUserId()==1){
//            where = " where 1=1 ";
        }else{
            where = " and t.dongshi_belong="+sysUserEntity.getUserId();
        }
        String sql = "select t.id as id,(select nickname from nideshop_user t1 where t1.id=t.dongshi_belong) as dongshi," +
                " (select nickname from nideshop_user t1 where t1.id=t.hehuoren_belong) as hehuoren," +
                " (select nickname from nideshop_user t1 where t1.id=t.daili_belong) as dailishang," +
                " (select nickname from nideshop_user t1 where t1.id=t.reference_id) as zhituiren," +
                " t.nickname as nickname," +
                " t.mobile as mobile," +
                " t.register_time as register_time," +
                " t2.bank_account_name as bank_account_name," +
                " t2.bank_account as bank_account," +
                " t2.bank_name as bank_name" +
                " from nideshop_user t,bank_account t2  where t.id=t2.user_id "+where;

        return sql;
    }

    public PageUtils queryList(Map<String, Object> params) {
        return SqlQuery(QuerySql(),params);
    }

    public List<Map<String,Object>> queryListNonePage(Map params){
        return SqlQueryNonePage(QuerySql(),params);
    }
}
