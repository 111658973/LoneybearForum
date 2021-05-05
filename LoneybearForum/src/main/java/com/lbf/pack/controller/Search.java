package com.lbf.pack.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lbf.pack.Util.ElasticSearchUtil;
import com.lbf.pack.beans.FloorBean;
import com.lbf.pack.beans.ResponseBean;
import com.lbf.pack.beans.UserFullDataBean;
import com.lbf.pack.mapper.FloorMapper;
import com.lbf.pack.service.ElasticSearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Controller
@Api(tags = "搜索引擎分区")
public class Search {
    @Autowired
    ElasticSearchService searchService;

    @Autowired
    FloorMapper floorMapper;
    @GetMapping("/search")
    public String show(){
        return "SearchPage";
    }


    @ApiOperation(value = "获取搜索内容")
    @GetMapping("/search/getContent")
    @ResponseBody
    public Map<String,Object> showDeletePostCyclePage(){
        Map<String,Object> returnJson = new HashMap<>();
        return returnJson;
    }

    @ApiOperation(value = "获取用户信息列表")
    @GetMapping("/search/GetUserContent")
    @ResponseBody

    public List<UserFullDataBean> getUserContent(){
        List<UserFullDataBean> list = new ArrayList<>();
        return list;
    }

    @ApiOperation(value = "获取分区信息列表")
    @GetMapping("/search/getZoneContent")
    @ResponseBody
    public List<UserFullDataBean> getZoneContent(){
        List<UserFullDataBean> list = new ArrayList<>();
        return list;
    }

    @ApiOperation(value = "获取帖子列表")
    @GetMapping("/search/getPostContent")
    @ResponseBody
    public List<Map<String,Object>> getPostContent(){
        List<Map<String,Object>> list = new ArrayList<>();
        return list;
    }

    @GetMapping("/doc/{index}/{id}")
    @ResponseBody
    public Map<String,Object> getP(@PathVariable String index,@PathVariable String id){
        Map<String,Object> list = new HashMap<>();

        ElasticSearchUtil elasticSearchUtil = new ElasticSearchUtil();
        list = elasticSearchUtil.GetData(index, id);
        return list;
    }

    @GetMapping("/search/{content}")
    @ResponseBody
    public Map<String,Object> getHit(@PathVariable String content) throws IOException {
        Map<String,Object> list = new HashMap<>();
        ElasticSearchUtil elasticSearchUtil = new ElasticSearchUtil();

        List<Map<String, Object>> fmaps = searchService.SearchFloors(content);
        List<Map<String, Object>> umaps = searchService.SearchUsers(content);
        List<Map<String, Object>> zmaps = searchService.SearchZones(content);

        list.put("response",new ResponseBean(200,"获取搜索内容成功",null));
        list.put("zones",zmaps);
        list.put("floors",fmaps);
        list.put("users",umaps);

        return list;

    }

    @GetMapping("/search/init")
    @ResponseBody
    public String init() throws IOException {
        searchService.init();
        return "初始化成功";
    }

}
