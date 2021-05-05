package com.lbf.pack.controller;


import com.alibaba.fastjson.JSON;
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
import com.lbf.pack.service.QueryDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "分区模块")
@Controller
public class ZoneController {
    @Autowired
    QueryDataService queryDataService;
    @Autowired
    PostMapper postMapper;
    @Autowired
    FloorMapper floorMapper;
    @Autowired
    UserJsonMapper userJsonMapper;
    @Autowired
    UserDataMapper userDataMapper;
    String zoneName;
    @Autowired
    Zonemapper zonemapper;
    @Autowired
    PostRecycleMapper postRecycleMapper;
    @Autowired
    BlackListService blackListService;
    @Autowired
    AdminMapper adminMapper;
    @GetMapping("/zone/{zname}")
    public String show(@PathVariable String zname) {
        zoneName = zname;
        return "Zonedata";
    }


    @GetMapping("/zone/{zname}/getzonesdata")
    @ResponseBody
    public Map<String, Object> GetDataResource(@PathVariable String zname) {
        return queryDataService.GetZonesDataByZname(zname);
    }


    @ApiOperation(value = "创建新帖子,从url接收zname")
    @PostMapping("/zone/{zname}/createpost")
    @ResponseBody
    public Map<String, Object> CreateNewPost(@PathVariable String zname,
                                             HttpSession session,
                                             MultipartHttpServletRequest request) throws IOException {

        Map<String, Object> returnJson = new HashMap<>();
        PostBean newPost = new PostBean();
        newPost.setUid(queryDataService.GetUserUidFromSession(session));
        newPost.setZid(queryDataService.GetZidByName(zname));
        newPost.setPtittle(request.getParameter("tittle"));
        newPost.setPcreateTime(new TimeUtil().getCuerrent_time());
        newPost.setPlastVisitedTime(new TimeUtil().getCuerrent_time());
        postMapper.insert(newPost);

        FloorBean firstFloor = new FloorBean();
        firstFloor.setFloor(1);
        firstFloor.setPid(queryDataService.GetMaxPostCount());
        firstFloor.setUid(queryDataService.GetUserUidFromSession(session));
        firstFloor.setFtime(new TimeUtil().getCuerrent_time());
        firstFloor.setType(2);

        String rawhtml = request.getParameter("content");

        List<Map<String, String>> imgSrcList = new RegexUtil().getImgSrcList(rawhtml);
        Base64Utils base64Utils = new Base64Utils();
        ArrayList<String> filenameList = new ArrayList<>();
        for(Map<String,String> src:imgSrcList){
            String imgName = "img"+ new TimeUtil().getCuerrent_date_no_shortline()+"_"+ new RandomUtils().GetRandomEmailVerifyCode()+"." +src.get("suffix");
            String fileadress = "/static/images/PostImgs/post_"+queryDataService.GetMaxPostCount();
            base64Utils.SaveBase64(src.get("url"),"./src/main/resources"+fileadress,imgName);
            filenameList.add(".."+fileadress+"/"+imgName);
        }
        String html_noalt = rawhtml.replaceAll("alt=\"(.*?)\"","");
        for (int i = 0;i<imgSrcList.size();i++ ){
            html_noalt = html_noalt.replace(imgSrcList.get(i).get("raw"),filenameList.get(i));
        }

        firstFloor.setContent(html_noalt);
        if (firstFloor.getUid() != -1) {

            floorMapper.insert(firstFloor);
            Long pid =postMapper.selectList(new QueryWrapper<PostBean>().orderByDesc("pid").last("LIMIT 1")).get(0).getPid();
            returnJson.put("pid",pid);
            returnJson.put("code", 200);
            returnJson.put("msg", "创建成功");
        } else {
            returnJson.put("code", -1);
            returnJson.put("msg", "请先登录");
        }
        return returnJson;

    }


