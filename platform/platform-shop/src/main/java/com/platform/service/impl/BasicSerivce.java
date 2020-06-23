package com.platform.service.impl;

import com.platform.dao.Sqlca;
import com.platform.utils.PageUtils;
import com.platform.utils.Query;
import com.platform.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Component
public class BasicSerivce {

    @Autowired
    public DataSource dataSource;

    PageUtils pageUtil = null;

    List<Map<String,Object>> list = new ArrayList<>();

    private int SqlTotal(Connection connection,String sql){

        String total = Sqlca.getString(connection,"select count(*) from (" +sql+") ttt");

        return Integer.parseInt(total);
    }

    private String SqlPageLimit(String sql,Map<String,Object> map){
        String order = map.get("order").toString();
        String sidx = String.valueOf(map.get("sidx"));
        if("null".equals(sidx) || "".equals(sidx)){
            sidx="id";
        }
        int page = Integer.parseInt(map.get("page").toString());
        int limit = Integer.parseInt(map.get("limit").toString());
        int startNum = (page-1)*limit;
        sql += " order by "+sidx+" "+order;
        sql += " limit "+startNum+","+limit;

        return sql;
    }

    private String SqlParamQuery(String sql,Map<String,Object> mm){
        StringBuffer buffer = new StringBuffer("select * from ("+sql+") tt where 1=1   ");
        String dd = sql.substring(6,sql.lastIndexOf(" from"));
        String[] ss = dd.split(",");
        if(mm.get("keyword")!=null && !"".equals(mm.get("keyword"))){
            buffer.append("and (");
        }
        for(String s : ss){
            if(s.contains("as ")){
                s=s.substring(s.lastIndexOf("as ")+2);
            }
            if(null!=mm.get("keyword") && !"".equals(mm.get("keyword"))){
                if(String.valueOf(mm.get("keyword")).startsWith("202")){
                    buffer.append(s.trim()+" like '"+mm.get("keyword")+"%'" +" or ");
                }else{
                    buffer.append(s.trim()+"='"+mm.get("keyword")+"'" +" or ");
                }
            }
        }
        buffer = new StringBuffer(buffer.substring(0,buffer.length()-3));
        if(mm.get("keyword")!=null && !"".equals(mm.get("keyword"))){
            buffer.append(")");
        }
        return buffer.toString();
    }

    protected List<Map<String,Object>> SqlQueryNonePage(String sql, Map params){
        try{

            list = Sqlca.getArrayListFromMap(SqlParamQuery(sql,params),dataSource.getConnection());

        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    protected PageUtils SqlQuery(String sql, Map params){

        try{

            //查询列表数据
            Query query = new Query(params);

            list = Sqlca.getArrayListFromMap(SqlPageLimit(SqlParamQuery(sql,params),params),dataSource.getConnection());

            pageUtil = new PageUtils(list, SqlTotal(dataSource.getConnection(),SqlParamQuery(sql,params)), query.getLimit(), query.getPage());

        }catch (Exception e){
            e.printStackTrace();
        }
        return pageUtil;
    }

    protected R SqlQueryEntityID(String sql,String symbol){
        try{
            list = Sqlca.getArrayListFromMap(sql,dataSource.getConnection());
            return R.ok().put(symbol,list.get(0));
        }catch (Exception e){
            e.printStackTrace();
        }
        return R.error();
    }

    protected R SqlUpdate(String targetTable,Map map,String where){
        try{
            StringBuffer buffer = new StringBuffer("update "+targetTable+" set ");
            for(Object key : map.keySet()){
                if(null!=map.get(key) && !"".equals(map.get(key))){
                    buffer.append(key).append(" ='").append(map.get(key)).append("'").append(",");
                }
            }
            buffer = new StringBuffer(buffer.substring(0,buffer.length()-1));
            buffer.append(where);
            Sqlca.updateObject(dataSource.getConnection(),buffer.toString(),new String[]{});
        }catch (Exception e){
            e.printStackTrace();
            return R.error();
        }
        return R.ok();
    }

    public static boolean isDate(String date){
        Pattern p = Pattern.compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))?$");
        return p.matcher(date).matches();
    }


    protected R SqlDelete(String sql){
        try{
            Sqlca.updateObject(dataSource.getConnection(),sql,new String[]{});
        }catch (Exception e){
            e.printStackTrace();
        }
        return R.ok();
    }
}
