package com.banmingi.bmshop.goods.client;

import com.banmingi.bmshop.item.api.CategoryApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @auther 半命i 2019/11/5
 * @description
 */
@FeignClient("item-service")
public interface CategoryClient extends CategoryApi {
}
