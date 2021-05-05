package com.lbf.pack.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lbf.pack.beans.UserFullDataBean;
import com.lbf.pack.beans.UserLoginBean;
import com.lbf.pack.mapper.UserDataMapper;
import com.lbf.pack.mapper.UserLoginMapper;
import com.lbf.pack.service.BlackListService;
import com.lbf.pack.service.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Permission;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.alibaba.fastjson.JSON.parseObject;

@Service
public class CustomUserDetailServiceImpl implements CustomUserDetailService {
    @Autowired
    UserLoginMapper userLoginMapper;
    @Autowired
    BlackListService blackListService;
    @Autowired
    UserDataMapper userDataMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        GrantedAuthority DEFAULT_ROLE = new SimpleGrantedAuthority("1");
        try{
            UserLoginBean user = userLoginMapper.selectOne(new QueryWrapper<UserLoginBean>().eq("username", username));
            String authority = user.getAuthority();
            List<GrantedAuthority> authorities=new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(authority));

            Map<Object, Object> black = blackListService.getByUid(userDataMapper.selectOne(new QueryWrapper<UserFullDataBean>().eq("username", user.getUsername())).getUid());
            if(black.size()!=0 && black.get("deal").equals("封禁")){
                throw new AuthenticationServiceException("用户已封禁");
            }

            user.setAuthorities(authorities);

            return user;
        }catch (NullPointerException e){
            e.printStackTrace();
            Map<String,Object> returnJson = new HashMap<>();
            returnJson.put("code",0);
            returnJson.put("msg","用户不存在");
            String s = JSONObject.toJSONString(returnJson);
            throw new AuthenticationServiceException("用户不存在");
        }

    }
}
