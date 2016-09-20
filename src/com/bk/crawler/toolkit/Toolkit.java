package com.bk.crawler.toolkit;

import org.apache.commons.lang3.StringUtils;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.FileOutputStream;
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

    public String convert2String(Object obj){
        if(obj==null){
            return "";
        }
        String result="";
        try {
            result=obj.toString();
        } catch (Exception e) {
        }
        return result;
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

    public static String getEncoding(String str) {
        String encode = "GB2312";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s = encode;
                return s;
            }
        } catch (Exception exception) {
        }
        encode = "ISO-8859-1";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s1 = encode;
                return s1;
            }
        } catch (Exception exception1) {
        }
        encode = "UTF-8";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s2 = encode;
                return s2;
            }
        } catch (Exception exception2) {
        }
        encode = "GBK";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s3 = encode;
                return s3;
            }
        } catch (Exception exception3) {
        }
        return "";
    }

    public String getPrefix(String filePath){
        String result=null;
        try {
            File f =new File(filePath);
            String fileName=f.getName();
            result=fileName.substring(fileName.lastIndexOf(".")+1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public String getPrefixByContentType(String type)
    {
        String result="";
        final String png="image/png";
        final String jpg="image/jpeg";
        final String gif="image/gif";
        final String icon="image/x-icon";
        if(type.toLowerCase().equals(png)){
            result="png";
        }else if(type.toLowerCase().equals(jpg)){
            result="jpg";
        }else if(type.toLowerCase().equals(gif)){
            result="gif";
        }else if(type.toLowerCase().equals(icon)){
            result="icon";
        }
        return result;
    }

    public boolean compressPic(String srcFilePath, String descFilePath)
    {
        File file = null;
        BufferedImage src = null;
        FileOutputStream out = null;
        ImageWriter imgWrier;
        ImageWriteParam imgWriteParams;

        // 指定写图片的方式为 jpg
        imgWrier = ImageIO.getImageWritersByFormatName("jpg").next();
        imgWriteParams = new javax.imageio.plugins.jpeg.JPEGImageWriteParam(null);
        // 要使用压缩，必须指定压缩方式为MODE_EXPLICIT
        imgWriteParams.setCompressionMode(imgWriteParams.MODE_EXPLICIT);
        // 这里指定压缩的程度，参数qality是取值0~1范围内，
        imgWriteParams.setCompressionQuality((float)0.5);
        imgWriteParams.setProgressiveMode(imgWriteParams.MODE_DISABLED);
        ColorModel colorModel = ColorModel.getRGBdefault();
        // 指定压缩时使用的色彩模式
        imgWriteParams.setDestinationType(new javax.imageio.ImageTypeSpecifier(colorModel, colorModel
                .createCompatibleSampleModel(16, 16)));

        try
        {
            if(StringUtils.isBlank(srcFilePath))
            {
                return false;
            }
            else
            {
                file = new File(srcFilePath);
                src = ImageIO.read(file);
                out = new FileOutputStream(descFilePath);

                imgWrier.reset();
                // 必须先指定 out值，才能调用write方法, ImageOutputStream可以通过任何 OutputStream构造
                imgWrier.setOutput(ImageIO.createImageOutputStream(out));
                // 调用write方法，就可以向输入流写图片
                imgWrier.write(null, new IIOImage(src, null, null), imgWriteParams);
                out.flush();
                out.close();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
