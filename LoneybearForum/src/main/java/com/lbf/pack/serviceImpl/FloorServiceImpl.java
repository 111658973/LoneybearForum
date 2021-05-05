package com.lbf.pack.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.lbf.pack.beans.FloorBean;
import com.lbf.pack.mapper.FloorMapper;
import com.lbf.pack.service.FloorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FloorServiceImpl extends ServiceImpl<FloorMapper, FloorBean> implements FloorService {
    @Autowired
    FloorMapper floorMapper;
    @Override
    public List<FloorBean> GetFloorListAll() {
        return floorMapper.selectList(new QueryWrapper<FloorBean>());
    }
}
