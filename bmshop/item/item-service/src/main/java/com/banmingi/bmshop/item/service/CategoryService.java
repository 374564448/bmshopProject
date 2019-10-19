package com.banmingi.bmshop.item.service;

import com.banmingi.bmshop.item.mapper.CategoryMapper;
import com.banmingi.bmshop.item.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @auther 半命i 2019/10/19
 * @description
 */
@Service
public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 查询商品分类. 根据父节点id查询子节点
     * @param pid parent_id
     * @return
     */
    public List<Category> queryCategoriesByPid(Long pid) {
        Category record = new Category();
        record.setParentId(pid);

        return this.categoryMapper.select(record);
    }
}
