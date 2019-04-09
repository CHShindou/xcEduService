package com.xuecheng.filesystem.service;

import com.alibaba.fastjson.JSON;
import com.xuecheng.filesystem.dao.FileSystemRepository;
import com.xuecheng.framework.domain.filesystem.FileSystem;
import com.xuecheng.framework.domain.filesystem.response.FileSystemCode;
import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import org.csource.fastdfs.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @Auther: Shindou
 * @Date: 2019/4/9
 * @Description: com.xuecheng.filesystem.service
 * @Version: 1.0
 */

@Service
public class FileSystemService {

    @Value("${xuecheng.fastdfs.tracker_servers}")
    String tracker_servers;
    @Value("${xuecheng.fastdfs.charset}")
    String charset;
    @Value("${xuecheng.fastdfs.connect_timeout_in_seconds}")
    int connect_timeout;
    @Value("${xuecheng.fastdfs.network_timeout_in_seconds}")
    int network_timeout;

    @Autowired
    FileSystemRepository fileSystemRepository;


    //上传文件，并保存信息到数据库
    public UploadFileResult uploadAndSaveFile(
            MultipartFile multipartFile,String filetag,String businesskey,String metadata){

        if(multipartFile == null){
            ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_FILEISNULL);
        }
        String fileId = this.uploadFile(multipartFile);
        if(fileId == null){
            ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_SERVERFAIL);
        }

        //创建文件对象
        FileSystem fileSystem = new FileSystem();
        fileSystem.setFileId(fileId);
        fileSystem.setFilePath(fileId);
        fileSystem.setFileName(multipartFile.getOriginalFilename());
        fileSystem.setBusinesskey(businesskey);
        fileSystem.setFiletag(filetag);
        fileSystem.setFileSize(multipartFile.getSize());
        fileSystem.setFileType(multipartFile.getContentType());
        if(metadata != null){
            Map map = JSON.parseObject(metadata,Map.class);
            fileSystem.setMetadata(map);
        }

        //保存到数据库
        fileSystem = fileSystemRepository.save(fileSystem);
        return new UploadFileResult(CommonCode.SUCCESS,fileSystem);

    }

    //将文件上传到文件服务器
    private String uploadFile(MultipartFile multipartFile){

        try {
            //加载文件服务器配置信息
            //使用下面这种方式加载，可以写入文件，但是一直无法收到tracker服务器返回的信息，read timed out 异常。
//            ClientGlobal.initByTrackers(tracker_servers);
//            ClientGlobal.setG_charset(charset);
//            ClientGlobal.setG_connect_timeout(connect_timeout);
//            ClientGlobal.setG_network_timeout(network_timeout);

            ClientGlobal.initByProperties("config/fastdfs-client.properties");

        } catch (Exception e) {
            e.printStackTrace();
            ExceptionCast.cast(FileSystemCode.FS_INITFDFSERROR);
        }

        try {
            //创建tracker客户端
            TrackerClient trackerClient = new TrackerClient();
            //连接Tracker服务器
            TrackerServer trackerServer = trackerClient.getConnection();
            //连接Storage服务端
            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
            //连接Storage客户端
            StorageClient1 storageClient1 = new StorageClient1(trackerServer, storageServer);

            //上传文件
            byte[] bytes = multipartFile.getBytes();
            String fileName = multipartFile.getOriginalFilename();
            String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);

            String fileId = storageClient1.upload_file1(bytes, fileType, null);

            return fileId;

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
