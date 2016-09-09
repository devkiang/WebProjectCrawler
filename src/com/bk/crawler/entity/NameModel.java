package com.bk.crawler.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by shikee_app03 on 16/9/9.
 */
@Entity
public class NameModel {
    @Id
    @GeneratedValue()
    public Long getName_id() {
        return name_id;
    }

    public void setName_id(Long name_id) {
        this.name_id = name_id;
    }

    public String getName_value() {
        return name_value;
    }

    public void setName_value(String name_value) {
        this.name_value = name_value;
    }

    private Long name_id;
    private String name_value;
}
