package com.bk.crawler.toolkit;

import com.bk.crawler.dao.HibernateTemplate;
import com.bk.crawler.entity.NameModel;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shikee_app03 on 16/9/9.
 */
public class NamePlug extends HibernateTemplate{
    public void addNameModelByTxtFilePath(String path)
    {
        FileHelper fileHelper=new FileHelper();
        List<String> contents=fileHelper.getAllContent(path);
        List<NameModel> nameModelList=new ArrayList<NameModel>();
        for (String value:contents) {
            NameModel nameModel=new NameModel();
            nameModel.setName_value(value);
            nameModelList.add(nameModel);
        }
        this.addNameModelList(nameModelList);
    }


    public void addNameModelList(List<NameModel> nameModelList)
    {
        Session s= getSession();
        Transaction t=s.beginTransaction();
        try {
            for (NameModel m:nameModelList) {
                XLog.debug(m.getName_value());
                s.save(m);
            }
            t.commit();
        } catch (Exception e) {
            e.printStackTrace();
            t.rollback();
        } finally {
            s.close();
            nameModelList=null;
        }

    }

    public static  void  main(String[] arg)
    {
        NamePlug plug=new NamePlug();
        plug.addNameModelByTxtFilePath("/Users/shikee_app03/Desktop/kiang/name.txt");
    }
}
