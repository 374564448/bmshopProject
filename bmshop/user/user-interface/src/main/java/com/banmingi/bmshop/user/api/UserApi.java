package com.banmingi.bmshop.user.api;

import com.banmingi.bmshop.user.pojo.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @auther 半命i 2019/11/28
 * @description
 */
public interface UserApi {

    /**
     * 查找用户.
     * @param username
     * @param password
     * @return
     */
    @GetMapping("query")
    User queryUser(@RequestParam("username")String username,
                                          @RequestParam("password")String password);
}
