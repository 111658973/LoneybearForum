package com.lbf.pack.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.lbf.pack.beans.UserFullDataBean;
import com.lbf.pack.mapper.UserDataMapper;

import com.lbf.pack.service.UserDataService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDataServiceImpl extends ServiceImpl<UserDataMapper, UserFullDataBean> implements UserDataService {
    @Autowired
    UserDataMapper userDataMapper;
    @Override
    public List<UserFullDataBean> GetUserDataAll() {
        return userDataMapper.selectList(new QueryWrapper<UserFullDataBean>());
    }
}
