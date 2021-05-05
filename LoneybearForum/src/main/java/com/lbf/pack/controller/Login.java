package com.lbf.pack.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.lbf.pack.beans.UserLoginBean;
import com.lbf.pack.mapper.UserLoginMapper;
//import com.lbf.pack.service.CustomUserDetailService;
import com.lbf.pack.service.UserLoginService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;
@Api(tags = "登陆模块")
@Controller
public class Login {

    @Autowired
    UserLoginMapper userLoginMapper;

    @Autowired
    UserLoginService userservice;

    @Autowired
    DefaultKaptcha defaultKaptcha;
//    @Autowired
//    CustomUserDetailService customUserDetailService;


    @GetMapping(value ={"/","/index","/login","/login?{log}"})    //能够接受一个参数
    public String showLogin(){
        return "login";
    }


    /**
     * 弃用
     * @RequestParam("username") String username,
     * @RequestParam("password")String password,
     * @RequestParam("verifyCode")String verifyCode,
     */

//    @PostMapping("/verifying")
//    @ResponseBody
//    public Map<String,Object> verify(
//                         @RequestHeader("User-Agent") String header,
//                         @RequestHeader Map<String,String> headers,
//                         @RequestBody String veri,
//                         HttpServletRequest request,
//                         HttpSession session,
//                         Model model) {
//
//        /**
//         * param## userdata是从页面获取的数据账号密码验证码ip
//         */
//        Map<String,Object> userdata= new HashMap<>();
//        Map<String,Object> returnValue= new HashMap<>();
//        ArrayList<String> arr = new ArrayList<>();
//        for(String v:veri.split("&")){
//            for(String vv:v.split("="))
//                arr.add(vv);
//        }
//        for(int i = 0;i<arr.size();i++){
//            userdata.put(arr.get(i++),arr.get(i));
//        }
//        userdata.put("headers",headers);
//        userdata.put("loginIp",request.getRemoteAddr());
//
//        QueryWrapper<UserLoginBean> queryWrapper = new QueryWrapper<>();
//        queryWrapper.select("password","username","authority").eq("username",userdata.get("username"));
////        List<UsersBean> user = usersMapper.selectList(queryWrapper);
//        UserLoginBean userLogindata = userLoginMapper.selectOne(queryWrapper);//存的数据库里查的username和password
//
//        //验证码获取
//
//
//
//
//        /**
//         * 表单中获取的数据放在Map<String,String>中
//         */
//        boolean verify = false;
//        String rightCode = (String) request.getSession().getAttribute("rightCode");
//        if(userdata.get("verifycode").equals(rightCode)){
//            verify = true;
//        }
//        /**
//         * 获取当前时间
//         */
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String cuerrent_time = df.format(new Date());
//
//        /**
//         * 记录当前登陆时间存入数据库后传个对象给前端ajax success函数
//         */
//        System.out.println();
//        //账号不存在，表单清空
//        if(userLogindata ==null){
//            returnValue.put("status",0);
//            returnValue.put("msg","账号不存在");
//            return  returnValue;
//        }
//
//        /**
//         * 以下情况需要给页面url传参数
//         */
//
//        //验证码不对,账号密码不清空
//        else if(!verify){
//            returnValue.put("status",3);
//            returnValue.put("msg","验证码错误");
//            return  returnValue;
//        }
//
//        //密码不对，账号不清空
//        else if(!(verify2(userdata.get("password").toString(),userLogindata.getPassword())))
//        {
//            returnValue.put("status",2);
//            returnValue.put("msg","账号或密码错误");
//            return  returnValue;
//            //传参数给页面例如账号密码不对，账号密码为空之类的
//        }
//
//        //成功
//        else if(verify2(userdata.get("password").toString(),userLogindata.getPassword())&&verify){
//
//            UpdateWrapper<UserLoginBean> updateWrapper = new UpdateWrapper<>();
//            updateWrapper.eq("username",userdata.get("username"));
//            UserLoginBean update = new UserLoginBean();
//            update.setLastVisitedTime(cuerrent_time);
//            update.setLastVisitedIp(userdata.get("loginIp").toString());
//            session.setAttribute("username",userdata.get("username"));
//            session.setAttribute("authority",userLogindata.getAuthority());
//            session.setMaxInactiveInterval(0);
//            returnValue.put("status",200);
//            returnValue.put("msg","登陆成功");
//            userLoginMapper.update(update,updateWrapper);
////            return "/signin";
//            return returnValue;
//        }
//
//        //其他情况
//        else {
//            returnValue.put("status",-1);
//            returnValue.put("msg","未知错误");
//            return  returnValue;
//        }
//        //
//
//    }



    @PostMapping("/verified")
    public String verified(){
        return "redirect:/homepage";
    }

    public Boolean verify2(String DBpwd,String Querypwd)
    {

        if(DBpwd.equals(Querypwd)){
            return Boolean.TRUE;
        }
        else return Boolean.FALSE;

    }

    @GetMapping("/login/getVerifycode")
    @ResponseBody
    Map<String,Object> getVerifycode(){
        Map<String,Object> returnValue = new HashMap<>();

        return returnValue;
    }


    @GetMapping("/defaultKaptcha")
    @ResponseBody
    public Map<String,Object> defaultKaptcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String,Object> returnJson = new HashMap<>();
        byte[] captcha = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            // 将生成的验证码保存在session中
            String createText = defaultKaptcha.createText();
            request.getSession().setAttribute("VerifyCode", createText);
            BufferedImage bi = defaultKaptcha.createImage(createText);
            ImageIO.write(bi, "jpg", out);
            captcha = out.toByteArray();
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setContentType("image/jpeg");
            ServletOutputStream sout = response.getOutputStream();
            sout.write(captcha);
            sout.flush();
            sout.close();
            returnJson.put("code",200);
            returnJson.put("msg","获取验证码成功");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            returnJson.put("code",-1);
            returnJson.put("msg","获取验证码失败");
        }
        return returnJson;

    }
    @GetMapping("/infos")
    @ResponseBody
    public List<String> getinfo(){
        List<String> list = new ArrayList<>();
        list.add(SecurityContextHolder.getContext().getAuthentication().toString());
//        list.add(SecurityContextHolder.getContext().getAuthentication().getName());
//        list.add(SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString());
//        list.add(SecurityContextHolder.getContext().getAuthentication().getCredentials().toString());
//        list.add(SecurityContextHolder.getContext().getAuthentication().getDetails().toString());
//        list.add(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
//        list.add(SecurityContextHolder.getContext().getAuthentication().)

        return list;
    }

}

