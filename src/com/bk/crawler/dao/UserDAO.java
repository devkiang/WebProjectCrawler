package com.bk.crawler.dao;

import com.bk.crawler.entity.Users;

/**
 * Created by shikee_app03 on 16/8/31.
 */
public interface UserDAO {
    public Users getUserByUid(String uid);
    public boolean checkAccount(String account);
    public Users getUserByAccountAndPassword(String account,String password);
    public Users getUserByUidAndToken(String account,String token);
    public Users save(Users user);
    public String getRandomName();
}
