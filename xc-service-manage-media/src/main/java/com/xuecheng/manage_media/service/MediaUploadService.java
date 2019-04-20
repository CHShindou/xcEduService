package com.xuecheng.manage_media.service;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.domain.media.MediaFile;
import com.xuecheng.framework.domain.media.response.CheckChunkResult;
import com.xuecheng.framework.domain.media.response.MediaCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_media.config.RabbitMQConfig;
import com.xuecheng.manage_media.dao.MediaFileRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

/**
 * @Auther : shindou
 * @Date : 2019/4/18
 * @Description : 功能描述
 * @Version : 1.0
 */

@Service
public class MediaUploadService {

    @Autowired
    MediaFileRepository mediaFileRepository;

    @Autowired
    RabbitTemplate rabbitTemplate;

    //上传文件根路径
    @Value("${xc-service-manage-media.upload-location}")
    String parentPath;

    //获取rabbitmq配置路由
    @Value("${xc-service-manage-media.mq.routingkey-media-video}")
    String routingKey;


    /**根据Md5获取文件目录
     * 规则：
     * 一级目录：Md5的第一个字符
     * 二级目录：Md5的第二个字符
     * 三级目录：Md5
     * 文件名：Md5+扩展名
     * @param fileMd5
     * @param fileExt
     * @return
     */
    private String getFilePath(String fileMd5,String fileExt){
        return parentPath+"/"+fileMd5.substring(0,1)+"/"+fileMd5.substring(1,2)+"/"+fileMd5+"/"+fileMd5+"."+fileExt;
    }

    //获取文件目录的方法，不包含文件名
    private String getFileFolderPath(String fileMd5){
        return parentPath+"/"+fileMd5.substring(0,1)+"/"+fileMd5.substring(1,2)+"/"+fileMd5+"/";
    }

    //获取文件的相对路径，不包含根路径
    private String getFileRelativePath(String fileMd5){
        return fileMd5.substring(0,1)+"/"+fileMd5.substring(1,2)+"/"+fileMd5+"/";
    }


    //上传文件前注册文件
    public ResponseResult register(String fileMd5, String fileName, Long fileSize, String mimeType, String fileExt){
        //获取文件根目录
        String filePath = this.getFilePath(fileMd5,fileExt);

        //检查文件在磁盘上是否存在
        File file = new File(filePath);
        boolean exists = file.exists();

        //检查数据库中是否存在文件信息
        Optional<MediaFile> mediaFileOptional = mediaFileRepository.findById(fileMd5);

        //当磁盘和数据库都存在该文件时，则表示该文件已经存在
        if (exists && mediaFileOptional.isPresent()){
            ExceptionCast.cast(MediaCode.UPLOAD_FILE_REGISTER_EXIST);
        }

        //否则文件不存在，创建该文件目录
        String fileFolderPath = this.getFileFolderPath(fileMd5);
        File folderFile = new File(fileFolderPath);
        if(!folderFile.exists()){
            folderFile.mkdirs();
        }

        return new ResponseResult(CommonCode.SUCCESS);
    }


    //检查文件分片
    public CheckChunkResult checkChunk(String fileMd5, Integer chunk, Integer chunkSize){
        //获取文件分块目录
        String chunkFilePath = this.getFileFolderPath(fileMd5)+"chunk/";

        //分块文件以chunk命名，没有扩展名
        File chunkFile = new File(chunkFilePath + chunk);

        if(chunkFile.exists()){
            //该分片已存在
            return new CheckChunkResult(MediaCode.CHUNK_FILE_EXIST_CHECK,true);
        }else{
            return new CheckChunkResult(MediaCode.CHUNK_FILE_EXIST_CHECK,false);
        }
    }

