package com.cff.springbootwork.redis;

import com.alibaba.fastjson.JSONObject;
import com.cff.springbootwork.redis.util.RestApiUtil;
import com.cff.springbootwork.redis.util.Sqlca;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Map;


/**
 * 监听所有db的过期事件__keyevent@*__:expired"
 * @author lsm
 */
@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {

    private static Logger logger = LogManager.getLogger(RedisKeyExpirationListener.class);

    @Autowired
    private DataSource dataSource;

    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    /**
     * 针对redis数据失效事件，进行数据处理
     * @param message
     * @param pattern
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        // 用户做自己的业务处理即可,注意message.toString()可以获取失效的key
        logger.info("RedisKeyExpirationListener-message:"+message);
        String expiredKey = message.toString();
        logger.info("RedisKeyExpirationListener-expiredKey:"+expiredKey);
        if(expiredKey.startsWith("YTP")){
            try {
                Connection connection = dataSource.getConnection();
                String order_status = Sqlca.getString(dataSource.getConnection(),"select order_status from nideshop_order where order_sn='"+expiredKey+"' and order_status=107");
                logger.info("order_status:"+order_status);
                if("107".equals(order_status)){
                    Sqlca.updateObject(connection,"update nideshop_order set order_status=109 where order_sn=?",new String[]{expiredKey});
                    ArrayList<Map<String, Object>> list = Sqlca.getArrayListFromMap("select id,user_id from nideshop_order where order_sn='"+expiredKey+"'",dataSource.getConnection());
                    Map<String,Object> map = list.get(0);
                    String token = Sqlca.getString(dataSource.getConnection(),"select token from tb_token where user_id="+map.get("user_id"));
                    RestApiUtil restApiUtil = new RestApiUtil();
                    logger.info("RedisKeyExpirationListener-order-id:"+map.get("id")+"----user_id:"+map.get("user_id")+"----token:"+token);
                    JSONObject params = new JSONObject();
                    params.put("orderId",String.valueOf(map.get("id")));
                    //String json = restApiUtil.post("https://zfpay.mylcwl.com:1443/platform/api/pay/refund",params,token);
                    String json = restApiUtil.post("https://www.mtmzg.cn/api/pay/refund",params,token);
                    logger.info("RedisKeyExpirationListener-restApiUtil-json:"+json);
//                JSONObject object = JSONObject.parseObject(json);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
