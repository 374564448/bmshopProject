package com.banmingi.bmshop.user.controller;

import com.banmingi.bmshop.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @auther 半命i 2019/11/18
 * @description
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户数据的校验: 用户名唯一性、手机号.
     * @param data
     * @param type:1校验用户名 2校验手机号
     * @return
     */
    @GetMapping("check/{data}/{type}")
    public ResponseEntity<Boolean> checkUser(@PathVariable("data")String data,
                                             @PathVariable("type")Integer type) {
        Boolean bool = this.userService.checkUser(data,type);
        if(bool == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(bool);
    }
}
