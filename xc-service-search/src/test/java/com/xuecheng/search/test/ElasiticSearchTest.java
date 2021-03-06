package com.xuecheng.search.test;

import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: Shindou
 * @Date: 2019/4/12
 * @Description: com.xuecheng.search.test
 * @Version: 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ElasiticSearchTest {

    @Autowired
    RestClient restClient;

    @Autowired
    RestHighLevelClient restHighLevelClient;


    //创建索引
    @Test
    public void createIndex() throws Exception{
        //创建  创建索引请求对象，并设置索引名称
        CreateIndexRequest createIndexRequest = new CreateIndexRequest("xc_course");
        //设置索引参数
        createIndexRequest.settings(
                Settings.builder().put("number_of_shards",1).put("number_of_replicas",0));

        //创建映射
        createIndexRequest.mapping("doc","{\n" +
                "\"properties\": {\n" +
                "\"name\": {\n" +
                "\"type\": \"text\",\n" +
                "\"analyzer\":\"ik_max_word\",\n" +
                "\"search_analyzer\":\"ik_smart\"\n" +
                "},\n" +
                "\"description\": {\n" +
                "\"type\": \"text\",\n" +
                "\"analyzer\":\"ik_max_word\",\n" +
                "\"search_analyzer\":\"ik_smart\"\n" +
                "},\n" +
                "\"studymodel\": {\n" +
                "\"type\": \"keyword\"\n" +
                "},\n" +
                "\"price\": {\n" +
                "\"type\": \"float\"\n" +
                "},\n" +
                "\"timestamp\": {\n" +
                "\"type\": \"date\",\n" +
                "\"format\": \"yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis\"\n" +
                "}\n" +
                "}\n" +
                "}", XContentType.JSON);

        //创建索引操作客户端
        IndicesClient indicesClient = restHighLevelClient.indices();

        //创建响应对象
        CreateIndexResponse createIndexResponse = indicesClient.create(createIndexRequest);

        //得到响应结果
        boolean acknowledged = createIndexResponse.isAcknowledged();
        System.out.println(acknowledged);
    }


    //删除索引
    @Test
    public void deleteIndex() throws Exception{
        //创建   删除索引请求对象
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("xc_course");

        //创建索引操作客户端
        IndicesClient indices = restHighLevelClient.indices();

        //删除索引响应对象
        DeleteIndexResponse delete = indices.delete(deleteIndexRequest);

        //得到响应结果
        boolean acknowledged = delete.isAcknowledged();

        System.out.println(acknowledged);

    }


    //添加文档
    @Test
    public void addDocument() throws Exception{
        //创建一个Map集合
        Map<String, Object> map = new HashMap<>();
        //添加内容
        map.put("name", "springCloud实战");
        map.put("description", "这里是描述信息，我就不详细讲了，主要用来测试用的，随便写写够个几十字就行了");
        map.put("price", 5.6f);
        map.put("studymodel","201001");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        map.put("timestamp",simpleDateFormat.format(new Date()));

        //创建   索引请求对象
        IndexRequest indexRequest = new IndexRequest("xc_course","doc");

        //指定文档内容
        indexRequest.source(map);

        //索引响应对象
        IndexResponse indexResponse = restHighLevelClient.index(indexRequest);

        //获取响应结果
        DocWriteResponse.Result result = indexResponse.getResult();
        System.out.println(result);
    }


    /**
     * 查询文档
     */
    @Test
    public void queryDocument() throws Exception{
        //创建 查询请求对象（通过文档ID来查询,并非搜索）
        GetRequest getRequest = new GetRequest("xc_course","doc","23333");

        //响应对象
        GetResponse getResponse = restHighLevelClient.get(getRequest);
        if(getResponse.isExists()){
            Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();
            System.out.println(sourceAsMap);
        }
    }

    /**
     * 更新文档
     */
    @Test
    public void updateDocument()throws Exception{
        // 更新请求对象
        UpdateRequest updateRequest = new UpdateRequest("xc_course","doc","23333");

        //设置更新的内容
        Map<String,Object> map = new HashMap<>();
        map.put("price",55.2);
        updateRequest.doc(map);
        //更新并获取响应对象
        UpdateResponse updateResponse = restHighLevelClient.update(updateRequest);
        DocWriteResponse.Result result = updateResponse.getResult();
        RestStatus status = updateResponse.status();
        System.out.println(status);
        System.out.println(result);
    }


    //删除文档（根据文档ID删除）
    @Test
    public void deleteDocument()throws Exception{
        //删除文档请求对象
        DeleteRequest deleteRequest = new DeleteRequest("xc_course","doc","7iwmA6cASQuc5z4-IUCIUQ");

        DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest);

        DocWriteResponse.Result result = deleteResponse.getResult();
        System.out.println(result);
    }
}
