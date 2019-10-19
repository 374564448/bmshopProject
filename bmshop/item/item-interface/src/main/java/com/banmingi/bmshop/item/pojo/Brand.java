package com.banmingi.bmshop.item.pojo;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @auther 半命i 2019/10/19
 * @description
 */
@Data
@Table(name = "tb_brand")
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name; //品牌名称
    private String image; //品牌图片路径
    private Character letter;  //品牌首字母
}
