package com.platform.api;

import com.platform.annotation.IgnoreAuth;
import com.platform.annotation.LoginUser;
import com.platform.entity.UserVo;
import com.platform.service.ApiMyCenterService;
import com.platform.util.ApiBaseAction;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Api(tags = "个人中心")
@RestController
@RequestMapping("/api/center")
public class ApiMyCenter extends ApiBaseAction {

    @Autowired
    ApiMyCenterService apiMyCenterService;

    @PostMapping(value = "getMyCenterInfo")
    public Object getMyCenterInfo(@LoginUser UserVo loginUser){
        return toResponsSuccess(apiMyCenterService.getMyCenterInfo(loginUser.getUserId()));
    }

    @PostMapping(value = "withdrawApply")
    public Object withdrawApply(@LoginUser UserVo loginUser,@RequestParam Map<String,Object> map){
        String success = apiMyCenterService.withdrawApply(loginUser,map);
        if("success".equals(success)){
            return toResponsMsgSuccess("提交成功") ;
        }
        return toResponsFail(success);
    }

    @PostMapping(value = "consumerRecordByUserID")
    public Object consumerRecordByUserID(@LoginUser UserVo loginUser,@RequestParam Map<String,Object> map){
        return toResponsSuccess(apiMyCenterService.consumerRecordByUserID(loginUser,map));
    }

    @PostMapping(value = "bankInfoByID")
    public Object bankInfoByID(@LoginUser UserVo loginUser,@RequestParam Map<String,Object> map){
        return toResponsSuccess(apiMyCenterService.bankInfoByID(loginUser,map));
    }

    @PostMapping(value = "upGrade")
    public Object upGrade(@LoginUser UserVo loginUser){
        return  toResponsSuccess(apiMyCenterService.upGrade(loginUser));
    }

