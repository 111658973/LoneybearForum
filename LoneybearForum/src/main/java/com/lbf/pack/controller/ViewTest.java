package com.lbf.pack.controller;


import com.aliyun.oss.model.PutObjectResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lbf.pack.beans.FloorBean;
import com.lbf.pack.beans.PostOperationBean;
import com.lbf.pack.beans.UserFullDataBean;
import com.lbf.pack.beans.ZoneBean;
import com.lbf.pack.mapper.FloorMapper;
import com.lbf.pack.mapper.PostOperationMapper;
import com.lbf.pack.mapper.UserDataMapper;
import com.lbf.pack.mapper.Zonemapper;
import com.lbf.pack.service.PostOperationService;
import com.lbf.pack.service.UserDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
@ApiIgnore
@Controller
public class ViewTest {
    @Autowired
    UserDataMapper userDataMapper;
    @Autowired
    UserDataService userDataService;
    @Autowired
    PostOperationService postOperationService;
    @Autowired
    Zonemapper zonemapper;
    @Autowired
    FloorMapper floorMapper;
    @GetMapping("/test")
    public String testTHY(Model model) {
        return "test/VueTest";
    }

    @PostMapping("/test1")
    public String testNullUser(Model model) {
        model.addAttribute("msg", "登陆成功");
        model.addAttribute("msg1", "<a href=\"https://www.baidu.com\" th:href=\"${lnik}\">baidu</a>");
        model.addAttribute("lnik", "LoneyBear.com");
        return "ThymeleafTest";
    }


    @PostMapping("/veri")
    public String testAxios(@RequestBody String veri) {
        ArrayList<String> arr = new ArrayList<>();
        for (String v : veri.split("&")) {
            for (String vv : v.split("="))
                arr.add(vv);
        }
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < arr.size(); i++) {
            map.put(arr.get(i++), arr.get(i));
        }

        return "test";
    }


    @GetMapping("/json")
    @ResponseBody
    public List<UserFullDataBean> show() {
        QueryWrapper<UserFullDataBean> queryWrapper = new QueryWrapper<>();
        queryWrapper.select().eq("username", "111658973");
        List<UserFullDataBean> user1 = userDataMapper.selectList(queryWrapper);
//        UserFullDataBean userFullDataBean = userDataMapper.selectOne(queryWrapper);
        System.out.println(user1.get(0).getFavouriteZoneIdList());
        return user1;

    }

    @GetMapping("/downloadTest")
    public String showudownload() {

        return "/test/final";

    }

    @PostMapping("/download/{param}")
    @ResponseBody
    public String showupload(@PathVariable String param, HttpServletResponse response) {
        System.out.println(param);
        String url = "/Users/chaibf/Graduation/src/main/resources/static/images/background/" + param;
        String downloadFilePath = url;//被下载的文件在服务器中的路径,
        String fileName = param;//被下载文件的名称

        File file = new File(downloadFilePath);
        if (file.exists()) {
            response.setContentType("application/force-download");// 设置强制下载不打开
            response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            HttpStatus statusCode = HttpStatus.OK;
//            return new ResponseEntity<byte[]>(body, headers, statusCode);


        }
        return null;
    }

    @PostMapping("/up")
    @ResponseBody
    public Map<String, Object> upload(HttpServletRequest request) {
        MultipartHttpServletRequest params = ((MultipartHttpServletRequest) request);
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        return null;

    }


    @GetMapping("/esmap")
    @ResponseBody
    public Map<String,Object> getmap(){
        Map<String,Object> map  = new HashMap<>();

        List<UserFullDataBean> username = userDataMapper.selectList(new QueryWrapper<UserFullDataBean>());
        List<ZoneBean> zid = zonemapper.selectList(new QueryWrapper<ZoneBean>());
        List<FloorBean> fid = floorMapper.selectList(new QueryWrapper<FloorBean>());


        map.put("user",username);
        map.put("zone",zid);
        map.put("floor",fid);
        return map;
    }

    @GetMapping("/testPage")
    @ResponseBody
    public IPage<FloorBean> testPage(){
        return floorMapper.selectPage(new Page<>(0,2),new QueryWrapper<FloorBean>());
    }


    @GetMapping("getop")
    @ResponseBody
    public PostOperationBean gee(){
        return postOperationService.getById(1);
    }


    @PostMapping("/callback")
    @ResponseBody
    public Map AliyunOSSCallback(String s, @RequestBody String responseBody, PutObjectResult putObjectResult){



        Map map =new HashMap();
        map.put("s",s);
        map.put("b",responseBody);
        return map;
    }

}
