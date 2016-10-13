package com.bk.crawler.service;

import com.bk.crawler.dao.NewsBriefDAO;
import com.bk.crawler.dao.NewsBriefDAOImpl;
import com.bk.crawler.entity.NewsBrief;
import com.bk.crawler.entity.ResponseModel;
import java.util.List;

/**
 * Created by shikee_app03 on 16/7/20.
 */
public class NewsBriefServiceImpl implements NewsBriefService{
    private NewsBriefDAO newsBriefDAO;
    public NewsBriefServiceImpl(){
        newsBriefDAO=new NewsBriefDAOImpl();
    }

    @Override
    public ResponseModel list(int page, int size) {
        ResponseModel responseModel=new ResponseModel();
        try {
            List result=newsBriefDAO.list(page,size);
            responseModel.setSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
            responseModel.setFail("服务器异常");
        } finally {
        }
        return responseModel;
    }

    @Override
    public ResponseModel getNewsBriefListBy(List<Long> newsIdList) {
        ResponseModel responseModel=new ResponseModel();
        List<NewsBrief> newsBriefList= newsBriefDAO.getNewsBriefListByNewsIdList(newsIdList);
        if(newsBriefList==null){
            responseModel.setFail("查询失败,没有查询到相关数据!");
        }else{
            responseModel.setSuccess(newsBriefList);
        }
        return responseModel;
    }

    @Override
    public ResponseModel getNewsBriefListByCategoryId(Long cid,int page,int size) {
        ResponseModel responseModel=new ResponseModel();
        List<NewsBrief> result=newsBriefDAO.getListByCategoryId(cid,page,size);
        responseModel.setSuccess(result);
        return responseModel;
    }
}
