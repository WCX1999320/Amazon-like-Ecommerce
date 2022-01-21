package com.cy.store.service.impl;

import com.cy.store.entity.Address;
import com.cy.store.mapper.AddressMapper;
import com.cy.store.service.IAddressService;
import com.cy.store.service.IDistrictService;
import com.cy.store.service.ex.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AddressServiceImpl implements IAddressService {
    @Autowired
    private AddressMapper addressMapper;
    @Autowired
    private IDistrictService districtService;
    @Value("${user.address.max-count}")
    private Integer maxCount;
    @Override
    public void addNewAddress(Integer uid, String username, Address address) {
        Integer count=addressMapper.countByUid(uid);
        if(count>=maxCount){
            throw new AddressCountLimitException("Address is more than limit");
        }

        String provinceName = districtService.getNameByCode(address.getProvinceCode());
        String cityName = districtService.getNameByCode(address.getCityCode());
        String areaName = districtService.getNameByCode(address.getAreaCode());
        address.setProvinceName(provinceName);
        address.setCityName(cityName);
        address.setAreaName(areaName);

        address.setUid(uid);
        Integer isDefault = count == 0 ? 1 : 0;
        address.setIsDefault(isDefault);
        Date now = new Date();
        address.setCreatedUser(username);
        address.setCreatedTime(now);
        address.setModifiedUser(username);
        address.setModifiedTime(now);
        Integer rows = addressMapper.insert(address);
        if (rows != 1) {
            throw new InsertException("Some unexpected exception occurred");
        }
    }

    @Override
    public List<Address> getByUid(Integer uid) {
        List<Address> result = addressMapper.findByUid(uid);
        for (Address address : result) {
            //address.setAid(null);
            //address.setUid(null);
            address.setProvinceCode(null);
            address.setCityCode(null);
            address.setAreaCode(null);
            address.setTel(null);
            address.setIsDefault(null);
            address.setCreatedUser(null);
            address.setCreatedTime(null);
            address.setModifiedUser(null);
            address.setModifiedTime(null);
        }
        return result;
    }

    @Override
    public void setDefault(Integer aid, Integer uid, String username) {
        Address result=addressMapper.findByAid(aid);
        if(result==null){
            throw new AddressNotFoundException("Address not Found");
        }
        if(result.getUid()!=uid){
            throw new AccessDeniedException("Illegal Access");
        }
        Integer rows=addressMapper.updateNonDefault(uid);
        if(rows<1){
            throw new UpdateException("Some unexpected exception occurred");
        }
        rows=addressMapper.updateDefaultByAid(aid,username,new Date());
        if(rows!=1){
            throw new UpdateException("Some unexpected exception occurred");
        }
    }

    @Override
    public void delete(Integer aid, Integer uid, String username) {
        Address result=addressMapper.findByAid(aid);
        if(result==null){
            throw new AddressNotFoundException("Address not Found");
        }
        if(result.getUid()!=uid){
            throw new AccessDeniedException("Illegal Access");
        }
        Integer rows=addressMapper.deleteByAid(aid);
        if(rows!=1){
            throw new DeleteException("Some unexpected exception occurred");
        }
        if (result.getIsDefault() == 0) {
            return;
        }
        Integer count=addressMapper.countByUid(uid);
        if(count==0){
            return;
        }
        Address address=addressMapper.findLastModified(uid);
        rows=addressMapper.updateDefaultByAid(address.getAid(),username,new Date());
        if(rows!=1){
            throw new DeleteException("Some unexpected exception occurred");
        }

    }

    @Override
    public Address getByAid(Integer aid, Integer uid) {
        Address address = addressMapper.findByAid(aid);
        if(address==null){
            throw new AddressNotFoundException("Address not Found");
        }
        if(address.getUid()!=uid){
            throw new AccessDeniedException("Illegal Access");
        }
        address.setProvinceCode(null);
        address.setCityCode(null);
        address.setAreaCode(null);
        address.setCreatedUser(null);
        address.setCreatedTime(null);
        address.setModifiedUser(null);
        address.setModifiedTime(null);
        return address;
    }
}
