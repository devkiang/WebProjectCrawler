package com.bk.crawler.dao;

import com.bk.crawler.entity.NameModel;
import com.bk.crawler.entity.Users;
import org.hibernate.*;

import java.util.List;

/**
 * Created by shikee_app03 on 16/8/31.
 */
public class UserDAOImpl extends HibernateTemplate implements  UserDAO {
    @Override
    public Users getUserByUid(String uid) {
        return null;
    }

    @Override
    public boolean checkAccount(String account) {
        boolean result=true;
        Session s=getSession();
        try {
            Query q=s.createQuery("from  Users  as u where u.account = ?");
            q.setParameter(0,account);
            List<Users> usersList=q.list();
            if(usersList.size()>0){
                result=false;
            }
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            s.close();
        }
        return result;
    }

    @Override
    public Users getUserByAccountAndPassword(String account, String password) {
        Users result=null;
        Session s=getSession();
        try {
            Query q=s.createQuery("from Users as u where u.account =? and  password = ?");
            q.setParameter(0,account);
            q.setParameter(1,password);
            List<Users> usersList=q.list();
            if(usersList.size()>0){
                result=usersList.get(0);
//                Hibernate.initialize(result.getCollections());
//                Hibernate.initialize(result.getComment_list());
            }
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            s.close();
        }
        return result;
    }

    @Override
    public Users getUserByUidAndToken(String account, String token) {
        return null;
    }

    @Override
    public Users save(Users user) {
        Users result=null;
        Session s=getSession();
        try {
            Transaction t=s.beginTransaction();
            s.save(user);
            t.commit();
            result=user;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            s.close();
        }
        return result;
    }

    @Override
    public String getRandomName() {
        Session s=getSession();
        Transaction t=s.beginTransaction();
        SQLQuery q=s.createSQLQuery("select * from users order by rand() LIMIT 1");
        List<NameModel> result=q.list();
        if(result.size()<1){
            return null;
        }
        NameModel nameModel=result.get(0);
        return nameModel.getName_value();
    }

}
