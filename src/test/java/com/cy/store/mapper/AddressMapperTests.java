package com.cy.store.mapper;

import com.cy.store.entity.Address;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AddressMapperTests {
    @Autowired
    private AddressMapper addressMapper;
    @Test
    public void insert(){
        Address address = new Address();
        address.setUid(18);
        address.setName("admin");
        address.setPhone("17858802974");
        address.setAddress("America");
        Integer rows = addressMapper.insert(address);
        System.out.println("rows=" + rows);
    }
    @Test
    public void countByUid() {
        Integer uid = 18;
        Integer count = addressMapper.countByUid(uid);
        System.out.println("count=" + count);
    }
    @Test
    public void findByUid(){
        Integer id=18;
        List<Address> addressList=addressMapper.findByUid(id);
        System.out.println(addressList);
    }
    @Test
    public void findByAid(){
        System.err.println(addressMapper.findByAid(1));
    }
    @Test
    public void updateNonDefault(){
        addressMapper.updateNonDefault(7);
    }
    @Test
    public void updateDefaultByAid(){
        addressMapper.updateDefaultByAid(1,"w",new Date());
    }
    @Test
    public void deleteByAid(){
        addressMapper.deleteByAid(10);
    }
    public void findLastModified(){
        addressMapper.findLastModified(7);
    }
}
