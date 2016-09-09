package com.bk.crawler.service;

/**
 * Created by shikee_app03 on 16/9/5.
 */
public interface EncryptionService {
    public String encrypt(String content, String password);
    public String decrypt(String content, String password);
}
