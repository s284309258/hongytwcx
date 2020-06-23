package com.cff.springbootwork;

import com.alibaba.fastjson.JSONObject;
import com.cff.springbootwork.activemq.handler.JmsProducer;
import com.cff.springbootwork.activemq.model.DefaultMqModel;
import com.cff.springbootwork.activemq.model.SeccondMqModel;
import com.cff.springbootwork.web.util.RestApiUtil;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;
//
//import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=ApiApplication.class)
public class Test {
    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    JmsProducer jmsProducer;

//    @org.junit.Test
//    public void redisExpire(){
//        String key="YTP72D20200527101751630";
//        String value="123123";
//        ValueOperations operations = redisTemplate.opsForValue();
//        operations.set(key,value);
//        redisTemplate.expire("YTP72D20200527101751630",20, TimeUnit.SECONDS);
//        System.out.println("YTP72D20200527101751630:"+operations.get("YTP72D20200527101751630"));
//    }

//    @org.junit.Test
//    public void mqtest(){
//        SeccondMqModel defaultMqModel = new SeccondMqModel();
//        defaultMqModel.setContent("hahahahahahhahaha哈啊哈哈");
//        defaultMqModel.setTitle("测试");
//        defaultMqModel.setType(1);
//        jmsProducer.send(defaultMqModel);
//    }
//"userId=2&username=admin&token=cz12kl7az80hyjrg10h097dix65dsdx8"
    @org.junit.Test
    public void testPost(){
        RestApiUtil restUtil = new RestApiUtil();
        try{
            JSONObject obj = new JSONObject();
//            obj.put("userId",2);
            obj.put("orderId","12");
//            String resultString = restUtil.post(
//                    "http://localhost:28080/platform/api/pay/refund/1",
//                    "","cz12kl7az80hyjrg10h097dix65dsdx8");

            com.cff.springbootwork.redis.util.RestApiUtil restApiUtil = new com.cff.springbootwork.redis.util.RestApiUtil();
            restApiUtil.post("http://localhost:28080/platform/api/pay/refund",obj,"zj9dfn709f0ko9ranxl4k47653n4bf42");

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
