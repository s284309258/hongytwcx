package com.platform.service.impl;

import com.platform.utils.PageUtils;
import com.platform.utils.R;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class HuiyuanyinhangkaImpl extends BasicSerivce{


    public PageUtils queryList(Map params){
        String sql = "select id,user_id,nick_name,mobile,bank_account_name,bank_account,bank_name,balance,withdraw_money,total_money from bank_account ";
        return SqlQuery(sql,params);
    }

    public R info(Integer id){
        String sql = "select id,user_id,nick_name,mobile,bank_account_name,bank_account,bank_name,balance,withdraw_money,total_money from bank_account where id="+id;
        return SqlQueryEntityID(sql,"huiyuanyinhangka");
    }

//    public int save(Map map){
//
//    }

    public R update(Map map){
        return SqlUpdate("bank_account",map," where id="+map.get("id"));
    }

    public R delete(Integer[] ids){
        String separator = StringUtils.join(ids,",");
        return SqlDelete("delete from bank_account where id in("+separator+")");
    }
}
