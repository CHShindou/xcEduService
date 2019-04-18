package com.xuecheng.manage_media.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
        File sourceFile = new File("E:\\XmyTemp\\develop\\lucene.mp3");

        //分片后文件保存的路径
        String chunkPath = "E:\\XmyTemp\\develop\\chunk\\";
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


    //将分片文件合并
    @Test
    public void mergeChunk() throws Exception {
        //分片文件目录
        String chunkPath = "E:\\XmyTemp\\develop\\chunk\\";
        File chunkFolder = new File(chunkPath);
        //获取分片目录的所有文件
        File[] listChunkFiles = chunkFolder.listFiles();
        List<File> lists = Arrays.asList(listChunkFiles);

        //对列表按名称进行排序
        Collections.sort(lists,new Comparator<File>(){

            @Override
            public int compare(File o1, File o2) {
                if(Integer.parseInt(o1.getName()) > Integer.parseInt(o2.getName())){
                    return 1;
                }
                return -1;
            }
        });

        //创建合并后的文件
        File sourceFile = new File("E:\\XmyTemp\\develop\\xxXbb.mp3");
        if(!sourceFile.exists()){
            sourceFile.createNewFile();
        }
        //写对象
        RandomAccessFile ranWrite = new RandomAccessFile(sourceFile,"rw");

        //创建缓存区
        byte[] bytes = new byte[1024];
        //开始合并
        for(File chunkFile:lists){
            //创建一个输入流
            RandomAccessFile ranRead = new RandomAccessFile(chunkFile,"r");
            int len = 0;
            while ((len = ranRead.read(bytes))!= -1){
                ranWrite.write(bytes,0,len);
            }
            ranRead.close();
        }
        ranWrite.close();
    }
}