    //上传分片
    public ResponseResult uploadChunk(MultipartFile multipartFile, Integer chunk, String fileMd5){
        if(multipartFile == null){
            ExceptionCast.cast(MediaCode.UPLOAD_FILE_REGISTER_ISNULL);
        }
        //获取分片存放的目录，如果不存在则创建
        File chunkFolder = new File(this.getFileFolderPath(fileMd5)+"chunk/");
        if(!chunkFolder.exists()){
            chunkFolder.mkdirs();
        }
        //创建块文件
        File chunkFile = new File(this.getFileFolderPath(fileMd5)+"chunk/"+chunk);

        //讲文件写入块文件中
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            inputStream = multipartFile.getInputStream();
            fileOutputStream = new FileOutputStream(chunkFile);
            IOUtils.copy(inputStream,fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
            ExceptionCast.cast(MediaCode.CHUNK_FILE_UPLOAD_FAIL);
        }finally{
            try {
                inputStream.close();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ResponseResult(CommonCode.SUCCESS);
    }

    //文件合并
    public ResponseResult mergeChunk(String fileMd5, String fileName, Long fileSize, String mimeType, String fileExt){
        //获取合并后的文件
        String filePath = this.getFilePath(fileMd5,fileExt);
        //如果合并之后的文件已存在，则先删除该文件
        File file = new File(filePath);
        if(file.exists()){
            file.delete();
        }
        //创建新的合并文件
        boolean newFile = false;
        try {
            newFile = file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //获取分块文件目录
        File chunkFolderFile = new File(this.getFileFolderPath(fileMd5)+"chunk/");

        //获取所有块文件的集合，并排序
        List<File> chunkFileLists = this.getFileList(chunkFolderFile);

        //合并文件
        try {
            file = this.mergeFile(chunkFileLists,file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(file == null){
            //合并失败
            ExceptionCast.cast(MediaCode.MERGE_FILE_FAIL);
        }

        //校验合并之后的文件
        boolean checkFile = this.checkFileByMd5(file,fileMd5);

        if(!checkFile){
            //校验不通过，文件合并失败
            ExceptionCast.cast(MediaCode.MERGE_FILE_CHECKFAIL);
        }

        //校验成功，将文件信息添加到数据库中
        MediaFile mediaFile = this.saveMediaFile(fileMd5, fileName, fileSize, mimeType, fileExt);

        //发送消息给rabbitmq
        Map<String,String> msgMap = new HashMap<>();
        msgMap.put("mediaId",mediaFile.getFileId());
        String msg = JSON.toJSONString(msgMap);
        rabbitTemplate.convertAndSend(RabbitMQConfig.EX_MEDIA_PROCESSTASK, routingKey, msg);

        return new ResponseResult(CommonCode.SUCCESS);

    }

    //获取所有块文件的集合，并排好序
    private List<File> getFileList(File file){
        File[] files = file.listFiles();
        List<File> lists = Arrays.asList(files);
        //排序
        Collections.sort(lists, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if(Integer.parseInt(o1.getName()) > Integer.parseInt(o2.getName())){
                    return 1;
                }else{
                    return -1;
                }
            }
        });

        return lists;
    }

    //将块文件合并
    private File mergeFile(List<File> files,File mergeFile) throws IOException {
        RandomAccessFile ranWrite = new RandomAccessFile(mergeFile,"rw");

        byte[] bytes = new byte[1024];
        for (File file:files){
            RandomAccessFile ranRead = new RandomAccessFile(file,"r");
            int len = 0;
            while((len = ranRead.read(bytes)) != -1){
                ranWrite.write(bytes,0,len);
            }
            ranRead.close();
        }
        ranWrite.close();

        return mergeFile;
    }

    //根据文件MD5值校验文件
    private boolean checkFileByMd5(File file,String fileMd5){
        if(file == null || StringUtils.isEmpty(fileMd5)){
            return false;
        }

        FileInputStream fileInputStream = null;

        try {
            fileInputStream = new FileInputStream(file);
            //使用MD5工具获取文件的md5值
            String md5Hex = DigestUtils.md5Hex(fileInputStream);

            if(fileMd5.equalsIgnoreCase(md5Hex)){
                //md5值相同
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;
    }


    //添加文件信息到数据库中
    private MediaFile saveMediaFile(String fileMd5, String fileName, Long fileSize, String mimeType, String fileExt){
        MediaFile mediaFile = new MediaFile();
        mediaFile.setFileId(fileMd5);
        mediaFile.setFileName(fileMd5 + "." +fileExt);
        mediaFile.setFileOriginalName(fileName);

        //文件的相对路径
        mediaFile.setFilePath(this.getFileRelativePath(fileMd5));

        mediaFile.setFileSize(fileSize);
        mediaFile.setMimeType(mimeType);
        mediaFile.setUploadTime(new Date());
        mediaFile.setFileType(fileExt);
        //文件状态
        mediaFile.setFileStatus("301002");
        //文件处理状态设置成未处理
        mediaFile.setProcessStatus("303001");

        mediaFile = mediaFileRepository.save(mediaFile);

        return mediaFile;
    }

}
