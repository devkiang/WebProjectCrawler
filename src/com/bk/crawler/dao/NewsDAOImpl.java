package com.bk.crawler.dao;

import com.bk.crawler.entity.News;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

/**
 * Created by shikee_app03 on 16/7/15.
 */
public class NewsDAOImpl extends HibernateTemplate implements NewsDAO {
    @Override
    public List<News> list(int page, int size) {
        Session s=getSession();
        List<News> result;
        try {
            Query q=s.createQuery("from NewsModel as news");
            q.setFirstResult(page*size);
            q.setMaxResults(size);
            result = q.list();
        } finally {
            s.close();
        }
        return result;
    }


    @Override
    public News findNewsByNid(long nId) {
        Session s=getSession();
        News news=null;
        try {
            Long lNid=0L+nId;
            Query q=s.createQuery("from NewsModel as nm where nm.id=?");
            q.setParameter(0,nId);
            List<News> result=(List<News>) q.list();
            if(result.size()>0){
                news=result.get(0);
                return news;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            s.close();
        }
        return news;
    }

}
