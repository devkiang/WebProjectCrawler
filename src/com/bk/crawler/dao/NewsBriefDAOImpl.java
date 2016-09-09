package com.bk.crawler.dao;
import com.bk.crawler.entity.NewsBrief;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.jdbc.Expectation;

import java.util.List;

/**
 * Created by shikee_app03 on 16/7/20.
 */
public class NewsBriefDAOImpl extends HibernateTemplate implements NewsBriefDAO{
    @Override
    public List<NewsBrief> list(int page, int size) {
        Session s=getSession();
        List<NewsBrief> result=null;
        try {
            Query q=s.createQuery("from NewsBriefModel as nbm where nbm.category.cid=1 order by  nbm.time desc ");
            q.setFirstResult(page*size);
            q.setMaxResults(size);
            result = q.list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            s.close();
        }
        return result;
    }

    @Override
    public NewsBrief getNewsBriefByNewsId(Long nid) {
        Session s= getSession();
        NewsBrief result=null;
        try {
            Query q = s.createQuery("from NewsBriefModel as nbm where nbm.nId=?");
            q.setParameter(0,nid);
            List list=q.list();
            if(list.size()>0){
                result = (NewsBrief) list.get(0);
            }
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            s.close();
        }
        return result;
    }

    @Override
    public List<NewsBrief> getNewsBriefListByNewsIdList(List<Long> news_id_list) {
        Session s= getSession();
        List<NewsBrief> result=null;
        try {
            StringBuffer tmp_locateStr=new StringBuffer("_");
            for (Long news_id:news_id_list) {
                tmp_locateStr.append(news_id+"_");
            }
            String locateStr=tmp_locateStr.toString();
//            SQLQuery q=s.createSQLQuery("SELECT * FROM NewsBriefModel as nbm where nbm.nId in (:news_id_list) ORDER BY locate(nbm.nId,:locateStr)");
            Query q = s.createQuery("from NewsBriefModel as nbm where nbm.nId in (:news_id_list) ORDER BY locate(concat('_',nbm.nId,'_') ,:locateStr)");
            q.setParameterList("news_id_list",news_id_list);
            q.setParameter("locateStr",locateStr);
            List list=q.list();
            if(list.size()>0){
                result=list;
            }
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            s.close();
        }
        return result;
    }

}
