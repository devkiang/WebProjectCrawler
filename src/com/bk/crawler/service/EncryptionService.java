package com.bk.crawler.service;

import java.util.Map;

/**
 * Created by shikee_app03 on 16/9/5.
 */
public interface EncryptionService {
    public static String DefaultKey="ceb89727213a4573b0ffab5c61a9727e";
    public String encrypt(String content, String password);
    public String decrypt(String content, String password);
    public Map<String,Object> decrypt(Map<String,Object> parameters);
}
