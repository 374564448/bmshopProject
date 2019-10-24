package com.banmingi.bmshop.item.pojo;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @auther 半命i 2019/10/24
 * @description
 */
@Data
@Table(name = "tb_stock")
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long skuId;
    private Integer seckillStock; //秒杀可用库存
    private Integer seckillTotal; //已秒杀数量
    private Integer stock;  //正常库存

}
