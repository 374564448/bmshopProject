package com.banmingi.bmshop.goods.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * @auther 半命i 2019/11/15
 * @description 生成thymeleaf静态页面,目的是在第一次页面初始化就把数据加载在页面中(由动态页面变成静态页面)，减少并发压力
 */
@Service
public class GoodsHtmlService {

    @Autowired
    private TemplateEngine engine;  //模板引擎
    @Autowired
    private GoodsService goodsService;

    /**
     * 页面静态化
     * @param spuId
     */
    public void createHtml(Long spuId) {

        //初始化运行上下文
        Context context = new Context();
        //设置数据模型
        context.setVariables(goodsService.loadData(spuId));

        PrintWriter printWriter = null;
        try {
            //创建流对象,把静态文件生成到服务器本地,路径设置在nginx路径的html路径下
            File file = new File("E:\\Nginx\\nginx-1.14.2\\html\\item\\" + spuId + ".html");
            printWriter = new PrintWriter(file);

            this.engine.process("item",context,printWriter);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if(printWriter != null) {
                printWriter.close();
            }
        }
    }

}
