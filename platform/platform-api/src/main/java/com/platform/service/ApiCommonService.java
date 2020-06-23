package com.platform.service;

import com.alibaba.fastjson.JSONObject;
import com.platform.entity.OrderVo;
import com.platform.entity.UserInfo;
import com.platform.entity.UserLevelVo;
import com.platform.entity.UserVo;
import com.platform.util.ApiBaseAction;
import com.platform.util.RestApiUtil;
import com.platform.util.Sqlca;
import com.platform.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

@Service
public class ApiCommonService  extends ApiBaseAction {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ApiOrderService orderService;

    @Autowired
    private ApiUserService userService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ApiOrderGoodsService orderGoodsService;

    /***
     * 会员授权登陆生成会员上下级关系,父级链,会员初始级别
     * @param userVo 当前会员
     * @param refUserVo 当前会员的直推会员
     * @return
     */
    public ApiCommonService GenerateSeniorRelation(UserVo userVo,UserVo refUserVo){

        if (userVo.getUserId()==null) {
            if(refUserVo!=null){
                ////**************设置上下级关系链**************////
                if(0==refUserVo.getDaili_belong()){
                    //上级是自己注册的没有推荐人
                    if(refUserVo.getUser_level_id()==3){
                        //上级是代理,直属下级代理就是上级
                        userVo.setDaili_belong(refUserVo.getUserId());
                    }else{
                        //上级不是代理,直属下级代理就是0
                        userVo.setDaili_belong(0);
                    }
                }else{
                    //上级是朋友推荐的朋友就是推荐人
                    if(refUserVo.getUser_level_id()==3){
                        //上级是代理,直属下级代理就是上级
                        userVo.setDaili_belong(refUserVo.getUserId());
                    }else{
                        //上级不是代理,直属下级代理就是继承上级的代理
                        userVo.setDaili_belong(refUserVo.getDaili_belong());
                    }
                }

                if(0==refUserVo.getHehuoren_belong()){
                    //上级是自己注册的没有推荐人
                    if(refUserVo.getUser_level_id()==2){
                        //上级是合伙人,直属下级合伙人就是上级
                        userVo.setHehuoren_belong(refUserVo.getUserId());
                    }else{
                        //上级不是合伙人,直属下级合伙人就是0
                        userVo.setHehuoren_belong(0);
                    }
                }else{
                    //上级是朋友推荐的朋友就是推荐人
                    if(refUserVo.getUser_level_id()==2){
                        //上级是合伙人,直属下级合伙人就是上级
                        userVo.setHehuoren_belong(refUserVo.getUserId());
                    }else{
                        //上级不是合伙人,直属下级合伙人就是继承上级的董事
                        userVo.setHehuoren_belong(refUserVo.getHehuoren_belong());
                    }
                }

                if(0==refUserVo.getDongshi_belong()){
                    //上级是自己注册的没有推荐人
                    if(refUserVo.getUser_level_id()==1){
                        //上级是董事,直属下级董事就是上级
                        userVo.setDongshi_belong(refUserVo.getUserId());
                    }else{
                        //上级不是董事,直属下级董事就是0
                        userVo.setDongshi_belong(0);
                    }
                }else{
                    //上级是朋友推荐的朋友就是推荐人
                    if(refUserVo.getUser_level_id()==1){
                        //上级是董事,直属下级董事就是上级
                        userVo.setDongshi_belong(refUserVo.getUserId());
                    }else{
                        //上级不是董事,直属下级董事就是继承上级的董事
                        userVo.setDongshi_belong(refUserVo.getDongshi_belong());
                    }
                }
                ////**************设置上下级关系链**************////
            }
            Long reference_id = refUserVo!=null?refUserVo.getUserId():0L;
            String parent_chain = refUserVo!=null?refUserVo.getParent_chain():"0";
            userVo.setParent_chain(parent_chain+","+reference_id);
            userVo.setHuiyuan_belong(reference_id);
            userVo.setReference_id(reference_id);
            userVo.setUser_level_id(4);
        } else {
            userVo.setLast_login_ip(this.getClientIp());
            userVo.setLast_login_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        }
        return this;
    }

