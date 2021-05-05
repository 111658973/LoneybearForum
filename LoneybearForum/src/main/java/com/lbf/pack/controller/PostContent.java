package com.lbf.pack.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.lbf.pack.Util.Base64Utils;
import com.lbf.pack.Util.RandomUtils;
import com.lbf.pack.Util.RegexUtil;
import com.lbf.pack.Util.TimeUtil;
import com.lbf.pack.beans.*;
import com.lbf.pack.mapper.*;
import com.lbf.pack.service.BlackListService;
import com.lbf.pack.service.PostService;
import com.lbf.pack.service.QueryDataService;
import io.swagger.annotations.ApiOperation;
import javafx.geometry.Pos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.crypto.MacSpi;
import javax.naming.NoPermissionException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class PostContent {
    @Autowired
    QueryDataService queryDataService;
    @Autowired
    FloorMapper floorMapper;
    @Autowired
    UserDataMapper userDataMapper;
    @Autowired
    UserJsonMapper userJsonMapper;
    @Autowired
    PostMapper postMapper;
    @Autowired
    PostService postService;
    @Autowired
    BlackListService blackListService;
    @ApiOperation(value = "根据前端url传来的参数动态显示页面")
    @GetMapping(value = {"/post/{postId}"})
    public String show(){
        return "post";
    }


    /**
     * 根据url传来的参数来获取一个帖子需要的所有信息
     * @param postId 帖子id
     * @return 一个Map
     */
    @ApiOperation(value = "根据url传来的分区id和帖子id来查表返回json")
    @GetMapping("/post/{postId}/getresources")
    @ResponseBody
    public Map<String,Object> getPostResources(@PathVariable int postId) throws InterruptedException {
        Map<String, Object> returnMap = new HashMap<>();

        //帖子的基本信息
        PostBean PostInfo = queryDataService.GetPostInfo(postId);
        //楼层信息
        List<FloorBean> FloorList = queryDataService.GetFloorList(postId);

        //分区信息
        ZoneBean ZoneData =  queryDataService.GetZonesDataByZid(PostInfo.getZid());

        //回复信息
        List<Map<String,Object>> FloorData = queryDataService.GetFormattedFloors(postId);


        returnMap.put("postdata",PostInfo);
        returnMap.put("floorlist",FloorList);
        returnMap.put("zonedata",ZoneData);
        returnMap.put("floors",queryDataService.GetFormattedRest(postId));
        returnMap.put("head",queryDataService.GetFormattedFloorByIndex(postId,1).get(0));
        Thread.sleep(1000);
        return returnMap;
    }

    @GetMapping("/getPost/{pid}")
    @ResponseBody
    public List<Map<String,Object>> get(@PathVariable int pid){
        return queryDataService.GetFormattedFloors(1);
    }


    /**
     * 回复，只能回复文本
     * @param floor
     * @param request
     * @param session
     * @return
     */
    @PostMapping("/post/replyfloor/{floor}")
    @ResponseBody
    public Map<String,Object> ReplyFloor(@PathVariable int floor,
                                         MultipartHttpServletRequest request,
                                         HttpSession session){
        String value = request.getParameter("content");
        String pid = request.getParameter("pid");
        Map<String,Object> user = queryDataService.GetUserdataFromSession(session,false);
        Map<String,Object> returnJson = new HashMap<>();
        FloorBean NewFloor = new FloorBean();
        try {
            UserFullDataBean userdata = (UserFullDataBean) user.get("data");
            NewFloor.setContent(value);
            NewFloor.setPid(Integer.parseInt(pid));
            NewFloor.setUid(userdata.getUid());
            NewFloor.setFloor(floor);
            String cuerrent_time = new TimeUtil().getCuerrent_time();
            NewFloor.setFtime(cuerrent_time);
            NewFloor.setType(3);
            PostBean postBean = postService.getById(pid);
            postBean.setPlastVisitedTime(cuerrent_time);
            postMapper.update(postBean,new QueryWrapper<PostBean>().eq("pid",pid));
            returnJson.put("code",200);
            returnJson.put("msg","回复成功");
            floorMapper.insert(NewFloor);
        }catch (NullPointerException e){
            e.printStackTrace();
            returnJson.put("code",-1);
            returnJson.put("msg","请先登陆");
        }

        return returnJson;

    }


    /**
     * 回复主题帖
     * @param pid
     * @return
     */
    @PostMapping("post/replyTopic/{pid}")
    @ResponseBody
    public Map<String,Object> ReplyTopic(@PathVariable int pid,
                                         HttpSession session,
                                         MultipartHttpServletRequest request) throws IOException {
        Map<String,Object> returnJson = new HashMap<>();
        long uid = queryDataService.GetUserUidFromSession(session);

        Map<Object, Object> byUid = blackListService.getByUid(uid);
        if(byUid.size()!=0){
            returnJson.put("code",413);
            returnJson.put("msg","用户被禁言");
            return returnJson;
        }
        if(uid != -1){
            int MaxFloor = floorMapper.selectOne(new QueryWrapper<FloorBean>().select("floor").orderByDesc("floor").eq("pid",pid).last("limit 1")).getFloor();
            int currentFloor = MaxFloor+1;

            String rawhtml = request.getParameter("content");

            List<Map<String, String>> imgSrcList = new RegexUtil().getImgSrcList(rawhtml);
            Base64Utils base64Utils = new Base64Utils();
            ArrayList<String> filenameList = new ArrayList<>();
            for(Map<String,String> src:imgSrcList){
                String imgName = "img"+ new TimeUtil().getCuerrent_date_no_shortline()+"_"+ new RandomUtils().GetRandomEmailVerifyCode()+"." +src.get("suffix");
                String fileadress = "/static/images/PostImgs/post_"+pid;
                base64Utils.SaveBase64(src.get("url"),"./src/main/resources"+fileadress,imgName);
                filenameList.add(".."+fileadress+"/"+imgName);
            }
            String html_noalt = rawhtml.replaceAll("alt=\"(.*?)\"","");
            for (int i = 0;i<imgSrcList.size();i++ ){
                html_noalt = html_noalt.replace(imgSrcList.get(i).get("raw"),filenameList.get(i));
            }




            FloorBean newFloor = new FloorBean();
            newFloor.setFloor(currentFloor);
            newFloor.setPid(pid);
            newFloor.setUid(uid);
            newFloor.setContent(html_noalt);
            String cuerrent_time = new TimeUtil().getCuerrent_time();
            newFloor.setFtime(cuerrent_time);
            newFloor.setType(2);

            PostBean postBean = postService.getById(pid);
            postBean.setPlastVisitedTime(cuerrent_time);
            postMapper.update(postBean,new QueryWrapper<PostBean>().eq("pid",pid));

            floorMapper.insert(newFloor);
            returnJson.put("code",200);
            returnJson.put("msg","回复成功");

        }
        else {
            returnJson.put("code",-1);
            returnJson.put("msg","请先登陆");
        }
        return returnJson;
    }


    @ApiOperation(value = "收藏帖子")
    @PostMapping("/post/{pid}/collectpost")
    @ResponseBody
    public Map<String,Object> follow(@PathVariable int pid,HttpSession session,MultipartHttpServletRequest request){
        Map<String,Object> returnJson = new HashMap<>();

        long uid = queryDataService.GetUserUidFromSession(session);
        if(uid!= -1){
            try {

                UserFullDataBean userdata= userDataMapper.selectOne(new QueryWrapper<UserFullDataBean>().eq("uid",uid));
                if(userdata.getFavorPostsIdList()==null){
                    userdata.setFavorPostsIdList(new ArrayList<>());
                }
                Boolean isEmpty = true;
                List<UserFullDataBean.favorPostsIdList> favorPostIdLidListBean = JSON.parseArray(userdata.getFavorPostsIdList().toString(), UserFullDataBean.favorPostsIdList.class);
                for(UserFullDataBean.favorPostsIdList PostId:favorPostIdLidListBean){
                    if (PostId.getPid()== pid){
                        isEmpty = false;
                        break;
                    }
                }
                if(isEmpty){
                    favorPostIdLidListBean.add(new UserFullDataBean.favorPostsIdList(pid));
                }
                String s = JSONObject.toJSONString(favorPostIdLidListBean);

                UserJsonStoreBean JsonString = new UserJsonStoreBean();
                JsonString.setFavor_posts_id_list(s);
                userJsonMapper.update(JsonString,new UpdateWrapper<UserJsonStoreBean>().eq("uid",uid));
            }catch (NullPointerException e){
                e.printStackTrace();
                UserFullDataBean.favorPostsIdList PostIdList =new UserFullDataBean.favorPostsIdList();
                PostIdList.setPid(pid);
                List<UserFullDataBean.favorPostsIdList> favorPostIdLidListBean = new ArrayList<>();

                favorPostIdLidListBean.add(PostIdList);

                String s = JSONObject.toJSONString(PostIdList);
                UserJsonStoreBean JsonString = new UserJsonStoreBean(s,null);
                userJsonMapper.update(JsonString,new UpdateWrapper<UserJsonStoreBean>().eq("uid",uid));


            }

        }
        else {
            returnJson.put("code",-1);
            returnJson.put("msg","请先登陆");
        }


        return returnJson;
    }

    @ApiOperation(value = "获取关注和收藏信息")
    @GetMapping("/post/{pid}/getButtonsinfo")
    @ResponseBody
    public Map<String,Object> getButtonsInfo(@PathVariable int pid,HttpSession session){
        Map<String,Object> returnJson = new HashMap<>();

        long uid = queryDataService.GetUserUidFromSession(session);
        if(uid!= -1){
            try {
                Map<String, Object> PostCollectInfo = queryDataService.GetPostCollectInfo(pid, session);
                long zid = postMapper.selectOne(new QueryWrapper<PostBean>().select("zid").eq("pid",pid)).getZid();
                Map<String, Object> ZoneFollowedInfo = queryDataService.GetZoneFollowInfo(zid,session);
                returnJson.put("PostCollectInfo",PostCollectInfo);
                returnJson.put("ZoneFollowedInfo",ZoneFollowedInfo);

                returnJson.put("code",200);
                returnJson.put("msg","获取关注信息成功");
            }catch (NullPointerException e){
                e.printStackTrace();
                return null;
            }


        }
        else {
            returnJson.put("code",-1);
            returnJson.put("msg","请先登陆");
        }


        return returnJson;
    }


}
