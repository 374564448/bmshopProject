package com.banmingi.bmshop.search.repository;

import com.banmingi.bmshop.search.pojo.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @auther 半命i 2019/11/6
 * @description
 */
public interface GoodsRepository extends ElasticsearchRepository<Goods,Long> {
}
