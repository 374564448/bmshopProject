package com.banmingi.bmshop.item.service;

import com.banmingi.bmshop.common.pojo.PageResult;
import com.banmingi.bmshop.item.mapper.BrandMapper;
import com.banmingi.bmshop.item.pojo.Brand;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @auther 半命i 2019/10/19
 * @description
 */
@Service
public class BrandService {

    @Autowired
    private BrandMapper brandMapper;

    /**
     * 根据查询条件分页并排序查询品牌信息.
     *
     * @param key
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @return
     */
    public PageResult<Brand> queryBrandsByPage(
            String key, Integer page, Integer rows, String sortBy, Boolean desc) {
        //初始化example对象
        Example example = new Example(Brand.class);
        Example.Criteria criteria = example.createCriteria();

        //根据name模糊查询,或者根据首字母查询
        if(StringUtils.isNotBlank(key)) {
            criteria.andLike("name","%"+ key +"%").orEqualTo("letter",key);
        }
        //添加分页条件
        PageHelper.startPage(page,rows);

        //添加排序条件
        if(StringUtils.isNotBlank(sortBy)) {
            example.setOrderByClause(sortBy+ " " + (desc?"desc":"asc"));
        }

        List<Brand> brands = this.brandMapper.selectByExample(example);
        //包装成pageInfo
        PageInfo<Brand> pageInfo = new PageInfo<>(brands);

        //封装结果集并返回
        return new PageResult<Brand>(pageInfo.getTotal(),pageInfo.getList());
    }

    /**
     * 新增品牌.
     *
     * @param brand
     * @param cids
     */
    @Transactional
    public void saveBrand(Brand brand, List<Long> cids) {
        //新增brand
        this.brandMapper.insertSelective(brand);
        //新增中间表(tb_category_brand)
        cids.forEach(cid -> {
            this.brandMapper.insertCategoryAndBrand(cid,brand.getId());
        });

    }

    /**
     * 根据分类id查询品牌列表.
     * @param cid
     * @return
     */
    public List<Brand> queryBrandsByCid(Long cid) {
        return this.brandMapper.selectBrandsByCid(cid);
    }
}