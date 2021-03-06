package com.xuecheng.search.service;

import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.course.TeachplanMediaPub;
import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther : shindou
 * @Date : 2019/4/14
 * @Description : 功能描述
 * @Version : 1.0
 */
@Service
public class EsCourseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EsCourseService.class);

    //加载xc_course库常量
    @Value("${xuecheng.elasticsearch.course.index}")
    String index;

    @Value("${xuecheng.elasticsearch.course.type}")
    String type;

    @Value("${xuecheng.elasticsearch.course.source_field}")
    String source_field;

    //加载xc_course_media库常量
    @Value("${xuecheng.elasticsearch.media.index}")
    String media_index;

    @Value("${xuecheng.elasticsearch.media.type}")
    String media_type;

    @Value("${xuecheng.elasticsearch.media.source_field}")
    String media_source_field;

    @Autowired
    RestHighLevelClient restHighLevelClient;

    @Autowired
    RestClient restClient;

    //课程搜索(关键字搜索)
    public QueryResponseResult<CoursePub> searchCourse(int page, int size, CourseSearchParam courseSearchParam){

        //判断分页参数并初始化
        if(page <= 0){
            page = 1;
        }
        if(size <= 0){
            size = 5;
        }
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.types(type);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.fetchSource(source_field.split(","),new String[]{});

        //创建bool搜索对象
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //获取关键字
        if(StringUtils.isNotBlank(courseSearchParam.getKeyword())){
            MultiMatchQueryBuilder multiMatchQueryBuilder =
                    QueryBuilders.multiMatchQuery(courseSearchParam.getKeyword(),"name","description","teachplan")
                            .minimumShouldMatch("60%");
            multiMatchQueryBuilder.field("name",5);

            boolQueryBuilder.must(multiMatchQueryBuilder);
        }

        //获取一级分类
        if(StringUtils.isNotBlank(courseSearchParam.getMt())){
            boolQueryBuilder.filter(QueryBuilders.termQuery("mt",courseSearchParam.getMt()));

            //获取二级分类(有一级分类才会有有二级分类，否则不加载该参数)
            if(StringUtils.isNotBlank(courseSearchParam.getSt())){
                boolQueryBuilder.filter(QueryBuilders.termQuery("st",courseSearchParam.getSt()));
            }
        }

        //获取难度等级
        if(StringUtils.isNotBlank(courseSearchParam.getGrade())){
            boolQueryBuilder.filter(QueryBuilders.termQuery("grade",courseSearchParam.getGrade()));
        }

        //获取价格区间
        Float price_min = courseSearchParam.getPrice_min();
        Float price_max = courseSearchParam.getPrice_max();
        if((price_min != null && price_max != null) && price_max >= price_min){
            boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").gte(price_min).lte(price_max));
        }


        //获取排序字段
        if(StringUtils.isNotBlank(courseSearchParam.getSort())){
            searchSourceBuilder.sort(new FieldSortBuilder(courseSearchParam.getSort()).order(SortOrder.DESC));
        }

        //过滤字段
        /*
        if(StringUtils.isNotBlank(courseSearchParam.getFilter())){
            boolQueryBuilder.mustNot(QueryBuilders.multiMatchQuery(
                    courseSearchParam.getFilter(),"name","description","teachplan").operator(Operator.AND));
        }
        */

        //分页
        searchSourceBuilder.from((page-1)*size);
        searchSourceBuilder.size(size);

        //高亮显示
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<font class='eslight'>");
        highlightBuilder.postTags("</font>");
        //设置高亮字段
        highlightBuilder.fields().add(new HighlightBuilder.Field("name"));
        searchSourceBuilder.highlighter(highlightBuilder);

        searchSourceBuilder.query(boolQueryBuilder);

        searchRequest.source(searchSourceBuilder);

        //执行搜索，并获取响应对象
        SearchResponse searchResponse = null;
        try {
            searchResponse = restHighLevelClient.search(searchRequest);
        } catch (IOException e) {
            //打印日志
            LOGGER.error("xuecheng search error..{}",e.getMessage());
            e.printStackTrace();
            return new QueryResponseResult<>(CommonCode.FAIL,new QueryResult());
        }
        SearchHits responseHits = searchResponse.getHits();
        long totalHits = responseHits.getTotalHits();
        List<CoursePub> lists = new ArrayList<>();
        SearchHit[] hits = responseHits.getHits();
        for(SearchHit hit:hits){
            CoursePub coursePub = new CoursePub();
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            coursePub.setPubTime((String) sourceAsMap.get("pub_time"));
            coursePub.setId((String) sourceAsMap.get("id"));
            coursePub.setPic((String) sourceAsMap.get("pic"));
            coursePub.setPrice((Double) sourceAsMap.get("price"));
            coursePub.setPrice_old((Double) sourceAsMap.get("price_old"));
            coursePub.setName((String) sourceAsMap.get("name"));

            //获取高亮字段
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            HighlightField highlightName = highlightFields.get("name");
            if(highlightName != null){
                Text[] fragments = highlightName.getFragments();
                StringBuffer sb = new StringBuffer();
                for(Text fragment:fragments){
                    sb.append(fragment.toString());
                }

                coursePub.setName(sb.toString());
            }
            lists.add(coursePub);
        }
        QueryResult queryResult = new QueryResult();
        queryResult.setTotal(totalHits);
        queryResult.setList(lists);
        return new QueryResponseResult<>(CommonCode.SUCCESS,queryResult);
    }


    //根据ID精确搜索xc_course库
    public Map<String,CoursePub> termById(String id){
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.types(type);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.termQuery("id",id));
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = null;
        try {
            searchResponse = restHighLevelClient.search(searchRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }

        SearchHits responseHits = searchResponse.getHits();
        SearchHit[] hits = responseHits.getHits();
        Map<String,CoursePub> map = new HashMap<>();
        for (SearchHit hit:hits){
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String courseId = (String) sourceAsMap.get("id");
            String name = (String) sourceAsMap.get("name");
            String pic = (String) sourceAsMap.get("pic");
            String teachplan = (String) sourceAsMap.get("teachplan");
            String description = (String) sourceAsMap.get("description");
            String grade = (String) sourceAsMap.get("grade");
            String charge = (String) sourceAsMap.get("charge");

            CoursePub coursePub = new CoursePub();
            coursePub.setId(courseId);
            coursePub.setName(name);
            coursePub.setPic(pic);
            coursePub.setTeachplan(teachplan);
            coursePub.setDescription(description);
            coursePub.setGrade(grade);
            coursePub.setCharge(charge);

            map.put(courseId,coursePub);

        }
        return map;
    }

    //通过一组id查询xc_course_media库
    public QueryResponseResult<TeachplanMediaPub> searchMediaByIds(String[] ids){

        SearchRequest searchRequest = new SearchRequest(media_index);
        searchRequest.types(media_type);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.fetchSource(media_source_field.split(","),new String[]{});
        searchSourceBuilder.query(QueryBuilders.termsQuery("teachplan_id",ids));

        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = null;
        try {
            searchResponse = restHighLevelClient.search(searchRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SearchHits responseHits = searchResponse.getHits();
        long total = responseHits.getTotalHits();
        SearchHit[] hits = responseHits.getHits();

        List<TeachplanMediaPub> lists = new ArrayList<>();

        for (SearchHit hit:hits){
            TeachplanMediaPub teachplanMediaPub = new TeachplanMediaPub();
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String courseId = (String) sourceAsMap.get("courseid");
            String mediaId = (String) sourceAsMap.get("media_id");
            String mediaUrl = (String) sourceAsMap.get("media_url");
            String teachplanId = (String) sourceAsMap.get("teachplan_id");
            String mediaFileoriginalname = (String) sourceAsMap.get("media_fileoriginalname");

            teachplanMediaPub.setCourseId(courseId);
            teachplanMediaPub.setMediaFileOriginalName(mediaFileoriginalname);
            teachplanMediaPub.setMediaId(mediaId);
            teachplanMediaPub.setMediaUrl(mediaUrl);
            teachplanMediaPub.setTeachplanId(teachplanId);
            lists.add(teachplanMediaPub);
        }

        QueryResult<TeachplanMediaPub> queryResult = new QueryResult<>();
        queryResult.setList(lists);
        queryResult.setTotal(total);
        return new QueryResponseResult<>(CommonCode.SERVER_ERROR,queryResult);

    }
}
