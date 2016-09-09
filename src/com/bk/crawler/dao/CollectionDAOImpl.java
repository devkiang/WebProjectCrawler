package com.bk.crawler.dao;

import com.bk.crawler.entity.Collection;
import com.bk.crawler.toolkit.XLog;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

/**
 * Created by shikee_app03 on 16/9/8.
 */
public class CollectionDAOImpl extends  HibernateTemplate implements  CollectionDAO{
    @Override
    public List<Collection> getCollectionByUid(Long uid,int page,int size) {
        Session s=getSession();
        List<Collection> result=null;
        try {
            Query q=s.createQuery("from Collection as c where c.user.uid=? order by c.collection_id desc");
            q.setParameter(0,uid);
            q.setFirstResult(page*size);
            q.setMaxResults(size);
            result=q.list();
            if(result.size()<1){
                result=null;
            }
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            s.close();
        }
        return result;
    }

    @Override
    public boolean deleteCollectionByNewsid(Long uid, Long nid) {
        boolean result=false;
        Session s=getSession();
        Transaction t=s.beginTransaction();
        try {
            Query q=s.createQuery("delete from Collection as c where c.user.uid=? and c.news.id=?");
            q.setParameter(0,uid);
            q.setParameter(1,nid);
            q.executeUpdate();
            t.commit();
            result=true;
        } catch (HibernateException e) {
            t.rollback();
            e.printStackTrace();
        } finally {
            s.close();
        }
        return result;
    }

    @Override
    public boolean deleteCollectionByCollectionid(Long uid, Long cid) {
        boolean result=false;
        Session s=getSession();
        Transaction t=s.beginTransaction();
        try {
            Query q=s.createQuery("delete from Collection as c where c.user.uid=? and c.id=?");
            q.setParameter(0,uid);
            q.setParameter(1,cid);
            q.executeUpdate();
            t.commit();
            result=true;
        } catch (HibernateException e) {
            t.rollback();
            e.printStackTrace();
        } finally {
            s.close();
        }
        return result;
    }

    @Override
    public boolean deleteUserAllCollection(Long uid) {
        boolean result=false;
        Session s=getSession();
        Transaction t=s.beginTransaction();
        try {
            Query q=s.createQuery("delete from Collection as c where c.user.uid=?");
            q.setParameter(0,uid);
            q.executeUpdate();
            t.commit();
            result=true;
        } catch (HibernateException e) {
            t.rollback();
            e.printStackTrace();
        } finally {
            s.close();
        }
        return result;
    }

    @Override
    public boolean addUserCollection(Long uid, Long nid) {
        boolean result=false;
        Session s=getSession();
        Transaction t=s.beginTransaction();
        try {
            Query q=s.createQuery("insert into Collection(user,news) select u,n from Users as u,NewsModel as n where u.uid=? and n.id=?");
            q.setParameter(0,uid);
            q.setParameter(1,nid);
            q.executeUpdate();
            t.commit();
            result=true;
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            s.close();
        }
        return result;
    }

    @Override
    public Collection getCollectionWith(Long uid, Long nid) {
        Collection result=null;
        Session s=getSession();
        Query q = s.createQuery("from Collection as c where c.user.uid=? and c.news.id=? ");
        try {
            q.setParameter(0,uid);
            q.setParameter(1,nid);
            List tmp_result=q.list();
            if(tmp_result.size()>0){
                result= (Collection) tmp_result.get(0);
            }
        } catch (HibernateException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } finally {
            s.close();
        }
        return result;
    }
}
