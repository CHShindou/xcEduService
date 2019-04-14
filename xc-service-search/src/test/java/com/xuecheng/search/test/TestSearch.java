package com.xuecheng.search.test;


import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Auther : shindou
 * @Date : 2019/4/13
 * @Description : 功能描述   ElasticSearch的各种搜索方式
 * @Version : 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestSearch {

    @Autowired
    RestHighLevelClient restHighLevelClient;

    @Autowired
    RestClient restClient;

    //搜索type下全部文档
    @Test
    public void searchAll() throws Exception{
        SearchRequest searchRequest = new SearchRequest("xc_course");
        searchRequest.types("doc");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchSourceBuilder.fetchSource(new String[]{"name","description"},new String[]{});

        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest);

        SearchHits hits = searchResponse.getHits();
        SearchHit[] hitsHits = hits.getHits();
        for(SearchHit hit:hitsHits){
            String index = hit.getIndex();
            String id = hit.getId();
            String type = hit.getType();
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String name = (String) sourceAsMap.get("name");
            String description = (String) sourceAsMap.get("description");
            System.out.println(name);
            System.out.println(description);
        }
    }


    //搜索结果分页显示
    @Test
    public void searchAllPage() throws IOException {
        int page = 1;   //查询哪一页
        int size = 1;   //每页显示的个数

        SearchRequest searchRequest = new SearchRequest("xc_course");
        searchRequest.types("doc");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //分页查询，这里的参数是起始的下标  起始下标=(page-1)*size；
        searchSourceBuilder.from((page-1)*size);
        searchSourceBuilder.size(size);

        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchSourceBuilder.fetchSource(new String[]{"name","description"},new String[]{});

        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest);

        SearchHits responseHits = searchResponse.getHits();
        long totalHits = responseHits.getTotalHits();
        SearchHit[] hits = responseHits.getHits();
        for(SearchHit hit:hits){
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String name = (String) sourceAsMap.get("name");
            String description = (String) sourceAsMap.get("description");
            System.out.println(name);
            System.out.println(description);
        }
    }

    //精确查询term
    @Test
    public void termSearch() throws Exception{
        //搜索请求对象
        SearchRequest searchRequest = new SearchRequest("xc_course");
        searchRequest.types("doc");

        //设置搜索条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.termQuery("name","spring"));
        searchSourceBuilder.fetchSource(new String[]{"name","description"},new String[]{});
        //将搜索条件添加在请求对象中
        searchRequest.source(searchSourceBuilder);

        //执行搜索，并获取响应对象
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest);

        //从响应对象中获取结果
        SearchHits responseHits = searchResponse.getHits();
        long totalHits = responseHits.getTotalHits();
        SearchHit[] hits = responseHits.getHits();
        for(SearchHit hit:hits){
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            System.out.println(sourceAsMap);
        }
    }

    //根据id精确查找
    @Test
    public void termsSearchByIds() throws Exception{
        //搜索请求对象
        SearchRequest searchRequest = new SearchRequest("xc_course");
        searchRequest.types("doc");

        //设置搜索条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        searchSourceBuilder.query(QueryBuilders.termsQuery("_id",list));
        searchSourceBuilder.fetchSource(new String[]{"name","description"},new String[]{});
        //将搜索条件添加在请求对象中
        searchRequest.source(searchSourceBuilder);

        //执行搜索，并获取响应对象
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest);

        //从响应对象中获取结果
        SearchHits responseHits = searchResponse.getHits();
        long totalHits = responseHits.getTotalHits();
        SearchHit[] hits = responseHits.getHits();
        for(SearchHit hit:hits){
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            System.out.println(sourceAsMap);
        }
    }

    //match query分词再检索
    @Test
    public void matchSearch() throws IOException {
        SearchRequest searchRequest = new SearchRequest("xc_course");
        searchRequest.types("doc");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.fetchSource(new String[]{"name","description","price"},new String[]{});

//        searchSourceBuilder.query(QueryBuilders.matchQuery("name","spring 开发框架").minimumShouldMatch("70%"));
        searchSourceBuilder.query(QueryBuilders.matchQuery("description","程序开发基础").operator(Operator.OR));
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
        SearchHits responseHits = searchResponse.getHits();
        long totalHits = responseHits.getTotalHits();
        System.out.println(totalHits);
        SearchHit[] hits = responseHits.getHits();
        for(SearchHit hit:hits){
            String id = hit.getId();
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            System.out.println(sourceAsMap.get("name"));
            System.out.println(sourceAsMap.get("price"));
        }
    }

    //multi query 多条件查询  可以查询多个字段
    @Test
    public void multiSearch() throws Exception{
        SearchRequest searchRequest = new SearchRequest("xc_course");
        searchRequest.types("doc");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery("Spring框架","name","description").minimumShouldMatch("50%");
        multiMatchQueryBuilder.field("name",10);

        searchSourceBuilder.query(multiMatchQueryBuilder);
        searchSourceBuilder.fetchSource(new String[]{"name","price","description"},new String[]{});

        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
        SearchHits responseHits = searchResponse.getHits();
        long totalHits = responseHits.getTotalHits();
        System.out.println(totalHits);
        SearchHit[] hits = responseHits.getHits();
        for(SearchHit hit:hits){
            String sourceAsString = hit.getSourceAsString();
            System.out.println(sourceAsString);
        }

    }


    //布尔查询
    @Test
    public void boolSearch() throws Exception{
        SearchRequest searchRequest = new SearchRequest("xc_course");
        searchRequest.types("doc");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.fetchSource(new String[]{"name","description","price","studymodel"},new String[]{});

        //multi query 条件
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(
                "spring开发程序","name","description").minimumShouldMatch("70%");
        //提高name字段的权重
        multiMatchQueryBuilder.field("name",5);

        //term query 条件
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("studymodel", "201002");

        //bool 条件合并
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(multiMatchQueryBuilder);
        boolQueryBuilder.must(termQueryBuilder);

        searchSourceBuilder.query(boolQueryBuilder);
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest);

        SearchHits responseHits = searchResponse.getHits();
        long totalHits = responseHits.getTotalHits();
        SearchHit[] hits = responseHits.getHits();
        for(SearchHit hit:hits){
            String sourceAsString = hit.getSourceAsString();
            System.out.println(sourceAsString);
        }

    }


    //查询结果过滤
    @Test
    public void filterQueryResult() throws Exception{
        SearchRequest searchRequest = new SearchRequest("xc_course");
        searchRequest.types("doc");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.fetchSource(new String[]{"name","description","price"},new String[]{});

        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(
                "java开发框架","name","description").minimumShouldMatch("60%");
        multiMatchQueryBuilder.field("name",10);

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(multiMatchQueryBuilder);

        searchSourceBuilder.query(boolQueryBuilder);
        searchRequest.source(searchSourceBuilder);

        //设置过滤
        boolQueryBuilder.filter(QueryBuilders.termQuery("studymodel","201001"));
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").gte(80).lte(100));

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
        long totalHits = searchResponse.getHits().getTotalHits();
        System.out.println(totalHits);

    }

    //排序
    @Test
    public void orderSearch() throws Exception{
        SearchRequest searchRequest = new SearchRequest("xc_course");
        searchRequest.types("doc");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.fetchSource(new String[]{"name","description","price"},new String[]{});
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());

        //设置排序
        //按照价格降序排序
        searchSourceBuilder.sort(new FieldSortBuilder("price").order(SortOrder.DESC));

        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
        SearchHit[] searchHits = searchResponse.getHits().getHits();
        for (SearchHit hit:searchHits){
            String sourceAsString = hit.getSourceAsString();
            System.out.println(sourceAsString);

        }
    }

    //搜索结果高亮
    @Test
    public void highLightSearch() throws Exception{
        SearchRequest searchRequest = new SearchRequest("xc_course");
        searchRequest.types("doc");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.fetchSource(new String[]{"name","description","price"},new String[]{});

        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(
                "java开发","name","description").operator(Operator.OR);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(multiMatchQueryBuilder);

        //设置过滤
        boolQueryBuilder.filter(QueryBuilders.termQuery("studymodel","201001"));
        //设置排序
        searchSourceBuilder.sort(new FieldSortBuilder("price").order(SortOrder.DESC));
        searchSourceBuilder.query(boolQueryBuilder);

        //设置高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        //高亮的前缀和后缀
        highlightBuilder.preTags("<tag>");
        highlightBuilder.postTags("</tag>");
        //设置高亮字段
        highlightBuilder.fields().add(new HighlightBuilder.Field("name"));
        highlightBuilder.fields().add(new HighlightBuilder.Field("description"));
        searchSourceBuilder.highlighter(highlightBuilder);

        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
        long totalHits = searchResponse.getHits().getTotalHits();
        System.out.println(totalHits);

        SearchHit[] hits = searchResponse.getHits().getHits();
        for (SearchHit hit:hits){
            //取出正常结果(没加高亮效果)
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String name = (String) sourceAsMap.get("name");
            String description = (String) sourceAsMap.get("description");
            System.out.println(name+"======"+description);
            //取出高亮效果的结果
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            HighlightField name1 = highlightFields.get("name");
            if(name1 != null){
                Text[] fragments = name1.getFragments();
                StringBuffer sb = new StringBuffer();
                for(Text text:fragments){
                    sb.append(text.toString());
                }

                System.out.println(sb.toString());
            }
        }
    }


}
