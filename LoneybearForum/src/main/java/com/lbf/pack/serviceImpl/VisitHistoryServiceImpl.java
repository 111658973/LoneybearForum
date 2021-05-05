package com.lbf.pack.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lbf.pack.beans.PostBean;
import com.lbf.pack.beans.ResponseBean;
import com.lbf.pack.beans.VisitHistoryBean;
import com.lbf.pack.mapper.VisitHistoryMapper;
import com.lbf.pack.service.QueryDataService;
import com.lbf.pack.service.VisitHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VisitHistoryServiceImpl extends ServiceImpl<VisitHistoryMapper,VisitHistoryBean> implements VisitHistoryService {
    @Autowired
    VisitHistoryMapper visitHistoryMapper;
    @Autowired
    QueryDataService queryDataService;

    public static boolean isNumeric(String str){
        for (int i = str.length();--i>=0;){
            if (!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }



    @Override
    public List<PostBean> getPostHistoryByUid(long uid) {
        List<VisitHistoryBean> visithistory = visitHistoryMapper.selectList(new QueryWrapper<VisitHistoryBean>().eq("uid",uid).like("url","/post/").orderByDesc("last_visit_time"));
        List<Integer> pids = new ArrayList<>();
        for(VisitHistoryBean v:visithistory){
            String s = v.getUrl().split("/")[2];
            if(isNumeric(s)){
                pids.add(Integer.parseInt(s));
            }
        }
        List<PostBean> posts = new ArrayList<>();
        for(int pid:pids){
            posts.add(queryDataService.GetPostInfo(pid));
        }
        return posts;
    }

    @Override
    public ResponseBean insert(VisitHistoryBean visitHistoryBean) {
        try{
            QueryWrapper<VisitHistoryBean> eq = new QueryWrapper<VisitHistoryBean>().eq("uid", visitHistoryBean.getUid()).eq("url", visitHistoryBean.getUrl()).eq("method", visitHistoryBean.getMethod());
            VisitHistoryBean query = visitHistoryMapper.selectOne(eq);
            visitHistoryBean.setTimes(query.getTimes()+1);
//            visitHistoryBean.setVid(query.getVid());
            visitHistoryMapper.update(visitHistoryBean,eq);
            return new ResponseBean(200,"操作成功","更新");
        }catch (NullPointerException e){
            visitHistoryBean.setTimes(1);
            visitHistoryMapper.insert(visitHistoryBean);
            return new ResponseBean(200,"操作成功","插入");
        }
    }

    @Override
    public List<VisitHistoryBean> getAllByUid(int uid) {
        return visitHistoryMapper.selectList(new QueryWrapper<VisitHistoryBean>().eq("uid",uid));
    }
}
