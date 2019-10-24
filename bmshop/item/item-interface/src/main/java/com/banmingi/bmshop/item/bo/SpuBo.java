package com.banmingi.bmshop.item.bo;

import com.banmingi.bmshop.item.pojo.Sku;
import com.banmingi.bmshop.item.pojo.Spu;
import com.banmingi.bmshop.item.pojo.SpuDetail;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @auther 半命i 2019/10/23
 * @description
 */
@Data
public class SpuBo extends Spu {

    private String cname;  //分类名称
    private String bname;  //品牌名称

    private SpuDetail spuDetail;

    private List<Sku> skus;

}
