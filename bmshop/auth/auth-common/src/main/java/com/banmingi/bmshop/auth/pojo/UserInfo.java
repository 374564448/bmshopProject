package com.banmingi.bmshop.auth.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @auther 半命i 2019/11/27
 * @description
 * 载荷对象
 * 主要是用于，解析浏览器请求时携带得cookie中得token，从token中解析出用户名和用户id
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    private Long id;
    private String username;
}
