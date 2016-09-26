package com.bk.crawler.dao;

import com.bk.crawler.entity.Comment;
import com.bk.crawler.entity.News;
import com.bk.crawler.entity.NewsBrief;
import org.hibernate.*;

import java.util.List;

/**
 * Created by shikee_app03 on 16/9/19.
 */
public class CommentDAOImpl extends HibernateTemplate implements CommentDAO {

    private NewsDAO newsDAO=new NewsDAOImpl();
    private NewsBriefDAO newsBriefDAO=new NewsBriefDAOImpl();
    @Override
    public boolean addComment(Comment comment) {
        boolean result=false;
        Session s = getSession();
        Transaction t = s.beginTransaction();
        try {
            s.save(comment);
            t.commit();
            this.countReportNum(comment);
            result=true;
        } catch (Exception e) {
//            t.rollback();
            e.printStackTrace();
        } finally {
            s.close();
        }
        return result;
    }


    /**
     * 统计+1当前回复
     *
     * */
    private Long countReportNum(Comment comment)
    {
        Long result=0L;
        Long cid=0L;
        if(comment.getParent_id()==0){//一级评论
            cid=comment.getComment_id();
            Long countNum=this.getCommentCount(comment.getNews().getId());
            result=countNum;
            News news=newsDAO.findNewsByNid(comment.getNews().getId());
            news.setCommentCount(countNum);
            Session s = getSession();
            Transaction t = s.beginTransaction();
            try {
                s.update(news);
                t.commit();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                s.close();
            }
            Session s2=getSession();
            Transaction t2=s2.beginTransaction();
            try {
                NewsBrief newsBrief=newsBriefDAO.getNewsBriefByNewsId(news.getId());
                newsBrief.setCommentCount(countNum);
                s2.update(newsBrief);
                t2.commit();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                s2.close();
            }

            return result;
        }else{
            cid=comment.getRoot_parent_id();
        }
        Session s = getSession();
        Transaction t = s.beginTransaction();
        try {
//
//            SQLQuery q=s.createSQLQuery("select count(comment_id) from Comment where FIND_IN_SET(comment_id,getCommentChild(:cid));");
//            q.setParameter("cid",cid);
//            List list=q.list();
//            result= Long.parseLong(list.get(0).toString());
            Comment rootComment= (Comment) s.get(Comment.class,comment.getRoot_parent_id());
            Long reportCount=rootComment.getReportCount();
            if(reportCount==null){
                reportCount=0L;
            }
            result=reportCount+1L;
            rootComment.setReportCount(result);
            s.update(rootComment);
            t.commit();
        } catch (Exception e) {
//            t.rollback();
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
//            Query q=s.createQuery("from Comment as c where c.parent_id=?");
            SQLQuery q=s.createSQLQuery("select * from Comment where FIND_IN_SET(comment_id,getCommentChild(:parent_id))").addEntity(Comment.class);
            q.setMaxResults(size);
            q.setFirstResult(page*size);
            q.setParameter("parent_id",parent_id);
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

    @Override
    public List<Comment> getRootChildCommentListByRootId(Long root_parent_id, int size, int page) {
        Session s = getSession();
        List<Comment> result=null;
        try {
            Query q=s.createQuery("from Comment as c where c.root_parent_id=?");
            q.setMaxResults(size);
            q.setFirstResult(page*size);
            q.setParameter(0,root_parent_id);
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
    public Long getCommentCount(Long news_id) {
        Session s = getSession();
        Long result=null;
        try {
            Query q=s.createQuery("select count (c.comment_id) from Comment as c where c.news.id=? and c.parent_id=0");
            q.setParameter(0,news_id);
            result=Long.parseLong((q.iterate().next()).toString());
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            s.close();
        }
        return result;
    }
}
