package com.lbf.pack.controller;

import com.lbf.pack.service.QueryDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

public class Authencation {
    @Autowired
    QueryDataService queryDataService;

    @GetMapping("/userinfo/uid")
    @ResponseBody
    public Long getUid(){
        return queryDataService.getCuerrentUid();
    }

}
