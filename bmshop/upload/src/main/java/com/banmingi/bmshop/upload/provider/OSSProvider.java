package com.banmingi.bmshop.upload.provider;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import org.apache.tomcat.jni.FileInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @auther 半命i 2019/10/22
 * @description
 */
public class OSSProvider {

    private static final String ENDPOINT = "oss-cn-qingdao.aliyuncs.com";
    private static final String ACCESS_KEY_ID = "LTAI4FxG4FAfSTEoMPxoAEr4";
    private static final String ACCESS_KEY_SECRET = "QB5W0qoZfYh73N8KT44AyjU2BmxS13";
    private static final String BUCKET_NAME = "banmingi-bmshop";

    /**
     * 上传文件到阿里云OSS服务器
     *
     * @param multipartFile
     * @return
     */
    public static String upload(MultipartFile multipartFile) {

        //从新定义随机文件名
        String fileName = multipartFile.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        String newName = UUID.randomUUID() + "." + suffix;

        OSS client = new OSSClientBuilder().build(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);

        try {
            client.putObject(new PutObjectRequest(BUCKET_NAME, newName, new ByteArrayInputStream(multipartFile.getBytes())));
            // 上传文件路径 = http://BUCKET_NAME.ENDPOINT/自定义路径/fileName
            String url = "http://" + BUCKET_NAME + "." + ENDPOINT + "/" + newName;
            return url;
        } catch (IOException e) {
            return null;
        } finally {
            client.shutdown();
        }
    }

}
