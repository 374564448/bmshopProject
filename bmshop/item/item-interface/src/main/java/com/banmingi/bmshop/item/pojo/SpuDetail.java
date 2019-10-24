package com.banmingi.bmshop.item.pojo;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @auther 半命i 2019/10/23
 * @description
 */
@Data
@Table(name = "tb_spu_detail")
public class SpuDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long spuId;  //对应的tb_spu表的id
    private String description; //商品描述
    private String specialSpec; //商品特殊规格的名称及可选值模板
    private String genericSpec; //商品的全局规格属性
    private String packingList; //包装清单
    private String afterService; //售后服务


}
