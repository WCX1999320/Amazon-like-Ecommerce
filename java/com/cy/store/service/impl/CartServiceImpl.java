package com.cy.store.service.impl;

import com.cy.store.entity.Cart;
import com.cy.store.entity.Product;
import com.cy.store.mapper.CartMapper;
import com.cy.store.mapper.ProductMapper;
import com.cy.store.service.ICartService;
import com.cy.store.service.ex.AccessDeniedException;
import com.cy.store.service.ex.CartNotFoundException;
import com.cy.store.service.ex.InsertException;
import com.cy.store.service.ex.UpdateException;
import com.cy.store.vo.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
public class CartServiceImpl implements ICartService {
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;
    @Override
    public void addToCart(Integer uid, Integer pid, Integer amount, String username) {
        Cart result=cartMapper.findByUidAndPid(uid,pid);
        Date date=new Date();
        if(result==null){
            Cart cart=new Cart();
            cart.setUid(uid);
            cart.setPid(pid);
            cart.setNum(amount);
            Product product=productMapper.findById(pid);
            cart.setPrice(product.getPrice());
            cart.setCreatedTime(date);
            cart.setCreatedUser(username);
            cart.setModifiedTime(date);
            cart.setModifiedUser(username);
            Integer rows = cartMapper.insert(cart);
            if (rows != 1) {
                throw new InsertException("Some unexpected exception occurred");
            }
        }else{
            Integer cid = result.getCid();
            Integer num = result.getNum() + amount;
            Integer rows = cartMapper.updateNumByCid(cid, num, username, date);
            if (rows != 1) {
                throw new UpdateException("Some unexpected exception occurred");
            }
        }
    }

    @Override
    public List<CartVO> getVOById(Integer uid) {
        return cartMapper.findVOByUid(uid);
    }

    @Override
    public Integer addNum(Integer cid, Integer uid, String username) {
        Cart result=cartMapper.findByCid(cid);
        if(result==null){
            throw new CartNotFoundException("Cart not Found");
        }
        if(result.getUid()!=uid){
            throw new AccessDeniedException("Illegal  Access");
        }
        Integer num= result.getNum()+1;
        Integer rows=cartMapper.updateNumByCid(cid,num,username,new Date());
        if (rows != 1) {
            throw new UpdateException("Some unexpected exception occurred");
        }
        return  num;
    }

    @Override
    public List<CartVO> getVOByCid(Integer uid, Integer[] cids) {
        List<CartVO> list = cartMapper.findVOByCids(cids);
        Iterator<CartVO> it=list.iterator();
        while (it.hasNext()){
            CartVO cart= it.next();
            if (cart.getUid()!=uid){
                it.remove();
            }
        }
        return list;
    }
}
