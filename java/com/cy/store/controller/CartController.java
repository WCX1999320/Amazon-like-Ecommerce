package com.cy.store.controller;

import com.cy.store.service.ICartService;
import com.cy.store.util.JsonResult;
import com.cy.store.vo.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/carts")
public class CartController extends BaseController{
    @Autowired
    private ICartService cartService;
    @RequestMapping("/add_to_cart")
    public JsonResult<Void> addToCart(Integer pid, Integer amount, HttpSession session) {
        cartService.addToCart(getUidFromSession(session),pid,amount,getUsernameFromSession(session));
        return new JsonResult<>(OK);
    }
    @RequestMapping({"","/"})
    public JsonResult<List<CartVO>> getVOByUid(HttpSession session){
        List<CartVO> result=cartService.getVOById(getUidFromSession(session));
        return new JsonResult<>(OK,result);
    }
    @RequestMapping("/{cid}/add/num")
    public JsonResult<Integer> addNum(Integer cid,HttpSession session){
        Integer data=cartService.addNum(cid,getUidFromSession(session),getUsernameFromSession(session));
        return new JsonResult<>(OK,data);
    }
    @GetMapping("/list")
    public JsonResult<List<CartVO>> getVOByCids(Integer[] cids, HttpSession session) {
        Integer uid = getUidFromSession(session);
        List<CartVO> data = cartService.getVOByCid(uid, cids);
        return new JsonResult<>(OK, data);
    }
}
