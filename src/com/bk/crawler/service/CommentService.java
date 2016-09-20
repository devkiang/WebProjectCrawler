package com.bk.crawler.service;

import com.bk.crawler.entity.Comment;
import com.bk.crawler.entity.ResponseModel;

/**
 * Created by shikee_app03 on 16/9/19.
 */
public interface CommentService {

    public ResponseModel getCommentList(Long news_id,int size,int page);
    public ResponseModel addComment(Comment comment);
    public ResponseModel getSubCommentList(Long parent_id,int size,int page);
}
