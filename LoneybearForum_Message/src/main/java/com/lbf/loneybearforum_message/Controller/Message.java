package com.lbf.loneybearforum_message.Controller;


import com.lbf.loneybearforum_message.Service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class Message {

    @Autowired
    MessageService messageService;
    @GetMapping("/message")
    public String show(){
        return "Message";
    }

    @GetMapping("/message/getAll")
    @ResponseBody
    public Map getResource(){
        Map<String,Object> map =new HashMap<>();
        map.put("messages", messageService.getAllMessagesByUid(Long.parseLong("1")));
        return map;
    }

    @GetMapping("/message/getReply")
    @ResponseBody
    public Map getReply(){
        Map<String,Object> map =new HashMap<>();
        map.put("messages", messageService.getAllMessagesByUid(Long.parseLong("1")));
        return map;
    }

    @GetMapping("/message/getAt")
    @ResponseBody
    public Map getAt(){
        Map<String,Object> map =new HashMap<>();
        map.put("messages", messageService.getAllMessagesByUid(Long.parseLong("1")));
        return map;
    }

    @GetMapping("/message/getLike")
    @ResponseBody
    public Map getLike(){
        Map<String,Object> map =new HashMap<>();
        map.put("messages", messageService.getAllMessagesByUid(Long.parseLong("1")));
        return map;
    }

    @GetMapping("/message/getNotify")
    @ResponseBody
    public Map getNotify(){
        Map<String,Object> map =new HashMap<>();
        map.put("messages", messageService.getAllMessagesByUid(Long.parseLong("1")));
        return map;
    }

    @GetMapping("/message/getMailbox")
    @ResponseBody
    public Map getMailbox(){
        Map<String,Object> map =new HashMap<>();
        map.put("messages", messageService.getAllMessagesByUid(Long.parseLong("1")));
        return map;
    }




}
