package com.cy.store.mapper;

import com.cy.store.entity.District;

import java.util.List;

public interface DistrictMapper {
    List<District>findByParent(String parent);
    String findNameByCode(String code);
}
