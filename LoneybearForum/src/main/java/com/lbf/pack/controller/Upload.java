package com.lbf.pack.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lbf.pack.Util.Base64Utils;
import com.lbf.pack.Util.RandomUtils;
import com.lbf.pack.Util.RegexUtil;
import com.lbf.pack.Util.TimeUtil;
import com.lbf.pack.beans.HtmlBean;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sun.misc.BASE64Decoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "上传模块")
@Controller
public class Upload {
    @PostMapping("/uploadImg")
    public Map<String,Object> uploadImg(){
        Map<String,Object> returnJson= new HashMap<>();
        return returnJson;
    }


    @PostMapping("/uploadBase64Img")
    public Map<String,Object> UploadBase64(){
        Map<String,Object> returnJson= new HashMap<>();
        return returnJson;
    }

    @PostMapping("/uploadPostContent")
    @ResponseBody
    public JSONArray upload(MultipartHttpServletRequest request) throws IOException {
        Map<String,Object> returnJson = new HashMap<>();
        String text = request.getParameter("text");
        String html = request.getParameter("html");
        String json = request.getParameter("json");

        JSONArray jsonArray = JSONArray.parseArray(json);
        List<HtmlBean> htmllist = JSONObject.parseArray(json, HtmlBean.class);

        List<Map<String, String>> imgSrcList = new RegexUtil().getImgSrcList(html);


        Base64Utils base64Utils = new Base64Utils();
        ArrayList<String> filenameList = new ArrayList<>();
        for(Map<String,String> src:imgSrcList){
            String imgName = "img"+ new TimeUtil().getCuerrent_date_no_shortline()+"_"+ new RandomUtils().GetRandomEmailVerifyCode()+"." +src.get("suffix");
            String fileadress;
            base64Utils.SaveBase64(src.get("url"),"./src/main/resources/static/images/testUpload/user1/BBB",imgName);
            filenameList.add(imgName);
        }

        String html_noalt = html.replaceAll("alt=\"(.*?)\"","");


        for (int i = 0;i<imgSrcList.size();i++ ){
            html_noalt = html_noalt.replace(imgSrcList.get(i).get("raw"),filenameList.get(i));
        }


        returnJson.put("content_JsonArray",jsonArray);
        return jsonArray;
    }



    @GetMapping("/editor")
    public String showEditor(){
        return  "/test/EditorTest";
    }

    @PostMapping("/cll")
    @ResponseBody
    public JSONArray a(MultipartHttpServletRequest request) throws IOException {
        Map<String,Object> returnJson = new HashMap<>();
        String text = request.getParameter("text");
        String html = request.getParameter("html");
        String json = request.getParameter("json");
        List<Map<String, String>> imgSrcList = new RegexUtil().getImgSrcList(html);


        Base64Utils base64Utils = new Base64Utils();
        ArrayList<String> filenameList = new ArrayList<>();
        for(Map<String,String> src:imgSrcList){
            String imgName = "img"+ new TimeUtil().getCuerrent_date_no_shortline()+"_"+ new RandomUtils().GetRandomEmailVerifyCode()+"." +src.get("suffix");
            String fileadress;
            base64Utils.SaveBase64(src.get("url"),"./src/main/resources/static/images/testUpload/user1/BBB",imgName);
            filenameList.add(imgName);
        }

        String html_noalt = html.replaceAll("alt=\"(.*?)\"","");


        for (int i = 0;i<imgSrcList.size();i++ ){
            html_noalt = html_noalt.replace(imgSrcList.get(i).get("raw"),filenameList.get(i));
        }
        return null;
    }
}
