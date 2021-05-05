package com.lbf.pack.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lbf.pack.beans.ZoneBean;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ZoneService extends IService<ZoneBean> {
    public List<ZoneBean> GetZoneDataAll();
}
