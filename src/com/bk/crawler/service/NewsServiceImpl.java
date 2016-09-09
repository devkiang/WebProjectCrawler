package com.bk.crawler.service;

import com.bk.crawler.dao.NewsDAO;
import com.bk.crawler.dao.NewsDAOImpl;
import com.bk.crawler.entity.News;

import java.util.List;

/**
 * Created by shikee_app03 on 16/7/15.
 */
public class NewsServiceImpl implements NewsService {
    private NewsDAO newsDAO;
    public NewsServiceImpl(){
        newsDAO=new NewsDAOImpl();
    }
    @Override
    public List<News> list(int page, int size) {
        return newsDAO.list(page,size);
    }

    @Override
    public News findNewsByNid(long Nid) {
        return newsDAO.findNewsByNid(Nid);
    }
}
