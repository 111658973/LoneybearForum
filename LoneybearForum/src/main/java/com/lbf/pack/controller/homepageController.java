package com.lbf.pack.controller;


import com.lbf.pack.beans.PostBean;

import com.lbf.pack.beans.ZoneBean;
import com.lbf.pack.mapper.PostMapper;
import com.lbf.pack.mapper.UserDataMapper;
import com.lbf.pack.mapper.Zonemapper;

import com.lbf.pack.service.ElasticSearchService;
import com.lbf.pack.service.QueryDataService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags="论坛首页")
@Controller
public class homepageController {

    @Autowired
    Zonemapper zonemapper;
    @Autowired
    PostMapper postMapper;
    @Autowired
    QueryDataService queryDataService;
    @Autowired
    ElasticSearchService elasticSearchService;
    @Autowired
    UserDataMapper userDataMapper;
    @GetMapping("/homepage")
    public String show() throws IOException {
        return "homepage";
    }

//
//    @GetMapping("/GetHomepageResource")
//    @ResponseBody
//    public Map<String,Object> getHomePageResource(HttpSession session){
//        /**
//         * Session里没有user记录
//         * 当然可以用security实现，也可以用无状态登陆带token
//         */
//        if((String)session.getAttribute("username")==null){
////            return "redirect:/login";
//            return  null;
//        }
//
//
//        /**
//         * Session有记录
//         * 根据user查user表的所有内容
//         * @Para userData 获取的user所有内容
//         */
//
//            String usernameInSession = (String) session.getAttribute("username");
//            int authorityInSession = (Integer) session.getAttribute("authority");
//            QueryWrapper<UserFullDataBean> queryWrapper = new QueryWrapper<>();
//            queryWrapper.select("*").eq("username",usernameInSession);
////        List<UserFullDataBean> userDataList = userDataMapper.selectList(queryWrapper);
//            UserFullDataBean userData = userDataMapper.selectOne(queryWrapper);
//
//
//
//
//        /**
//         * 根据favouriteZoneIdLists来获取关注的几个分区的信息
//         * 放在左侧导航栏
//         * @Param userloveZoneList里是查的用户喜欢的分区的Zonebean实体类
//         */
//        List<UserFullDataBean.favouriteZoneIdList> zids =new ArrayList<>();
//        if(userData.getFavorPostIdLidList()!=null){
//            zids = JSON.parseArray(userData.getFavouriteZoneIdList().toString(), UserFullDataBean.favouriteZoneIdList.class);
//        }
//        else{
//            zids = null;
//        }
//        List<ZoneBean> userLoveZoneList = new ArrayList<>();
//        if(zids!=null) {
//            for (UserFullDataBean.favouriteZoneIdList zid : zids) {
//                ZoneBean z = zonemapper.selectOne(new QueryWrapper<ZoneBean>().select("*").eq("zid", zid.getZid()));
//                userLoveZoneList.add(z);
//            }
//        }
//
//        Map<String,Object> returnJson= new HashMap<>();
//
//
//        /**
//         * 根据从大到小查4个热门分区
//         * 放在推荐分区
//         * @Param ZoneBean实体类
//         * @Param List<ZoneBean> hotZones 存热门的四个分区列表
//         */
//        QueryWrapper<ZoneBean> HotZoneQueryWarpper = new QueryWrapper<>();
//        HotZoneQueryWarpper.select("*").orderByDesc("like_number").last("Limit 4");
//        List<ZoneBean> HotZoneList =zonemapper.selectList(HotZoneQueryWarpper);
//
//        /**
//         * 根据desc的顺序从大到小like-dislike排序，10个热门帖子
//         * @Param PostBean实体类
//         * @Param List<Postbean> HotPosts存热门列表
//         */
//        QueryWrapper<PostBean> HotPostQueryWarpper = new QueryWrapper<>();
//        HotPostQueryWarpper.select("*").orderByDesc("(plike_count-pdislike_count)").last("LIMIT 10");
//        List<PostBean> HotPostList =postMapper.selectList(HotPostQueryWarpper);
//
//
//        returnJson.put("hot_posts",HotPostList);
//        returnJson.put("recommand_zones",HotZoneList);
//        returnJson.put("user_data",userData);
//        returnJson.put("left_nav",userLoveZoneList);
//        session.setAttribute("isReady",true);
//        return returnJson;
//    }



    @ApiOperation(value = "查询主页的资料返回json")
    @GetMapping("/GetHomepageResource")
    @ResponseBody
    public Map<String, Object>  getHomePageResource(HttpSession session){
        Map<String,Object> returnJson = new HashMap<>();

        //session里查的用户资料
        Map<String,Object> userData;


        /**
         * Session里没有user记录
         * 当然可以用security实现，也可以用无状态登陆带token
         */

        userData = queryDataService.GetUserdataFromSession(session,false);


        /**
         * 根据从大到小查4个热门分区
         * 放在推荐分区
         * @Param ZoneBean实体类
         * @Param List<ZoneBean> hotZones 存热门的四个分区列表
         */
       List<ZoneBean> HotZoneList = queryDataService.GetHotZones(4);

        /**
         * 根据desc的顺序从大到小like-dislike排序，10个热门帖子
         * @Param PostBean实体类
         * @Param List<Postbean> HotPosts存热门列表
         */
        List<PostBean> HotPostList = queryDataService.GetHotPostsDataLimit(10,0);


        returnJson.put("hot_posts",HotPostList);
        returnJson.put("hot_zones",HotZoneList);
        returnJson.put("user_data",userData);


        session.setAttribute("isReady",true);


//        returnList.add(HotPostList);
//        returnList.add(HotZoneList);
//        returnList.add(userData);
//        returnList.add(userLoveZoneList);
        return returnJson;
    }

    @ApiOperation(value = "根据type获取最新帖子或者最热门帖子")
    @GetMapping("/homepage/getpost/{type}/{page}")
    @ResponseBody
    public Map<String,Object> getSrotPostdata(@PathVariable int type,@PathVariable int page){
        Map<String,Object> returnJson = new HashMap<>();
        switch (type){
            case 1:
                returnJson.put("hotposts",queryDataService.GetHotPostsDataLimit(10,page));
                break;
            case 2:
                returnJson.put("newposts",queryDataService.GetNewestPostDataLimit(10,page));
                break;
        }


        return returnJson;
    }
}
