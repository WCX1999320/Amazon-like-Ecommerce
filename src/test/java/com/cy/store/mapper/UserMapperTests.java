package com.cy.store.mapper;

import com.cy.store.entity.User;
import org.apache.ibatis.annotations.Param;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserMapperTests {
    @Autowired
    private UserMapper userMapper;
    @Test
    public void insert() {
        User user = new User();
        user.setUsername("user100");
        user.setPassword("123456");
        Integer rows = userMapper.insert(user);
        System.out.println(rows==1? "Registration Success":"Registration Fail");
    }

    @Test
    public void findByUsername() {
        String username = "user05";
        User result = userMapper.findByUsername(username);
        System.out.println(result);
    }
    @Test
    public void updatePasswordByUid(){
        userMapper.updatePasswordByUid(7,"321","w",new Date());
    }
    @Test
    public void findByUid(){
        System.out.println(userMapper.findByUid(7));
    }
    @Test
    public void updateInfoByUid(){
        User user=new User();
        user.setUid(7);
        user.setPhone("12345678");
        user.setEmail("1234@123.com");
        user.setGender(1);
        userMapper.updateInfoByUid(user);
    }
    @Test
    public void updateAvatarByUid() {
        Integer uid = 20;
        String avatar = "/upload/avatar.png";
        String modifiedUser = "超级管理员";
        Date modifiedTime = new Date();
        Integer rows = userMapper.updateAvatarByUid(uid, avatar, modifiedUser, modifiedTime);
        System.err.println("rows=" + rows);
    }
}
