package com.bk.crawler.action;

import com.bk.crawler.service.EncryptionService;
import com.bk.crawler.service.EncryptionServiceImpl;
import com.bk.crawler.service.IOSEncryptionServiceImpl;
import com.bk.crawler.toolkit.Toolkit;
import com.bk.crawler.toolkit.XLog;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Created by shikee_app03 on 16/7/20.
 */
public class CrawlerBaseAction extends ActionSupport {
    private String encodeKey="ceb89727213a4573b0ffab5c61a9727e";
    public Map<String,Object> paramMap=new HashMap<String, Object>();

    public Map<String, Object> getJsonResult() {
        return jsonResult;
    }

    public void setJsonResult(Map<String, Object> jsonResult) {
        this.jsonResult = jsonResult;
    }

    private Map<String, Object> jsonResult=new HashMap<String, Object>();
    private EncryptionService encryptionService= new IOSEncryptionServiceImpl();
    public CrawlerBaseAction(){

        HttpServletRequest request= ServletActionContext.getRequest();
        Map<String,Object>  parameterMap=request.getParameterMap();
        XLog.debug("调试模式");
        for(Entry<String, Object> entry:parameterMap.entrySet()){
            String key=entry.getKey();
            String[] value= (String[]) entry.getValue();
            if(value!=null&&value.length>0){
                String orgin_value=encryptionService.decrypt(value[0],encodeKey);
                paramMap.put(key,orgin_value);
                //XLog.debug("调试模式");
                XLog.debug("解密"+key+":"+orgin_value);
            }
        }
        XLog.debug("解密参数完成!["+request.getRequestURL() +"]");
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

    public void addError(String msg,Integer code)
    {
        jsonResult.put("msg",msg);
        jsonResult.put("code",code+"");
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
}
