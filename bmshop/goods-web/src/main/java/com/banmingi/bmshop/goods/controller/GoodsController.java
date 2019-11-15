package com.banmingi.bmshop.goods.controller;

import com.banmingi.bmshop.goods.service.GoodsHtmlService;
import com.banmingi.bmshop.goods.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

/**
 * @auther 半命i 2019/11/12
 * @description
 */
@Controller
public class GoodsController {

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private GoodsHtmlService goodsHtmlService;


    @GetMapping("item/{id}.html")
    public String toItemPage(@PathVariable("id") Long id, Model model) {
        Map<String, Object> map = this.goodsService.loadData(id);

        model.addAllAttributes(map);

        //生成静态页面
        this.goodsHtmlService.createHtml(id);

        return "item";
    }
}
