package com.bk.crawler.action;

import com.bk.crawler.entity.ResponseModel;
import com.bk.crawler.service.CollectionService;
import com.bk.crawler.service.CollectionServiceImpl;
import com.bk.crawler.toolkit.Toolkit;

/**
 * Created by shikee_app03 on 16/9/8.
 */
public class CollectionAction extends CrawlerBaseAction{

    private CollectionService collectionService=new CollectionServiceImpl();
    Toolkit t=new Toolkit();
    public String userCollection(){

        Long uid=t.convert2Long(paramMap.get("uid"));
        int page=t.convert2Int(paramMap.get("page"));
        int size=t.convert2Int(paramMap.get("size"));
        if(uid==null||uid<1L) {
            addError("非法请求");
            return SUCCESS;
        }

        ResponseModel responseModel=collectionService.getCollectionListByUid(uid,page,size);
        if(responseModel.getCode()==200){
            addSuccess(responseModel.getResponseData(),responseModel.getMsg());
        }else{
            addError(responseModel.getMsg());
        }
        return SUCCESS;
    }

    public String deleteUserCollection(){
        Long uid = t.convert2Long(paramMap.get("uid"));
        Long nid=t.convert2Long(paramMap.get("nid"));
        if(uid==null||uid<1||nid==null||nid<1) {
            addError("非法请求");
            return SUCCESS;
        }

        ResponseModel responseModel=collectionService.deleteCollectionByNewsid(uid,nid);
        if(responseModel.getCode()==200){
            addSuccess(responseModel.getResponseData(),responseModel.getMsg());
        }else{
            addError(responseModel.getMsg());
        }
        return SUCCESS;
    }

    public String addCollection(){
        Long uid = t.convert2Long(paramMap.get("uid"));
        Long nid=t.convert2Long(paramMap.get("nid"));
        if(uid==null||uid<1||nid==null||nid<1) {
            addError("非法请求");
            return SUCCESS;
        }
        ResponseModel responseModel=collectionService.addCollection(uid,nid);
        if(responseModel.getCode()==200){
            addSuccess(responseModel.getResponseData(),responseModel.getMsg());
        }else{
            addError(responseModel.getMsg());
        }
        return SUCCESS;
    }
}