    /**
     * @param zname 分区的zid，对应数据库里面的zrul
     * @param type  类型，一是按时间先后顺序，2是按热度
     * @return
     */
    @GetMapping("/zone/{zname}/getZonePostData/{type}")
    @ResponseBody
    public List<PostBean> getZonePostData(@PathVariable String zname, @PathVariable int type) {
        Map<String, Object> returnValue = new HashMap<>();

        long zid = queryDataService.GetZidByName(zname);

        List<PostBean> PostLists = queryDataService.GetZonePostDataDescBy(zid, 10, 0, 2);
        queryDataService.GetZoneSortedPosts(zid, type, 10, 0);
        List list= new ArrayList();
        for (PostBean post : PostLists) {
            list.add(queryDataService.GetPostInfo(post.getPid()));
        }

        returnValue.put("code", 200);
        return list;

    }

    @ApiOperation(value = "关注分区")
    @PostMapping("/zone/{zid}/followzone")
    @ResponseBody
    public Map<String, Object> follow(@PathVariable int zid, HttpSession session,MultipartHttpServletRequest request) {
        Map<String, Object> returnJson = new HashMap<>();

        long uid = queryDataService.GetUserUidFromSession(session);
        if (uid != -1) {
            try {

                UserFullDataBean userdata = userDataMapper.selectOne(new QueryWrapper<UserFullDataBean>().select("favourite_zone_id_list").eq("uid", uid));

                Boolean isEmpty = true;
                List<UserFullDataBean.favouriteZoneIdList> favorZoneIdListBean = JSON.parseArray(userdata.getFavouriteZoneIdList().toString(), UserFullDataBean.favouriteZoneIdList.class);
                for (UserFullDataBean.favouriteZoneIdList zoneIdList : favorZoneIdListBean) {
                    if (zoneIdList.getZid() == zid) {
                        isEmpty = false;
                        break;
                    }
                }
                if (isEmpty) {
                    favorZoneIdListBean.add(new UserFullDataBean.favouriteZoneIdList(zid));

                }

                String s = JSONObject.toJSONString(favorZoneIdListBean);

                UserJsonStoreBean JsonString = new UserJsonStoreBean();
                JsonString.setFavourite_zone_id_list(s);
                userJsonMapper.update(JsonString, new UpdateWrapper<UserJsonStoreBean>().eq("uid", uid));
            } catch (NullPointerException e) {
                e.printStackTrace();

                List<UserFullDataBean.favouriteZoneIdList> favorZoneIdListBean = new ArrayList<>();
                favorZoneIdListBean.add(new UserFullDataBean.favouriteZoneIdList(zid));
                String s = JSONObject.toJSONString(favorZoneIdListBean);
                UserJsonStoreBean JsonString = new UserJsonStoreBean();

                JsonString.setFavourite_zone_id_list(s);
                userJsonMapper.update(JsonString, new UpdateWrapper<UserJsonStoreBean>().eq("uid", uid));


            }

        } else {
            returnJson.put("code", -1);
            returnJson.put("msg", "请先登陆");
        }


        return returnJson;
    }

    @ApiOperation(value = "获取关注和收藏信息")
    @GetMapping("/zone/{zname}/getButtonsinfo")
    @ResponseBody
    public Map<String, Object> getButtonsInfo(@PathVariable String zname, HttpSession session) {
        Map<String, Object> returnJson = new HashMap<>();

        long uid = queryDataService.GetUserUidFromSession(session);
        if (uid != -1) {
            long zid = queryDataService.GetZidByName(zname);

            Map<String, Object> ZoneFollowedInfo = queryDataService.GetZoneFollowInfo(zid, session);
            returnJson.put("ZoneFollowedInfo", ZoneFollowedInfo);

            returnJson.put("code", 200);
            returnJson.put("msg", "获取关注信息成功");
        } else {
            returnJson.put("code", -1);
            returnJson.put("msg", "请先登陆");
        }


        return returnJson;
    }

