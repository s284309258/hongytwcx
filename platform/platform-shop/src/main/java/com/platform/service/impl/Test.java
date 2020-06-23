package com.platform.service.impl;

import com.platform.util.RestApiUtil;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

public class Test {
    public static void main(String[] args) {
        String sql = "select  " +
                "                     (select 1 from dual) as huiyuan_nickname,huiyuan_id, " +
                "                     hehuoren_nickname,hehuoren_belong, " +
                "                     huiyuanzhishu_nickname,huiyuanzhishu_belong, " +
                "                     dongshi_nickname,dongshi_belong, " +
                "                     daili_nickname,daili_belong, " +
                "                     user_level_id,mobile,goods_id,goods_name,goods_price,goods_num,goods_total_pay,shouhuo_address,shouhuo_tel,shouhuo_name, " +
                "                     consumer_time " +
                "                     from consumer_record tt";

        System.out.println(sql.indexOf("huiyuan_nickname"));
        System.out.println(sql.indexOf(" from"));
        String ddd = sql.substring(6,sql.lastIndexOf(" from"));
        System.out.println("========="+ddd);
        String[] ss = ddd.split(",");
        StringBuffer buffer = new StringBuffer("select * from ("+sql+") tt where 1=1    ");
        HashMap<String,String> mm = new HashMap<>();
        mm.put("keyword","123");
        if(mm.get("keyword")!=null && !"".equals(mm.get("keyword"))){
            buffer.append("and (");
        }
        for(String s : ss){
            if(s.contains("as ")){
                s=s.substring(s.lastIndexOf("as ")+2);
            }
            if(null!=mm.get("keyword") && !"".equals(mm.get("keyword"))){
                buffer.append(s.trim()+"='"+mm.get("keyword")+"'" + " or ");
            }
        }
        buffer = new StringBuffer(buffer.substring(0,buffer.length()-3));
        if(mm.get("keyword")!=null && !"".equals(mm.get("keyword"))){
            buffer.append(")");
        }

        System.out.println(buffer);

        ArrayList arrayList = new ArrayList();
        arrayList.add("123");
        arrayList.add("444");
        System.out.println(StringUtils.join(arrayList,","));

        System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        BigDecimal dd = new BigDecimal(10);
        BigDecimal ff = dd.multiply(new BigDecimal(0.01));
        System.out.println(ff.setScale(2, RoundingMode.DOWN));
//        System.out.println("sss"+ff.setScale(2)+"dddd");
        Long ssdd = 0L;
        System.out.println("Long:"+ssdd);

        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("date:"+date);

//        try{
//
//            RestApiUtil apiUtil = new RestApiUtil();
//            StringBuffer buffer1 = new StringBuffer();
//            buffer1.append("order_sn=").append("aaaaaaaaaadddddddddddd").append("&user_id=").append("3423").append("&id=")
//                    .append("2333").append("&add_time=").append("333333").append("&order_type=")
//                    .append("44444").append("&order_price=").append("9999")
//                    .append("&multiple_hours=").append("50");
//            apiUtil.post("http://localhost:8099/pay/redisQueenPayTimeOut",buffer1.toString());
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }

        BigDecimal dddd = new BigDecimal(200);
        BigDecimal ffff = new BigDecimal(200);
        if(dddd.compareTo(ffff)<0){
            System.out.println("111111111111111");
        }

        BigDecimal sdfsdf = new BigDecimal(10);
        System.out.println(sdfsdf.intValue());

        System.out.println((int)Math.random()*10);

        Random r=new Random();
        System.out.println(r.nextInt(19));

    }
}
