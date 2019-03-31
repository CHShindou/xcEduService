package com.xuecheng.manage_cms_client.config;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Auther : shindou
 * @Date : 2019/3/29
 * @Description : 功能描述
 * @Version : 1.0
 */
@Configuration
public class MongoConfig {

    @Value("${spring.data.mongodb.database}")
    String db;

    @Bean
    public GridFSBucket getGridFsBucket(MongoClient mongoClient){
        MongoDatabase mongoDatabase = mongoClient.getDatabase(db);
        GridFSBucket gridFSBucket = GridFSBuckets.create(mongoDatabase);
        return gridFSBucket;
    }
}
