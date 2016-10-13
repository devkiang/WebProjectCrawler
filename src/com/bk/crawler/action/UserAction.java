package com.bk.crawler.action;

import com.bk.crawler.entity.ResponseModel;
import com.bk.crawler.entity.Users;
import com.bk.crawler.service.*;
import com.bk.crawler.toolkit.NamePlug;
import com.bk.crawler.toolkit.Toolkit;
import com.bk.crawler.toolkit.XLog;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by shikee_app03 on 16/8/30.
 */
public class UserAction extends CrawlerBaseAction{


    UserService userService=new UserServiceImpl();
    EncryptionService encryptionService=new IOSEncryptionServiceImpl();
    private Toolkit toolkit=new Toolkit();
    public String login(){
        String account= (String) paramMap.get("account");
        String tmp_password=(String) paramMap.get("auth");//authen="password=x&&timestamp=4445555566"
        /////////================> 解封密码和时间戳
        if(tmp_password==null){
            addError("非法请求!");
            return SUCCESS;
        }
        String tmp_authen[] = tmp_password.split("&&");
        String tmp_split_password="";
        String tmp_stlit_timestamp="";
        if(tmp_authen.length!=2){
            addError("非法请求!");
            return SUCCESS;
        }
        tmp_split_password=tmp_authen[0].replace("password=","");
        tmp_stlit_timestamp=tmp_authen[1].replace("timestamp=","");

        long timestamp=Long.parseLong(tmp_stlit_timestamp);
        long curr_timestamp=(new Toolkit()).getCurrSimestamp();
        long result=curr_timestamp-timestamp;
        if(result>60){
            addError("非法请求!");
            return SUCCESS;
        }

        String password=tmp_split_password;
        ResponseModel responseModel=userService.login(account,password);
        if(responseModel.getCode()==200){
            Users user=(Users) responseModel.getResponseData();
            addSuccess(user,responseModel.getMsg());
        }else{
            addError(responseModel.getMsg());
        }
        return SUCCESS;
    }

    public String updatePwd(){
        String account=toolkit.convert2String(paramMap.get("account"));
        String newPwd=toolkit.convert2String(paramMap.get("newPwd"));
        ResponseModel responseModel=userService.updatePwd(account,newPwd);
        if(responseModel.getCode()==200){
            addSuccess(responseModel.getResponseData(),responseModel.getMsg());
        }else{
            addError(responseModel.getMsg());
        }
        return SUCCESS;
    }


    public String checkPhone(){
        String phone=toolkit.convert2String(paramMap.get("account"));
        ResponseModel responseModel=userService.validateAccount(phone);
        if(responseModel.getCode()==200){
            addSuccess(responseModel.getResponseData(),"请求成功");
        }else{
            addError(responseModel.getMsg());
        }
        return SUCCESS;
    }

    public String register(){
        String account=(String)paramMap.get("account");
        String password=(String) paramMap.get("password");
        String name=(String) paramMap.get("name");
        String avatar=(String) paramMap.get("avatar");
        Long level=1L;
        Long exp=1L;
        Users user=new Users();
        user.setAccount(account);
        user.setPassword(password);
        user.setName(name);
        user.setAvatar(avatar);
        user.setLevel(level);
        user.setExp(exp);
        ResponseModel responseModel=userService.register(user);
        if(responseModel.getCode()==200){
            ResponseModel loginReModel=userService.login(user.getAccount(),user.getPassword());
            if(loginReModel.getCode()==200){
                addSuccess(loginReModel.getResponseData(),responseModel.getMsg());
            }else{
                addError(loginReModel.getMsg());
            }
//            addSuccess(responseModel.getResponseData(),responseModel.getMsg());
        }else{
            addError(responseModel.getMsg());
        }
        return SUCCESS;
    }

