package com.banmingi.bmshop.item.mapper;

import com.banmingi.bmshop.item.pojo.Category;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * @auther 半命i 2019/10/19
 * @description
 */
public interface CategoryMapper extends Mapper<Category>, SelectByIdListMapper<Category,Long> {
}
