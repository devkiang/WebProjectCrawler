package com.bk.crawler.toolkit;

import java.security.MessageDigest;
import java.util.Date;

/**
 * Created by shikee_app03 on 16/9/2.
 */
public class Toolkit {
    public long getCurrSimestamp(){
       return (new Date()).getTime()/1000;
    }

    public String str2Md5(String str) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] strTemp = str.getBytes();
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char strs[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                strs[k++] = hexDigits[byte0 >>> 4 & 0xf];
                strs[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(strs);
        } catch (Exception e) {
            return null;
        }
    }

    public Long convert2Long(Object obj)
    {
        if(obj==null){
            return 0L;
        }
        Long result=0L;
        try {
            result=Long.parseLong(obj.toString());
        } catch (NumberFormatException e) {
        }
        return result;
    }

    public int convert2Int(Object obj)
    {
        if(obj==null){
            return 0;
        }
        int result=0;
        try {
            result=Integer.parseInt(obj.toString());
        } catch (NumberFormatException e) {
        }
        return result;
    }
}
