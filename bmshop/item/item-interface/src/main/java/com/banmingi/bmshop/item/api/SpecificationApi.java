package com.banmingi.bmshop.item.api;

import com.banmingi.bmshop.item.pojo.SpecParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @auther 半命i 2019/10/22
 * @description
 */
@RequestMapping("spec")
public interface SpecificationApi {


    /**
     * 根据条件查询规格参数.
     *
     * @param gid
     * @return
     */
    @GetMapping("params")
    List<SpecParam> queryParams(
            @RequestParam(value = "gid", required = false) Long gid,
            @RequestParam(value = "cid", required = false) Long cid,
            @RequestParam(value = "generic", required = false) Boolean generic,
            @RequestParam(value = "searching", required = false) Boolean searching
    );
}
