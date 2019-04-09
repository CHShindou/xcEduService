package com.xuecheng.filesystem.test;

import org.csource.fastdfs.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Auther: Shindou
 * @Date: 2019/4/9
 * @Description: com.xuecheng.filesystem.test
 * @Version: 1.0
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestFileUpload {


    @Value("${xuecheng.fastdfs.tracker_servers}")
    String tracker_servers;
    @Value("${xuecheng.fastdfs.charset}")
    String charset;
    @Value("${xuecheng.fastdfs.connect_timeout_in_seconds}")
    int connect_timeout;
    @Value("${xuecheng.fastdfs.network_timeout_in_seconds}")
    int network_timeout;

    @Test
    public void testUpload(){

        try {
//            ClientGlobal.initByTrackers(tracker_servers);
//            ClientGlobal.setG_charset(charset);
//            ClientGlobal.setG_connect_timeout(connect_timeout);
//            ClientGlobal.setG_network_timeout(network_timeout);
            ClientGlobal.initByProperties("config/fastdfs-client.properties");

            //创建tracker客户端
            TrackerClient trackerClient = new TrackerClient();
            //连接Tracker服务器
            TrackerServer trackerServer = trackerClient.getConnection();
            //连接Storage服务端
            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
            //连接Storage客户端
            StorageClient1 storageClient1 = new StorageClient1(trackerServer, storageServer);

            //上传文件
            String filename = "E:\\My Pictures\\01.jpg";
            String fileId = storageClient1.upload_file1(filename,"jpg",null);
            System.out.println(fileId);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
