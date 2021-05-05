package com.lbf.pack.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lbf.pack.beans.AdminBean;
import com.lbf.pack.mapper.AdminMapper;
import com.lbf.pack.service.AdminService;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, AdminBean> implements AdminService {
}
