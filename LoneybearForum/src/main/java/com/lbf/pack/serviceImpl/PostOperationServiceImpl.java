package com.lbf.pack.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lbf.pack.beans.PostOperationBean;
import com.lbf.pack.mapper.PostOperationMapper;
import com.lbf.pack.service.PostOperationService;
import org.springframework.stereotype.Service;

@Service
public class PostOperationServiceImpl extends ServiceImpl<PostOperationMapper,PostOperationBean> implements PostOperationService {
}
