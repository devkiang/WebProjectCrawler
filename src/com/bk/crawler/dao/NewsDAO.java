package com.bk.crawler.dao;

import com.bk.crawler.entity.News;

import java.util.List;

/**
 * Created by shikee_app03 on 16/7/15.
 */
public interface NewsDAO {
    public List<News> list(int page, int size);
    public News findNewsByNid(long nId);
}
