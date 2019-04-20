package com.xuecheng.api.media;

import com.xuecheng.framework.domain.media.MediaFile;
import com.xuecheng.framework.domain.media.request.QueryMediaFileRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;

/**
 * @Auther: Shindou
 * @Date: 2019/4/20
 * @Description: com.xuecheng.api.media
 * @Version: 1.0
 */
public interface MediaFileControllerApi {

    //按条件分页查询所有媒资
    QueryResponseResult<MediaFile> findMediaFiles(
            int page, int size, QueryMediaFileRequest queryMediaFileRequest);
}
