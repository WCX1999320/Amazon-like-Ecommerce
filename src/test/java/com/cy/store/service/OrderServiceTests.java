package com.cy.store.service;

import com.cy.store.entity.Address;
import com.cy.store.entity.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class OrderServiceTests {
    @Autowired
    private IOrderService orderService;
    @Test
    public void create() {
        Integer aid = 21;
        Integer[] cids = {4,7};
        Integer uid = 31;
        String username = "订单管理员";
        Order order = orderService.create(aid, cids, uid, username);
        System.out.println(order);
    }
}
