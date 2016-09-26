package com.bk.crawler.action;

import com.bk.crawler.entity.Comment;
import com.bk.crawler.entity.News;
import com.bk.crawler.entity.ResponseModel;
import com.bk.crawler.entity.Users;
import com.bk.crawler.service.CommentService;
import com.bk.crawler.service.CommentServiceImpl;
import com.bk.crawler.toolkit.Toolkit;

import java.util.List;

/**
 * Created by shikee_app03 on 16/9/19.
 */
public class CommentAction extends CrawlerBaseAction{
    private Toolkit toolkit=new Toolkit();
    private CommentService commentService=new CommentServiceImpl();
    public String reportComment(){
        Long uid=toolkit.convert2Long(paramMap.get("uid"));
        String content=toolkit.convert2String(paramMap.get("content"));
        Long parent_id=toolkit.convert2Long(paramMap.get("parent_id"));
        Long news_id=toolkit.convert2Long(paramMap.get("nid"));
        Long root_parent_id=toolkit.convert2Long(paramMap.get("root_parent_id"));
        Comment comment=new Comment();
        Users user=new Users();
        user.setUid(uid);
        News news=new News();
        news.setId(news_id);
        comment.setContent(content);
        comment.setParent_id(parent_id);
        comment.setRoot_parent_id(root_parent_id);
        comment.setUser(user);
        comment.setNews(news);
        ResponseModel responseModel=commentService.addComment(comment);
        if(responseModel.getCode()==200){
            addSuccess(responseModel.getResponseData(),responseModel.getMsg());
        }else{
            addError(responseModel.getMsg());
        }
        return SUCCESS;
    }

    public String commentList(){
        Long news_id=toolkit.convert2Long(paramMap.get("nid"));
        int size=toolkit.convert2Int(paramMap.get("size"));
        int page=toolkit.convert2Int(paramMap.get("page"));
        ResponseModel responseModel=commentService.getCommentList(news_id,size,page);
        if(responseModel.getCode()==200){
            addSuccess(responseModel.getResponseData(),responseModel.getMsg());
        }else{
            addError(responseModel.getMsg());
        }
        return SUCCESS;
    }

    public String subCommentList(){
        int size=toolkit.convert2Int(paramMap.get("size"));
        int page=toolkit.convert2Int(paramMap.get("page"));
        Long root_parent_id=toolkit.convert2Long(paramMap.get("root_parent_id"));
        ResponseModel responseModel=commentService.getRootChildCommentList(root_parent_id,size,page);
        if(responseModel.getCode()==200){
            addSuccess(responseModel.getResponseData(),responseModel.getMsg());
        }else{
            addError(responseModel.getMsg());
        }
        return SUCCESS;
    }

    public String deleteComment(){


        return SUCCESS;
    }

    public String editComment(){

        return SUCCESS;
    }
}
