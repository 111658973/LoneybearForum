package com.lbf.pack.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.lbf.pack.beans.BlackListBean;
import com.lbf.pack.beans.UserFullDataBean;
import com.lbf.pack.beans.ZoneBean;
import com.lbf.pack.mapper.BlackListMapper;

import com.lbf.pack.mapper.UserDataMapper;
import com.lbf.pack.mapper.Zonemapper;
import com.lbf.pack.service.BlackListService;
import com.lbf.pack.service.QueryDataService;
import com.lbf.pack.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BlackListServiceImpl extends ServiceImpl<BlackListMapper, BlackListBean> implements BlackListService {
    @Autowired
    RedisService redisService;
    @Autowired
    BlackListMapper blackListMapper;
    @Autowired
    UserDataMapper userDataMapper;
    @Autowired
    QueryDataService queryDataService;
    @Autowired
    Zonemapper zonemapper;
    @Override
    public Map<String, Object> addNewUser(int uid,int zid,int OperatorId, String type, String reason, String time,long duration_minute) {
        Map<String,Object> returnJson = new HashMap<>();
        BlackListBean blackListBean = new BlackListBean();
        blackListBean.setUid(uid);
        blackListBean.setZid(zid);
        blackListBean.setStatus(type);
        blackListBean.setReason(reason);
        blackListBean.setOperatorId(OperatorId);
        blackListBean.setTime(time);
        blackListBean.setLastTime(duration_minute);

        try{
            BlackListBean query = blackListMapper.selectOne(new QueryWrapper<BlackListBean>().eq("uid",uid).eq("zid",zid));
            blackListMapper.update(blackListBean,new QueryWrapper<BlackListBean>().eq("uid",uid).eq("zid",zid));

            returnJson.put("code",200);
            returnJson.put("msg","添加黑名单成功");
        }catch (NullPointerException e){
            blackListMapper.insert(blackListBean);
            returnJson.put("code",201);
            returnJson.put("msg","添加黑名单成功");
            return returnJson;
        }



        return returnJson;

    }

    @Override
    public Map<String, Object> ReleaseUser(int uid, int zid) {
        Map<String,Object> returnJson = new HashMap<>();
        try{
            blackListMapper.delete(new QueryWrapper<BlackListBean>().eq("uid",uid).eq("zid",zid));
            returnJson.put("code",200);
            returnJson.put("msg","删除记录");
        }catch (NullPointerException e){
            e.printStackTrace();
            returnJson.put("code",-1);
            returnJson.put("msg","未找到用户记录");
        }
        return returnJson;
    }

    @Override
    public Map<String, Object> CompleteBan(int uid, String reason, long duration_minute) {
        return null;
    }

    @Override
    public List<Map<String,Object>> getAll() {
        List<String> black = redisService.scan("blacklist");
        List<Map<String, Object>> listByKeys = redisService.getListByKeys(black);

        return listByKeys;
    }

    @Override
    public Map<Object, Object> getByUid(long uid) {
        try{
            String username = userDataMapper.selectOne(new QueryWrapper<UserFullDataBean>().eq("uid",uid)).getUsername();
            Map<Object, Object> mapByName = redisService.getMapByName("blacklist-" + username);
            return  mapByName;
        }catch (NullPointerException e){
            e.printStackTrace();
            return null;
        }


    }

    @Override
    public List<Map<String, Object>> getALlByZname(String zname) {

        List<Map<String, Object>> all = getAll();
        try {
            all.removeIf(map -> !map.get("zname").equals(zname));
        }catch (NullPointerException e){
            e.printStackTrace();
            return  null;
        }
        return all;
    }


}
