package com.cy.store.controller;

import com.cy.store.controller.ex.*;
import com.cy.store.entity.User;
import com.cy.store.service.IUserService;
import com.cy.store.service.ex.InsertException;
import com.cy.store.service.ex.UsernameDuplicateException;
import com.cy.store.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController extends BaseController{

    @Autowired
    private IUserService userService;
    public static  final int AVATAR_MAX_SIZE=10*1024*1024;
    public static final List<String> AVATAR_TYPE=new ArrayList<>();
    static {
        AVATAR_TYPE.add("image/jpeg");
        AVATAR_TYPE.add("image/png");
        AVATAR_TYPE.add("image/bmp");
        AVATAR_TYPE.add("image/gif");
    }
    @PostMapping("/reg")
    public JsonResult<Void> reg(User user){
        userService.reg(user);
        return new JsonResult<>(OK);
    }
    @PostMapping("/login")
    public JsonResult<User> login(String username, String password, HttpSession session){
        User data=userService.login(username,password);
        session.setAttribute("uid",data.getUid());
        session.setAttribute("username",data.getUsername());
        return new JsonResult<>(OK,data);
    }
    @PostMapping("/change_password")
    public JsonResult<Void> changePassword(String oldPassword,String newPassword,HttpSession session){
        Integer uid=getUidFromSession(session);
        String username=getUsernameFromSession(session);
        userService.changePassword(uid,username,oldPassword,newPassword);
        return new JsonResult<>(OK);
    }
    @GetMapping("/get_by_uid")
    public JsonResult<User> getByUid(HttpSession session) {
        Integer uid = getUidFromSession(session);
        User data = userService.getByUid(uid);
        return new JsonResult<User>(OK, data);
    }
    @RequestMapping("/change")
    public JsonResult<Void> changeInfo(User user,HttpSession session){
        Integer uid = getUidFromSession(session);
        String username = getUsernameFromSession(session);
        userService.changeInfo(uid, username, user);
        return new JsonResult<Void>(OK);
    }
    @RequestMapping("change_avatar")
    public JsonResult<String> changeAvatar(HttpSession session, MultipartFile file){
        if(file.isEmpty()){
            throw new FileEmptyException("File is Empty");
        }
        if(file.getSize()>AVATAR_MAX_SIZE){
            throw new FileSizeException("File is so big");
        }
        String contentType=file.getContentType();
        if(!AVATAR_TYPE.contains(contentType)){
            throw new FileTypeException("File Type is not supported");
        }
        String parent = session.getServletContext().getRealPath("upload");
        File dir=new File(parent);
        if(!dir.exists()){
            dir.mkdirs();
        }
        String suffix="";
        String originalFileName=file.getOriginalFilename();
        int beginIndex = originalFileName.lastIndexOf(".");
        if (beginIndex > 0) {
            suffix = originalFileName.substring(beginIndex);
        }
        String filename=UUID.randomUUID().toString().toUpperCase()+suffix;
        File dest=new File(dir,filename);
        try {
            file.transferTo(dest);
        } catch (IllegalStateException e) {
            throw new FileStateException("File State Exception");
        } catch (IOException e) {
            throw new FileUploadIOException("File Upload Exception");
        }
        String avatar="/upload/"+filename;
        userService.changeAvatar(getUidFromSession(session),getUsernameFromSession(session),avatar);
        return new JsonResult<>(OK,avatar);
    }
}
