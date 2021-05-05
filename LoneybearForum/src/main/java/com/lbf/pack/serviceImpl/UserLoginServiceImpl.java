package com.lbf.pack.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lbf.pack.beans.UserLoginBean;
import com.lbf.pack.service.UserLoginService;
import org.springframework.stereotype.Service;
import com.lbf.pack.mapper.UserLoginMapper;

@Service
public class UserLoginServiceImpl extends ServiceImpl<UserLoginMapper, UserLoginBean> implements UserLoginService {


}
