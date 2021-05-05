package com.lbf.pack.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lbf.pack.beans.ResponseBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 未登陆返回json而不是跳转
 */
public class CustomNotAuthencated implements AuthenticationEntryPoint {
    private  static  ObjectMapper objectMapper =  new  ObjectMapper();
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        PrintWriter out = response.getWriter();
        response.setContentType("application/json;charset=UTF-8");

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        Map<String,Object> map = new HashMap<>();
        map.put("msg","授权失败");
        map.put("code",403);

        out.write(objectMapper.writeValueAsString(map));
        out.flush();
        out.close();
    }
}
