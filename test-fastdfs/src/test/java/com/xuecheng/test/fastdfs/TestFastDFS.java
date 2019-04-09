package com.xuecheng.test.fastdfs;

import org.csource.fastdfs.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Administrator
 * @version 1.0
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestFastDFS {


    //测试上传文件
    @Test
    public void testUpload(){

        try {
            //加载配置文件
            ClientGlobal.initByProperties("config/fastdfs-client.properties");
            //创建tracker客户端
            TrackerClient trackerClient = new TrackerClient();
            //连接tracker服务端
            TrackerServer trackerServer = trackerClient.getConnection();
            //获取storage服务器
            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
            //storage客户端
            StorageClient1 storageClient1 = new StorageClient1(trackerServer, storageServer);

            //上传文件
            String filename = "E:\\My Pictures\\1.jpg";
            String fileId = storageClient1.upload_file1(filename,"jpg",null);
            System.out.println(fileId);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //测试下载文件
    @Test
    public void testDownLoad(){
        FileOutputStream fileOutputStream = null;
        try {
            //加载配置文件
            ClientGlobal.initByProperties("config/fastdfs-client.properties");
            //创建tracker客户端
            TrackerClient trackerClient = new TrackerClient();
            //连接tracker服务端
            TrackerServer trackerServer = trackerClient.getConnection();
            //获取storage服务器
            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
            //storage客户端
            StorageClient1 storageClient1 = new StorageClient1(trackerServer, storageServer);

            byte[] bytes = storageClient1.download_file1("group1/M00/00/00/wKgpllyq8CCAakqCAAUMFPOWyfI337.jpg");
            fileOutputStream = new FileOutputStream(new File("F:\\a.jpg"));
            fileOutputStream.write(bytes);
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }




}
