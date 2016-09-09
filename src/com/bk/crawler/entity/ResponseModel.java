package com.bk.crawler.entity;

/**
 * Created by shikee_app03 on 16/8/30.
 */
public class ResponseModel {
    public void setSuccess(String msg,Object responseData)
    {
        this.msg=msg;
        this.responseData=responseData;
        this.code=200;
    }

    public void setSuccess(Object responseData)
    {
        this.msg="请求成功";
        this.responseData=responseData;
        this.code=200;
    }

    public void setFail(String msg)
    {
        this.msg=msg;
        this.responseData=null;
        this.code=-200;
    }

    public void setFail()
    {
        this.msg="请求失败";
        this.responseData=null;
        this.code=-200;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getResponseData() {
        return responseData;
    }

    public void setResponseData(Object responseData) {
        this.responseData = responseData;
    }

    private String msg;
    private int code;
    private Object responseData;
}
