package com.bk.crawler.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by shikee_app03 on 16/8/30.
 */
@Entity(name = "Category")
public class Category {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public long getCid() {
        return cid;
    }

    public void setCid(long cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private long cid;
    private String name;
}

