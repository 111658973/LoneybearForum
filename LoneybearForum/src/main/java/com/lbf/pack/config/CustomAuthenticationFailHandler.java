package com.lbf.pack.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class CustomAuthenticationFailHandler implements AuthenticationFailureHandler {

    @Autowired
    private ObjectMapper objectMapper;
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        log.info("登录成功");
        log.info("username=>" + request.getParameter("username"));

        Map<String, Object> map = new HashMap<>();

        switch (e.getMessage()){
            case "用户不存在":
                map.put("code",0);
                map.put("msg","用户不存在");
                break;
            case "Bad credentials":
                map.put("code",2);
                map.put("msg","密码错误");
                break;
            case "验证码错误":
                map.put("code",3);
                map.put("msg","验证码错误");
                break;
            case "用户已封禁":{
                map.put("code",406);
                map.put("msg","用户被封禁");
            }
        }


//        map.put("code",-1);
//        map.put("msg","登录失败");
//        map.put("reason",e.getMessage());

        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(map));
    }
}
