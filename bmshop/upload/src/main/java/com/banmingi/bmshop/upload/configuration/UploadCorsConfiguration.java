package com.banmingi.bmshop.upload.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 解决跨域问题.
 *
 * @auther 半命i 2019/10/19
 * @description
 */
@Configuration
public class UploadCorsConfiguration {

    @Bean
    public CorsFilter corsFilter() {
        //初始化cors配置对象
        CorsConfiguration configuration = new CorsConfiguration();
        //允许跨域的域名.如果要携带cookie,不能写*
        configuration.addAllowedOrigin("http://manage.bmshop.com");
        configuration.setAllowCredentials(true); //允许携带Cookie
        configuration.addAllowedMethod("*"); //允许所有请求方法通过  GET、POST、PUT......
        configuration.addAllowedHeader("*"); //允许携带任何头信息

        //初始化cors配置源对象
        UrlBasedCorsConfigurationSource corsConfigurationSource = new UrlBasedCorsConfigurationSource();
        corsConfigurationSource.registerCorsConfiguration("/**",configuration);

        return new CorsFilter(corsConfigurationSource);
    }

}
