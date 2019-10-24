package com.banmingi.bmshop.item.pojo;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @auther 半命i 2019/10/23
 * @description
 */
@Data
@Table(name = "tb_spu")
public class Spu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long brandId; //品牌id
    private Long cid1;  //一级类目
    private Long cid2;  //二级类目
    private Long cid3;  //三级类目
    private String title; //标题
    private String subTitle; //子标题
    private Boolean saleable;  //是否上架
    private Boolean valid;  //是否有效,逻辑删除用
    private Date createTime;  //创建时间
    private Date lastUpdateTime;  //最后修改时间

}
