package com.lbf.pack.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lbf.pack.beans.PostBean;
import com.lbf.pack.beans.ResponseBean;
import com.lbf.pack.beans.VisitHistoryBean;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface VisitHistoryService extends IService<VisitHistoryBean> {
    public ResponseBean insert(VisitHistoryBean visitHistoryBean);

    public List<VisitHistoryBean> getAllByUid(int uid);

    public List<PostBean> getPostHistoryByUid(long uid);
}
