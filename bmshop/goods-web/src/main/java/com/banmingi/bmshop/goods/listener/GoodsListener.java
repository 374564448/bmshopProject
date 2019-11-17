package com.banmingi.bmshop.goods.listener;

import com.banmingi.bmshop.goods.service.GoodsHtmlService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @auther 半命i 2019/11/17
 * @description 监听处理消息队列的消息
 */
@Component
public class GoodsListener {

    @Autowired
    private GoodsHtmlService goodsHtmlService;

    /**
     * 处理新增商品和更新商品的消息.(生成新的静态页面)
     * @param id
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "BMSHOP.ITEM.SAVE.QUEUE",durable = "true"),
            exchange = @Exchange(value = "BMSHOP.ITEM.EXCHANGE",ignoreDeclarationExceptions = "true",type = ExchangeTypes.TOPIC),
            key = {"item.insert","item.update"}
    ))
    public void save(Long id) {
        if(id == null) {
            return;
        }
        this.goodsHtmlService.createHtml(id);
    }

    /**
     * 处理删除商品的消息.(删除静态页面)
     * @param id
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "BMSHOP.ITEM.DELETE.QUEUE",durable = "true"),
            exchange = @Exchange(value = "BMSHOP.ITEM.EXCHANGE",ignoreDeclarationExceptions = "true",type = ExchangeTypes.TOPIC),
            key = {"item.delete"}
    ))
    public void delete(Long id) {
        if(id == null) {
            return;
        }
        this.goodsHtmlService.deleteHtml(id);
    }
}
