package com.bk.crawler.service;

import com.bk.crawler.dao.CollectionDAO;
import com.bk.crawler.dao.CollectionDAOImpl;
import com.bk.crawler.entity.Collection;
import com.bk.crawler.entity.News;
import com.bk.crawler.entity.NewsBrief;
import com.bk.crawler.entity.ResponseModel;
import com.bk.crawler.toolkit.XLog;
import com.sun.net.httpserver.Authenticator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shikee_app03 on 16/9/8.
 */
public class CollectionServiceImpl implements CollectionService {
    CollectionDAO collectionDAO=new CollectionDAOImpl();
    private NewsBriefService newsBriefService= new NewsBriefServiceImpl();
    @Override
    public ResponseModel getCollectionListByUid(Long uid,int page,int size) {
        ResponseModel responseModel=new ResponseModel();
        if(page<0){
            page=0;
        }
        if(size<0){
            size=20;
        }
        if(uid==null||uid<1){
            responseModel.setFail("非法请求");
        }else{
            List<Collection> result=collectionDAO.getCollectionByUid(uid,page,size);
            if(result==null&&page==0){
                responseModel.setFail("您还没有收藏文章噢!o(╯□╰)o");
            }else if(result==null){
                responseModel.setSuccess(null);
            }else{
                List<Long> news_id_list=new ArrayList<Long>();
                for (Collection collection:result) {
                    news_id_list.add(collection.getNews().getId());
                }
                ResponseModel newsbriefResponseModel =newsBriefService.getNewsBriefListBy(news_id_list);
                if(newsbriefResponseModel.getCode()==200){
                    List<NewsBrief> newsBriefList= (List<NewsBrief>) newsbriefResponseModel.getResponseData();
                    responseModel.setSuccess(newsBriefList);
                }else{
                    responseModel.setFail("您的网络异常");
                }
            }
        }
        return responseModel;
    }

    @Override
    public ResponseModel deleteCollectionByNewsid(Long uid, Long newsId) {
        ResponseModel responseModel=new ResponseModel();
        if(uid==null||uid<1||newsId==null||newsId<1){
            responseModel.setFail("非法请求");
        }else{
            boolean result=collectionDAO.deleteCollectionByNewsid(uid,newsId);
            if(result==false){
                responseModel.setFail("删除失败");
            }else{
                responseModel.setSuccess(null,"删除成功");
            }
        }
        return responseModel;
    }

    @Override
    public ResponseModel deleteCollectionByCollectionid(Long uid, Long cid) {
        ResponseModel responseModel=new ResponseModel();
        if(uid==null||uid<1||cid==null||cid<1){
            responseModel.setFail("非法请求");
        }else{
            boolean result=collectionDAO.deleteCollectionByCollectionid(uid,cid);
            if(result==false){
                responseModel.setFail("删除失败");
            }else{
                responseModel.setSuccess(null,"删除成功");
            }
        }
        return responseModel;
    }

    @Override
    public ResponseModel deleteUserAllCollection(Long uid) {
        ResponseModel responseModel=new ResponseModel();
        if(uid==null||uid<1){
            responseModel.setFail("非法请求");
        }else{
            boolean result=collectionDAO.deleteUserAllCollection(uid);
            if(result==false){
                responseModel.setFail("删除失败");
            }else{
                responseModel.setSuccess(null,"删除成功");
            }
        }
        return responseModel;
    }

    @Override
    public ResponseModel addCollection(Long uid, Long nid) {
        ResponseModel responseModel=new ResponseModel();
        if(uid==null||uid<1||nid==null||nid<1){
            responseModel.setFail("非法请求");
        }else{
            Collection db_collection=collectionDAO.getCollectionWith(uid,nid);
            if(db_collection!=null){
                responseModel.setFail("已经收藏");
                return responseModel;
            }

            boolean result=collectionDAO.addUserCollection(uid,nid);
            if(result==false){
                responseModel.setFail("添加收藏失败");
            }else{
                responseModel.setSuccess(null,"添加收藏成功");
            }
        }
        return responseModel;
    }

    @Override
    public ResponseModel getCollectionByNewsId(Long uid, Long nid) {
        ResponseModel responseModel=new ResponseModel();
        if(uid==null||uid<1||nid==null||nid<1){
            responseModel.setFail("非法请求");
        }else{
            Collection db_collection=collectionDAO.getCollectionWith(uid,nid);
            if(db_collection!=null){
                responseModel.setSuccess("已经收藏",true);
                return responseModel;
            }else{
                responseModel.setSuccess("没有收藏",false);
            }
        }
        return responseModel;
    }
}