    public String randomName(){
        ResponseModel responseModel=userService.randomName();
        if(responseModel.getCode()==200){
            addSuccess(responseModel.getResponseData(),"随机名字获取成功");
        }else{
            addError("获取失败");
        }
        return SUCCESS;
    }

    public String logout(){
        String uid=(String)paramMap.get("uid");
        String sign=(String)paramMap.get("sign");
        if (uid==null||uid.isEmpty()) {
            addError("非法请求");
            return SUCCESS;
        }
        HttpServletRequest request = ServletActionContext.getRequest();
        String path = request.getRequestURL().toString();//url
        String reqPamrs = request.getQueryString();//?后面的参数
        ResponseModel tokenResponse=userService.getToken(path,uid,sign);
        if(tokenResponse.getCode()==200){
            Object obj_token=tokenResponse.getResponseData();
            obj_token=obj_token==null?"":obj_token.toString();
            tokenResponse=userService.logout(uid,obj_token.toString());
            if(tokenResponse.getCode()==200){
                addSuccess("","["+uid+"]"+"退出成功!");
            }else{
                addSuccess("","退出失败!");
            }
        }else{
            addSuccess("",tokenResponse.getMsg());
        }
        return SUCCESS;
    }

    public String uploadAvatar(){

        MultiPartRequestWrapper request= (MultiPartRequestWrapper) ServletActionContext.getRequest();
        Map orgin_params=encryptionService.decrypt(request.getParameterMap());
        Long uid = toolkit.convert2Long(orgin_params.get("uid"));
        String sign=orgin_params.get("sign")==null?"":orgin_params.get("sign").toString();
        String requestUrl = request.getRequestURL().toString();//url
        String uploadSavePath="/user_avatar";
        String uploadMinSavePath="/user_avatar_min";

        String root = ServletActionContext.getServletContext().getRealPath(uploadSavePath+"/"+uid);
        String minPath=ServletActionContext.getServletContext().getRealPath(uploadMinSavePath+"/"+uid);
        File rootDic = new File(root);
        rootDic.setReadable(true, false);
        rootDic.setExecutable(true, false);
        rootDic.setWritable(true, false);
        File minPathDic=new File(minPath);
        minPathDic.setReadable(true, false);
        minPathDic.setExecutable(true, false);
        minPathDic.setWritable(true, false);
        //判断文件夹是否存在,如果不存在则创建文件夹
        if (!rootDic.exists()) {
            rootDic.mkdirs();
        }else{
            this.deleteAll(rootDic);
        }
        if(!minPathDic.exists()){
            minPathDic.mkdirs();
        }else {
            this.deleteAll(minPathDic);//清除目录下原有的图片
        }
        String[] types=request.getContentTypes("avatar");
        String type=types[0];
        XLog.debug("type:"+type);
        File[] files=request.getFiles("avatar");
        XLog.debug("file:"+files[0]);
        ResponseModel tokenResponseModel=userService.getToken(requestUrl,uid+"",sign);
        if(tokenResponseModel.getCode()!=200){
            addError(tokenResponseModel.getMsg());
            return SUCCESS;
        }
        String token=tokenResponseModel.getResponseData().toString();
        if(files==null||files.length!=1){
            addError("avatar参数不正确");
            return SUCCESS;
        }
        ResponseModel userResponseModel=userService.getUser(uid,token);
        if(userResponseModel.getCode()==200){//获取当前user资料成功
            Users new_user= (Users) userResponseModel.getResponseData();
            File file=files[0];
            String fileName="avatar_"+uid+"_"+toolkit.getCurrSimestamp()+"."+toolkit.getPrefix(toolkit.getPrefixByContentType(type));
            InputStream is = null;
            OutputStream os=null;
            try {
                is = new FileInputStream(file);
                os = new FileOutputStream(new File(root, fileName));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                addError("上传失败,服务器异常!");
                return SUCCESS;
            }

            System.out.println("fileFileName: " + fileName);

            byte[] buffer = new byte[500];
            int length = 0;

            try {
                while(-1 != (length = is.read(buffer, 0, buffer.length)))
                {
                    os.write(buffer);
                }
                os.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
                addError("上传失败,服务器异常!");
                return SUCCESS;
            }
            toolkit.compressPic(root+"/"+fileName,minPath+"/"+fileName);
            String avatar_url=request.getScheme()+"://"+ request.getServerName()+":"+request.getServerPort() +request.getContextPath()+uploadMinSavePath+"/"+uid+"/"+fileName;
            Map<String,Object> result=new HashMap<String, Object>();
            new_user.setAvatar(avatar_url);
            ResponseModel updateResponseModel=userService.updateUser(new_user);
            if(updateResponseModel.getCode()!=200){
                addError("头像修改失败");
                return SUCCESS;
            }
            result.put("url",avatar_url);
            addSuccess(result,"头像上传成功");
        }else{
            addError("avatar参数不正确");
            return SUCCESS;
        }
        return SUCCESS;
    }


