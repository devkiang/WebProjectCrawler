package com.bk.crawler.action;

import com.bk.crawler.entity.News;
import com.bk.crawler.entity.NewsBrief;
import com.bk.crawler.entity.ResponseModel;
import com.bk.crawler.service.*;
import com.bk.crawler.toolkit.Toolkit;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shikee_app03 on 16/7/15.
 */
public class NewsAction extends CrawlerBaseAction{
    private NewsService newsService=new NewsServiceImpl();
    private NewsBriefService newsBriefService=new NewsBriefServiceImpl();
    private CollectionService collectionService=new CollectionServiceImpl();
    private UserService userService=new UserServiceImpl();
    Toolkit t= new Toolkit();
    public String index(){
        String pageIndex= (String) paramMap.get("page");
        String sizeStr=(String) paramMap.get("size");
        int page=0;
        int size=20;
        if(pageIndex!=null){
            try {
                page=Integer.parseInt(pageIndex);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        if(sizeStr!=null){
            try {
                size=Integer.parseInt(sizeStr);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        if(page<0){
            page=0;
        }
        if(size<1){
            size=1;
        }
        List<NewsBrief> list=newsBriefService.list(page,size);
        addSuccess(list,"新闻列表获取成功");
        return SUCCESS;
    }

    public String newsDetail(){

        Long nid=t.convert2Long(paramMap.get("nid"));
        Long uid=t.convert2Long(paramMap.get("uid"));
        String sign=(String)paramMap.get("sign");
        if(nid==null||nid<1){
            addError("传入的nid为空");
            return SUCCESS;
        }
        News result=null;
        try {
            result=newsService.findNewsByNid(nid);
            if(uid!=null&&uid>0&&sign!=null&&!sign.isEmpty()){//如果用户传入uid和sign,就进行收藏查询
                HttpServletRequest request = ServletActionContext.getRequest();
                String path = request.getRequestURL().toString();//url
                String reqPamrs = request.getQueryString();//?后面的参数
                ResponseModel tokenResponse=userService.getToken(path,uid+"",sign);
                if(tokenResponse.getCode()==200){//如果能获取到token,就进行token登录验证
                    ResponseModel validateLoginResponseModel=userService.validateLogin(uid+"",tokenResponse.getResponseData().toString());
                    if(validateLoginResponseModel.getCode()==200){//如果token验证通过,就去查询是否收藏
                        ResponseModel collectionResponseModel=collectionService.getCollectionByNewsId(uid,nid);
                        if(collectionResponseModel.getCode()==200){
                            result.setCollection((Boolean) collectionResponseModel.getResponseData());
                        }
                    }else{
                        addError("非法请求,传入的签名无效!");
                    }
                }else{
                    addError("非法请求,传入的签名无效!");
                }

            }
            addSuccess(result,"获取咨询详情成功");
        } catch (Exception e) {
            e.printStackTrace();
            addError("服务器异常:"+e.getMessage());
            return SUCCESS;
        }
        return SUCCESS;
    }
}
