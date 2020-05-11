package com.xuecheng.manage_media_process.mq;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.domain.media.MediaFile;
import com.xuecheng.framework.domain.media.MediaFileProcess_m3u8;
import com.xuecheng.framework.utils.HlsVideoUtil;
import com.xuecheng.framework.utils.Mp4VideoUtil;
import com.xuecheng.manage_media_process.dao.MediaFileRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @Auther : shindou
 * @Date : 2019/4/19
 * @Description : 功能描述
 * @Version : 1.0
 */

@Component
public class MediaProcessTask {

    @Autowired
    MediaFileRepository mediaFileRepository;

    //文件保存的根目录
    @Value("${xc-service-manage-media.video-location}")
    String videoLocation;

    //ffmpeg安装路径
    @Value("${xc-service-manage-media.ffmpeg-path}")
    String ffmpegPath;


    //@RabbitListener(queues = {"${xc-service-manage-media.mq.queue-media-video-processor}"},containerFactory = "customContainerFactory")

    @RabbitListener(queues = "${xc-service-manage-media.mq.queue-media-video-processor}")
    public void receiveMediaProcessTask(String msg){

        if(StringUtils.isBlank(msg)){
            return;
        }
        //解析接收到的消息，获取文件ID
        Map map = JSON.parseObject(msg, Map.class);
        String mediaId = (String) map.get("mediaId");

        //通过id查询数据库，获取文件信息
        Optional<MediaFile> optionalMediaFile = mediaFileRepository.findById(mediaId);
        if(!optionalMediaFile.isPresent()){
            return;
        }
        MediaFile mediaFile = optionalMediaFile.get();
        //获取文件类型，如果不是avi则不处理
        String fileType = mediaFile.getFileType();
        if(!"avi".equalsIgnoreCase(fileType)){
            //将文件处理状态改成无需处理：303004
            mediaFile.setProcessStatus("303004");
            mediaFileRepository.save(mediaFile);
        }
        //文件的绝对路径
        String filePath = videoLocation + mediaFile.getFilePath() + mediaFile.getFileName();
        //mp4文件名称
        String mp4Name = mediaFile.getFileId() + ".mp4";
        //mp4文件路径
        String mp4folderPath = videoLocation + mediaFile.getFilePath();
        //将avi文件转换成mp4
        //String ffmpeg_path, String video_path, String mp4_name, String mp4folder_path
        Mp4VideoUtil mp4VideoUtil = new Mp4VideoUtil(ffmpegPath,filePath,mp4Name,mp4folderPath);
        String result = mp4VideoUtil.generateMp4();
        if(!"success".equals(result)){
            //转换失败，写入处理日志
            mediaFile.setProcessStatus("303003");   //处理状态改为处理失败
            MediaFileProcess_m3u8 mediaFileProcess_m3u8 = new MediaFileProcess_m3u8();
            mediaFileProcess_m3u8.setErrormsg(result);
            mediaFile.setMediaFileProcess_m3u8(mediaFileProcess_m3u8);
            mediaFileRepository.save(mediaFile);
            return;
        }

        //转换mp4，生成m3u8文件
        //String ffmpeg_path, String video_path, String m3u8_name,String m3u8folder_path
        //生成的mp4文件的绝对路径
        String mp4FilePath = mp4folderPath + mp4Name;
        //m3u8文件名以及保存m3u8文件的目录
        String m3u8Name = mediaFile.getFileId() + ".m3u8";
        String m3u8FolderPath = mp4folderPath + "hls/";
        HlsVideoUtil sVideoUtil = new HlsVideoUtil(ffmpegPath,mp4FilePath,m3u8Name,m3u8FolderPath);
        String m3u8Result = sVideoUtil.generateM3u8();
        if(!"success".equals(m3u8Result)){
            //转换失败，写入处理日志
            MediaFileProcess_m3u8 mediaFileProcess_m3u8 = new MediaFileProcess_m3u8();
            mediaFileProcess_m3u8.setErrormsg(m3u8Result);
            mediaFile.setMediaFileProcess_m3u8(mediaFileProcess_m3u8);
            mediaFile.setProcessStatus("303003");   //处理状态改为处理失败
            mediaFileRepository.save(mediaFile);
            return;
        }
        //处理成功  获取m3u8  ts文件列表
        List<String> ts_list = sVideoUtil.get_ts_list();
        MediaFileProcess_m3u8 mediaFileProcess_m3u8 = new MediaFileProcess_m3u8();
        mediaFileProcess_m3u8.setTslist(ts_list);
        mediaFile.setProcessStatus("303002");   //处理成功状态码
        mediaFile.setMediaFileProcess_m3u8(mediaFileProcess_m3u8);
        mediaFile.setFileUrl(mediaFile.getFilePath() + "hls/" + m3u8Name);

        mediaFileRepository.save(mediaFile);

    }
}
