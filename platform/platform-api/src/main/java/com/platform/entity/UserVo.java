package com.platform.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;


/**
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-08-15 08:03:41
 */
public class UserVo implements Serializable {
    private static final long serialVersionUID = 1L;

    //主键
    private Long userId;
    //会员名称
    private String username;
    //会员密码
    private String password;
    //性别
    private Integer gender;
    //出生日期
    private Date birthday;
    //注册时间
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
//    private Date register_time;
    private String register_time;
    //最后登录时间
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
//    private Date last_login_time;
    private String last_login_time;
    //最后登录Ip
    private String last_login_ip;
    //会员等级
    private Integer user_level_id;
    //别名
    private String nickname;
    //手机号码
    private String mobile;
    //注册Ip
    private String register_ip;
    //头像
    private String avatar;
    //微信Id
    private String weixin_openid;
    //推荐人ID
    private long reference_id;

    private long huiyuan_belong;

    private long hehuoren_belong;

    private long dongshi_belong;

    private long daili_belong;

    private String parent_chain;

    private int task_num;

    public long getReference_id() {
        return reference_id;
    }

    public void setReference_id(long reference_id) {
        this.reference_id = reference_id;
    }

    public long getHuiyuan_belong() {
        return huiyuan_belong;
    }

    public void setHuiyuan_belong(long huiyuan_belong) {
        this.huiyuan_belong = huiyuan_belong;
    }

    public long getHehuoren_belong() {
        return hehuoren_belong;
    }

    public void setHehuoren_belong(long hehuoren_belong) {
        this.hehuoren_belong = hehuoren_belong;
    }

    public long getDongshi_belong() {
        return dongshi_belong;
    }

    public void setDongshi_belong(long dongshi_belong) {
        this.dongshi_belong = dongshi_belong;
    }

    public long getDaili_belong() {
        return daili_belong;
    }

    public void setDaili_belong(long daili_belong) {
        this.daili_belong = daili_belong;
    }

    public String getParent_chain() {
        return parent_chain;
    }

    public void setParent_chain(String parent_chain) {
        this.parent_chain = parent_chain;
    }

    public int getTask_num() {
        return task_num;
    }

    public void setTask_num(int task_num) {
        this.task_num = task_num;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getRegister_time() {
        return register_time;
    }

    public void setRegister_time(String register_time) {
        this.register_time = register_time;
    }

    public String getLast_login_time() {
        return last_login_time;
    }

    public void setLast_login_time(String last_login_time) {
        this.last_login_time = last_login_time;
    }

    //    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
//    public Date getRegister_time() {
//        return register_time;
//    }
//
//    public void setRegister_time(Date register_time) {
//        this.register_time = register_time;
//    }
//
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
//    public Date getLast_login_time() {
//        return last_login_time;
//    }
//
//    public void setLast_login_time(Date last_login_time) {
//        this.last_login_time = last_login_time;
//    }

    public String getLast_login_ip() {
        return last_login_ip;
    }

    public void setLast_login_ip(String last_login_ip) {
        this.last_login_ip = last_login_ip;
    }

    public Integer getUser_level_id() {
        return user_level_id;
    }

    public void setUser_level_id(Integer user_level_id) {
        this.user_level_id = user_level_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRegister_ip() {
        return register_ip;
    }

    public void setRegister_ip(String register_ip) {
        this.register_ip = register_ip;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getWeixin_openid() {
        return weixin_openid;
    }

    public void setWeixin_openid(String weixin_openid) {
        this.weixin_openid = weixin_openid;
    }
}
