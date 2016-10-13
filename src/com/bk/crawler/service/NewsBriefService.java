package com.bk.crawler.service;

import com.bk.crawler.entity.NewsBrief;
import com.bk.crawler.entity.ResponseModel;

import java.util.List;

/**
 * Created by shikee_app03 on 16/7/20.
 */
public interface NewsBriefService {
    public ResponseModel list(int page, int size);
    public ResponseModel getNewsBriefListBy(List<Long> newsIdList);
    public ResponseModel getNewsBriefListByCategoryId(Long cid,int page,int size);
}
