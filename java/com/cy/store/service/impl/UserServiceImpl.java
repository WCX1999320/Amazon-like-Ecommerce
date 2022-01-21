package com.cy.store.service.impl;

import com.cy.store.entity.User;
import com.cy.store.mapper.UserMapper;
import com.cy.store.service.IUserService;
import com.cy.store.service.ex.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public void reg(User user) {
        String username=user.getUsername();
        User result=userMapper.findByUsername(username);
        if(result!=null){
            throw new UsernameDuplicateException(username+" has been used");
        }
        //MD5 encoding password
        String password=user.getPassword();
        String salt= UUID.randomUUID().toString().toUpperCase();
        user.setSalt(salt);
        String md5Password=getMD5Password(password,salt);
        user.setPassword(md5Password);
        user.setIsDelete(0);
        Date now=new Date();
        user.setCreatedUser(username);
        user.setCreatedTime(now);
        user.setModifiedUser(username);
        user.setModifiedTime(now);
        Integer row=userMapper.insert(user);
        if(row!=1){
            throw new InsertException("Some problem occurred during registration");
        }
    }

    @Override
    public User login(String username, String password) {
        User result=userMapper.findByUsername(username);
        if(result==null){
            throw new UserNotFoundException("User not Found");
        }
        if(result.getIsDelete()==1){
            throw new UserNotFoundException("User not Found");
        }
        String salt=result.getSalt();
        String md5Password=getMD5Password(password,salt);
        if(!md5Password.equals(result.getPassword())){
            throw new PasswordNotMatchException("The password is wrong");
        }
        User user=new User();
        user.setUid(result.getUid());
        user.setUsername(result.getUsername());
        user.setAvatar(result.getAvatar());
        return user;
    }

    @Override
    public void changePassword(Integer uid, String username, String oldPassword, String newPassword) {
        User result=userMapper.findByUid(uid);
        if (result == null) {
            throw new UserNotFoundException("User not Found");
        }

        if (result.getIsDelete().equals(1)) {
            throw new UserNotFoundException("User not Found");
        }
        String salt = result.getSalt();
        String oldMd5Password = getMD5Password(oldPassword, salt);
        if (!result.getPassword().equals(oldMd5Password)) {
            throw new PasswordNotMatchException("The password is wrong");
        }

        String newMd5Password = getMD5Password(newPassword, salt);
        Date now = new Date();
        Integer rows = userMapper.updatePasswordByUid(uid, newMd5Password, username, now);
        if (rows != 1) {
            throw new UpdateException("Some problem occurred during updating");
        }
    }

    @Override
    public User getByUid(Integer uid) {
        User result=userMapper.findByUid(uid);
        if(result==null||result.getIsDelete()==1){
            throw new UserNotFoundException("User not Found");
        }
        User user=new User();
        user.setUsername(result.getUsername());
        user.setPhone(result.getPhone());
        user.setEmail(result.getEmail());
        user.setGender(result.getGender());
        return user;
    }

    @Override
    public void changeInfo(Integer uid, String username, User user) {
        User result=userMapper.findByUid(uid);
        if(result==null||result.getIsDelete()==1){
            throw new UserNotFoundException("User not Found");
        }
        user.setUid(uid);
        user.setModifiedUser(username);
        user.setModifiedTime(new Date());
        Integer rows=userMapper.updateInfoByUid(user);
        if (rows != 1) {
            throw new UpdateException("Some problem occurred during updating");
        }
    }

    @Override
    public void changeAvatar(Integer uid, String username, String avatar) {
        User result=userMapper.findByUid(uid);
        if(result==null||result.getIsDelete()==1){
            throw new UserNotFoundException("User not Found");
        }
        Integer rows=userMapper.updateAvatarByUid(uid,avatar,username,new Date());
        if (rows != 1) {
            throw new UpdateException("Some problem occurred during updating");
        }
    }

    private String getMD5Password(String password,String salt){
        for(int i=0;i<3;i++){
            password= DigestUtils.md5DigestAsHex((salt + password + salt).getBytes()).toUpperCase();
        }
        return password;
    }
}
