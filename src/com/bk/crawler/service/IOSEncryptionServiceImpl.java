package com.bk.crawler.service;

import com.bk.crawler.toolkit.AES;

/**
 * Created by shikee_app03 on 16/9/6.
 */
public class IOSEncryptionServiceImpl implements EncryptionService{
    @Override
    public String encrypt(String content, String password) {
        AES ase=new AES(password);
        return ase.encrypt(content.getBytes());
    }

    @Override
    public String decrypt(String content, String password) {
        AES ase=new AES(password);
        return ase.decrypt(content);
    }
}
