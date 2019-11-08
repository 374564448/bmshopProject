package com.banmingi.bmshop.elasticsearch.test;

import com.banmingi.bmshop.common.pojo.PageResult;
import com.banmingi.bmshop.item.bo.SpuBo;
import com.banmingi.bmshop.search.SearchServiceApplication;
import com.banmingi.bmshop.search.client.GoodsClient;
import com.banmingi.bmshop.search.pojo.Goods;
import com.banmingi.bmshop.search.repository.GoodsRepository;
import com.banmingi.bmshop.search.service.SearchService;
import com.netflix.discovery.shared.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @auther 半命i 2019/11/6
 * @description
 */
@SpringBootTest(classes = SearchServiceApplication.class)
@RunWith(SpringRunner.class)
public class ElasticSearchTest {
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private SearchService searchService;
    @Autowired
    private GoodsClient goodsClient;

    @Test
    public void test() {
        this.elasticsearchTemplate.createIndex(Goods.class);
        this.elasticsearchTemplate.putMapping(Goods.class);

        //分页查询spu
        Integer page = 1;
        Integer rows = 100;
        do {
            PageResult<SpuBo> result = this.goodsClient.querySpuByPage(null, null, page, rows);
            //当前页数据
            List<SpuBo> items = result.getItems();

            //处理List<SpuBo> => List<Goods>
            List<Goods> goodsList = items.stream().map(spuBo -> {
                try {
                    return this.searchService.buildGoods(spuBo);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }).collect(Collectors.toList());

            //执行新增数据的方法
            this.goodsRepository.saveAll(goodsList);
            rows = items.size();
            page++;
        } while (rows == 100);
    }
}
