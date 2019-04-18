package com.xuecheng.manage_media.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.RandomAccessFile;

/**
 * @Auther: Shindou
 * @Date: 2019/4/18
 * @Description: com.xuecheng.manage_media.test
 * @Version: 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestChunk {

    /**
     * 测试将文件分片
     */
    @Test
    public void testChunk() throws Exception {

        //获取要被分片的文件对象
        File sourceFile = new File("F:\\Xtemp\\WordPress.avi");

        //分片后文件保存的路径
        String chunkPath = "F:\\Xtemp\\chunk\\";
        File chunkFolder = new File(chunkPath);
        //如果分片目录不存在就创建该目录
        if(!chunkFolder.exists()){
            chunkFolder.mkdirs();
        }

        //设置每个分片的大小为1M
        long chunkSize = 1*1024*1024;

        //分块的数量为：  Math.ceil————向上取整
        long chunkNum = (long) Math.ceil(sourceFile.length()*1.0/chunkSize);
        //感觉这里应该用不到
        if(chunkNum<=0){
            chunkNum = 1;
        }

        //设置缓存区大小
        byte[] bytes = new byte[1024];
        //使用randomAccessFile访问文件
        RandomAccessFile ranRead = new RandomAccessFile(sourceFile,"r");
        //开始分片操作
        for (int i=0;i<chunkNum;i++){
            //创建分块文件
            File chunkFile = new File(chunkPath + i);
            boolean newFile = chunkFile.createNewFile();
            if(newFile){
                //向分块文件中写数据
                RandomAccessFile ranWrite = new RandomAccessFile(chunkFile,"rw");
                int len = -1;
                while((len = ranRead.read(bytes)) != -1){
                    ranWrite.write(bytes,0,len);
                    if(chunkFile.length()>chunkSize){
                        break;
                    }
                }
                ranWrite.close();
            }
        }

        ranRead.close();
    }
}
