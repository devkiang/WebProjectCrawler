package com.bk.crawler.service;

import com.bk.crawler.entity.ResponseModel;

/**
 * Created by shikee_app03 on 16/9/8.
 */
public interface CollectionService {
    public ResponseModel getCollectionListByUid(Long uid,int page,int size);
    public ResponseModel deleteCollectionByNewsid(Long uid,Long newsId);
    public ResponseModel deleteCollectionByCollectionid(Long uid,Long cid);
    public ResponseModel deleteUserAllCollection(Long uid);
    public ResponseModel addCollection(Long uid ,Long nid);
    public ResponseModel getCollectionByNewsId(Long uid,Long nid);
}
