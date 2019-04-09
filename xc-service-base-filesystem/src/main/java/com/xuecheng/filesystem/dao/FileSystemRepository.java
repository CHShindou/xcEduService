package com.xuecheng.filesystem.dao;

import com.xuecheng.framework.domain.filesystem.FileSystem;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Auther: Shindou
 * @Date: 2019/4/9
 * @Description: com.xuecheng.filesystem.dao
 * @Version: 1.0
 */
public interface FileSystemRepository extends MongoRepository<FileSystem,String> {
}
