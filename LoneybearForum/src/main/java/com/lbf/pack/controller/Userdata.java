package com.lbf.pack.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.lbf.pack.beans.*;
import com.lbf.pack.mapper.PostMapper;
import com.lbf.pack.mapper.UserDataMapper;
import com.lbf.pack.mapper.UserEditMapper;
import com.lbf.pack.service.FileHandlerService;
import com.lbf.pack.service.QueryDataService;
import com.lbf.pack.service.UserDataService;
import com.lbf.pack.service.VisitHistoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "个人资料模块")
@Controller
public class Userdata {
    @Autowired
    QueryDataService queryDataService;
    @Autowired
    UserEditMapper userEditMapper;
    @Autowired
    UserDataMapper userDataMapper;
    @Autowired
    PostMapper postMapper;
    @Autowired
    FileHandlerService fileHandlerService;
    @Autowired
    VisitHistoryService visitHistoryService;
    @Autowired
    UserDataService userDataService;
    @GetMapping(value = "/userdata")
    String show(){
        return "userdata";
    }


    @GetMapping("/GetUserdataResource")
    @ResponseBody
    public Map<String, Object> getUserdata(HttpSession session){
        return queryDataService.GetUserdataFromSession(session,true);
    }


    @GetMapping("/GetUserdataResource/{uid}")
    @ResponseBody
    public Map<String, Object> getUserdata(@PathVariable Long uid){
        return queryDataService.getUserPageDataByUid(uid);
    }

    @PostMapping("/userdata/editData")
    public Map<String,Object> getResetData(HttpSession session){
        return null;
    }

    /**
     * 用户资料页面修改一些东西
     * @return
     */
    @PostMapping("/userdata/UpdateUserData")
    @ResponseBody
    public Map<String,Object> UpdateUser(HttpSession session, MultipartHttpServletRequest request, Authentication authentication) throws IOException {
        Map<String,String> userdata = new HashMap<>();
        ArrayList<String> paramLists = new ArrayList<>();

        UserEditDataBean updateUfdb = new UserEditDataBean();
        updateUfdb.setEmail(request.getParameter("mail"));
        updateUfdb.setNick(request.getParameter("username"));
        updateUfdb.setPhoneNumber(request.getParameter("phoneNumber"));
        updateUfdb.setUintroduction(request.getParameter("introduction"));
        updateUfdb.setSex(request.getParameter("radio"));

        MultipartFile newIcon = request.getFile("file");
        MultipartFile newDisplayImg = request.getFile("display");

//        String username = session.getAttribute("username").toString();
        String username = authentication.getName();

        Boolean isIconEmpty = fileHandlerService.SaveMultiPartFileToLocalPath(newIcon, "./src/main/resources/static/images/Users/" + username);
        Boolean isDisplayEmpty = fileHandlerService.SaveMultiPartFileToLocalPath(newDisplayImg, "./src/main/resources/static/images/Users/" + username);

        if (isIconEmpty) {

            updateUfdb.setIconUrl("../static/images/Users/" + username +"/"+ newIcon.getOriginalFilename());
        }
        if (isDisplayEmpty){
            updateUfdb.setDisplayImgUrl("../static/images/Users/"+username+"/"+ newDisplayImg.getOriginalFilename());

        }



        Map<String,Object> returnValue = new HashMap<>();//url编码解码一下，不然汉字不显示


//        updateufdb.setIconUrl(userdata.get("new_iconUrl"));
//        updateufdb.setDisplayImgUrl(userdata.get("new_displayUrl"));

        /**
         * 比较email和phone
         * 查的session里面的名字可能有问题
         */
        UserEditDataBean queryUfdb = userEditMapper.selectOne(new QueryWrapper<UserEditDataBean>().select("email,phone_number").eq("username",username));
        if(queryUfdb.getPhoneNumber()!=null && queryUfdb.getPhoneNumber().equals(updateUfdb.getPhoneNumber()) && queryUfdb.getEmail()!=null && queryUfdb.getEmail().equals(updateUfdb.getEmail())){ //邮箱手机都重复
            returnValue.put("code",103);
            returnValue.put("msg","邮箱和手机重复");
        }
        else if(queryUfdb.getPhoneNumber()!=null &&queryUfdb.getPhoneNumber().equals(updateUfdb.getPhoneNumber())){//手机重复
            returnValue.put("code",101);
            returnValue.put("msg","手机重复");
        }
        else if(queryUfdb.getEmail()!=null && queryUfdb.getEmail().equals(updateUfdb.getEmail())) {//邮箱重复
            returnValue.put("code",102);
            returnValue.put("msg","邮箱重复");

        }
        else{
            returnValue.put("code",200);
            returnValue.put("msg","success");
        }
        userEditMapper.update(updateUfdb,new UpdateWrapper<UserEditDataBean>().eq("username",username));
        return returnValue;

    }

