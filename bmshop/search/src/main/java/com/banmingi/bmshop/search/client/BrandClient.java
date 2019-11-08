package com.banmingi.bmshop.search.client;

import com.banmingi.bmshop.item.api.BrandApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @auther 半命i 2019/11/5
 * @description
 */
@FeignClient("item-service")
public interface BrandClient extends BrandApi {
}
