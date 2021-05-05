package com.lbf.pack.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lbf.pack.Util.RandomUtils;
import com.lbf.pack.beans.FloorBean;
import com.lbf.pack.beans.PostBean;
import com.lbf.pack.beans.UserFullDataBean;
import com.lbf.pack.beans.ZoneBean;
import com.lbf.pack.mapper.FloorMapper;
import com.lbf.pack.mapper.PostMapper;
import com.lbf.pack.mapper.Zonemapper;
import com.lbf.pack.service.*;
import io.swagger.annotations.Api;
import javafx.geometry.Pos;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Api(tags = "资源处理")
@RestController
public class Resources {
    @Autowired
    QueryDataService queryDataService;
    @Autowired
    PostService postService;
    @Autowired
    SendEmailService sendEmailService;
    @Autowired
    PostMapper postMapper;
    @Autowired
    FloorMapper floorMapper;
    @Autowired
    Zonemapper zonemapper;
    @Autowired
    ZoneService zoneService;
    @Autowired
    UserDataService userDataService;
    @Autowired
    FloorService floorService;
    @GetMapping("/GetHotPostsDataLimit/{index}")
    public List<PostBean> GetHotPostsDataLimit(@PathVariable int index){
        return queryDataService.GetHotPostsDataLimit(index,0);
    }

    @GetMapping("/GetNewestPostDataLimit/{index}")
    public List<PostBean> GetNewestPostDataLimit(@PathVariable int index){
        return queryDataService.GetHotPostsDataLimit(index,0);
    }

    @GetMapping("/GetUserFullDataFromSession")
    @ResponseBody
    public Map<String, Object> GetUserData(HttpSession session){
        return queryDataService.GetUserdataFromSession(session,true);
    }

    @PostMapping("/getEmailVerifycode")
    @ResponseBody
    public Map<String,Object> getemailVerifycode(@RequestParam("username")String username,
                                                 @RequestParam("email")String email,
                                                 MultipartHttpServletRequest request) throws MessagingException {
        Map<String,Object> returnValue = new HashMap<>();

        String verifycode = new RandomUtils().GetRandomEmailVerifyCode().toString();
        returnValue.put("status","1");
        returnValue.put("msg","验证码发送成功");
        sendEmailService.sendVerifycodeMail(email,verifycode);
        sendEmailService.storeVerifycodeInRedis(username,verifycode);

        return returnValue;
    }

    @GetMapping("/getUserByUidOrNick/{param}")
    @ResponseBody
    public List<UserFullDataBean> getUserByUidOrNick(@PathVariable String param){
//        Map<String,Object> returnValue = new HashMap<>();
        List<UserFullDataBean> beanList = queryDataService.SimpleSearchUserByUidOrNick(param);
        return beanList;
//        if(beanList!=null){
//            returnValue.put("status","200");
//            returnValue.put("msg","获取成功");
//            returnValue.put("data",beanList);
//        }
//        else {
//            returnValue.put("status","201");
//            returnValue.put("msg","数据为空");
//        }
//        return returnValue;
    }


    @GetMapping("/getpostlist/all")
    @ResponseBody
    public List<PostBean> getPostListAll(){
        return postService.getAllPostList();
    }

    @GetMapping("/getuserlist/all")
    @ResponseBody
    public List<UserFullDataBean> getUserListAll(){
        return userDataService.GetUserDataAll();
    }

    @GetMapping("/getzonelist/all")
    @ResponseBody
    public List<ZoneBean> getZoneListAll(){
        return zoneService.GetZoneDataAll();
    }

    @GetMapping("/getFloorlist/all")
    @ResponseBody
    public List<FloorBean> getFloorListAll(){
        return floorService.GetFloorListAll();
    }


    @GetMapping("/getPostByPidOrTittle/{zname}/{param}")
    @ResponseBody
    public List<PostBean> getPosts(@PathVariable String param, @PathVariable String zname, Authentication authentication){
        return queryDataService.SimpleSearchPostByUidOrTittle(param,zname);
    }

    @GetMapping("/getBlackList/{Zonename}")
    @ResponseBody
    public List<Map<String,Object>> getBLackList(@PathVariable String Zonename){
        return null;
    }

    @GetMapping("/authenticaion")
    @ResponseBody
    public Authentication userInfo(){
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
