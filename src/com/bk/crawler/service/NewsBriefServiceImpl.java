package com.bk.crawler.service;

import com.bk.crawler.dao.NewsBriefDAO;
import com.bk.crawler.dao.NewsBriefDAOImpl;
import com.bk.crawler.entity.NewsBrief;
import com.bk.crawler.entity.ResponseModel;

import java.util.ArrayList;
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
    public List<NewsBrief> list(int page, int size) {
        return newsBriefDAO.list(page,size);
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
}
