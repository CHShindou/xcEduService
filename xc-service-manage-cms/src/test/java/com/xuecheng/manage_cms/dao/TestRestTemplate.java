package com.xuecheng.manage_cms.dao;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

/**
 * @Auther: Shindou
 * @Date: 2019/3/29
 * @Description: com.xuecheng.manage_cms.dao
 * @Version: 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestRestTemplate {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    GridFsTemplate gridFsTemplate;

    @Autowired
    GridFSBucket gridFSBucket;

    @Test
    public void testRest(){
        String url = "http://localhost:31001/cms/config/getmodel/5a791725dd573c3574ee333f";
        ResponseEntity<Map> forEntity = restTemplate.getForEntity(url, Map.class);
        System.out.println(forEntity);
    }

    //测试使用GridFsTemplate 网mongoDB中存储文件
    @Test
    public void testGridFs() throws FileNotFoundException {
        //要存储的文件
        File file = new File("E:/XmyTemp/index_banner.html");
        //获取输入流写入
        FileInputStream fileInputStream = new FileInputStream(file);
        //向GridFs中存入
        ObjectId objectId = gridFsTemplate.store(fileInputStream, "轮播图测试by23");
        //获取Id
        System.out.println(objectId);
    }

    //测试使用GridFSBucket读取mongodb中存储的文件
    @Test
    public void testGridFSBucket() throws IOException {
        //mongodb中存取的文件ID
        String fileId = "5c9eeaf4377c7a0cf8b81815";
        //根据ID查询文件
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(fileId)));
        //打开下载流对象
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        //创建gridFsResource获取流对象
        GridFsResource gridFsResource = new GridFsResource(gridFSFile,gridFSDownloadStream);
        //获取流中的数据并打印
        String s = IOUtils.toString(gridFsResource.getInputStream(),"UTF-8");
        System.out.println(s);
    }
}
