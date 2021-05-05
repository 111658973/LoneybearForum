package com.lbf.pack.serviceImpl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lbf.pack.Util.ElasticSearchUtil;
import com.lbf.pack.beans.*;
import com.lbf.pack.mapper.FloorMapper;
import com.lbf.pack.mapper.PostMapper;
import com.lbf.pack.mapper.UserDataMapper;
import com.lbf.pack.mapper.Zonemapper;
import com.lbf.pack.service.ElasticSearchService;
import com.lbf.pack.service.QueryDataService;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class ElasticSearchServiceImpl implements ElasticSearchService {
    private static final Logger logger = LoggerFactory.getLogger(ElasticSearchServiceImpl.class);
    @Autowired
    UserDataMapper userDataMapper;
    @Autowired
    FloorMapper floorMapper;
    @Autowired
    Zonemapper zonemapper;
    @Autowired
    PostMapper postMapper;
    @Autowired
    QueryDataService queryDataService;



    @Override
    public void init() throws IOException {
        ElasticSearchUtil elasticSearchUtil = new ElasticSearchUtil();
        elasticSearchUtil.ClearIndex("loneybearforum_zones");
        elasticSearchUtil.ClearIndex("loneybearforum_users");
        elasticSearchUtil.ClearIndex("loneybearforum_floors");


        List<ZoneBean> listZone = zonemapper.selectList(new QueryWrapper<ZoneBean>());
        List<ZoneBean> list_zones = new ArrayList<>();
        for (ZoneBean z : listZone) {
            list_zones.add(queryDataService.GetZonesDataByZid(z.getZid()));
        }
        List<Map<String, Object>> map_zone = queryDataService.ParseToMapList(list_zones);
        elasticSearchUtil.updateIndex("loneybearforum_zones", map_zone);



        List<FloorBean> list = floorMapper.selectList(new QueryWrapper<FloorBean>());
        List<FloorBean> list_floors = new ArrayList<>();
        for (FloorBean f : list) {
            list_floors.add(queryDataService.GetFloorInfoById(f.getFid()));
        }
        List<Map<String, Object>> map_floor = queryDataService.ParseToMapList(list_floors);
        elasticSearchUtil.updateIndex("loneybearforum_floors", map_floor);

        List<UserFullDataBean> listt = userDataMapper.selectList(new QueryWrapper<UserFullDataBean>());
        List<Map<String, Object>> map_user = queryDataService.ParseToMapList(listt);

        elasticSearchUtil.updateIndex("loneybearforum_users", map_user);
    }

    @Override
    public List<Map<String, Object>> SearchZones(String content) throws IOException {

            RestHighLevelClient client = new ElasticSearchUtil().getClient();

            SearchRequest searchRequest = new SearchRequest("loneybearforum_zones");
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();


//        MatchQueryBuilder QueryBuilder = QueryBuilders.matchQuery("content", content);

            BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
                    .should(QueryBuilders.matchQuery("zname", content).analyzer("ik_smart"))
                    .should(QueryBuilders.matchQuery("zintroduction", content));

            searchSourceBuilder.query(queryBuilder);
            searchSourceBuilder.size(40);

            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.requireFieldMatch(true).field("zname")
                    .preTags("<span style=\"background: yellow\">").postTags("</span>");
            highlightBuilder.requireFieldMatch(true).field("zintroduction")
                    .preTags("<span style=\"background: yellow\">").postTags("</span>");

            searchSourceBuilder.highlighter(highlightBuilder);
            searchRequest.source(searchSourceBuilder);


            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = searchResponse.getHits();
            SearchHit[] searchHits = hits.getHits();

            List<Map<String, Object>> maplist = new ArrayList<>();
            int i = 1;
            for (SearchHit hit : searchHits) {

                // do something with the SearchHit
                Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                if (highlightFields.get("zname") != null) {
                    HighlightField highlight = highlightFields.get("zname");
                    Text[] fragments = highlight.fragments();
                    String fragmentString = fragments[0].string();
                    sourceAsMap.put("hightlightzname", fragmentString);
                    sourceAsMap.put("index", i++);
                    sourceAsMap.put("score", hit.getScore());
                    if (highlightFields.get("zintroduction") == null) {

                        sourceAsMap.put("hightlightzintroduction", sourceAsMap.get("zintroduction"));

                    }
                    maplist.add(sourceAsMap);
                }
                if (highlightFields.get("zintroduction") != null) {
                    HighlightField highlight = highlightFields.get("zintroduction");
                    Text[] fragments = highlight.fragments();
                    String fragmentString = fragments[0].string();
                    sourceAsMap.put("hightlightzintroduction", fragmentString);
                    sourceAsMap.put("index", i++);
                    sourceAsMap.put("score", hit.getScore());
                    if (highlightFields.get("zname") == null) {

                        sourceAsMap.put("hightlightzname", sourceAsMap.get("zname"));

                    }
                    maplist.add(sourceAsMap);
                }
            }
        LinkedHashSet<Map<String,Object>> set2 = new LinkedHashSet<>(maplist);
        maplist.clear();
        maplist.addAll(set2);
        return maplist;


    }

    @Override
    public List<Map<String, Object>> SearchUsers(String content) throws IOException {
        RestHighLevelClient client = new ElasticSearchUtil().getClient();

        SearchRequest searchRequest = new SearchRequest("loneybearforum_users");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();


        MatchQueryBuilder queryBuilder = QueryBuilders.matchQuery("nick", content);
//
//        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
//                .should(QueryBuilders.matchQuery("nick", content));

        searchSourceBuilder.query(queryBuilder);
        searchSourceBuilder.size(40);

        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.requireFieldMatch(true).field("nick")
                .preTags("<span style=\"background: yellow\">").postTags("</span>");

        searchSourceBuilder.highlighter(highlightBuilder);
        searchRequest.source(searchSourceBuilder);


        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();

        List<Map<String, Object>> maplist = new ArrayList<>();
        int i = 1;
        for (SearchHit hit : searchHits) {
            // do something with the SearchHit
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            if (highlightFields.get("nick") != null) {
                HighlightField highlight = highlightFields.get("nick");
                Text[] fragments = highlight.fragments();
                String fragmentString = fragments[0].string();
                sourceAsMap.put("hightlightnick", fragmentString);
                sourceAsMap.put("index", i++);
                sourceAsMap.put("score", hit.getScore());
                maplist.add(sourceAsMap);
            }
        }
        return maplist;
    }

    @Override
    public List<Map<String, Object>> SearchFloors(String content) throws IOException {
        RestHighLevelClient client = new ElasticSearchUtil().getClient();

        SearchRequest searchRequest = new SearchRequest("loneybearforum_floors");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();


//        MatchQueryBuilder QueryBuilder = QueryBuilders.matchQuery("content", content);

        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .should(QueryBuilders.matchQuery("content", content))
                .should(QueryBuilders.matchQuery("tittle", content));

        searchSourceBuilder.query(queryBuilder);
        searchSourceBuilder.size(40);

        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.requireFieldMatch(true).field("content")
                .preTags("<span style=\"background: yellow\">").postTags("</span>");
        highlightBuilder.requireFieldMatch(true).field("tittle")
                .preTags("<span style=\"background: yellow\">").postTags("</span>");

        searchSourceBuilder.highlighter(highlightBuilder);
        searchRequest.source(searchSourceBuilder);


        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();

        List<Map<String, Object>> maplist = new ArrayList<>();
        int i = 1;
        for (SearchHit hit : searchHits) {

            // do something with the SearchHit
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            if (highlightFields.get("content") != null) {
                HighlightField highlight = highlightFields.get("content");
                Text[] fragments = highlight.fragments();
                String fragmentString = fragments[0].string();
                sourceAsMap.put("hightlightcontent", fragmentString);
                sourceAsMap.put("index", i++);
                sourceAsMap.put("score", hit.getScore());

                if (highlightFields.get("tittle") == null) {

                    sourceAsMap.put("hightlighttittle", sourceAsMap.get("tittle"));

                    }
                maplist.add(sourceAsMap);
            }
            if (highlightFields.get("tittle") != null) {
                HighlightField highlight = highlightFields.get("tittle");
                Text[] fragments = highlight.fragments();
                String fragmentString = fragments[0].string();
                sourceAsMap.put("hightlighttittle", fragmentString);
                sourceAsMap.put("index", i++);
                sourceAsMap.put("score", hit.getScore());
                if (highlightFields.get("content") == null) {

                    sourceAsMap.put("hightlightcontent", sourceAsMap.get("content"));

                }
                maplist.add(sourceAsMap);
            }
        }
        LinkedHashSet<Map<String,Object>> set2 = new LinkedHashSet<>(maplist);
        maplist.clear();
        maplist.addAll(set2);
        return maplist;

    }

    @Override
    public Map<String,Object> MainSearch(String content) throws IOException {
        Map<String,Object> returnJSon = new HashMap<>();

        ElasticSearchUtil elasticSearchUtil = new ElasticSearchUtil();
        List<Map<String, Object>> loneybearforum_zones = elasticSearchUtil.Search("loneybearforum_zones", content);
        List<Map<String, Object>> loneybearforum_users = elasticSearchUtil.Search("loneybearforum_users", content);
        List<Map<String, Object>> loneybearforum_floors = elasticSearchUtil.Search("loneybearforum_floors", content);

        returnJSon.put("code",new ResponseBean(200,"获取成功",null));
        returnJSon.put("zones",loneybearforum_zones);
        returnJSon.put("users",loneybearforum_users);
        returnJSon.put("floors",loneybearforum_floors);


        return returnJSon;
    }

}
