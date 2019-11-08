package com.banmingi.bmshop.search.service;

import com.banmingi.bmshop.common.pojo.PageResult;
import com.banmingi.bmshop.item.pojo.Brand;
import com.banmingi.bmshop.item.pojo.Sku;
import com.banmingi.bmshop.item.pojo.SpecParam;
import com.banmingi.bmshop.item.pojo.Spu;
import com.banmingi.bmshop.item.pojo.SpuDetail;
import com.banmingi.bmshop.search.client.BrandClient;
import com.banmingi.bmshop.search.client.CategoryClient;
import com.banmingi.bmshop.search.client.GoodsClient;
import com.banmingi.bmshop.search.client.SpecificationClient;
import com.banmingi.bmshop.search.pojo.Goods;
import com.banmingi.bmshop.search.pojo.SearchRequest;
import com.banmingi.bmshop.search.repository.GoodsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @auther 半命i 2019/11/5
 * @description
 */
@Service
public class SearchService {

    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private BrandClient brandClient;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private SpecificationClient specificationClient;
    @Autowired
    private GoodsRepository goodsRepository;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     *
     * @param request
     * @return
     */
    public PageResult<Goods> search(SearchRequest request) {
        if(StringUtils.isBlank(request.getKey())) {
            return null;
        }
        //自定义查询构建器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //添加查询条件
        queryBuilder.withQuery(QueryBuilders.matchQuery("all",request.getKey()).operator(Operator.AND));
        //添加分页 分页页码从0开始
        queryBuilder.withPageable(PageRequest.of(request.getPage()-1,request.getSize()));
        //添加结果集过滤
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id","skus","subTitle"},null));

        //执行查询,获取结果集
        Page<Goods> goodsPage = this.goodsRepository.search(queryBuilder.build());

        return new PageResult<>(goodsPage.getTotalElements(),goodsPage.getTotalPages(),goodsPage.getContent());
    }

    /**
     * 根据spu构建Goods
     * @param spu
     * @return
     * @throws IOException
     */
    public Goods buildGoods(Spu spu) throws IOException {
        Goods goods = new Goods();

        //根据分类id查询分类名称
        List<String> names =
                this.categoryClient.queryNamesByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));

        //根据品牌id查询品牌
        Brand brand = this.brandClient.queryBrandById(spu.getBrandId());

        //根据spuId查询所有sku
        List<Sku> skus = this.goodsClient.querySkusBySpuId(spu.getId());
        //初始化价格集合,来收集所有的sku的价格
        List<Long> prices = new ArrayList<>();
        //收集sku必要字段信息
        List<Map<String,Object>> skuMapList = new ArrayList<>();
        skus.forEach(sku -> {
            prices.add(sku.getPrice());

            Map<String,Object> map = new HashMap<>();
            map.put("id",sku.getId());
            map.put("title",sku.getTitle());
            map.put("price",sku.getPrice());
            //获取sku中的图片(数据库中的图片可能是多张,所以取第一张)
            map.put("image",StringUtils.isBlank(sku.getImages())?"":StringUtils.split(sku.getImages(),",")[0]);
            skuMapList.add(map);
        });

        //根据spu中的cid3查询出所有的搜索规格参数
        List<SpecParam> params = this.specificationClient.queryParams(null, spu.getCid3(), null, true);
        //根据spuId获取spuDetail
        SpuDetail spuDetail = this.goodsClient.querySpuDetailBySpuId(spu.getId());
        //把通用规格参数值进行反序列化
        Map<String, Object> genericSpecMap = MAPPER.readValue(spuDetail.getGenericSpec(), new TypeReference<Map<String, Object>>() {});
        //把特殊规格参数值进行反序列化
        Map<String, List<Object>> specialSpecMap = MAPPER.readValue(spuDetail.getSpecialSpec(), new TypeReference<Map<String, List<Object>>>() {});

        Map<String,Object> specs = new HashMap<>();
        params.forEach(param-> {
            //判断规格参数类型是否是通用的参数
            if (param.getGeneric()) {
               String value = genericSpecMap.get(param.getId().toString()).toString();
                //判断规格参数是否是数值类型,如果是数值类型,应该返回一个区间
               if (param.getNumeric()) {
                    value = chooseSegment(value,param);
               }
               specs.put(param.getName(),value);
            } else { //特殊规格参数
                List<Object> value = specialSpecMap.get(param.getId().toString());
                specs.put(param.getName(),value);
            }
        });

        goods.setId(spu.getId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setBrandId(spu.getBrandId());
        goods.setCreateTime(spu.getCreateTime());
        goods.setSubTitle(spu.getSubTitle());
        //拼接all字段,需要标题分类名称和品牌名称
        goods.setAll(spu.getTitle()+" "+ StringUtils.join(names," ") + " "+ brand.getName());
        //获取spu下所有sku的价格
        goods.setPrice(prices);
        //获取spu下所有sku并转换成json字符串
        goods.setSkus(MAPPER.writeValueAsString(skuMapList));
        //获取所有查询的规格参数 {name:value}
        goods.setSpecs(specs);
        return goods;
    }

    /**
     * 把tb_spec_param表中的segments字段 解析为前端显示的可搜索范围字段
     * @param value
     * @param specParam
     * @return
     */
    private String chooseSegment(String value, SpecParam specParam) {
        String[] split = specParam.getSegments().split(",");
        //使用NumberUtils工具类把字符串解析为double值
        Double val = NumberUtils.toDouble(value);
        String result = "其他";
        //获取每一个区间值，判断数值value是否在对应区间中
        for (String s : split) {
            String[] spl = s.split("-");
            //获取每个数值区间的起始区间
            Double begin = NumberUtils.toDouble(spl[0]);
            //因为有可能不存在结束区间，如1000以上，故end先取一个最大值
            Double end = Double.MAX_VALUE;
            //判断该数值区间是否为一个闭区间，如果为闭区间，则表示存在结束区间，应将end的取值为对应结束区间的值
            if (spl.length == 2) {
                end = NumberUtils.toDouble(spl[1]);
            }
            //获得了起始区间和结束区间后，开始判断传过来的value对应的值属于哪个区间
            if (val < end && val > begin) {
                if (spl.length == 1) {
                    result = spl[0] + specParam.getUnit() + "以上";
                } else {
                    result = s + specParam.getUnit();
                }
                break;
            }
        }
        return result;

    }


}