    /**
     * 用户资料页面四个切换按钮功能实现
     * @param type 1:我的帖子 2：我的收藏 3：关注分区 4：浏览记录
     * @param page 分页，默认10条记录每页，在函数里自己更改
     * @return
     */
    @ApiOperation(value = "用户资料页面四个切换按钮功能实现")
    @GetMapping("/userdata/switchdata/{type}/{page}")
    @ResponseBody
    public Map<String,Object> switchData(@PathVariable int type,@PathVariable int page,HttpSession session){
        Map<String,Object> returnJson = new HashMap<>();
        long uid = queryDataService.GetUserUidFromSession(session);
        if(uid != -1){
            switch (type){
                //我的帖子
                case 1:
                    List<PostBean> pidBeans = postMapper.selectList(new QueryWrapper<PostBean>().select("pid").eq("uid", uid).orderByDesc("pcreate_time").last("limit "+page*10+",10"));
                    ArrayList<Long> pidsList = new ArrayList<>();
                    for(PostBean p:pidBeans){
                        pidsList.add(p.getPid());
                    }
                    ArrayList<PostBean> PostBeanList = new ArrayList<>();
                    for(long i:pidsList){
                        PostBeanList.add(queryDataService.GetPostInfo(i));
                    }
                    returnJson.put("mypost",PostBeanList);
                    returnJson.put("code",211);
                    returnJson.put("msg","我的帖子获取成功");
                    break;
                //我的收藏
                case 2:
                    List<UserFullDataBean.favorPostsIdList> fav_posts = userDataMapper.selectOne(new QueryWrapper<UserFullDataBean>().select("favor_posts_id_list").eq("uid",uid)).getFavorPostsIdList();
                    List<PostBean> posts = new ArrayList<>();
                    List<UserFullDataBean.favorPostsIdList> pids = JSON.parseArray(fav_posts.toString(), UserFullDataBean.favorPostsIdList.class);

                    for(UserFullDataBean.favorPostsIdList l:pids){
                        posts.add(queryDataService.GetPostInfo(l.getPid()));
                    }
                    returnJson.put("code",212);
                    returnJson.put("msg","我的收藏获取成功");
                    returnJson.put("data",posts);
                    break;
                //关注分区
                case 3:
                    List<UserFullDataBean.favouriteZoneIdList> fav_zones = userDataMapper.selectOne(new QueryWrapper<UserFullDataBean>().select("favourite_zone_id_list").eq("uid",uid)).getFavouriteZoneIdList();
                    List<UserFullDataBean.favouriteZoneIdList> zids = JSON.parseArray(fav_zones.toString(), UserFullDataBean.favouriteZoneIdList.class);

                    List<Map<String,Object>> list = new ArrayList<>();
//                    for(UserFullDataBean.favouriteZoneIdList l:zids){
//                        list.add(queryDataService.GetZonesDataByZid(l.getZid()));
//                    }
                    for(UserFullDataBean.favouriteZoneIdList z:zids){
                        Map<String,Object> map = new HashMap<>();
                        map.put("zonedata",queryDataService.GetZonesDataByZid(z.getZid()));
                        map.put("zonepost",queryDataService.GetZoneSortedPosts(z.getZid(), 2, 100, 0));
                        list.add(map);
                    }
                    returnJson.put("data",list);
                    returnJson.put("code",213);
                    returnJson.put("msg","关注分区获取成功");
                    break;
                //浏览记录
                case 4:

                    returnJson.put("data",visitHistoryService.getPostHistoryByUid(uid));
                    returnJson.put("code",214);
                    returnJson.put("msg","历史记录获取成功");

                    break;
            }
        }
        else{
            returnJson.put("code",-1);
            returnJson.put("msg","请先登陆");
        }


        return returnJson;
    }


