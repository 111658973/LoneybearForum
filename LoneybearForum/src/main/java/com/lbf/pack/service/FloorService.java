package com.lbf.pack.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lbf.pack.beans.FloorBean;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FloorService extends IService<FloorBean> {
    public List<FloorBean> GetFloorListAll();
}
