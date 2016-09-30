package com.bk.crawler.toolkit;

import com.bk.crawler.entity.ResponseModel;
import com.bk.crawler.service.*;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by shikee_app03 on 16/8/30.
 */
public class LoginInterceptor extends MethodFilterInterceptor {
    public Map<String,Object> paramMap=new HashMap<String, Object>();
    private String encodeKey="ceb89727213a4573b0ffab5c61a9727e";
    private Map<String,Object> jsonResult=new HashMap<String, Object>();
    private EncryptionService encryptionService= new IOSEncryptionServiceImpl();
    private UserService userService= new UserServiceImpl();
    public Map<String, Object> getJsonResult() {
        return jsonResult;
    }

    public void setJsonResult(Map<String, Object> jsonResult) {
        this.jsonResult = jsonResult;
    }

//    @Override
//    public void init() {
//        super.init();
//        this.formatParam();
//    }

    @Override
    protected String doIntercept(ActionInvocation actionInvocation) throws Exception {
        this.formatParam();//格式化所有参数
        String result="";
        //校验是否为有效请求
        Object sign = paramMap.get("sign");
        Object uid  = paramMap.get("uid");
        XLog.debug(".........");
        XLog.debug("sgin:"+sign);
        XLog.debug("uid:"+uid);
        if(null == sign||null==uid||((String)sign).isEmpty()||((String)uid).isEmpty()){
            Method method=actionInvocation.getProxy().getAction().getClass().getSuperclass().getMethod("addError",String.class);
            method.invoke(actionInvocation.getProxy().getAction(),"请先登录!");
            result=Action.SUCCESS;
            return result;
        }

        HttpServletRequest request = ServletActionContext.getRequest();
        String path = request.getRequestURL().toString();//url
        String reqPamrs = request.getQueryString();//?后面的参数
        String token=null;
        ResponseModel validateResponse=this.validateRequest(path,uid.toString(),sign.toString());
        if(validateResponse.getCode()==200){
            token=validateResponse.getResponseData()==null?null:validateResponse.getResponseData().toString();
            this.addSuccess("","用户已经登录");
            result=actionInvocation.invoke();
            return result;
        }else{
            Method method=actionInvocation.getProxy().getAction().getClass().getSuperclass().getMethod("addError",String.class,Integer.class);
            method.invoke(actionInvocation.getProxy().getAction(),validateResponse.getMsg(),validateResponse.getCode());
            result=Action.SUCCESS;
            return result;
        }
    }

    public void addError(String msg,boolean isException){
        jsonResult.put("msg",msg);
        if(isException){
            jsonResult.put("code","-200");
        }else{
            jsonResult.put("code","-1");
        }
        jsonResult.put("data",null);
    }

    public void addError(String msg){
        addError(msg,false);
    }

    public void addSuccess(Object data,String msg){
        jsonResult.put("msg",msg);
        jsonResult.put("code","200");
        jsonResult.put("data",data);
    }

    public void formatParam(){
        HttpServletRequest request= ServletActionContext.getRequest();
        Map<String,Object>  parameterMap=request.getParameterMap();
        paramMap.clear();
        for(Map.Entry<String, Object> entry:parameterMap.entrySet()){
            String key=entry.getKey();

            String[] value= (String[]) entry.getValue();
            if(value!=null&&value.length>0){
                String orgin_value=encryptionService.decrypt(value[0],encodeKey);
                paramMap.put(key,orgin_value);
            }
        }
    }


    private ResponseModel validateRequest(String actionUrl,String uid,String sign)
    {
        return userService.getToken(actionUrl,uid,sign);
    }
}
