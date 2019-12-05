package com.banmingi.bmshop.cart.controller;

import com.banmingi.bmshop.cart.pojo.Cart;
import com.banmingi.bmshop.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @auther 半命i 2019/12/2
 * @description
 */
@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * 添加商到购物车.
     * @param cart
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> addCart(@RequestBody Cart cart) {
        this.cartService.addCart(cart);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    /**
     * 查询购物车.
     * @return
     */
    @GetMapping
    public ResponseEntity<List<Cart>> queryCarts() {
        List<Cart> carts = this.cartService.queryCarts();
        if (CollectionUtils.isEmpty(carts)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(carts);
    }

    /**
     * 更新购物车数量.
     * @param cart
     * @return
     */
    @PutMapping
    public ResponseEntity<Void> updateNum(@RequestBody Cart cart) {
        this.cartService.updateNum(cart);
        return ResponseEntity.noContent().build();
    }

}
