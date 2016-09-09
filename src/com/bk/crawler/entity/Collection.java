package com.bk.crawler.entity;

import javax.persistence.*;

/**
 * Created by shikee_app03 on 16/8/30.
 */
@Entity
public class Collection {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public Long getCollection_id() {
        return collection_id;
    }

    public void setCollection_id(Long collection_id) {
        this.collection_id = collection_id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }
    private Long collection_id;
    private News news;
    private Users user;
}
