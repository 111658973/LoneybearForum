package com.lbf.pack.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lbf.pack.beans.FloorBean;
import com.lbf.pack.beans.PostBean;
import com.lbf.pack.beans.ZoneBean;
import com.lbf.pack.mapper.FloorMapper;
import com.lbf.pack.mapper.PostMapper;
import com.lbf.pack.mapper.Zonemapper;

import com.lbf.pack.service.ZoneService;
import io.swagger.annotations.Api;
import javafx.geometry.Pos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZoneServiceImpl extends ServiceImpl<Zonemapper, ZoneBean> implements ZoneService {
    @Autowired
    Zonemapper zonemapper;
    @Autowired
    PostMapper postMapper;
    @Override
    public List<ZoneBean> GetZoneDataAll() {
        try {
            List<ZoneBean> zoneBeans = zonemapper.selectList(new QueryWrapper<ZoneBean>());
            for(ZoneBean z:zoneBeans){
                z.setPostcount(postMapper.selectCount(new QueryWrapper<PostBean>().eq("zid",z.getZid())));
            }
            return zoneBeans;
        }catch (NullPointerException e){
            e.printStackTrace();
            return null;
        }
    }
}