    private void InnerUpdateUserTaskNumAndUpgrade(UserVo refUserVo){
        UserLevelVo userLevelVo = userService.getUserLevelVo(refUserVo);
        Integer taskNum = userLevelVo.getTaskNum();
        UserVo ref_UserVo_new = new UserVo();
        //默认级别为不升级
        ref_UserVo_new.setUser_level_id(refUserVo.getUser_level_id());
        //更新的用户为推荐人ID
        ref_UserVo_new.setUserId(refUserVo.getUserId());

        int task_num = refUserVo.getTask_num();
        Integer task = task_num+1;

        ref_UserVo_new.setTask_num(task);
        //更新推荐人任务量，任务量加1
        if(task>=taskNum){
            if(refUserVo.getUser_level_id()!=1){
                //设置推荐人级别+1
                if(refUserVo.getUser_level_id()==4){
                    ref_UserVo_new.setUser_level_id(3);
                    refUserVo.setUser_level_id(3);
                    //如果推荐人达成任务量直推会员对应的代理商等信息为推荐人
                    ref_UserVo_new.setTask_num(0);
                    refUserVo.setTask_num(0);
                    userService.updateTaskNum0(refUserVo.getUserId());
                    //1.更新直推会员的任务量，达到级别则更新名下会员的代理商和合伙人等信息
                    userService.updateLevelUnderUserDaili(refUserVo.getUserId());
                }else if(ref_UserVo_new.getUser_level_id()==3){
                    ref_UserVo_new.setUser_level_id(2);
                    refUserVo.setUser_level_id(2);
                    ref_UserVo_new.setTask_num(0);
                    refUserVo.setTask_num(0);
                    userService.updateTaskNum0(refUserVo.getUserId());
                    //1.更新直推会员的任务量，达到级别则更新名下会员的代理商和合伙人等信息
                    userService.updateLevelUnderUserHuoheren(refUserVo.getUserId());
                }
            }
        }
        ////**************会员升级和任务量更新**************////
        //更新直推人的任务量和级别
        userService.update(ref_UserVo_new);
    }

