package com.banmingi.bmshop.sms;

import com.banmingi.bmshop.sms.listener.SMSListener;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * @auther 半命i 2019/11/18
 * @description
 */
@SpringBootTest(classes = SMSServiceApplication.class)
@RunWith(SpringRunner.class)
public class TestSMS {
    @Autowired
    private SMSListener smsListener;

    @Test
    public void testSendSMS() {
        Map<String,String> map = new HashMap<>();
        map.put("phone","15117326032");
        map.put("code","123456");
        this.smsListener.sendSMS(map);
    }
}
