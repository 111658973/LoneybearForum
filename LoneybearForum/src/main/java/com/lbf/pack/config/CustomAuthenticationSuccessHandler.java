package com.lbf.pack.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lbf.pack.Util.TimeUtil;
import com.lbf.pack.beans.UserLoginBean;
import com.lbf.pack.mapper.UserLoginMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private static ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private UserLoginMapper userLoginMapper;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("登录成功");
        log.info("username=>" + request.getParameter("username"));

        Map<String, Object> map = new HashMap<>();

        UserLoginBean user = new UserLoginBean();
        user.setLastVisitedTime(new TimeUtil().getCuerrent_time());
        user.setLastVisitedIp(request.getRemoteAddr());
        userLoginMapper.update(user,new QueryWrapper<UserLoginBean>().eq("username",request.getParameter("username")));



        map.put("code",200);
        map.put("msg","登录成功");
        map.put("data",authentication);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(map));

    }
}
