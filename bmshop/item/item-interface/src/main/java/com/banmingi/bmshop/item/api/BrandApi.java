package com.banmingi.bmshop.item.api;

import com.banmingi.bmshop.item.pojo.Brand;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * @auther 半命i 2019/10/19
 * @description
 */
@RequestMapping("brand")
public interface BrandApi {


    /**
     * 根据品牌id查询品牌.
     * @param id
     * @return
     */
    @GetMapping("{id}")
    Brand queryBrandById(@PathVariable("id") Long id);

}
