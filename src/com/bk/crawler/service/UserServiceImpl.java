package com.bk.crawler.service;

import com.bk.crawler.dao.HibernateTemplate;
import com.bk.crawler.dao.UserDAO;
import com.bk.crawler.dao.UserDAOImpl;
import com.bk.crawler.entity.ResponseModel;
import com.bk.crawler.entity.Users;
import com.bk.crawler.toolkit.Toolkit;
import com.bk.crawler.toolkit.XLog;
import com.opensymphony.xwork2.ActionContext;

import java.util.Map;
import java.util.UUID;

/**
 * Created by shikee_app03 on 16/8/30.
 */
public class UserServiceImpl implements  UserService{
    private EncryptionService encryptionService= new IOSEncryptionServiceImpl();
    private UserDAO userDAO=new UserDAOImpl();
    @Override
    public ResponseModel login(String account, String password) {

        ResponseModel response=new ResponseModel();
        if(account==null||account.isEmpty()){
            response.setFail("请输入账号");
        }else if(password==null||password.isEmpty()){
            response.setFail("请输入密码");
        }else{
            Users user=userDAO.getUserByAccountAndPassword(account,password);
            if(user!=null){
                Map session = ActionContext.getContext().getSession();
                UUID uuid = UUID.randomUUID();
                String token=uuid.toString().replace("-","");
                XLog.debug("token:"+token);
                MemCachedManager.mcc.set(user.getUid()+"",token);
                token=encryptionService.encrypt(token,"");//加密后的token
                user.setToken(token);
                response.setSuccess("登录成功",user);
            }else{
                response.setFail("账号密码不正确");
            }
        }
        return response;
    }

    @Override
    public ResponseModel logout(String uid, String token) {
        ResponseModel responseModel=new ResponseModel();
        if(uid==null||token==null||uid.isEmpty()||token.isEmpty()){
            responseModel.setFail("非法调用");
        }else{
            Object obj_local_token=MemCachedManager.mcc.get(uid);
            String local_token=obj_local_token==null?"":obj_local_token.toString();
            if(local_token.equals(token)){
                boolean result=MemCachedManager.mcc.delete(uid);
                if(!result){
                    XLog.debug("异常退出:删除缓存token失败!");
                }
                responseModel.setSuccess("退出成功!",null);
            }else{
                responseModel.setFail("没有找到登录记录,退出失败!");
            }
        }
        return responseModel;
    }

    @Override
    public ResponseModel validateLogin(String uid, String token) {
        ResponseModel response = new ResponseModel();
        Object o_local_token=MemCachedManager.mcc.get(uid);
        if(o_local_token!=null){
            String local_token=o_local_token.toString();
            if(local_token.equals(token)){
                response.setSuccess("验证成功",null);
            }else{
                response.setFail("用户在别处登录");
            }
        }else {
            response.setFail("验证失败,用户没有登录");
        }
        return response;
    }

    @Override
    public ResponseModel validateAccount(String account) {
        ResponseModel responseModel=new ResponseModel();
        if(account==null||account.isEmpty()){
            responseModel.setFail("账号不能为空");
        }else {
            if(userDAO.checkAccount(account)){
                responseModel.setSuccess("可以注册",null);
            }else{
                responseModel.setFail("用户名被占用");
            }
        }
        return responseModel;
    }

    @Override
    public ResponseModel register(Users user) {
        ResponseModel responseModel=new ResponseModel();
        if(user.getAccount()==null||user.getAccount().isEmpty()){
            responseModel.setFail("账号不能为空");
        }else if(user.getPassword()==null||user.getPassword().isEmpty()){
            responseModel.setFail("密码不能为空");
        }else if(this.validateAccount(user.getAccount()).getCode()!=200){
            responseModel.setFail("用户名被占用");
        }else{
            if(user.getName()==null||user.getName().isEmpty()){
                ResponseModel randomNameResponseModel=this.randomName();
                if(randomNameResponseModel.getCode()==200){
                    user.setName(randomNameResponseModel.getResponseData().toString());
                }
            }
            user=userDAO.save(user);
            if (user==null) {
                responseModel.setFail("注册失败");
            }else{
                responseModel.setSuccess("注册成功",user);
            }
        }
        return responseModel;
    }

    @Override
    public ResponseModel getToken(String actionUrl, String uid,String sign) {
        ResponseModel responseModel=new ResponseModel();
        if(uid==null||uid.isEmpty()||sign==null||sign.isEmpty()){
            responseModel.setFail("错误的传参");
        }else{
            String mcm_uid=(String)MemCachedManager.mcc.get(uid);
            if(mcm_uid==null||mcm_uid.isEmpty()){
                responseModel.setFail("非法请求");
            }else{
                String token=this.validateRequest(actionUrl,uid,sign);
                if(token==null||token.isEmpty()){
                    responseModel.setFail("没有找到对应的token");
                }else{
                    responseModel.setSuccess("token获取成功",token);
                }
            }
        }
        return responseModel;
    }

    private ResponseModel randomName(){
        ResponseModel responseModel=new ResponseModel();
        String name=userDAO.getRandomName();
        if(name==null){
            responseModel.setFail("数据异常");
        }else{
            responseModel.setSuccess(name);
        }
        return responseModel;
    }

    private String validateRequest(String actionUrl,String uid,String sign)
    {
        String result=null;
        Object o_token=MemCachedManager.mcc.get(uid);
        if(o_token==null){
            return  result;
        }
        String token=o_token.toString();
        String url=actionUrl+"?"+"uid="+uid+"&token="+token;//xxx.do?uid=0&token=xxxxx
        String url_md5=(new Toolkit()).str2Md5(url);
        if(sign.equals(url_md5)){
            result = token;
        }
        return result;
    }
}
