package com.banmingi.bmshop.auth.client;

import com.banmingi.bmshop.user.api.UserApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @auther 半命i 2019/11/28
 * @description
 */
@FeignClient("user-service")
public interface UserClient extends UserApi {
}
