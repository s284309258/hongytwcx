package com.platform.entity;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;

/**
 * 名称：SmsConfig <br>
 * 描述：短信配置信息<br>
 *
 * @author 李鹏军
 * @version 1.0
 * @since 1.0.0
 */
@Data
public class SmsConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 类型 1：腾讯云
     */
    @Range(min = 1, max = 3, message = "类型错误")
    private Integer type;

    /**
     * appid
     */
    private int appid;

    /**
     * key
     */
    private String appkey;

    /**
     * 签名
     */
    private String sign;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public int getAppid() {
        return appid;
    }

    public void setAppid(int appid) {
        this.appid = appid;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
