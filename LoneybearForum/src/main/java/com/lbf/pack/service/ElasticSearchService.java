package com.lbf.pack.service;


import com.lbf.pack.Util.ElasticSearchUtil;
import com.lbf.pack.beans.ResponseBean;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public interface ElasticSearchService {
    public void init() throws IOException;

    public Map<String,Object> MainSearch(String content) throws IOException;

    public List<Map<String,Object>> SearchZones(String content)throws IOException;

    public List<Map<String,Object>> SearchUsers(String content)throws IOException;

    public List<Map<String,Object>> SearchFloors(String content)throws IOException;


}
