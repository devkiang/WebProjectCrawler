package com.bk.crawler.service;

import com.bk.crawler.toolkit.XLog;
import org.hibernate.jdbc.Expectation;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Created by shikee_app03 on 16/9/5.
 */
public class EncryptionServiceImpl implements EncryptionService{
    /**
     * 加密
     *
     * @param content 需要加密的内容
     * @param password  加密密码
     * @return
     */
    public String encrypt(String content, String password) {
        try {
            SecretKey secretKey = this.getKey(password);
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            byte[] byteContent = content.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
            byte[] result = cipher.doFinal(byteContent);
            String str_result=this.parseByte2HexStr(result);
            XLog.debug("加密结果:"+str_result);
            return str_result; // 加密
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**解密
     * @param content  待解密内容
     * @param password 解密密钥
     * @return
     */
    public String decrypt(String content, String password) {
        try {
            byte[] b_content=this.parseHexStr2Byte(content);
            SecretKey secretKey = this.getKey(password);
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
            if(b_content==null||b_content.length<1){
                return null;
            }
            byte[] result = cipher.doFinal(b_content);
            XLog.debug("解密结果:"+new String(result));
            return new String(result);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (NumberFormatException e)
        {
//            e.printStackTrace();
            XLog.debug(e.getMessage());
            return null;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private byte[] parseHexStr2Byte(String hexStr) throws NumberFormatException{
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    private String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    private SecretKey getKey(String strKey){
        try {
                KeyGenerator _generator = KeyGenerator.getInstance( "AES" );
                SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG" );
                secureRandom.setSeed(strKey.getBytes());
                _generator.init(128,secureRandom);
                return _generator.generateKey();
            }  catch (Exception e) {
                throw new RuntimeException( " 初始化密钥出现异常 " );

            }
    }

    public static void main(String[] arg){

        EncryptionService service=new EncryptionServiceImpl();
        String content = "bk";
        String password = "ceb89727213a4573b0ffab5c61a9727e";

        String result=service.encrypt(content,password);
        service.decrypt(result,password);

    }
}
