package com.cy.store.service;

import com.cy.store.entity.Address;
import com.cy.store.entity.District;
import com.cy.store.service.ex.ServiceException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class DistrictServiceTests {
    @Autowired
    private IDistrictService districtService;
    @Test
    public void getByParent(){
        List<District> result=districtService.getByParent("86");
        for(District d:result){
            System.err.println(d);
        }
    }
    @Test
    public void getNameByCode() {
        String code = "430000";
        String result = districtService.getNameByCode(code);
        System.out.println(result);
    }
}
