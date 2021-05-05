package com.lbf.pack.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lbf.pack.beans.UserEditDataBean;

import com.lbf.pack.mapper.UserEditMapper;
import com.lbf.pack.service.UserEditService;
import org.springframework.stereotype.Service;

@Service
public class UserEditServiceImpl extends ServiceImpl<UserEditMapper, UserEditDataBean> implements UserEditService {
}
