package com.bk.crawler.entity;

import javax.persistence.*;
import java.util.List;

/**
 * Created by shikee_app03 on 16/8/30.
 */
@Entity
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Long getLevel() {
        return level;
    }

    public void setLevel(Long level) {
        this.level = level;
    }

    public Long getExp() {
        return exp;
    }

    public void setExp(Long exp) {
        this.exp = exp;
    }

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    public List<Collection> getCollections() {
        return collections;
    }

    public void setCollections(List<Collection> collections) {
        this.collections = collections;
    }

    @OneToMany(mappedBy = "user")
    public List<Comment> getComment_list() {
        return comment_list;
    }

    public void setComment_list(List<Comment> comment_list) {
        this.comment_list = comment_list;
    }


    private Long uid;//uid
    private String name;//用户名
    private String account;//账号
    private String password;//md5密码
    private String token;//token 签证
    private String avatar;//头像
    private Long level;//等级
    private Long exp;//经验值
    private List<Comment> comment_list;
    private List<Collection> collections;
}