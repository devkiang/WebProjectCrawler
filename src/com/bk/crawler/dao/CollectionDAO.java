package com.bk.crawler.dao;

import com.bk.crawler.entity.Collection;

import java.util.List;

/**
 * Created by shikee_app03 on 16/9/8.
 */
public interface CollectionDAO {
    public List<Collection> getCollectionByUid(Long uid,int page,int size);
    public boolean deleteCollectionByNewsid(Long uid,Long nid);
    public boolean deleteCollectionByCollectionid(Long uid,Long cid);
    public boolean deleteUserAllCollection(Long uid);
    public boolean addUserCollection(Long uid,Long nid);
    public Collection getCollectionWith(Long uid,Long nid);
}
