package com.bk.crawler.toolkit;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shikee_app03 on 16/8/29.
 */
public class FileHelper {
    private String path= "categorys.txt"; // 文件和该类在同个目录下

    public List<String> getAllContent(String path)
    {
        this.path=path;
        return this.getAllContent();
    }

    public List<String> getAllContent(){
        List<String> result=null;
        try {
            String resultStr=this.readTxtFile();
            String[] resultList=resultStr.split("\r\n");
            if(resultList!=null&&resultList.length>0){
                result=new ArrayList<String>();
                for (String str:resultList) {
                    result.add(str);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private void writeToFile(String msg) throws IOException {
        try{
            File file =new File(path);
            if(!file.exists()){
                file.createNewFile();
            }
            FileWriter fileWritter = new FileWriter(file.getName(),true);
            BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
            bufferWritter.write(msg);
            bufferWritter.close();

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public String readTxtFile() throws Exception {

        BufferedReader reader = null;
        StringBuffer result=new StringBuffer();
        try {
            File file =new File(path);
            if(!file.exists()){
                file.createNewFile();
            }
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
            String str = null;
            while ((str = reader.readLine()) != null) {
                result.append(str+"\r\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "";
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result.toString();
    }
}
