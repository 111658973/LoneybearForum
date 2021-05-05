package com.lbf.pack.controller;

import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Api(tags = "错误处理")
@Controller
public class errorController {

    @RequestMapping("/errorPage")
    public String show404page() {
        return "error";
    }
}
