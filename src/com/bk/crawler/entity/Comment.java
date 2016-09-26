package com.bk.crawler.entity;

import javax.persistence.*;

/**
 * Created by shikee_app03 on 16/8/30.
 */
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public Long getComment_id() {
        return comment_id;
    }

    public void setComment_id(Long comment_id) {
        this.comment_id = comment_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getParent_id() {
        return parent_id;
    }

    public void setParent_id(Long parent_id) {
        this.parent_id = parent_id;
    }

    @ManyToOne(fetch=FetchType.EAGER)
    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public String getReport_time() {
        return report_time;
    }

    public void setReport_time(String report_time) {
        this.report_time = report_time;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }

    public Long getReportCount() {
        return reportCount;
    }
    public void setReportCount(Long reportCount) {
        this.reportCount = reportCount;
    }
    public Long getRoot_parent_id() {
        return root_parent_id;
    }
    public void setRoot_parent_id(Long root_parent_id) {
        this.root_parent_id = root_parent_id;
    }
    private Long comment_id;
    private String content;
    private Long parent_id;
    private Users user;
    private String report_time;
    private News news;
    private Long root_parent_id;
    private Long reportCount;

}
