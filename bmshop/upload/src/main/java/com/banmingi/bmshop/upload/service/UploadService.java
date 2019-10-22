package com.banmingi.bmshop.upload.service;

import com.banmingi.bmshop.upload.provider.OSSProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @auther 半命i 2019/10/20
 * @description
 */
@Service
public class UploadService {

    private static final List<String> CONTENT_TYPES =
            Arrays.asList("image/gif","image/jpeg","application/x-jpg","application/x-png");

    private static final Logger LOGGER = LoggerFactory.getLogger(UploadService.class);

    /**
     * 图片上传.
     * @param file
     * @return
     */
    public String uploadImage(MultipartFile file) {


            String originalFilename = file.getOriginalFilename();

            //校验文件类型
            String contentType = file.getContentType();
            if(!CONTENT_TYPES.contains(contentType)) {
                LOGGER.info("文件类型不合法：{}",originalFilename);
                return null;
            }

        try {
            //校验文件内容
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            if(bufferedImage == null) {
                LOGGER.info("文件内容不合法：{}",originalFilename);
                return null;
            }

            //上传至服务器
            String url = OSSProvider.upload(file);
            if(url == null) {
                LOGGER.info("服务器异常：{}",originalFilename);
            }
            //返回url,进行回显
            return url;
        } catch (IOException e) {
            LOGGER.info("服务器内部异常：{}",originalFilename);
            e.printStackTrace();
        }
        return null;
    }
}
