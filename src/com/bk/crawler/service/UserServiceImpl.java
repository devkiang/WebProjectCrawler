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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            responseModel.setSuccess("非法调用");
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
                responseModel.setSuccess("没有找到登录记录,退出失败!");
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
                response.setCode(-80);
            }
        }else {
            response.setFail("验证失败,用户没有登录");
            response.setCode(-80);
        }
        return response;
    }

    @Override
    public ResponseModel validateAccount(String account) {
        ResponseModel responseModel=new ResponseModel();
        if(account==null||account.isEmpty()){
            responseModel.setFail("账号不能为空");
        }else if(!this.validatePhone(account)){
            responseModel.setFail("请输入正确的手机号");
        }else{
            if(userDAO.checkAccount(account)){
                responseModel.setSuccess("可以注册",true);
            }else{
                responseModel.setSuccess("账号被占用",false);
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
        }else if(user.getPassword().length()<6){
            responseModel.setFail("密码不足六位数");
        }else {
            ResponseModel validateAccountResponse=this.validateAccount(user.getAccount());
            if(validateAccountResponse.getCode()!=200){
                responseModel.setFail(validateAccountResponse.getMsg());
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
        }
        return responseModel;
    }

    @Override
    public ResponseModel getToken(String actionUrl, String uid,String sign) {
        ResponseModel responseModel=new ResponseModel();
        if(uid==null||uid.isEmpty()||sign==null||sign.isEmpty()){
            responseModel.setFail("错误的请求,请先登录");
            responseModel.setCode(-80);
            XLog.debug("uid或sign为空");
        }else{
            String mcm_uid=(String)MemCachedManager.mcc.get(uid);
            if(mcm_uid==null||mcm_uid.isEmpty()){
                responseModel.setFail("您还没有登录,或者登录已经超时");
                responseModel.setCode(-80);
            }else{
                String token=this.validateRequest(actionUrl,uid,sign);
                if(token==null||token.isEmpty()){
                    responseModel.setFail("您的账号已经在别处登录,请重新登录");
                    responseModel.setCode(-80);
                }else{
                    responseModel.setSuccess("token获取成功",token);
                }
            }
        }
        return responseModel;
    }

    public ResponseModel randomName(){
        ResponseModel responseModel=new ResponseModel();
        String name=userDAO.getRandomName();
        if(name==null){
            responseModel.setFail("数据异常");
        }else{
            responseModel.setSuccess(name);
        }
        return responseModel;
    }

    @Override
    public ResponseModel getUser(Long uid, String token) {
        ResponseModel responseModel=new ResponseModel();
        ResponseModel validateLoginResponseModel=this.validateLogin(uid+"",token);
        if(validateLoginResponseModel.getCode()==200){
           Users user=userDAO.getUserByUid(uid);
            if(user!=null){
                responseModel.setSuccess(user);
            }else{
                responseModel.setFail("没有找到该用户");
            }
        }else{
            responseModel.setFail(validateLoginResponseModel.getMsg());
            responseModel.setCode(validateLoginResponseModel.getCode());
        }
        return responseModel;
    }

    @Override
    public ResponseModel updateUser(Users user) {
        ResponseModel responseModel=new ResponseModel();
        user=userDAO.update(user);
        if(user!=null){
            responseModel.setSuccess(user);
        }else{
            responseModel.setFail("修改失败");
        }
        return responseModel;
    }

    @Override
    public ResponseModel updatePwd(String account, String newPwd) {
        ResponseModel resultResponseModel=new ResponseModel();
        if(newPwd==null||newPwd.isEmpty()){
            resultResponseModel.setFail("修改的密码不能为空");
            return resultResponseModel;
        }
        if(newPwd.length()<6){
            resultResponseModel.setFail("请输入至少6位数的密码");
            return resultResponseModel;
        }

        ResponseModel responseModel=this.validateAccount(account);
        if(responseModel.getCode()==200){
            boolean result=Boolean.parseBoolean(responseModel.getResponseData().toString());
            if(result==true){//账号不存在
                resultResponseModel.setFail("该手机号尚未注册");
            }else{
                Users user=userDAO.getUserByAccount(account);
                if(user==null){
                    resultResponseModel.setFail("错误");
                }else{
                    user.setPassword(newPwd);
                    user=userDAO.update(user);
                    resultResponseModel.setSuccess("密码修改成功",user);
                }
            }
        }else{
            resultResponseModel.setFail(responseModel.getMsg());
        }
        return resultResponseModel;
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
        XLog.debug("token获取...");
        XLog.debug("token_url:"+actionUrl);
        XLog.debug("token_uid:"+uid);
        XLog.debug("服务器上的token:"+token);

        String url_md5=(new Toolkit()).str2Md5(url);
        XLog.debug("传来的sign:"+sign);
        XLog.debug("服务器的sign:"+url_md5);
        if(sign.equals(url_md5)){
            result = token;
        }
        return result;
    }

    private boolean validatePhone(String phone)
    {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(phone);
        return m.matches();
    }
}
