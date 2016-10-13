package com.bk.crawler.dao;

import com.bk.crawler.entity.Category;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.util.List;

/**
 * Created by shikee_app03 on 16/10/9.
 */
public class CategoryDAOImpl extends HibernateTemplate implements CategoryDAO {
    @Override
    public List<Category> getCategory() {
        Session s=getSession();
        List result= null;
        try {
            result = s.createQuery("from Category ").list();
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            s.close();
        }
        return result;
    }
}
