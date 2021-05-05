package com.lbf.pack.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lbf.pack.beans.BlackListBean;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface BlackListService extends IService<BlackListBean> {
    List<Map<String,Object>> getAll();

    Map<Object,Object> getByUid(long uid);

    List<Map<String, Object>> getALlByZname(String zname);

    Map<String,Object> addNewUser(int uid,int zid,int OperatorId, String type, String reason, String time,long duration_minute);

    Map<String,Object> ReleaseUser(int uid,int zid);

    Map<String,Object> CompleteBan(int uid,String reason,long duration_minute);


}
