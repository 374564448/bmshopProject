package com.banmingi.bmshop.sms.utils;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @auther 半命i 2019/11/18
 * @description
 */
@Component
public class SMSUtils {
    @Value("${sms.action}")
    private String action;
    @Value("${sms.userid}")
    private String userid;
    @Value("${sms.account}")
    private String account;
    @Value("${sms.password}")
    private String password;
    @Value("${sms.url}")
    private String url;


    /**
     * 发送短信验证码.
     * @param mobile
     * @param code
     * @return
     */
    public String sendSMS(String mobile, String code) {
        OkHttpClient client = new OkHttpClient();

        FormBody.Builder body = new FormBody.Builder();
        body.add("action",action);
        body.add("userid",userid);
        body.add("account",account);
        body.add("password",password);
        body.add("mobile",mobile);
        String content = "【bmshop】您的验证码是" + code + "。如非本人操作，请忽略此短信。";
        body.add("content",content);
        Request request = new Request.Builder()
                .url(url)
                .post(body.build()) //post请求
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()){
                //接收到的是xml格式的数据
                return response.body().string();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
