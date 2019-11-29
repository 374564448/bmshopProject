package com.banmingi.bmshop.gateway.filter;

import com.banmingi.bmshop.auth.utils.JwtUtils;
import com.banmingi.bmshop.common.utils.CookieUtils;
import com.banmingi.bmshop.gateway.config.FilterProperties;
import com.banmingi.bmshop.gateway.config.JwtProperties;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @auther 半命i 2019/11/28
 * @description
 */
@Component
@EnableConfigurationProperties({JwtProperties.class, FilterProperties.class})
public class LoginFilter extends ZuulFilter {

    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private FilterProperties filterProperties;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 10;
    }

    /**
     * 是否拦截.
     * @return
     */
    @Override
    public boolean shouldFilter() {
        //获取拦截白名单
        List<String> allowPaths = this.filterProperties.getAllowPaths();

        //初始化Zuul网关运行上下文
        RequestContext context = RequestContext.getCurrentContext();
        //获取request对象
        HttpServletRequest request = context.getRequest();

        //获取请求路径
        String url = request.getRequestURL().toString();

        //如果请求路径在白名单中就返回false(false表示不拦截)
        for (String allowPath : allowPaths) {
            if(StringUtils.contains(url,allowPath)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 拦截.
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        //初始化Zuul网关运行上下文
        RequestContext context = RequestContext.getCurrentContext();
        //获取request对象
        HttpServletRequest request = context.getRequest();
        //从cookie中获取token
        String token = CookieUtils.getCookieValue(request, this.jwtProperties.getCookieName());

        /*if(StringUtils.isBlank(token)) {
            context.setSendZuulResponse(false);
            context.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
        }*/

        try {
            JwtUtils.getInfoFromToken(token,this.jwtProperties.getPublicKey());
        } catch (Exception e) {
            e.printStackTrace();
            context.setSendZuulResponse(false);
            context.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
        }

        return null;
    }
}
