package com.platform.service.impl;

import com.platform.dao.Sqlca;
import com.platform.utils.PageUtils;
import com.platform.utils.R;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TixianshenqingImpl extends BasicSerivce{

    private String QuerySql(){
        String sql = "select t.id as id,t.user_id as user_id,t.apply_withdraw_money as apply_withdraw_money," +
                " (case when t.state='success' then '成功'" +
                " when t.state='fail' then '失败'" +
                " else '申请中' end) as state," +
                " t.apply_date as apply_date,t.remark as remark," +
                " tt.nick_name as nick_name,tt.mobile as mobile,tt.bank_account_name as bank_account_name,tt.bank_account as bank_account,tt.bank_name," +
                " tt.balance as balance,tt.withdraw_money as withdraw_money,tt.total_money as total_money " +
                " from withdraw_apply t,bank_account tt where t.bank_account_id=tt.id  order by t.apply_date desc";
        return sql;
    }
    public PageUtils queryList(Map params){
        return SqlQuery(QuerySql(),params);
    }

    public List<Map<String,Object>> queryListNonePage(Map params){
        return SqlQueryNonePage(QuerySql(),params);
    }

    public R info(Integer id){
        String sql = "select id,user_id,nick_name,mobile,bank_account_name,bank_account,bank_name,balance,withdraw_money,total_money from bank_account where id="+id;
        return SqlQueryEntityID(sql,"huiyuanyinhangka");
    }

//    public int save(Map map){
//
//    }

    public R update(Map map){
        return SqlUpdate("withdraw_apply",map," where id="+map.get("id"));
    }

    public R successPay(Map map){
        ArrayList ids = (ArrayList)map.get("ids");
        try{
            if("success".equals(map.get("success"))){
                for(Object id : ids){
                    String user_id = Sqlca.getString(dataSource.getConnection(),"select user_id from withdraw_apply where id="+id);
                    String apply_withdraw_money = Sqlca.getString(dataSource.getConnection(),"select apply_withdraw_money from withdraw_apply where id="+id);
                    Sqlca.updateObject(dataSource.getConnection(),"update bank_account set balance=balance-?,withdraw_money=withdraw_money+? where user_id="+user_id,new String[]{apply_withdraw_money,apply_withdraw_money});
                    Sqlca.updateObject(dataSource.getConnection(),"update withdraw_apply set state='success' where id="+id,new String[]{});
                }
                return R.ok();
            }else{
                String separator = StringUtils.join(ids.toArray(),",");
                Map<String,String> map1 = new HashMap<>();
                map1.put("state","fail");
                return SqlUpdate("withdraw_apply",map1," where id in("+separator+")");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return R.ok();
    }
}
