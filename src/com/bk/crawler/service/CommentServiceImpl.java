package com.bk.crawler.service;

import com.bk.crawler.dao.CommentDAO;
import com.bk.crawler.dao.CommentDAOImpl;
import com.bk.crawler.entity.Comment;
import com.bk.crawler.entity.ResponseModel;
import com.bk.crawler.toolkit.Toolkit;
import com.bk.crawler.toolkit.XLog;

import java.util.List;

/**
 * Created by shikee_app03 on 16/9/19.
 */
public class CommentServiceImpl implements CommentService{
    private CommentDAO commentDAO=new CommentDAOImpl();
    private Toolkit toolkit =new Toolkit();
    @Override
    public ResponseModel getCommentList(Long news_id, int size, int page) {
        ResponseModel responseModel=new ResponseModel();
        if(news_id==null||news_id<1){
            responseModel.setFail("非法请求");
        }
        if(size<1){
            size=20;
        }
        if(page<0){
            page=0;
        }
        List<Comment> result=commentDAO.getCommentListByNewsId(news_id,size,page);
//        if(result!=null){
//            for (Comment obj :result) {
//                obj.setContent(toolkit.bin2Str(obj.getContent()));
//            }
//        }
        responseModel.setSuccess(result);
        return responseModel;
    }

    @Override
    public ResponseModel addComment(Comment comment) {
        ResponseModel responseModel=new ResponseModel();
        if(comment==null||comment.getContent()==null||comment.getContent().isEmpty()){
            responseModel.setFail("不能提交空的评论");
        }else{
            comment.setContent(comment.getContent().replace("\n"," "));
            comment.setContent(comment.getContent().replace("\r"," "));
            comment.setContent(comment.getContent().replace("\r\n"," "));
            comment.setContent(comment.getContent().replace("  "," "));
            comment.setContent(comment.getContent().replace("  "," "));
            comment.setContent(comment.getContent().replace("  "," "));
            if(comment.getContent().length()>100){
                responseModel.setFail("评论内容不能超过100字");
            }else if(comment.getUser()==null||comment.getUser().getUid()==null){
                responseModel.setFail("用户没有登录");
            }else if(comment.getNews()==null||comment.getNews().getId()<1){
                responseModel.setFail("回复失败,新闻已经被删除或者屏蔽");
            }else if(comment.getParent_id()>0&&(comment.getRoot_parent_id()==null||comment.getRoot_parent_id()<1)) {
                responseModel.setFail("非法回复,二级回复请传入rootid");
            }else {
                comment.setReport_time(toolkit.getCurrSimestamp()+"");
//            String bin_content=toolkit.str2Bin(comment.getContent());
//            comment.setContent(bin_content);
                boolean falg=commentDAO.addComment(comment);
                if(falg){
                    responseModel.setSuccess("回复成功");
                }else {
                    responseModel.setFail("回复失败,服务器超时");
                }
            }
        }
        return responseModel;
    }

    @Override
    public ResponseModel getSubCommentList(Long parent_id, int size, int page) {
        ResponseModel responseModel=new ResponseModel();
        if(parent_id==null||parent_id<1){
            responseModel.setFail("非法请求");
            return responseModel;
        }
        if(size<1){
            size=20;
        }
        if(page<0){
            page=0;
        }
        List<Comment> result=commentDAO.getCommentListByCommentId(parent_id,size,page);
//        if(result!=null){
//            for (Comment obj :result) {
//                obj.setContent(toolkit.bin2Str(obj.getContent()));
//            }
//        }

        responseModel.setSuccess(result);
        return responseModel;
    }

    @Override
    public ResponseModel getRootChildCommentList(Long root_parent_id, int size, int page) {
        ResponseModel responseModel=new ResponseModel();
        if(root_parent_id==null||root_parent_id<1){
            responseModel.setFail("非法请求");
            return responseModel;
        }
        if(size<1){
            size=20;
        }
        if(page<0){
            page=0;
        }
        List<Comment> result=commentDAO.getRootChildCommentListByRootId(root_parent_id,size,page);
//        if(result!=null){
//            for (Comment obj :result) {
//                obj.setContent(toolkit.bin2Str(obj.getContent()));
//            }
//        }
        responseModel.setSuccess(result);
        return responseModel;
    }
}
