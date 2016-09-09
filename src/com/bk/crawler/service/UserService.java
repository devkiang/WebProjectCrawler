package com.bk.crawler.service;

import com.bk.crawler.entity.ResponseModel;
import com.bk.crawler.entity.Users;

/**
 * Created by shikee_app03 on 16/8/30.
 */
public interface UserService {
    public ResponseModel login(String account,String password);
    public ResponseModel logout(String uid,String token);
    public ResponseModel validateLogin(String uid,String token);
    public ResponseModel validateAccount(String account);
    public ResponseModel register(Users user);
    public ResponseModel getToken(String actionUrl, String uid,String sign);
}
