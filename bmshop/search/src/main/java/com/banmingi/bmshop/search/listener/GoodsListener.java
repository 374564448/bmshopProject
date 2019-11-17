package com.banmingi.bmshop.search.listener;

import com.banmingi.bmshop.search.service.SearchService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @auther 半命i 2019/11/17
 * @description 监听处理消息队列的消息.
 */
//@Component
public class GoodsListener {

    @Autowired
    private SearchService searchService;

    /**
     * 处理新增商品和更新商品的消息.(更新ES中的商品内容)
     * @param id
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "BMSHOP.SEARCH.SAVE.QUEUE",durable = "true"),
    exchange = @Exchange(value = "BMSHOP.ITEM.EXCHANGE",ignoreDeclarationExceptions = "true",type = ExchangeTypes.TOPIC),
            key = {"item.insert","item.update"}))
    public void save(Long id) throws IOException {
        if(id == null) {
            return;
        }
        this.searchService.save(id);
    }

    /**
     * 处理删除商品的消息.(删除ES中的商品内容)
     * @param id
     * @throws IOException
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "BMSHOP.SEARCH.DELETE.QUEUE",durable = "true"),
            exchange = @Exchange(value = "BMSHOP.ITEM.EXCHANGE",ignoreDeclarationExceptions = "true",type = ExchangeTypes.TOPIC),
            key = {"item.delete"}))
    public void delete(Long id) {
        if(id == null) {
            return;
        }
        this.searchService.delete(id);
    }

}
