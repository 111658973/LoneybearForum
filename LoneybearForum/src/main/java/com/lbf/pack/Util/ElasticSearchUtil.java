package com.lbf.pack.Util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lbf.pack.beans.ZoneBean;
import com.lbf.pack.service.QueryDataService;
import org.apache.http.HttpHost;
import org.elasticsearch.ElasticsearchException;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.AnalyzeRequest;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ElasticSearchUtil {
    @Autowired
    RestHighLevelClient restHighLevelClient;
    @Autowired
    QueryDataService queryDataService;
    public static final Logger logger = LogManager.getLogger(ElasticSearchUtil.class);

    public RestHighLevelClient getClient() {
        return new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http")
                ));
    }


    public void CreateIndex(String indexName) {

    }

    ;

    public void ClearIndex(String indexname) throws IOException {
        RestHighLevelClient client = getClient();
        DeleteByQueryRequest request = new DeleteByQueryRequest(indexname);
        MatchAllQueryBuilder matchQueryBuilder = QueryBuilders.matchAllQuery();
        request.setQuery(matchQueryBuilder);
        client.deleteByQuery(request, RequestOptions.DEFAULT);
        client.close();
    }

    public void updateIndex(String indexName, List<Map<String, Object>> list) throws IOException {
        RestHighLevelClient client = getClient();
        BulkRequest request = new BulkRequest();
        ClearIndex(indexName);
        int i = 1;
        for (Map<String, Object> m : list) {
            request.add(new IndexRequest(indexName).id(String.valueOf(i++)).source(m));
        }

        ElasticSearchUtil elasticSearchUtil = new ElasticSearchUtil();
        BulkResponse bulkResponse = client.bulk(request, RequestOptions.DEFAULT);

    }

    ;

    public void deleteIndex(String indexName) throws IOException {
        RestHighLevelClient client = getClient();
        try {
            DeleteIndexRequest request = new DeleteIndexRequest("does_not_exist");
            client.indices().delete(request, RequestOptions.DEFAULT);
        } catch (ElasticsearchException exception) {
            if (exception.status() == RestStatus.NOT_FOUND) {

            }
        }
    }

    public Map<String, Object> GetData(String indexname, String DocId) {
        try {
            RestHighLevelClient client = getClient();
            // 1?????????????????????
            GetRequest request = new GetRequest(indexname, DocId);
            // 2??????????????????
            //request.routing("routing");
            //request.version(2);

            //request.fetchSourceContext(new FetchSourceContext(false)); //????????????_source??????
            //?????????????????????
//            String[] includes = new String[]{"message", "*Date"};
//            String[] excludes = Strings.EMPTY_ARRAY;
//            FetchSourceContext fetchSourceContext = new FetchSourceContext(true, includes, excludes);
//            request.fetchSourceContext(fetchSourceContext);

            //??????????????????
			/*String[] includes = Strings.EMPTY_ARRAY;
			String[] excludes = new String[]{"message"};
			FetchSourceContext fetchSourceContext = new FetchSourceContext(true, includes, excludes);
			request.fetchSourceContext(fetchSourceContext);*/


            // ???stored??????
			/*request.storedFields("message");
			GetResponse getResponse = client.get(request);
			String message = getResponse.getField("message").getValue();*/


            //3???????????????
            GetResponse getResponse = null;
            try {
                // ????????????
                getResponse = client.get(request, RequestOptions.DEFAULT);
            } catch (ElasticsearchException e) {
                if (e.status() == RestStatus.NOT_FOUND) {
                    logger.error("???????????????id?????????");
                }
                if (e.status() == RestStatus.CONFLICT) {
                    logger.error("????????????????????????????????????????????????????????????");
                }
                logger.error("??????????????????", e);
            }

            //4???????????????
            if (getResponse != null) {
                String index = getResponse.getIndex();
                String type = getResponse.getType();
                String id = getResponse.getId();
                if (getResponse.isExists()) { // ????????????
                    long version = getResponse.getVersion();
                    String sourceAsString = getResponse.getSourceAsString(); //???????????? String
                    Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();  // ????????????Map
                    byte[] sourceAsBytes = getResponse.getSourceAsBytes();    //????????????????????????
                    logger.info("index:" + index + "  type:" + type + "  id:" + id);
                    logger.info(sourceAsString);
                    return sourceAsMap;

                } else {
                    logger.error("???????????????id?????????");
                    return null;
                }
            }


            //??????????????????????????????
			/*
			ActionListener<GetResponse> listener = new ActionListener<GetResponse>() {
			    @Override
			    public void onResponse(GetResponse getResponse) {

			    }

			    @Override
			    public void onFailure(Exception e) {

			    }
			};
			client.getAsync(request, listener);
			*/

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<String, Object> GetSource(String indexname, String DocId) {
        try {
            RestHighLevelClient client = getClient();
            // 1?????????????????????
            GetRequest request = new GetRequest(indexname, DocId);
            // 2??????????????????
            //request.routing("routing");
            //request.version(2);

            //request.fetchSourceContext(new FetchSourceContext(false)); //????????????_source??????
            //?????????????????????
//            String[] includes = new String[]{"message", "*Date"};
//            String[] excludes = Strings.EMPTY_ARRAY;
//            FetchSourceContext fetchSourceContext = new FetchSourceContext(true, includes, excludes);
//            request.fetchSourceContext(fetchSourceContext);

            //??????????????????
			/*String[] includes = Strings.EMPTY_ARRAY;
			String[] excludes = new String[]{"message"};
			FetchSourceContext fetchSourceContext = new FetchSourceContext(true, includes, excludes);
			request.fetchSourceContext(fetchSourceContext);*/


            // ???stored??????
			/*request.storedFields("message");
			GetResponse getResponse = client.get(request);
			String message = getResponse.getField("message").getValue();*/


            //3???????????????
            GetResponse getResponse = null;
            try {
                // ????????????
                getResponse = client.get(request, RequestOptions.DEFAULT);
            } catch (ElasticsearchException e) {
                if (e.status() == RestStatus.NOT_FOUND) {
                    logger.error("???????????????id?????????");
                }
                if (e.status() == RestStatus.CONFLICT) {
                    logger.error("????????????????????????????????????????????????????????????");
                }
                logger.error("??????????????????", e);
            }

            //4???????????????
            if (getResponse != null) {
                String index = getResponse.getIndex();
                String type = getResponse.getType();
                String id = getResponse.getId();
                if (getResponse.isExists()) { // ????????????
                    long version = getResponse.getVersion();
                    String sourceAsString = getResponse.getSourceAsString(); //???????????? String
                    Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();  // ????????????Map
                    byte[] sourceAsBytes = getResponse.getSourceAsBytes();    //????????????????????????
                    logger.info("index:" + index + "  type:" + type + "  id:" + id);
                    logger.info(sourceAsString);
                    return sourceAsMap;

                } else {
                    logger.error("???????????????id?????????");
                    return null;
                }
            }


            //??????????????????????????????
			/*
			ActionListener<GetResponse> listener = new ActionListener<GetResponse>() {
			    @Override
			    public void onResponse(GetResponse getResponse) {

			    }

			    @Override
			    public void onFailure(Exception e) {

			    }
			};
			client.getAsync(request, listener);
			*/

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Map<String, Object>> Search(String Indexname, String content) throws IOException {
        RestHighLevelClient client = getClient();

        SearchRequest searchRequest = new SearchRequest(Indexname);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();


//        MatchQueryBuilder QueryBuilder = QueryBuilders.matchQuery("content", content);

        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .should(QueryBuilders.matchQuery("content", content))
                .should(QueryBuilders.matchQuery("tittle", content))
                .should(QueryBuilders.matchQuery("nick", content))
                .should(QueryBuilders.matchQuery("zname", content))
                .should(QueryBuilders.matchQuery("zintroduction", content));



        searchSourceBuilder.query(queryBuilder);
        searchSourceBuilder.size(40);

        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.requireFieldMatch(true).field("content")
                .preTags("<span style=\"background: yellow\">").postTags("</span>");
        highlightBuilder.requireFieldMatch(true).field("tittle")
                .preTags("<span style=\"background: yellow\">").postTags("</span>");
        highlightBuilder.requireFieldMatch(true).field("nick")
                .preTags("<span style=\"background: yellow\">").postTags("</span>");
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
            if (highlightFields.get("content") != null) {
                HighlightField highlight = highlightFields.get("content");
                Text[] fragments = highlight.fragments();
                String fragmentString = fragments[0].string();
                sourceAsMap.put("hightlightContent", fragmentString);
                sourceAsMap.put("index", i++);
                sourceAsMap.put("score", hit.getScore());
                maplist.add(sourceAsMap);
            }
            if (highlightFields.get("tittle") != null) {
                HighlightField highlight = highlightFields.get("tittle");
                Text[] fragments = highlight.fragments();
                String fragmentString = fragments[0].string();
                sourceAsMap.put("hightlightTittle", fragmentString);
                sourceAsMap.put("index", i++);
                sourceAsMap.put("score", hit.getScore());
                maplist.add(sourceAsMap);
            }
            if (highlightFields.get("nick") != null) {
                HighlightField highlight = highlightFields.get("nick");
                Text[] fragments = highlight.fragments();
                String fragmentString = fragments[0].string();
                sourceAsMap.put("hightlightNick", fragmentString);
                sourceAsMap.put("index", i++);
                sourceAsMap.put("score", hit.getScore());
                maplist.add(sourceAsMap);
            }
            if (highlightFields.get("zname") != null) {
                HighlightField highlight = highlightFields.get("zname");
                Text[] fragments = highlight.fragments();
                String fragmentString = fragments[0].string();
                sourceAsMap.put("hightlightZname", fragmentString);
                sourceAsMap.put("index", i++);
                sourceAsMap.put("score", hit.getScore());
                maplist.add(sourceAsMap);
            }
            if (highlightFields.get("zintroduction") != null) {
                HighlightField highlight = highlightFields.get("zintroduction");
                Text[] fragments = highlight.fragments();
                String fragmentString = fragments[0].string();
                sourceAsMap.put("hightlightZname", fragmentString);
                sourceAsMap.put("index", i++);
                sourceAsMap.put("score", hit.getScore());
                maplist.add(sourceAsMap);
            }
            for (int k = 0; k < maplist.size()-1; k++) {
                for (int j = maplist.size()-1; j > i; j--) {
                    if ( maplist.get(j).get("zid")!=null && maplist.get(j).get("zid").equals(maplist.get(k).get("zid"))) {
                        maplist.remove(j);
                    }
                    else if(maplist.get(j).get("fid")!=null  && maplist.get(j).get("fid").equals(maplist.get(k).get("fid"))){
                        maplist.remove(j);
                    }
                }
            }
        }
        return maplist;
    }
}

