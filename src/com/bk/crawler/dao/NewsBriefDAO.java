package com.bk.crawler.dao;


import com.bk.crawler.entity.NewsBrief;

import java.util.List;

/**
 * Created by shikee_app03 on 16/7/20.
 */
public interface NewsBriefDAO {
    public List<NewsBrief> list(int page, int size);
    public NewsBrief getNewsBriefByNewsId(Long nid);
    public List<NewsBrief> getNewsBriefListByNewsIdList(List<Long> news_id_list);
}
