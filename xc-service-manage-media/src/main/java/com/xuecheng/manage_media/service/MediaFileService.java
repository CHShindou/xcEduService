package com.xuecheng.manage_media.service;

import com.xuecheng.framework.domain.media.MediaFile;
import com.xuecheng.framework.domain.media.request.QueryMediaFileRequest;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_media.dao.MediaFileRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

/**
 * @Auther: Shindou
 * @Date: 2019/4/20
 * @Description: com.xuecheng.manage_media.service
 * @Version: 1.0
 */

@Service
public class MediaFileService {

    @Autowired
    MediaFileRepository mediaFileRepository;

    //分页查询所有mediafile
    public QueryResponseResult<MediaFile> queryMediaFiles(
            int page, int size, QueryMediaFileRequest queryMediaFileRequest){

        //初始化参数
        if(page <= 0){
            page = 1;
        }
        page = page -1;
        if(size <= 0){
            size = 10;
        }
        if(queryMediaFileRequest == null){
            queryMediaFileRequest = new QueryMediaFileRequest();
        }

        //定义查询条件匹配器
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("tag",ExampleMatcher.GenericPropertyMatchers.contains())   //标签模糊查询
                .withMatcher("fileOriginalName",ExampleMatcher.GenericPropertyMatchers.contains());  //文件原始名称模糊查询

        MediaFile mediaFile = new MediaFile();
        //填充查询条件
        if(StringUtils.isNotBlank(queryMediaFileRequest.getFileOriginalName())){
            mediaFile.setFileOriginalName(queryMediaFileRequest.getFileOriginalName());
        }
        if(StringUtils.isNotBlank(queryMediaFileRequest.getTag())){
            mediaFile.setTag(queryMediaFileRequest.getTag());
        }
        if(StringUtils.isNotBlank(queryMediaFileRequest.getProcessStatus())){
            mediaFile.setProcessStatus(queryMediaFileRequest.getProcessStatus());
        }

        Example<MediaFile> example = Example.of(mediaFile,matcher);

        //分页参数
        Pageable pageable = PageRequest.of(page,size);

        Page<MediaFile> mediaFiles = mediaFileRepository.findAll(example, pageable);

        QueryResult<MediaFile> queryResult = new QueryResult<>();
        queryResult.setTotal(mediaFiles.getTotalElements());
        queryResult.setList(mediaFiles.getContent());

        return new QueryResponseResult<>(CommonCode.SUCCESS,queryResult);
    }
}
