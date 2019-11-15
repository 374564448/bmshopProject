package com.banmingi.bmshop.item.api;

import com.banmingi.bmshop.common.pojo.PageResult;
import com.banmingi.bmshop.item.bo.SpuBo;
import com.banmingi.bmshop.item.pojo.Sku;
import com.banmingi.bmshop.item.pojo.Spu;
import com.banmingi.bmshop.item.pojo.SpuDetail;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @auther 半命i 2019/11/5
 * @description
 */
public interface GoodsApi {
    /**
     * 根据spuId查询spuDetail.
     * @param spuId
     * @return
     */
    @GetMapping("spu/detail/{spuId}")
    SpuDetail querySpuDetailBySpuId(@PathVariable("spuId")Long spuId);

    /**
     * 根据条件分页查询spu.
     * @param key
     * @param saleable
     * @param page
     * @param rows
     * @return
     */
    @GetMapping("spu/page")
    PageResult<SpuBo> querySpuByPage(
            @RequestParam(value = "key",required = false) String key,
            @RequestParam(value = "saleable",required = false) Boolean saleable,
            @RequestParam(value = "page",defaultValue = "1") Integer page,
            @RequestParam(value = "rows",defaultValue = "5") Integer rows
    );

    /**
     * 根据spuId查询sku集合
     * @param spuId
     * @return
     */
    @GetMapping("sku/list")
   List<Sku> querySkusBySpuId(@RequestParam("id")Long spuId);

    /**
     * 根据id 查询spu
     * @param id
     * @return
     */
    @GetMapping("{id}")
    Spu querySpuById(@PathVariable("id") Long id);
}
