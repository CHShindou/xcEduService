package com.xuecheng.api.media;

import com.xuecheng.framework.domain.media.response.CheckChunkResult;
import com.xuecheng.framework.model.response.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Auther : shindou
 * @Date : 2019/4/18
 * @Description : 功能描述   文件上传功能接口
 * @Version : 1.0
 */
public interface MediaUploadControllerApi {

    /**
     * 上传文件之前  检查/校验文件
     * @return
     */
    public ResponseResult register(String fileMd5,String fileName,Long fileSize,String mimeType,String fileExt);

    /**
     * 检查分块：每次上传的分块都要检查是否存在(断点续传)
     * @param chunk 分块下标
     * @param chunkSize 分块大小
     */
    public CheckChunkResult checkChunk(String fileMd5,Integer chunk,Integer chunkSize);

    /**
     * 上传分块
     * @param multipartFile
     * @param chunk
     * @param fileMd5
     * @return
     */
    public ResponseResult uploadChunk(MultipartFile multipartFile,Integer chunk,String fileMd5);


    /**
     * 合并分块
     * @param fileMd5
     * @param fileName
     * @param fileSize
     * @param mimeType
     * @param fileExt
     * @return
     */
    public ResponseResult mergeChunks(String fileMd5,String fileName,Long fileSize,String mimeType,String fileExt);
}
