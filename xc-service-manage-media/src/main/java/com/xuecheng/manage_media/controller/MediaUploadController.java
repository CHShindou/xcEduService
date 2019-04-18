package com.xuecheng.manage_media.controller;

import com.xuecheng.api.media.MediaUploadControllerApi;
import com.xuecheng.framework.domain.media.response.CheckChunkResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_media.service.MediaUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Auther : shindou
 * @Date : 2019/4/18
 * @Description : 功能描述
 * @Version : 1.0
 */
@RestController
@RequestMapping("/media/upload")
public class MediaUploadController implements MediaUploadControllerApi {

    @Autowired
    MediaUploadService mediaUploadService;

    @Override
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public ResponseResult register(String fileMd5, String fileName, Long fileSize, String mimeType, String fileExt) {
        return mediaUploadService.register(fileMd5,fileName,fileSize,mimeType,fileExt);
    }

    @Override
    @RequestMapping(value = "/checkchunk",method = RequestMethod.POST)
    public CheckChunkResult checkChunk(String fileMd5, Integer chunk, Integer chunkSize) {
        return mediaUploadService.checkChunk(fileMd5,chunk,chunkSize);
    }

    @Override
    @RequestMapping(value = "/uploadchunk",method = RequestMethod.POST)
    public ResponseResult uploadChunk(MultipartFile multipartFile, Integer chunk, String fileMd5) {
        return mediaUploadService.uploadChunk(multipartFile,chunk,fileMd5);
    }

    @Override
    @RequestMapping(value = "/mergechunks",method = RequestMethod.POST)
    public ResponseResult mergeChunks(String fileMd5, String fileName, Long fileSize, String mimeType, String fileExt) {
        return mediaUploadService.mergeChunk(fileMd5,fileName,fileSize,mimeType,fileExt);
    }
}
