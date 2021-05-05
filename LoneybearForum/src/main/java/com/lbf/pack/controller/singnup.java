package com.lbf.pack.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lbf.pack.Util.PasswordEncoderUtil;
import com.lbf.pack.Util.RandomUtils;
import com.lbf.pack.Util.TimeUtil;
import com.lbf.pack.beans.UserLoginBean;
import com.lbf.pack.mapper.UserLoginMapper;
import com.lbf.pack.service.SendEmailService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

@Api(tags="注册模块")
@Controller
public class singnup {
    @Autowired
    UserLoginMapper userLoginMapper;

    @Autowired
    SendEmailService sendEmailService;
    @Autowired
    RedisTemplate redisTemplate;

    @GetMapping("/signup")
    public String show() {
        return "signup";
    }


    @GetMapping("/signuped")
    public String rediectToLogin(){
        return "redirect:/login";
    }

    /**
     * 表单提交后，获取下面的参数，
     * 账号和手机和邮箱不能重复，暂时为了验证方便把邮箱重复关掉
     * 然后验证码从redis缓存中读取一下，如果匹配就注册就成功
     * @return 状态码和信息
     */
    @PostMapping("/signuping")
    @ResponseBody
    public Map<String, Object> creatNewUser(MultipartHttpServletRequest request) {
        Map<String, Object> returnValue = new HashMap<>();
        String username =request.getParameter("username");
        String password =request.getParameter("password");
        String phoneNumber =request.getParameter("phoneNumber");
        String email =request.getParameter("email");
        String verifyCode =request.getParameter("verifycode");


        String cuerrent_date =new TimeUtil().getCuerrent_date();
        Boolean isVerified = isVerified(username, verifyCode);
        UUID uuid = UUID.randomUUID();

        UserLoginBean userbean = new UserLoginBean();
        userbean.setUsername(username);
        userbean.setPassword(new PasswordEncoderUtil().encryptPasswrod(password));
        userbean.setCreateDate(cuerrent_date);
        userbean.setPhoneNumber(phoneNumber);
        userbean.setUuid(uuid.toString());
        userbean.setEmail(email);


        userbean.setNick(new RandomUtils().GetRandomNickName());
        System.out.println(userbean);

        Boolean check_user_uuid = (Boolean) is_user_used(username, uuid.toString()).get("status");
        Boolean check_phone = (Boolean) is_phone_used(phoneNumber).get("status");


        /**
         * 账号存在
         */
        if (!check_user_uuid) {
            returnValue.put("status", -1);
            returnValue.put("msg", "账号已注册");
        }

        /**
         * 手机存在
         */
        else if (!check_phone) {
            returnValue.put("status", -2);
            returnValue.put("msg", "手机已注册");
        }

        /**
         * 验证码错误
         */
        else if (!isVerified) {
            returnValue.put("status", -3);
            returnValue.put("msg", "验证码错误");
        }

        /**
         * 账号以及uuid不存在
         * 验证码正确
         */
        else if (check_user_uuid && check_phone && isVerified) {
            returnValue.put("status", 200);
            returnValue.put("msg", "可以注册");


            userLoginMapper.insert(userbean);
        }
        return returnValue;
    }



    /**
     * 获取邮箱验证码，放到redis，缓存时间设定为5分钟
     * 前端ajax传过来一个账号就行
     * 存放在一个hash里面 username:{"code":xxxxxx,"time":300s},重新发就覆盖
     * @return returnValue 是一个json包含状态码和消息
     */



    public Map<String,Object> is_phone_used(String phone){
        QueryWrapper<UserLoginBean> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("phone_number").eq("phone_number", phone);
        UserLoginBean userLogindata = userLoginMapper.selectOne(queryWrapper);
        Map<String,Object> map = new HashMap<>();
        if(userLogindata == null){
            map.put("status",true);
            map.put("msg","phone not used");
        }
        else{
            map.put("status",false);
            map.put("msg","phone used");
        }
        return map;
    }

    public Map<String,Object> is_user_used(String username,String uuid){
        QueryWrapper<UserLoginBean> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username).or().eq("uuid",uuid);
        UserLoginBean userLogindata = userLoginMapper.selectOne(queryWrapper);
        Map<String,Object> map = new HashMap<>();
        if(userLogindata == null){
            map.put("status",true);
            map.put("msg","username not used");
        }
        else{
            map.put("status",false);
            map.put("msg","username used");
        }
        return map;
    }

    public Boolean isVerified(String user,String veri){
        try{
            String code = redisTemplate.opsForHash().entries(user).get("code").toString();
        }catch (NullPointerException e){
            e.printStackTrace();
            return false;
        }

        if(redisTemplate.opsForHash().entries(user)!=null && redisTemplate.opsForHash().entries(user).get("code").toString().equals(veri)){
            return true;
        }
        else return false;

    }
}
