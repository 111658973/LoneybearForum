package com.lbf.pack.service;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lbf.pack.beans.PostBean;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Map;

@Service
public interface PostService extends IService<PostBean> {

    /**
     * 修改html中的<img>标签中的bsae64url 为本地图片url
     * @param rawHTML 未经处理的整个html
     * @return 状态码和msg
     */
    @ApiOperation(value = "把帖子中的Html中的<img>里的内容存到本地然后修改图片url")
    public Map<String,Object> getLocalHTML(String rawHTML);


    @ApiOperation(value = "接收一个jsonArray对象，把图片存到本地后修改url后返回")
    public  JSONArray getLocalJsonArray(JSONArray json);

    public Map<String,Object> deletePostByPid(long pid);


    public Map<String,Object> moveToRecycle(long pid);

    public Map<String, Object> OutToRecycle(long pid);


    public List<PostBean> getAllPostList();
}
