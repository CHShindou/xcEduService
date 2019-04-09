package com.xuecheng.api.filesystem;

import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Auther: Shindou
 * @Date: 2019/4/9
 * @Description: com.xuecheng.api.filesystem
 * @Version: 1.0
 */
public interface FileSystemControllerApi {

    /**
     * @param multipartFile  文件
     * @param filetag        业务标签
     * @param businesskey    业务key
     * @param metadata       文件元信息
     * @return
     */
    UploadFileResult uploadFile(MultipartFile multipartFile,
                                String filetag,
                                String businesskey,
                                String metadata);
}
