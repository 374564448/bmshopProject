package com.banmingi.bmshop.auth.service;

import com.banmingi.bmshop.auth.client.UserClient;
import com.banmingi.bmshop.auth.config.JwtProperties;
import com.banmingi.bmshop.auth.pojo.UserInfo;
import com.banmingi.bmshop.auth.utils.JwtUtils;
import com.banmingi.bmshop.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @auther 半命i 2019/11/28
 * @description
 */
@Service
public class AuthService {

    @Autowired
    private UserClient userClient;

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 用户授权.
     * @param username
     * @param password
     * @return
     */
    public String accredit(String username, String password) {

        //根据用户名和密码查询
        User user = userClient.queryUser(username, password);

        //判断user
        if(user == null) {
            return null;
        }

        try {
            //通过JwtUtils生成Jwt类型的token
            UserInfo userInfo = new UserInfo();
            userInfo.setId(user.getId());
            userInfo.setUsername(user.getUsername());
            return JwtUtils.generateToken(userInfo,this.jwtProperties.getPrivateKey(),this.jwtProperties.getExpire());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
