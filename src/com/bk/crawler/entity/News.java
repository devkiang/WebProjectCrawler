package com.bk.crawler.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by shikee_app03 on 16/7/15.
 */
@Entity(name = "NewsModel")
public class News implements Serializable{
    private long id;
    private String title;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
    @Column(columnDefinition="TEXT")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getPageViews() {
        return pageViews;
    }

    public void setPageViews(long pageViews) {
        this.pageViews = pageViews;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="category_id",nullable = false)
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Transient
    public boolean isCollection() {
        return isCollection;
    }

    public void setCollection(boolean collection) {
        isCollection = collection;
    }

    private String time;
    private String content;
    private String url;
    private String source;
    private long pageViews;
    private Category category;
    private boolean isCollection;//用于用户是否被标记收藏而已 不映射到数据库字段

}
