package com.cy.store.service;

import com.cy.store.entity.User;
import com.cy.store.service.ex.ServiceException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceTests {
    @Autowired
    private IUserService userService;
    @Test
    public void reg(){
        try{
            User user=new User();
            user.setUsername("lower");
            user.setPassword("123456");
            userService.reg(user);
            System.out.println("注册成功！");
        }catch (ServiceException e){
            e.printStackTrace();
        }
    }
    @Test
    public void login(){
        User user=userService.login("test01","123");
        System.out.println(user);
    }
    @Test
    public void changePassword(){
        userService.changePassword(6,"Tom1122","321","123456");
    }
    @Test
    public void getByUid(){
        System.out.println(userService.getByUid(7));
    }
    @Test
    public void changeInfo(){
        User user=new User();
        user.setPhone("15512328888");
        user.setEmail("admin03@cy.cn");
        user.setGender(0);
        userService.changeInfo(7,"test01", user);
    }
    @Test
    public void changeAvatar() {
        Integer uid = 20;
        String username = "头像管理员";
        String avatar = "/upload/avatar.png";
        userService.changeAvatar(uid, username, avatar);
    }
}
