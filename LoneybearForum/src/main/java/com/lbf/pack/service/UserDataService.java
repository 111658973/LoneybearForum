package com.lbf.pack.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lbf.pack.beans.UserFullDataBean;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserDataService extends IService<UserFullDataBean> {
    public List<UserFullDataBean> GetUserDataAll();
}