    public String userUpdate(){
        Toolkit toolkit=new Toolkit();
        String userName=paramMap.get("name")==null?"":paramMap.get("name").toString();
        Long uid= toolkit.convert2Long(paramMap.get("uid"));
        String sign=paramMap.get("sign")==null?"":paramMap.get("sign").toString();
        String token=null;
        ResponseModel tokenResponseModel=userService.getToken(ServletActionContext.getRequest().getRequestURL().toString(),uid+"",sign);
        if(tokenResponseModel.getCode()==200) {
            token=tokenResponseModel.getResponseData().toString();
        }
        if(uid<1||token==null||token.isEmpty()){
            addError("非法请求");
            return SUCCESS;
        }

        if(userName==null||userName.isEmpty()){
            addError("请提交一个有效的用户名");
            return SUCCESS;
        }

        ResponseModel responseModel=userService.getUser(uid,token);
        if(responseModel.getCode()!=200){
            addError("非法请求");
            return SUCCESS;
        }
        Users user= (Users) responseModel.getResponseData();
        user.setName(userName);
        ResponseModel updateResponseModel=userService.updateUser(user);
        if(updateResponseModel.getCode()==200){
            user= (Users) updateResponseModel.getResponseData();
            addSuccess(user,"资料修改成功");
        }else{
            addError("资料修改失败");
        }
        return SUCCESS;
    }

    public String getUserInfo(){
        Toolkit toolkit=new Toolkit();
        Long uid= toolkit.convert2Long(paramMap.get("uid"));
        String sign=paramMap.get("sign")==null?"":paramMap.get("sign").toString();
        String token=null;
        ResponseModel tokenResponseModel=userService.getToken(ServletActionContext.getRequest().getRequestURL().toString(),uid+"",sign);
        if(tokenResponseModel.getCode()==200) {
            token=tokenResponseModel.getResponseData().toString();
        }
        if(uid<1||token==null||token.isEmpty()){
            addError("非法请求");
            return SUCCESS;
        }
        ResponseModel responseModel=userService.getUser(uid,token);
        if(responseModel.getCode()!=200){
            addError("非法请求");
            return SUCCESS;
        }
        Users user= (Users) responseModel.getResponseData();
        addSuccess(user,"资料获取成功");
        return SUCCESS;
    }

    ///插件///

    public String plug(){
        NamePlug plug=new NamePlug();
        plug.addNameModelByTxtFilePath("/Users/shikee_app03/Desktop/kiang/name.txt");
        addSuccess("","执行完成");
        return SUCCESS;
    }


    ///private

    private boolean deleteAll(File file) {
        if(file.isFile() || file.list().length ==0)
        {
            file.delete();
        }else{
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                this.deleteAll(files[i]);
                files[i].delete();
            }
//            if(file.exists())         //如果文件本身就是目录 ，就要删除目录
//                file.delete();
        }
        return true;
    }

}
