package com.banmingi.bmshop.item.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

/**
 * @auther 半命i 2019/10/19
 * @description
 */
@RequestMapping("category")
public interface CategoryApi {


    /**
     * 据分类id集合获取分类名称集合.
     * @param ids
     * @return
     */
    @GetMapping
    List<String> queryNamesByIds(@RequestParam("ids") List<Long> ids);

}
