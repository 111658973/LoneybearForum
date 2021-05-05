package com.lbf.pack.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.*;
public class RegexUtil{

        public List<Map<String, String>> getImgSrcList(String content){
            List<String> list = new ArrayList<String>();
            //目前img标签标示有3种表达式
            //<img alt="" src="1.jpg"/>   <img alt="" src="1.jpg"></img>     <img alt="" src="1.jpg">
            //开始匹配content中的<img />标签
            Pattern p_img = Pattern.compile("<(img|IMG)(.*?)(/>|></img>|>)");
            Matcher m_img = p_img.matcher(content);
            boolean result_img = m_img.find();
            if (result_img) {
                while (result_img) {
                    //获取到匹配的<img />标签中的内容
                    String str_img = m_img.group(2);
                    //开始匹配<img />标签中的src
                    Pattern p_src = Pattern.compile("(src|SRC)=(\"|\')(.*?)(\"|\')");
                    Matcher m_src = p_src.matcher(str_img);
                    if (m_src.find()) {
                        String str_src = m_src.group(3);
                        list.add(str_src);
                    }
                    //匹配content中是否存在下一个<img />标签，有则继续以上步骤匹配<img />标签中的src
                    result_img = m_img.find();
                }
            }
            //取，后面的东西
            List<Map<String, String>> srcs = new ArrayList<>();

            for(String s:list){
                Map<String,String> format = new HashMap<>();
                format.put("raw",s);
                format.put("suffix",s.split(",")[0].split(";")[0].split("/")[1]);
                format.put("url",s.split(",")[1]);
                srcs.add(format);

            }
            return srcs;
        }

        public String ReplaceSrcAndAlt(String content,List<Map<String, String>> filenameList){
            content = content.replaceAll("alt=\"(.*?)\"","");

            for (Map<String, String> map:filenameList ){
                content = content.replace(map.get("raw"),map.get("url"));
            }
//            content = content.replaceAll("src=\"(.*?)\"","src=\"../image/" + "AABBCC" + "\"");


            return content;

        }

}