    @ApiIgnore
    @IgnoreAuth
    @RequestMapping(value = "/notify", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    public void notify(HttpServletRequest request, HttpServletResponse response){
        apiMyCenterService.notify(request,response);
    }

    @PostMapping(value = "prepay")
    public Object prepay(@LoginUser UserVo loginUser, Integer orderId){
        return apiMyCenterService.prePay(loginUser,orderId);
    }

    @PostMapping(value = "getPhoneNumber")
    public Object getPhoneNumber(@LoginUser UserVo loginUser,@RequestParam Map<String,Object> map){
        return apiMyCenterService.getPhoneNumber(loginUser,map);
    }

    @PostMapping(value = "getGoodsSpecification")
    public Object getGoodsSpecification(@LoginUser UserVo loginUser,@RequestParam Map<String,Object> map){
        return apiMyCenterService.getGoodsSpecification(loginUser,map);
    }

    @IgnoreAuth
    @PostMapping(value = "recentConsumeList")
    public Object recentConsumeList(){
        Random random=new Random();
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> mm1 = new HashMap<>();
        mm1.put("id","1");
        mm1.put("nickname","没人要的...,30分钟前下单");
        mm1.put("photo","http://cdn.szbypos.com/upload/20200619/10473763831e22.png");
        list.add(mm1);
        Map<String,Object> mm2 = new HashMap<>();
        mm2.put("id","2");
        mm2.put("nickname","追忆,30分钟前下单");
        mm2.put("photo","http://cdn.szbypos.com/upload/20200619/104832954d1053.png");
        list.add(mm2);
        Map<String,Object> mm3 = new HashMap<>();
        mm3.put("id","3");
        mm3.put("nickname","自彬彬园子,30分钟前下单");
        mm3.put("photo","http://cdn.szbypos.com/upload/20200619/1049083458939e.png");
        list.add(mm3);
        Map<String,Object> mm4 = new HashMap<>();
        mm4.put("id","4");
        mm4.put("nickname","溯光～婧云,60分钟前下单");
        mm4.put("photo","http://cdn.szbypos.com/upload/20200619/10492440073f5e.png");
        list.add(mm4);
        Map<String,Object> mm5 = new HashMap<>();
        mm5.put("id","5");
        mm5.put("nickname","许兵,60分钟前下单");
        mm5.put("photo","http://cdn.szbypos.com/upload/20200619/105101809ebcdc.png");
        list.add(mm5);
        Map<String,Object> mm6 = new HashMap<>();
        mm6.put("nickname","来一单,60分钟前下单");
        mm6.put("photo","http://cdn.szbypos.com/upload/20200619/105118982ac581.png");
        list.add(mm6);
        Map<String,Object> mm7 = new HashMap<>();
        mm7.put("id","7");
        mm7.put("nickname","A王建芳,60分钟前下单");
        mm7.put("photo","http://cdn.szbypos.com/upload/20200619/1049543335f48c.png");
        list.add(mm7);
        Map<String,Object> mm8 = new HashMap<>();
        mm8.put("id","8");
        mm8.put("nickname","科凡定制丹阳店,30分钟前下单");
        mm8.put("photo","http://cdn.szbypos.com/upload/20200619/10504776fcbf0.png");
        list.add(mm8);
        Map<String,Object> mm9 = new HashMap<>();
        mm9.put("id","9");
        mm9.put("nickname","吸塑材料商李生,30分钟前下单");
        mm9.put("photo","http://cdn.szbypos.com/upload/20200619/105136933bab3e.png");
        list.add(mm9);
        Map<String,Object> mm10 = new HashMap<>();
        mm10.put("id","10");
        mm10.put("nickname","飞(粉象 指纹锁）,30分钟前下单");
        mm10.put("photo","http://cdn.szbypos.com/upload/20200619/105237552dfd50.png");
        list.add(mm10);
        Map<String,Object> mm11 = new HashMap<>();
        mm11.put("id","11");
        mm11.put("nickname","Xsh,10分钟前下单");
        mm11.put("photo","http://cdn.szbypos.com/upload/20200619/1051531908fb95.png");
        list.add(mm11);
        Map<String,Object> mm12 = new HashMap<>();
        mm12.put("id","12");
        mm12.put("nickname","Fangツ,10分钟前下单");
        mm12.put("photo","http://cdn.szbypos.com/upload/20200619/1052175452586.png");
        list.add(mm12);
        Map<String,Object> mm13 = new HashMap<>();
        mm13.put("id","13");
        mm13.put("nickname","你被“陈菜园子”拉入群聊,10分钟前下单");
        mm13.put("photo","http://cdn.szbypos.com/upload/20200619/1052571903a42.png");
        list.add(mm13);
        Map<String,Object> mm14 = new HashMap<>();
        mm14.put("id","14");
        mm14.put("nickname","友情风中流星,10分钟前下单");
        mm14.put("photo","http://cdn.szbypos.com/upload/20200619/10532524126074.png");
        list.add(mm14);
        Map<String,Object> mm15 = new HashMap<>();
        mm15.put("id","15");
        mm15.put("nickname","Ccco,60分钟前下单");
        mm15.put("photo","http://cdn.szbypos.com/upload/20200619/105944215e1651.png");
        list.add(mm15);
        Map<String,Object> mm16 = new HashMap<>();
        mm16.put("id","16");
        mm16.put("nickname","科凡定制丹阳店smile~,60分钟前下单");
        mm16.put("photo","http://cdn.szbypos.com/upload/20200619/11112983337dbe.png");
        list.add(mm16);
        Map<String,Object> mm17 = new HashMap<>();
        mm17.put("id","17");
        mm17.put("nickname","我要饭回来了,60分钟前下单");
        mm17.put("photo","http://cdn.szbypos.com/upload/20200619/11115320651f44.png");
        list.add(mm17);
        Map<String,Object> mm18 = new HashMap<>();
        mm18.put("id","18");
        mm18.put("nickname","报价猪（拉群即可报价）,10分钟前下单");
        mm18.put("photo","http://cdn.szbypos.com/upload/20200619/111217932aafa1.png");
        list.add(mm18);
        Map<String,Object> mm19 = new HashMap<>();
        mm19.put("id","19");
        mm19.put("nickname","IsQianqiAn,10分钟前下单");
        mm19.put("photo","http://cdn.szbypos.com/upload/20200619/1100033065e200.png");
        list.add(mm19);
        Map<String,Object> mm20 = new HashMap<>();
        mm20.put("id","20");
        mm20.put("nickname","园子彬彬,10分钟前下单");
        mm20.put("photo","http://cdn.szbypos.com/upload/20200619/110024588d75e9.png");
        list.add(mm20);
        return toResponsSuccess(list);
    }
}
