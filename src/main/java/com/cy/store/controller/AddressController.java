package com.cy.store.controller;

import com.cy.store.entity.Address;
import com.cy.store.service.IAddressService;
import com.cy.store.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/address")
public class AddressController extends BaseController{
    @Autowired
    private IAddressService addressService;
    @PostMapping("add_new_address")
    public JsonResult<Void> addNewAddress(HttpSession session, Address address){
        Integer uid=getUidFromSession(session);
        String username=getUsernameFromSession(session);
        addressService.addNewAddress(uid,username,address);
        return new JsonResult<>(OK);
    }
    @GetMapping({"", "/"})
    public JsonResult<List<Address>> getByUid(HttpSession session) {
        Integer uid = getUidFromSession(session);
        List<Address> data = addressService.getByUid(uid);
        return new JsonResult<List<Address>>(OK, data);
    }
    @PutMapping("/{aid}")
    @CachePut(value = "addresses",key = "#aid")
    public JsonResult<Void>setDefault(@PathVariable("aid")Integer aid,HttpSession session){
        addressService.setDefault(aid,getUidFromSession(session),getUsernameFromSession(session));
        return new JsonResult<>(OK);
    }
    @DeleteMapping("/{aid}")
    @CacheEvict(value = "addresses", allEntries = true)
    public JsonResult<Void> delete(@PathVariable("aid") Integer aid,HttpSession session){
        addressService.delete(aid,getUidFromSession(session),getUsernameFromSession(session));
        return new JsonResult<Void>(OK);
    }
}
