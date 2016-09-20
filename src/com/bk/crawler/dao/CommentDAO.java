package com.bk.crawler.dao;

import com.bk.crawler.entity.Comment;

import java.util.List;

/**
 * Created by shikee_app03 on 16/9/19.
 */
public interface CommentDAO {
    public boolean addComment(Comment comment);
    public List<Comment> getCommentListByUid(Long uid, int size, int page);
    public List<Comment> getCommentListByNewsId(Long ınewsid,int size,int page);
    public List<Comment> getCommentListByCommentId(Long parent_id,int size,int page);
    /*
    * 如果传来的commentid下面还有子节点的话,子节点也会被一并删除
    * */
    public boolean deleteComment(Long commentid);
}