    @ApiOperation(value = "根据type获取最新帖子或者最热门帖子,默认10条一页")
    @GetMapping("/zone/{zname}/getpost/{type}/{page}")
    @ResponseBody
    public Map<String, Object> getSortPostdate(@PathVariable int type, @PathVariable int page, @PathVariable String zname) {
        long zid = -1;
        Map<String, Object> returnJson = new HashMap<>();
        try {
            zid = queryDataService.GetZidByName(zname);
        } catch (NullPointerException e) {
            e.printStackTrace();
            returnJson.put("code", 0);
            returnJson.put("msg", "zid不存在");
        }
        try {
            if (zid != -1) {
                returnJson.put("sortedposts", queryDataService.GetZoneSortedPosts(zid, type, 10, page));
                returnJson.put("code", 200);
                returnJson.put("msg", "获取帖子数据成功");
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            returnJson.put("code", -1);
            returnJson.put("msg", "帖子数据为空");
        }

        return returnJson;
    }

    @GetMapping("/zone/{zname}/getinfo")
    @ResponseBody
    public ZoneBean getzoneinfo(@PathVariable String zname){
        return zonemapper.selectOne(new QueryWrapper<ZoneBean>().eq("zurl",zname));
    }


    @GetMapping({"/zone/{zname}/manage",})
    public String showManagePqage(){
        return "zonemanage";
    }

    @GetMapping("/zone/{zname}/manage/getBlacklist/all")
    @ResponseBody
    public List<Map<String, Object>> getBlackListAll(@PathVariable String zname){

        return blackListService.getALlByZname(zname);
    }
    
    @GetMapping("zone/{zname}/manage/getManagerList")
    @ResponseBody
    public List<Map<String,Object>> getManagersList(@PathVariable String zname){
        long zid = queryDataService.GetZidByName(zname);
        ZoneBean zoneBean = zonemapper.selectOne(new QueryWrapper<ZoneBean>().select("zmanager_id").eq("zid", zid));
        List<ZoneBean.ZoneManagerRelation> zmanagerId = JSON.parseArray(zoneBean.getZmanagerId().toString(), ZoneBean.ZoneManagerRelation.class);
        List<Map<String,Object>> managerList = new ArrayList<>();
        if(zmanagerId!=null){
            for(ZoneBean.ZoneManagerRelation zr:zmanagerId){
                long uid = zr.getUid();
                Map<String,Object> map = new HashMap<>();
                UserFullDataBean uid1 = userDataMapper.selectOne(new QueryWrapper<UserFullDataBean>().eq("uid", uid));
                AdminBean adminBean = adminMapper.selectOne(new QueryWrapper<AdminBean>().eq("uid", uid).eq("zid", zid));
                map.put("uid",uid1.getUid());
                map.put("nick",uid1.getNick());
                map.put("username",uid1.getUsername());
                map.put("authority",uid1.getAuthority());
                map.put("time",adminBean.getTime());
                map.put("operator",adminBean.getOperator());
                map.put("role",adminBean.getAuthority()+"_zone_"+zname);
                managerList.add(map);
            }
        }
        return managerList;
    }

    @GetMapping("zone/{zname}/getGoodPostList")
    @ResponseBody
    public List<PostBean> getGood(@PathVariable String zname){
        long zid = queryDataService.GetZidByName(zname);
        return queryDataService.getGoodPostListByZid(zid);

    }

    @GetMapping("zone/{zname}/getPostList")
    @ResponseBody
    public List<PostBean> getPostList(@PathVariable String zname){
        long zid = queryDataService.GetZidByName(zname);
        List<PostBean> list = new ArrayList<>();
        List<PostBean> postBeans = postMapper.selectList(new QueryWrapper<PostBean>().select("pid").eq("zid", zid).eq("enabled",true));
        for (PostBean p:postBeans){
            list.add(queryDataService.GetPostInfo(p.getPid()));

        }
        return list;
    }


    @GetMapping("zone/{zname}/getPostRecycle/all")
    @ResponseBody
    public List<PostRecycleBean> getPostRecycle(@PathVariable String zname){
        long zid = queryDataService.GetZidByName(zname);
        List<PostRecycleBean> postRecycleBeans = postRecycleMapper.selectList(new QueryWrapper<PostRecycleBean>().select("*").eq("zid",zid));
        for(PostRecycleBean p:postRecycleBeans){
            p.setPost(queryDataService.GetPostInfo(p.getPid()));
            p.setUser(userDataMapper.selectOne(new QueryWrapper<UserFullDataBean>().select("nick").eq("username",p.getOperator())));
        }
        return postRecycleBeans;
    }
}

