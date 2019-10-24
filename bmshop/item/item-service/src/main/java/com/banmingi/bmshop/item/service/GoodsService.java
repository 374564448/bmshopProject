package com.banmingi.bmshop.item.service;

import com.banmingi.bmshop.common.pojo.PageResult;
import com.banmingi.bmshop.item.bo.SpuBo;
import com.banmingi.bmshop.item.mapper.BrandMapper;
import com.banmingi.bmshop.item.mapper.CategoryMapper;
import com.banmingi.bmshop.item.mapper.SkuMapper;
import com.banmingi.bmshop.item.mapper.SpuDetailMapper;
import com.banmingi.bmshop.item.mapper.SpuMapper;
import com.banmingi.bmshop.item.mapper.StockMapper;
import com.banmingi.bmshop.item.pojo.Brand;
import com.banmingi.bmshop.item.pojo.Spu;
import com.banmingi.bmshop.item.pojo.SpuDetail;
import com.banmingi.bmshop.item.pojo.Stock;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @auther 半命i 2019/10/23
 * @description
 */
@Service
public class GoodsService {

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private SpuDetailMapper spuDetailMapper;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private CategoryService categoryService;

    /**
     * 根据条件分页查询spu.
     * @param key
     * @param saleable
     * @param page
     * @param rows
     * @return
     */
    public PageResult<SpuBo> querySpuByPage(String key, Boolean saleable, Integer page, Integer rows) {
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();

        //添加查询条件
        if(StringUtils.isNotBlank(key)) {
            criteria.andLike("title","%"+ key +"%");
        }

        //添加上下架的过滤条件
        if(saleable != null) {
            criteria.andEqualTo("saleable",saleable);
        }

        //添加分页
        PageHelper.startPage(page,rows);

        //执行查询,获取spu集合
        List<Spu> spus = this.spuMapper.selectByExample(example);

        PageInfo<Spu> pageInfo = new PageInfo<>(spus);

        //spu集合转化成SpuBo集合
        List<SpuBo> spuBos = spus.stream().map(spu -> {
            SpuBo spuBo = new SpuBo();
            BeanUtils.copyProperties(spu, spuBo);
            //查询品牌名称
            Brand brand = this.brandMapper.selectByPrimaryKey(spu.getBrandId());
            spuBo.setCname(brand.getName());
            //查询分类名称
            List<String> names =
                    categoryService.queryNamesByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
            spuBo.setBname(StringUtils.join(names, "-"));

            return spuBo;

        }).collect(Collectors.toList());

        //返回PageResult<SpuBo>


        return new PageResult<>(pageInfo.getTotal(),spuBos);
    }

    /**
     * 新增商品(手机).
     * @param spuBo
     */
    @Transactional
    public void saveGoods(SpuBo spuBo) {
        //先新增spu
        spuBo.setId(null);
        spuBo.setSaleable(true);
        spuBo.setValid(true);
        spuBo.setCreateTime(new Date());
        spuBo.setLastUpdateTime(spuBo.getCreateTime());
        this.spuMapper.insertSelective(spuBo);

        //再新增spuDetail
        SpuDetail spuDetail = spuBo.getSpuDetail();
        spuDetail.setSpuId(spuBo.getId());
        this.spuDetailMapper.insertSelective(spuDetail);

        spuBo.getSkus().forEach(sku -> {
            //再新增sku
            sku.setId(null);
            sku.setSpuId(spuBo.getId());
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            this.skuMapper.insertSelective(sku);

            //再新增stock
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            this.stockMapper.insertSelective(stock);
        });




    }
}
