package com.bk.crawler.dao;

import com.bk.crawler.entity.Comment;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

/**
 * Created by shikee_app03 on 16/9/19.
 */
public class CommentDAOImpl extends HibernateTemplate implements CommentDAO {

    @Override
    public boolean addComment(Comment comment) {
        boolean result=false;
        Session s = getSession();
        Transaction t = s.beginTransaction();
        try {
            s.save(comment);
            t.commit();
            result=true;
        } catch (Exception e) {
            t.rollback();
            e.printStackTrace();
        } finally {
            s.close();
        }
        return result;
    }

    @Override
    public List<Comment> getCommentListByUid(Long uid, int size, int page) {
        Session s = getSession();
        List<Comment> result=null;
        try {
            Query q=s.createQuery("from Comment as c where c.user.id=?");
            q.setMaxResults(size);
            q.setFirstResult(page*size);
            q.setParameter(0,uid);
            List tmp=q.list();
            if(tmp.size()>0){
                result=tmp;
            }
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            s.close();
        }
        return result;
    }

    @Override
    public List<Comment> getCommentListByNewsId(Long newsid, int size, int page) {
        Session s = getSession();
        List<Comment> result=null;
        try {
            Query q=s.createQuery("from Comment as c where c.news.id=? and c.parent_id=? order by c.report_time desc ");
            q.setMaxResults(size);
            q.setFirstResult(page*size);
            q.setParameter(0,newsid);
            q.setParameter(1,0L);
            List tmp=q.list();
            if(tmp.size()>0){
                result=tmp;
            }
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            s.close();
        }
        return result;
    }

    @Override
    public List<Comment> getCommentListByCommentId(Long parent_id, int size, int page) {
        Session s = getSession();
        List<Comment> result=null;
        try {
            Query q=s.createQuery("from Comment as c where c.parent_id=?");
            q.setMaxResults(size);
            q.setFirstResult(page*size);
            q.setParameter(0,parent_id);
            List tmp=q.list();
            if(tmp.size()>0){
                result=tmp;
            }
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            s.close();
        }
        return result;
    }

    @Override
    public boolean deleteComment(Long commentid) {
        return false;
    }
}
