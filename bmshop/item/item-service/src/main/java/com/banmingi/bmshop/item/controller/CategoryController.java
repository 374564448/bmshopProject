package com.banmingi.bmshop.item.controller;

import com.banmingi.bmshop.item.pojo.Category;
import com.banmingi.bmshop.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @auther 半命i 2019/10/19
 * @description
 */
@RestController
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 查询商品分类.根据父节点id查询子节点
     *
     * @param pid parent_id
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity<List<Category>> queryCategoriesByPid(
            @RequestParam(value = "pid", defaultValue = "0") Long pid) {
        if (pid == null || pid < 0) {
            //400: 参数不合法
            return ResponseEntity.badRequest().build();
        }
        List<Category> categories = this.categoryService.queryCategoriesByPid(pid);
        if (CollectionUtils.isEmpty(categories)) {
            //404: 资源服务器未找到
            return ResponseEntity.notFound().build();
        }
        //200: 查询成功
        return ResponseEntity.ok(categories);
    }


    /**
     * 据分类id集合获取分类名称集合.
     * @param ids
     * @return
     */
    @GetMapping
    public ResponseEntity<List<String>> queryNamesByIds(@RequestParam("ids") List<Long> ids) {
        List<String> names = this.categoryService.queryNamesByIds(ids);
        if (CollectionUtils.isEmpty(names)) {
            //404: 资源服务器未找到
            return ResponseEntity.notFound().build();
        }
        //200: 查询成功
        return ResponseEntity.ok(names);
    }


}
