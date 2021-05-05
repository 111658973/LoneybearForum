package com.lbf.pack.serviceImpl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lbf.pack.beans.FloorBean;
import com.lbf.pack.beans.PostBean;
import com.lbf.pack.mapper.FloorMapper;
import com.lbf.pack.mapper.PostMapper;
import com.lbf.pack.service.PostService;
import com.lbf.pack.service.QueryDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, PostBean> implements PostService {
    @Autowired
    PostMapper postMapper;
    @Autowired
    FloorMapper floorMapper;
    @Autowired
    QueryDataService queryDataService;

    @Override
    public Map<String, Object> getLocalHTML(String rawHTML) {
        Map<String,Object> reutrnJson = new HashMap<>();
        return reutrnJson;

    }

    @Override
    public JSONArray getLocalJsonArray(JSONArray json) {
        for(Object obj:json){

        }
        return null;
    }

    @Override
    public Map<String, Object> moveToRecycle(long pid) {
        Map<String, Object> returnJSon = new HashMap<>();

        try {
            PostBean post = postMapper.selectOne(new QueryWrapper<PostBean>().eq("pid",pid));
            post.setEnabled(false);
            postMapper.update(post,new QueryWrapper<PostBean>().eq("pid",pid));
            returnJSon.put("code",200);
            returnJSon.put("msg","移到回收站成功");
        }catch (NullPointerException e){
            e.printStackTrace();
            returnJSon.put("code",-1);
            returnJSon.put("msg","找不到指定内容，移到回收站失败");
        }
        return returnJSon;
    }

    @Override
    public List<PostBean> getAllPostList() {
        List<PostBean> pids = postMapper.selectList(new QueryWrapper<PostBean>().select("pid"));
        List<PostBean> list = new ArrayList<>();
        for(PostBean p:pids){
            list.add(queryDataService.GetPostInfo(p.getPid()));
        }
        return list;
    }

    @Override
    public Map<String, Object> OutToRecycle(long pid) {
        Map<String, Object> returnJSon = new HashMap<>();

        try {
            PostBean post = postMapper.selectOne(new QueryWrapper<PostBean>().eq("pid",pid));
            post.setEnabled(true);
            postMapper.update(post,new QueryWrapper<PostBean>().eq("pid",pid));
            returnJSon.put("code",200);
            returnJSon.put("msg","移到回收站成功");
        }catch (NullPointerException e){
            e.printStackTrace();
            returnJSon.put("code",-1);
            returnJSon.put("msg","找不到指定内容，移到回收站失败");
        }
        return returnJSon;
    }

    @Override
    public Map<String, Object> deletePostByPid(long pid) {
        Map<String,Object> returnJSon = new HashMap<>();
        try {
            postMapper.delete(new QueryWrapper<PostBean>().eq("pid",pid));
            floorMapper.delete(new QueryWrapper<FloorBean>().eq("pid",pid));
            returnJSon.put("code",200);
            returnJSon.put("msg","删除成功");
        }catch (NullPointerException e){
            e.printStackTrace();
            returnJSon.put("code",-1);
            returnJSon.put("msg","找不到指定帖子，删除失败");
        }

        return  returnJSon;

    }

    public JSONObject DisplayElement(JSONObject obj,String html){
        try{
            html+= "<" + obj.get("tag") +" ";
            if(obj.get("tag").equals("img")){

            }
            if(obj.get("attrs")!=null){

            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        return null;
    }
}
