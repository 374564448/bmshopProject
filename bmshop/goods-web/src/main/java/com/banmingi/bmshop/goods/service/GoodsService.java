package com.banmingi.bmshop.goods.service;

import com.banmingi.bmshop.goods.client.BrandClient;
import com.banmingi.bmshop.goods.client.CategoryClient;
import com.banmingi.bmshop.goods.client.GoodsClient;
import com.banmingi.bmshop.goods.client.SpecificationClient;
import com.banmingi.bmshop.item.pojo.Brand;
import com.banmingi.bmshop.item.pojo.Sku;
import com.banmingi.bmshop.item.pojo.SpecGroup;
import com.banmingi.bmshop.item.pojo.SpecParam;
import com.banmingi.bmshop.item.pojo.Spu;
import com.banmingi.bmshop.item.pojo.SpuDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @auther 半命i 2019/11/13
 * @description
 */
@Service
public class GoodsService {

    @Autowired
    private BrandClient brandClient;
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private SpecificationClient specificationClient;

    /**
     * 加载各种参数.
     * @param spuId
     * @return
     */
    public Map<String,Object> loadData(Long spuId) {
        Map<String,Object> model = new HashMap<>();

        //根据spuId查询spu
        Spu spu = this.goodsClient.querySpuById(spuId);

        //查询spuDetail
        SpuDetail spuDetail = this.goodsClient.querySpuDetailBySpuId(spuId);

        //查询分类: List<Map<String,Object>>
        List<Long> cids = Arrays.asList(spu.getCid1(),spu.getCid2(),spu.getCid3());
        List<String> names = this.categoryClient.queryNamesByIds(cids);
        //初始化一个分类的List
        List<Map<String,Object>> categories = new ArrayList<>();
        for (int i = 0; i < cids.size(); i++) {
            Map<String,Object> map = new HashMap<>();
            map.put("id",cids.get(i));
            map.put("name",names.get(i));
            categories.add(map);
        }

        //查询brand
        Brand brand = this.brandClient.queryBrandById(spu.getBrandId());

        //查询skus
        List<Sku> skus = this.goodsClient.querySkusBySpuId(spuId);

        //查询groups(规格参数组)
        List<SpecGroup> groups = this.specificationClient.queryGroupsWithParam(spu.getCid3());

        //查询特殊的规格参数: Map<Long,String>
        List<SpecParam> params = this.specificationClient.queryParams(null, spu.getCid3(), false, null);
        Map<Long,String> paramMap = new HashMap<>();
        params.forEach(param -> {
            paramMap.put(param.getId(),param.getName());
        });

        model.put("spu",spu);
        model.put("spuDetail",spuDetail);
        model.put("categories",categories);
        model.put("brand",brand);
        model.put("skus",skus);
        model.put("groups",groups);
        model.put("paramMap",paramMap);

        return model;
    }

}
