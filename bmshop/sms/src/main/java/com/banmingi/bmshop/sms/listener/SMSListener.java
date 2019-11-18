package com.banmingi.bmshop.sms.listener;

import com.banmingi.bmshop.sms.utils.SMSUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * @auther 半命i 2019/11/18
 * @description
 */
@Component
public class SMSListener {

    @Autowired
    private SMSUtils smsUtils;

    /**
     * 发送短信验证码.
     * @param msg
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "BMSHOP.SMS.QUEUE",durable = "true"),
            exchange = @Exchange(value = "BMSHOP.SMS.EXCHANGE",ignoreDeclarationExceptions = "true",type = ExchangeTypes.TOPIC),
            key = {"verifycode.sms"}
    ))
    public void sendSMS(Map<String,String> msg) {
        if(CollectionUtils.isEmpty(msg)) {
            return;
        }
        String phone = msg.get("phone");
        String code = msg.get("code");
        if(StringUtils.isNoneBlank(phone) && StringUtils.isNoneBlank(code)) {
            this.smsUtils.sendSMS(phone,code);
        }
    }
}
