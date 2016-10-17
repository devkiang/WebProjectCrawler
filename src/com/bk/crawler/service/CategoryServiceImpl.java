package com.bk.crawler.service;

import com.bk.crawler.dao.CategoryDAO;
import com.bk.crawler.dao.CategoryDAOImpl;
import com.bk.crawler.entity.Category;
import com.bk.crawler.entity.ResponseModel;

import java.util.List;

/**
 * Created by shikee_app03 on 16/10/9.
 */
public class CategoryServiceImpl implements CategoryService{

    private CategoryDAO categoryDAO=new CategoryDAOImpl();
    @Override
    public ResponseModel getCategory() {
        ResponseModel responseModel=new ResponseModel();
        List<Category> result=categoryDAO.getCategory();
        Category allCategory=new Category();
        allCategory.setCid(0);
        allCategory.setName("全部");
        result.add(0,allCategory);
        responseModel.setSuccess(result);
        return responseModel;
    }
}
