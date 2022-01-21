package com.cy.store.service;

import com.cy.store.entity.Address;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AddressServiceTests {
    @Autowired
    private IAddressService addressService;
    @Test
    public void addNewAddress(){
        Integer uid = 20;
        String username = "管理员";
        Address address = new Address();
        address.setName("张三");
        address.setPhone("17858805555");
        address.setAddress("Boston");
        addressService.addNewAddress(uid, username, address);
        System.out.println("OK.");
    }
    @Test
    public  void setDefault(){
        Integer aid = 18;
        Integer uid = 30;
        String username = "系统管理员";
        addressService.setDefault(aid, uid, username);
    }
    @Test
    public void delete(){
        Integer aid = 18;
        Integer uid = 30;
        String username = "明明";
        addressService.delete(aid, uid, username);
    }
}
