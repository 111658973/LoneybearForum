package com.lbf.pack.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.lbf.pack.Util.PasswordEncoderUtil;
import com.lbf.pack.beans.UserLoginBean;
import com.lbf.pack.mapper.UserLoginMapper;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Api(tags = "重置密码")
@Controller
public class resetPassword {

    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    UserLoginMapper userLoginMapper;

    @GetMapping("/reset")
    public String show(){
        return "reset";
    }

    /**
     * 修改密码之前的验证
     * @param request 接收前端的formdata对象
     * @return
     */
    @PostMapping("/resetVerifying")
    @ResponseBody
    public Map<String,Object> ChangePasswordverify(MultipartHttpServletRequest request){
        Map<String,Object> returnValue = new HashMap<>();
        String username =request.getParameter("username");
//        String phoneNumber =request.getParameter("phoneNumber");  手机没有api暂时不用
//        String verifyCode =request.getParameter("verifycode_phone");
        String email =request.getParameter("email");
        String verifyCode =request.getParameter("verifycode_email");
        Boolean isVerified = isVerified(username, verifyCode);

        UserLoginBean result = userLoginMapper.selectOne(new QueryWrapper<UserLoginBean>().select("username","email").eq("username",username));
        try{

            /**
             *邮箱不正确
             */
            if(!result.getEmail().equalsIgnoreCase(email)){
                returnValue.put("code",0);
                returnValue.put("msg","邮箱不正确");
            }

            /**
             * 验证码不正确
             */

            else if(!isVerified){
                returnValue.put("code",-1);
                returnValue.put("msg","验证码不正确");
            }
            /**
             * 成功，更新数据库
             */
            else{
                returnValue.put("code",200);
                returnValue.put("msg","验证成功");
            }

        }catch (NullPointerException e){
            e.printStackTrace();
            returnValue.put("code",0);
            returnValue.put("msg","账号不存在");
        }


        return returnValue;
    }


    /**
     * 验证完了之后修改密码update
     * @param request
     * @return
     */
    @PostMapping("/resetVerified")
    @ResponseBody
    public Map<String,Object> ChangePassword(MultipartHttpServletRequest request){
        Map<String,Object> returnValue = new HashMap<>();
        String NewPassword = new PasswordEncoderUtil().encryptPasswrod(request.getParameter("newpassword"));
        String username = request.getParameter("username");
        String FomerPassword = userLoginMapper.selectOne(new QueryWrapper<UserLoginBean>().select("password").eq("username",username)).getPassword() ;

        UpdateWrapper<UserLoginBean> updateWrapper= new UpdateWrapper<>();
        updateWrapper.eq("username",username);

        UserLoginBean New = new UserLoginBean();

        /**
         * 和以前的密码一样
         */
        if(FomerPassword.equals(NewPassword)){
            returnValue.put("code",-1);
            returnValue.put("msg","和之前的密码一样，请重试");
        }
        else{


            New.setPassword(NewPassword);
            userLoginMapper.update(New,updateWrapper);
            returnValue.put("code",200);
            returnValue.put("msg","修改密码成功");
        }

        return returnValue;
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
