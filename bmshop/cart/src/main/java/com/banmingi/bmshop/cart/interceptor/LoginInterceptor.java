package com.banmingi.bmshop.cart.interceptor;

import com.banmingi.bmshop.auth.pojo.UserInfo;
import com.banmingi.bmshop.auth.utils.JwtUtils;
import com.banmingi.bmshop.cart.config.JwtProperties;
import com.banmingi.bmshop.common.utils.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @auther 半命i 2019/12/2
 * @description 购物车登录拦截.
 */
@Component
@EnableConfigurationProperties(JwtProperties.class)
public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 定义线程变量,用于存储UserInfo信息,以供在同一个线程内,后续业务获取到UserInfo的信息
     */
    private static final ThreadLocal<UserInfo> THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 前置方法.
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取Cookie中的token
        String token = CookieUtils.getCookieValue(request,this.jwtProperties.getCookieName());

        //解析token获取用户信息
        UserInfo userInfo = JwtUtils.getInfoFromToken(token, this.jwtProperties.getPublicKey());

        if (userInfo == null) {
            return false;
        }

        //把UserInfo放入线程变量
        THREAD_LOCAL.set(userInfo);

        return true;
    }

    /**
     * 完成后.
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception 清空THREAD_LOCAL(使用的是Tomcat的线程池,线程不会结束,必须要手动释放线程的局部变量)
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        THREAD_LOCAL.remove();
    }

    /**
     * 从线程变量中获取UserInfo
     * @return
     */
    public static UserInfo getUserInfo() {
        return THREAD_LOCAL.get();
    }




}
