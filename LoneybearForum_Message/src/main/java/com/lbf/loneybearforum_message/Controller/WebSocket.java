package com.lbf.loneybearforum_message.Controller;


import com.lbf.loneybearforum_message.Beans.ResBean;
import com.lbf.loneybearforum_message.Config.WebSocketServer;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.EncodeException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class WebSocket {
    @GetMapping("/message/notify/index")
    public ResponseEntity<String> index(){
        return ResponseEntity.ok("请求成功");
    }

    @GetMapping("/message/notify/page")
    public ModelAndView page(){
        return new ModelAndView("websocket");
    }

    @RequestMapping("/message/notify/push/{toUserId}")
    @ResponseBody
    public Map<String,Object>  pushToWeb(String message, @PathVariable String toUserId) throws IOException {
        WebSocketServer.sendInfo(message,toUserId);
        Map<String,Object> map = new HashMap<>();
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        map.put("code",new ResBean(200,"发送成功"));
        map.put("sender","系统");
//        map.put("receiver",)
        map.put("time",df1.format( new Date()));
        map.put("content",message);
        return map;
    }

    @RequestMapping("/message/notify/push/all")
    @ResponseBody
    public Map<String,Object> pushNotify(String message, String html, String tittle, HttpServletRequest request) throws IOException, EncodeException {
        WebSocketServer.sendAll(message,html,tittle,request);
        Map<String,Object> map = new HashMap<>();
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        map.put("status",new ResBean(200,"发送全体信息成功"));
        return map;
    }

}
