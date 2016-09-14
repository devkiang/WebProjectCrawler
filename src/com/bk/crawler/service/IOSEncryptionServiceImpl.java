package com.bk.crawler.service;

import com.bk.crawler.toolkit.AES;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

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
        String result=ase.decrypt(content);
        return result;
    }

    @Override
    public Map<String, Object> decrypt(Map<String, Object> parameters) {
        Map<String,Object>  parameterMap=new HashMap<String, Object>();
        for(Map.Entry<String, Object> entry:parameters.entrySet()){
            String key=entry.getKey();
            String[] value= (String[]) entry.getValue();
            if(value!=null&&value.length>0){
                String orgin_value=this.decrypt(value[0],EncryptionService.DefaultKey);
                parameterMap.put(key,orgin_value);
            }
        }
        return parameterMap;
    }
}
