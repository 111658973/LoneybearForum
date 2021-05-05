package com.lbf.pack.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@Controller
public class testController {

    @GetMapping("/Getreply")
    @ResponseBody
    public String returnString(){
        return "AABBBCCC";
    }

    @GetMapping("/final")
    public String show(){
        return "test/final";
    }

}
