package com.banmingi.bmshop.upload.controller;

import com.banmingi.bmshop.upload.service.UploadService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @auther 半命i 2019/10/20
 * @description
 */
@RestController
@RequestMapping("upload")
public class UploadController {

    @Autowired
    private UploadService uploadService;

    /**
     * 图片上传.
     * @param file
     * @return
     */
    @PostMapping("image")
    public ResponseEntity<String> uploadImage(@RequestParam("file")MultipartFile file) {
        String url = this.uploadService.uploadImage(file);
        if(StringUtils.isBlank(url)) {
            //图片上传失败 400
            return ResponseEntity.badRequest().build();
        }
      return ResponseEntity.status(HttpStatus.CREATED).body(url);
    }
}