    @ApiOperation(value = "用户资料页面四个切换按钮功能实现")
    @GetMapping("/userdata/switchdata/{uid}/{type}/{page}")
    @ResponseBody
    public Map<String,Object> seeswitchData(@PathVariable int type,@PathVariable int page,HttpSession session,@PathVariable Long uid){
        Map<String,Object> returnJson = new HashMap<>();
            switch (type){
                //帖子
                case 1:
                    List<PostBean> pidBeans = postMapper.selectList(new QueryWrapper<PostBean>().select("pid").eq("uid", uid).orderByDesc("pcreate_time").last("limit "+page*10+",10"));
                    ArrayList<Long> pidsList = new ArrayList<>();
                    for(PostBean p:pidBeans){
                        pidsList.add(p.getPid());
                    }
                    ArrayList<PostBean> PostBeanList = new ArrayList<>();
                    for(long i:pidsList){
                        PostBeanList.add(queryDataService.GetPostInfo(i));
                    }
                    returnJson.put("mypost",PostBeanList);
                    returnJson.put("code",211);
                    returnJson.put("msg","我的帖子获取成功");
                    break;
                //收藏
                case 2:
                    List<UserFullDataBean.favorPostsIdList> fav_posts = userDataMapper.selectOne(new QueryWrapper<UserFullDataBean>().select("favor_posts_id_list").eq("uid",uid)).getFavorPostsIdList();
                    List<PostBean> posts = new ArrayList<>();
                    List<UserFullDataBean.favorPostsIdList> pids = JSON.parseArray(fav_posts.toString(), UserFullDataBean.favorPostsIdList.class);

                    for(UserFullDataBean.favorPostsIdList l:pids){
                        posts.add(queryDataService.GetPostInfo(l.getPid()));
                    }
                    returnJson.put("code",212);
                    returnJson.put("msg","我的收藏获取成功");
                    returnJson.put("data",posts);
                    break;
                //分区
                case 3:
                    List<UserFullDataBean.favouriteZoneIdList> fav_zones = userDataMapper.selectOne(new QueryWrapper<UserFullDataBean>().select("favourite_zone_id_list").eq("uid",uid)).getFavouriteZoneIdList();
                    List<UserFullDataBean.favouriteZoneIdList> zids = JSON.parseArray(fav_zones.toString(), UserFullDataBean.favouriteZoneIdList.class);

                    List<Map<String,Object>> list = new ArrayList<>();
//                    for(UserFullDataBean.favouriteZoneIdList l:zids){
//                        list.add(queryDataService.GetZonesDataByZid(l.getZid()));
//                    }
                    for(UserFullDataBean.favouriteZoneIdList z:zids){
                        Map<String,Object> map = new HashMap<>();
                        map.put("zonedata",queryDataService.GetZonesDataByZid(z.getZid()));
                        map.put("zonepost",queryDataService.GetZoneSortedPosts(z.getZid(), 2, 100, 0));
                        list.add(map);
                    }
                    returnJson.put("data",list);
                    returnJson.put("code",213);
                    returnJson.put("msg","关注分区获取成功");
                    break;
                //浏览记录
                case 4:

                    returnJson.put("data",visitHistoryService.getPostHistoryByUid(uid));
                    returnJson.put("code",214);
                    returnJson.put("msg","历史记录获取成功");

                    break;
            }

        return returnJson;
    }

    @GetMapping("/userdata/{uid}")
    public String seeUserPage(@PathVariable Long uid){
        String me = SecurityContextHolder.getContext().getAuthentication().getName();

        String user = userDataService.getById(uid).getUsername();

        if(me.equals(user)){
            return "redirect:/userdata";
        }
        else {
        return "otherUserData";}
    }

}
