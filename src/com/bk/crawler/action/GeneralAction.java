package com.bk.crawler.action;

import com.bk.crawler.entity.ResponseModel;
import com.bk.crawler.service.CategoryService;
import com.bk.crawler.service.CategoryServiceImpl;

/**
 * Created by shikee_app03 on 16/10/9.
 */
public class GeneralAction extends CrawlerBaseAction{

    private CategoryService service=new CategoryServiceImpl();
    public String category(){
        ResponseModel responseModel=service.getCategory();
        if(responseModel.getCode()==200){
            addSuccess(responseModel.getResponseData(),responseModel.getMsg());
        }else{
            addError(responseModel.getMsg());
        }
        return SUCCESS;
    }
}