    public ApiCommonService UpdateUserTaskNumAndUpgrade(UserVo refUserVo,UserVo userVo,OrderVo orderInfo){
        ////**************会员升级和任务量更新**************////
        try{
            if(refUserVo!=null){
                if(orderInfo!=null){
                    if(orderInfo.getOrder_price().compareTo(new BigDecimal(0.01).setScale(2,RoundingMode.DOWN))>-1){
                        InnerUpdateUserTaskNumAndUpgrade(refUserVo);
                    }
                }else{
                    if(userVo.getUserId()==null){
                        InnerUpdateUserTaskNumAndUpgrade(refUserVo);
                    }
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return this;
    }

    public ApiCommonService PinDanLogicHandle(OrderVo orderInfo){
        try{
            ArrayList<Map<String, Object>> mapArrayList= Sqlca.getArrayListFromMap("select t.id,ttt.multiple_hours,ttt.multiple_num,ttt.is_multiple,tt.goods_id from nideshop_order t,nideshop_order_goods tt,nideshop_goods ttt where t.id=tt.order_id and tt.goods_id=ttt.id and t.order_sn='"+orderInfo.getOrder_sn()+"'",dataSource.getConnection());
            if(mapArrayList.size()>0){
                //查看商品拼单人数限时
                Map<String,Object> map = mapArrayList.get(0);
                String goods_id = String.valueOf(map.get("goods_id"));
                String multiple_num = String.valueOf(map.get("multiple_num"));
                String multiple_hours = String.valueOf(map.get("multiple_hours"));
                int num = Integer.parseInt(multiple_num);

                ArrayList<Map<String, Object>> subMapArrayList = Sqlca.getArrayListFromMap("select t.id from nideshop_order t,nideshop_order_goods tt where " +
                        " t.id=tt.order_id and tt.goods_id='"+goods_id+"' and t.order_type=2 and t.order_status=107 and t.pay_status=2",dataSource.getConnection());
                if(subMapArrayList.size()>=num){
                    for(Map<String, Object> mmm : subMapArrayList){
                        Integer id = Integer.parseInt(mmm.get("id").toString());
                        OrderVo orderVo = new OrderVo();
                        orderVo.setOrder_status(108);
                        orderVo.setId(id);
                        orderService.update(orderVo);
                    }

                }else{
                    RestApiUtil apiUtil = new RestApiUtil();
                    StringBuffer buffer1 = new StringBuffer();
                    buffer1.append("order_sn=").append(orderInfo.getOrder_sn()).append("&user_id=").append(orderInfo.getUser_id()).append("&id=")
                            .append(orderInfo.getId()).append("&add_time=").append(orderInfo.getAdd_time()).append("&order_type=")
                            .append(orderInfo.getOrder_type()).append("&order_price=").append(orderInfo.getOrder_price())
                            .append("&multiple_hours=").append("48");
                    logger.info("PinDanLogicHandle:"+buffer1.toString());
                    String str = apiUtil.post("http://localhost:38080/pay/redisQueenPayTimeOut",buffer1.toString());
                    logger.info("apiUtil.post:"+str);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return this;
    }

    public ApiCommonService InsertConsumerRecord(UserVo userVo, OrderVo orderInfo){
        try{
            //插入会员消费明细表 begin
            UserVo huiyuan = userService.queryObject(orderInfo.getUser_id());
            UserVo hehuoren = userService.queryObject(huiyuan.getHehuoren_belong());
            if(hehuoren==null){hehuoren=new UserVo();hehuoren.setUserId(0L);}
            UserVo zhishu = userService.queryObject(huiyuan.getReference_id());
            if(zhishu==null){zhishu=new UserVo();zhishu.setUserId(0L);}
            UserVo dongshi = userService.queryObject(huiyuan.getDongshi_belong());
            if(dongshi==null){dongshi=new UserVo();dongshi.setUserId(0L);}
            UserVo daili = userService.queryObject(huiyuan.getDaili_belong());
            if(daili==null){daili=new UserVo();daili.setUserId(0L);}
            String orderType = orderInfo.getOrder_type();
            if("2".equals(orderType)){
                orderType="团购拼单";
            }else if("cart".equals(orderType)){
                orderType="购物车购买";
            }else{
                orderType="立即购买";
            }
            String goods_name = Sqlca.getString(dataSource.getConnection(),"select concat(goods_name) as goods_name from nideshop_order_goods where order_id="+orderInfo.getId());
            String goods_id = Sqlca.getString(dataSource.getConnection(),"select concat(goods_id) as goods_id from nideshop_order_goods where order_id="+orderInfo.getId());
            String goods_num = Sqlca.getString(dataSource.getConnection(),"select sum(number) as goods_num from nideshop_order_goods where order_id="+orderInfo.getId());
            Sqlca.updateObject(dataSource.getConnection(),"insert into consumer_record(huiyuan_nickname,huiyuan_id," +
                    " hehuoren_nickname,hehuoren_belong," +
                    " huiyuanzhishu_nickname,huiyuanzhishu_belong," +
                    " dongshi_nickname,dongshi_belong," +
                    " daili_nickname,daili_belong," +
                    " user_level_id,mobile," +
                    " goods_id,goods_name," +
                    " goods_price,goods_num," +
                    " shouhuo_address,shouhuo_tel," +
                    " goods_total_pay,shouhuo_name," +
                    " create_date,consumer_time,order_type)" +
                    " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",new String[]{
                    StringUtils.toStringByObject(huiyuan.getNickname()),String.valueOf(huiyuan.getUserId()),
                    StringUtils.toStringByObject(hehuoren.getNickname()),String.valueOf(hehuoren.getUserId()),
                    StringUtils.toStringByObject(zhishu.getNickname()),String.valueOf(zhishu.getUserId()),
                    StringUtils.toStringByObject(dongshi.getNickname()),String.valueOf(dongshi.getUserId()),
                    StringUtils.toStringByObject(daili.getNickname()),String.valueOf(daili.getUserId()),
                    StringUtils.toStringByObject(huiyuan.getUser_level_id()),StringUtils.toStringByObject(huiyuan.getMobile()),
                    StringUtils.toStringByObject(goods_id),StringUtils.toStringByObject(goods_name),
                    orderInfo.getOrder_price().setScale(2,RoundingMode.DOWN).toString(),StringUtils.toStringByObject(goods_num),
                    StringUtils.toStringByObject(orderInfo.getAddress()),StringUtils.toStringByObject(orderInfo.getMobile()),
                    orderInfo.getOrder_price().setScale(2,RoundingMode.DOWN).toString(),StringUtils.toStringByObject(orderInfo.getConsignee()),
                    new SimpleDateFormat("yyyy-MM-dd").format(new Date()),new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
                    orderType
            });
        }catch (Exception e){
            e.printStackTrace();
        }
        return this;
    }

    public ApiCommonService UpdateSeniorUserBenefit(UserVo refUserVo, OrderVo orderInfo){
        if(refUserVo!=null){
            try{
                String reference_reward = Sqlca.getString(dataSource.getConnection(),"select reference_reward from nideshop_user_level where id="+refUserVo.getUser_level_id());
                Double reward_percent = Double.parseDouble(reference_reward)*0.01;
                BigDecimal reward = orderInfo.getOrder_price().multiply(new BigDecimal(reward_percent)).setScale(2, RoundingMode.DOWN);
                Sqlca.updateObject(dataSource.getConnection(),"update bank_account set balance=balance+"+reward+",total_money=total_money+"+reward+" where user_id="+refUserVo.getUserId(),new String[]{});
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return this;
    }

    public ApiCommonService UpdateLoginUserInfo(UserVo userVo, UserInfo userInfo, JSONObject sessionData){
        if(userVo.getUserId()==null){
            userVo.setUsername(userInfo.getNickName());
            userVo.setPassword(sessionData.getString("session_key"));
            userVo.setRegister_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            userVo.setRegister_ip(this.getClientIp());
            userVo.setLast_login_ip(userVo.getRegister_ip());
            userVo.setLast_login_time(userVo.getRegister_time());
            userVo.setWeixin_openid(sessionData.getString("openid"));
            userVo.setAvatar(userInfo.getAvatarUrl());
            //性别 0：未知、1：男、2：女
            userVo.setGender(userInfo.getGender());
            userVo.setNickname(userInfo.getNickName());
            userVo.setUser_level_id(4);
            userVo.setTask_num(0);
            userService.save(userVo);
            userService.insertBankAccount(userVo);
        }else{
            userVo.setLast_login_ip(this.getClientIp());
            userVo.setLast_login_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            userVo.setPassword(sessionData.getString("session_key"));
            userService.update(userVo);
        }
        return this;
    }

}
