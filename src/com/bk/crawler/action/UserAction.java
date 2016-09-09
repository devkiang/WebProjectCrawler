package com.bk.crawler.action;

import com.bk.crawler.entity.ResponseModel;
import com.bk.crawler.entity.Users;
import com.bk.crawler.service.MemCachedManager;
import com.bk.crawler.service.UserService;
import com.bk.crawler.service.UserServiceImpl;
import com.bk.crawler.toolkit.NamePlug;
import com.bk.crawler.toolkit.Toolkit;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by shikee_app03 on 16/8/30.
 */
public class UserAction extends CrawlerBaseAction{


    UserService userService=new UserServiceImpl();
    public String login(){
        String account= (String) paramMap.get("account");
        String tmp_password=(String) paramMap.get("auth");//authen="password=x&&timestamp=4445555566"
        /////////================> 解封密码和时间戳
        if(tmp_password==null){
            addError("非法请求!");
            return SUCCESS;
        }
        String tmp_authen[] = tmp_password.split("&&");
        String tmp_split_password="";
        String tmp_stlit_timestamp="";
        if(tmp_authen.length!=2){
            addError("非法请求!");
            return SUCCESS;
        }
        tmp_split_password=tmp_authen[0].replace("password=","");
        tmp_stlit_timestamp=tmp_authen[1].replace("timestamp=","");

        long timestamp=Long.parseLong(tmp_stlit_timestamp);
        long curr_timestamp=(new Toolkit()).getCurrSimestamp();
        long result=curr_timestamp-timestamp;
        if(result>5){
            addError("非法请求!");
            return SUCCESS;
        }

        String password=tmp_split_password;
        ResponseModel responseModel=userService.login(account,password);
        if(responseModel.getCode()==200){
            Users user=(Users) responseModel.getResponseData();
            addSuccess(user,responseModel.getMsg());
        }else{
            addError(responseModel.getMsg());
        }
        return SUCCESS;
    }

    public String register(){
        String account=(String)paramMap.get("account");
        String password=(String) paramMap.get("password");
        String name=(String) paramMap.get("name");
        String avatar=(String) paramMap.get("avatar");
        Long level=1L;
        Long exp=1L;
        Users user=new Users();
        user.setAccount(account);
        user.setPassword(password);
        user.setName(name);
        user.setAvatar(avatar);
        user.setLevel(level);
        user.setExp(exp);
        ResponseModel responseModel=userService.register(user);
        if(responseModel.getCode()==200){
            addSuccess(responseModel.getResponseData(),responseModel.getMsg());
        }else{
            addError(responseModel.getMsg());
        }
        return SUCCESS;
    }

    public String logout(){
        String uid=(String)paramMap.get("uid");
        String sign=(String)paramMap.get("sign");
        if (uid==null||uid.isEmpty()) {
            addError("非法请求");
            return SUCCESS;
        }
        HttpServletRequest request = ServletActionContext.getRequest();
        String path = request.getRequestURL().toString();//url
        String reqPamrs = request.getQueryString();//?后面的参数
        ResponseModel tokenResponse=userService.getToken(path,uid,sign);
        if(tokenResponse.getCode()==200){
            Object obj_token=tokenResponse.getResponseData();
            obj_token=obj_token==null?"":obj_token.toString();
            tokenResponse=userService.logout(uid,obj_token.toString());
            if(tokenResponse.getCode()==200){
                tokenResponse.setSuccess("["+uid+"]"+"退出成功!");
            }else{
                tokenResponse.setFail("退出失败!");
            }
        }else{
            addError(tokenResponse.getMsg());
        }
        return SUCCESS;
    }

    public String plug(){
        NamePlug plug=new NamePlug();
        plug.addNameModelByTxtFilePath("/Users/shikee_app03/Desktop/kiang/name.txt");
        addSuccess("","执行完成");
        return SUCCESS;
    }
}
