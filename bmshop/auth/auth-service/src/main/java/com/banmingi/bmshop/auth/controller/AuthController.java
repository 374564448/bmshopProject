package com.banmingi.bmshop.auth.controller;

import com.banmingi.bmshop.auth.config.JwtProperties;
import com.banmingi.bmshop.auth.pojo.UserInfo;
import com.banmingi.bmshop.auth.service.AuthService;
import com.banmingi.bmshop.auth.utils.JwtUtils;
import com.banmingi.bmshop.common.utils.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @auther 半命i 2019/11/28
 * @description
 */
@Controller
@EnableConfigurationProperties(JwtProperties.class)
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtProperties jwtProperties;


    /**
     * 用户授权.
     *
     * @param username
     * @param password
     * @param request
     * @param response
     * @return
     */
    @PostMapping("accredit")
    public ResponseEntity<Void> accredit(@RequestParam("username") String username,
                                         @RequestParam("password") String password,
                                         HttpServletRequest request,
                                         HttpServletResponse response) {

        String token = this.authService.accredit(username, password);

        if (StringUtils.isBlank(token)) {
            //身份认证为通过 401
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CookieUtils.setCookie(request, response, this.jwtProperties.getCookieName(), token, this.jwtProperties.getExpire() * 60);

        return ResponseEntity.ok(null);
    }

    /**
     * 身份验证.
     * @param token
     * @return
     */
    @GetMapping("verify")
    public ResponseEntity<UserInfo> verify(@CookieValue("BM_TOKEN") String token,
                                           HttpServletRequest request,
                                           HttpServletResponse response) {
        try {
            UserInfo user = JwtUtils.getInfoFromToken(token, this.jwtProperties.getPublicKey());
            if(user == null) {
                //身份认证为通过 401
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            //刷新jwt中token的有效时间
            token = JwtUtils.generateToken(user,this.jwtProperties.getPrivateKey(),this.jwtProperties.getExpire());

            //刷新cookie中的有效时间
            CookieUtils.setCookie(request,response,this.jwtProperties.getCookieName(),token,this.jwtProperties.getExpire() * 60);

            return ResponseEntity.ok(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
