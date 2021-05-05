package com.lbf.pack.serviceImpl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lbf.pack.beans.FloorBean;
import com.lbf.pack.beans.PostBean;
import com.lbf.pack.beans.UserFullDataBean;
import com.lbf.pack.beans.ZoneBean;
import com.lbf.pack.mapper.FloorMapper;
import com.lbf.pack.mapper.PostMapper;
import com.lbf.pack.mapper.UserDataMapper;
import com.lbf.pack.mapper.Zonemapper;
import com.lbf.pack.service.PostService;
import com.lbf.pack.service.QueryDataService;
import com.lbf.pack.service.UserDataService;
import com.lbf.pack.service.ZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class QueryDataServiceImpl implements QueryDataService {
    @Autowired
    UserDataMapper userDataMapper;
    @Autowired
    UserDataService userDataService;

    @Autowired
    Zonemapper zonemapper;

    @Autowired
    ZoneService zoneService;
    @Autowired
    PostMapper postMapper;
    @Autowired
    PostService postService;
    @Autowired
    FloorMapper floorMapper;
    @Autowired
    QueryDataService queryDataService;

    @Override
    public FloorBean GetFloorInfoById(long fid) {
        FloorBean f = floorMapper.selectOne(new QueryWrapper<FloorBean>().eq("fid", fid));
        f.setFloorReplysCount(floorMapper.selectCount(new QueryWrapper<FloorBean>().eq("pid",f.getPid()).eq("floor",f.getFloor()).gt("type",f.getType())));
        PostBean postBean = postMapper.selectOne(new QueryWrapper<PostBean>().eq("pid", f.getPid()));
        f.setTittle(postBean.getPtittle());
        f.setAuthor(userDataMapper.selectOne(new QueryWrapper<UserFullDataBean>().eq("uid",postBean.getUid())).getNick());
        ZoneBean zone = zonemapper.selectOne(new QueryWrapper<ZoneBean>().eq("zid", postBean.getZid()));
        f.setZname(zone.getZname());
        f.setZurl(zone.getZurl());
        return f;
    }

    @Override
    public List<Map<String, Object>> ParseToMapList(List objects) {
        List<Map<String, Object>> maplist = new ArrayList<>();
        for (Object o:objects){
            JSONObject object = JSONObject.parseObject(JSONObject.toJSONString(o));
            Map mapTypes = JSON.parseObject(object.toJSONString());
            maplist.add(mapTypes);
        }
        return maplist;
    }

    @Override
    public List<Map<String, Object>> getBlackList() {
        return null;
    }


    /**
     * 根绝id查分区内的帖子，还有帖子一楼的内容,查一页page为0
     * @param zid 分区id
     * @param Limit 每页数量
     * @param page 页码
     * @param order 排序方式，1是按时间，2是按热度
     * @return
     */
    @Override
    public List<PostBean> GetZonePostDataDescBy(long zid, int Limit, int page, int order) {
        String descOrder = null;
        switch (order){
            case 1:
                descOrder ="pcreate_time";
                break;
            case 2:
                descOrder = "plike_count - pdislike_count";
                break;
        }
        List<PostBean> PostsList = postMapper.selectList(new QueryWrapper<PostBean>().eq("zid",zid).orderByDesc(descOrder).last("Limit " +Limit*page+","+Limit).eq("status","1"));
        for(PostBean post:PostsList){
            long pid = post.getPid();
            post.setFirstFloor(floorMapper.selectOne(new QueryWrapper<FloorBean>().eq("pid",pid).eq("floor",1).eq("type",2)));
        }
        return PostsList;
    }


    @Override
    public Map<String, Object> GetPostCollectInfo(long pid, HttpSession session) {
        Map<String,Object> returnJson = new HashMap<>();
        boolean isPostCollected = false;
        long uid = queryDataService.GetUserUidFromSession(session);
        if(uid != -1){
            try{
                UserFullDataBean userdata= userDataMapper.selectOne(new QueryWrapper<UserFullDataBean>().select("favor_posts_id_list").eq("uid",uid));

                List<UserFullDataBean.favorPostsIdList> favorPostIdListBean = JSON.parseArray(userdata.getFavorPostsIdList().toString(), UserFullDataBean.favorPostsIdList.class);
                for(UserFullDataBean.favorPostsIdList favorPostId:favorPostIdListBean){
                    if(favorPostId.getPid()==pid){
                        isPostCollected = true;
                        break;
                    }
                }
                if(isPostCollected){
                    returnJson.put("isPostCillected",true);
                }
                else {
                    returnJson.put("isPostCillected",false);
                }
                returnJson.put("code",200);
                returnJson.put("msg","成功获取帖子关注信息");
            }catch (NullPointerException e){
                e.printStackTrace();
                returnJson.put("isPostCillected",false);
                returnJson.put("code",200);
                returnJson.put("msg","成功获取帖子关注信息");
            }

        }
        else{
            returnJson.put("code",-1);
            returnJson.put("msg","请先登陆");
        }
        return returnJson;
    }

    @Override
    public Map<String, Object> GetZoneFollowInfo(long zid, HttpSession session) {
        Map<String,Object> returnJson = new HashMap<>();
        boolean isZoneFollowed = false;
        long uid = queryDataService.GetUserUidFromSession(session);
        if(uid != -1){
            try {
                UserFullDataBean userdata= userDataMapper.selectOne(new QueryWrapper<UserFullDataBean>().select("favourite_zone_id_list").eq("uid",uid));
                List<UserFullDataBean.favouriteZoneIdList> favorZoneIdListBean = JSON.parseArray(userdata.getFavouriteZoneIdList().toString(), UserFullDataBean.favouriteZoneIdList.class);
                for(UserFullDataBean.favouriteZoneIdList favorZoneId:favorZoneIdListBean){
                    if(favorZoneId.getZid()==zid){
                        isZoneFollowed = true;
                        break;
                    }
                }
                if(isZoneFollowed){
                    returnJson.put("isZoneFollowed",true);
                }
                else {
                    returnJson.put("isZoneFollowed",false);
                }
                returnJson.put("code",200);
                returnJson.put("msg","成功获取分区关注信息");
            }catch (NullPointerException e){
                e.printStackTrace();
                returnJson.put("isZoneFollowed",false);
                returnJson.put("code",200);
                returnJson.put("msg","成功获取分区关注信息");
            }

        }
        else{
            returnJson.put("code",-1);
            returnJson.put("msg","请先登陆");
        }
        returnJson.put("isZoneFollowed",isZoneFollowed);
        return returnJson;
    }

    @Override
    public Map<String,Object> GetUserdataFromSession(HttpSession session,Boolean isPostsNeed) {
        List<Object> returnList= new ArrayList<>();
        Map<String,Object> returnJson= new HashMap<>();
        try{
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            UserFullDataBean userData = userDataMapper.selectOne(new QueryWrapper<UserFullDataBean>().select("*").eq("username",username));
            List<UserFullDataBean.favouriteZoneIdList> zids = new ArrayList<>();
            if(userData.getFavouriteZoneIdList()!=null) {
                zids = JSON.parseArray(userData.getFavouriteZoneIdList().toString(), UserFullDataBean.favouriteZoneIdList.class);
            }
            long UserUID = userData.getUid();

            /**
             * 根据favouriteZoneIdLists来获取关注的几个分区的信息
             * 放在左侧导航栏
             * @Param userloveZoneList里是查的用户喜欢的分区的Zonebean实体类
             */
            List<ZoneBean> userLoveZoneList = new ArrayList<>();
            for(UserFullDataBean.favouriteZoneIdList zid:zids) {
                ZoneBean z = zonemapper.selectOne(new QueryWrapper<ZoneBean>().select("*").eq("zid",zid.getZid()));
                userLoveZoneList.add(z);
            }



            /**
             * 根据用户名查到用户发的帖子
             */
            if(isPostsNeed) {
                List<PostBean> UserPosts = postMapper.selectList(new QueryWrapper<PostBean>().select("*").eq("uid", UserUID).eq("status","1"));
                for(PostBean post:UserPosts){
                    post.setFloorCount(queryDataService.GetPostFloorConoutByPid(post.getPid()));
                    post.setAuthorData(queryDataService.GetUserdataByUid(post.getUid()));
                    post.setFirstFloor(queryDataService.GetFloorContent(post.getPid(),1).get(0));
                    post.setPostZoneData(queryDataService.GetZonesDataByZid(post.getZid()));
                }
                returnJson.put("user_posts",UserPosts);
            }

            returnJson.put("data",userData);
            returnJson.put("user_favourite_zones",userLoveZoneList);
            returnJson.put("msg","获取用户信息成功");
            returnJson.put("code",200);
            return returnJson;
        }catch (NullPointerException e){
            e.printStackTrace();
            returnJson.put("msg","请登录");
            returnJson.put("code",-1);
            return returnJson;
        }



    }


    @Override
    public Map<String, Object> getUserPageDataByUid(Long uid) {
        List<Object> returnList = new ArrayList<>();
        Map<String, Object> returnJson = new HashMap<>();
        try {
            String username = userDataMapper.selectOne(new QueryWrapper<UserFullDataBean>().eq("uid", uid)).getUsername();
            UserFullDataBean userData = userDataMapper.selectOne(new QueryWrapper<UserFullDataBean>().select("*").eq("username", username));
            List<UserFullDataBean.favouriteZoneIdList> zids = new ArrayList<>();
            if (userData.getFavouriteZoneIdList() != null) {
                zids = JSON.parseArray(userData.getFavouriteZoneIdList().toString(), UserFullDataBean.favouriteZoneIdList.class);
            }
            long UserUID = userData.getUid();

            /**
             * 根据favouriteZoneIdLists来获取关注的几个分区的信息
             * 放在左侧导航栏
             * @Param userloveZoneList里是查的用户喜欢的分区的Zonebean实体类
             */
            List<ZoneBean> userLoveZoneList = new ArrayList<>();
            for (UserFullDataBean.favouriteZoneIdList zid : zids) {
                ZoneBean z = zonemapper.selectOne(new QueryWrapper<ZoneBean>().select("*").eq("zid", zid.getZid()));
                userLoveZoneList.add(z);
            }

            /**
             * 根据用户名查到用户发的帖子
             */
            List<PostBean> UserPosts = postMapper.selectList(new QueryWrapper<PostBean>().select("*").eq("uid", UserUID).eq("status", "1"));
            for (PostBean post : UserPosts) {
                post.setFloorCount(queryDataService.GetPostFloorConoutByPid(post.getPid()));
                post.setAuthorData(queryDataService.GetUserdataByUid(post.getUid()));
                post.setFirstFloor(queryDataService.GetFloorContent(post.getPid(), 1).get(0));
                post.setPostZoneData(queryDataService.GetZonesDataByZid(post.getZid()));
            }
            returnJson.put("user_posts", UserPosts);
            returnJson.put("data", userData);
            returnJson.put("user_favourite_zones", userLoveZoneList);
            returnJson.put("msg", "获取用户信息成功");
            returnJson.put("code", 200);
            return returnJson;

        } catch (NullPointerException e) {
            e.printStackTrace();
            returnJson.put("msg", "未找到用户");
            returnJson.put("code", -1);
            return returnJson;
        }
    }

    @Override
    public List<ZoneBean> GetUserFavouriteZonesData(int uid) {
        List<UserFullDataBean.favouriteZoneIdList> zids =new ArrayList<>();
        UserFullDataBean userData = userDataMapper.selectOne(new QueryWrapper<UserFullDataBean>().select("favourite_zone_id_list").eq("uid",uid));
        if(userData.getFavorPostsIdList()!=null){
            zids = JSON.parseArray(userData.getFavouriteZoneIdList().toString(), UserFullDataBean.favouriteZoneIdList.class);
        }
        else{
            zids = null;
        }
        List<ZoneBean> userLoveZoneList = new ArrayList<>();
        if(zids!=null) {
            for (UserFullDataBean.favouriteZoneIdList zid : zids) {
                ZoneBean z = zonemapper.selectOne(new QueryWrapper<ZoneBean>().select("*").eq("zid", zid.getZid()));
                userLoveZoneList.add(z);
            }
        }
        return userLoveZoneList;
    }

    @Override
    public List<ZoneBean> GetHotZones(int index) {
        List<ZoneBean> like_zones = zonemapper.selectList(new QueryWrapper<ZoneBean>().select("*").orderByDesc("like_number").last("Limit " + index));
        for(ZoneBean z:like_zones){
            z.setPostcount(postMapper.selectCount(new QueryWrapper<PostBean>().eq("zid",z.getZid()).eq("status","1")));
        }
        return like_zones;
    }


    @Override
    public List<FloorBean> GetFloorContent(long pid,int Floor) {
        return floorMapper.selectList(new QueryWrapper<FloorBean>().select("*").orderByAsc("type").eq("floor",Floor).eq("pid",pid));
    }

    @Override
    public UserFullDataBean GetBriefUserData(int uid) {
        return null;
    }


    @Override
    public Map<String, Object> GetZonesDataByZname(String zonename) {
        Map<String, Object> ReturnValue = new HashMap<>();
        ZoneBean ZoneData = zonemapper.selectOne(new QueryWrapper<ZoneBean>().select("*").eq("zurl",zonename));
        int ZonePostCount = postMapper.selectCount(new QueryWrapper<PostBean>().eq("zid",ZoneData.getZid()).eq("status","1"));
        ZoneData.setPostcount(ZonePostCount);
        ReturnValue.put("ZoneData",ZoneData);
        return ReturnValue;
    }


    public ZoneBean GetZonesDataByZid(long zoneId) {
        try{
            ZoneBean ZoneData = zonemapper.selectOne(new QueryWrapper<ZoneBean>().select("*").eq("zid",zoneId));
            int ZonePostCount = postMapper.selectCount(new QueryWrapper<PostBean>().eq("zid",zoneId).eq("status","1"));
            ZoneData.setPostcount(ZonePostCount);
            return ZoneData;
        }catch (NullPointerException e){
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public int GetPostFloorConoutByPid(long pid) {
        return floorMapper.selectCount(new QueryWrapper<FloorBean>().eq("pid",pid));
    }

    @Override
    public List<PostBean> GetZoneSortedPosts(long zid,int type, int size, int page) {
        String sort;
        switch (type){
            case 1:
                sort = "(plike_count-pdislike_count)";
                break;
            case 2:
                sort = "pcreate_time";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
        try{
            List<PostBean> PostsList=postMapper.selectList(new QueryWrapper<PostBean>().select("*").eq("zid",zid).orderByDesc(sort).eq("status","1").last("Limit " +size*page+","+size));
            List list = new ArrayList();
            for(PostBean post:PostsList){
                  list.add(queryDataService.GetPostInfo(post.getPid())) ;
//                post.setFloorCount(queryDataService.GetPostFloorConoutByPid(post.getPid()));
//                post.setAuthorData(queryDataService.GetUserdataByUid(post.getUid()));
//                post.setFirstFloor(queryDataService.GetFloorContent(post.getPid(),1).get(0));
//                post.setPostZoneData(queryDataService.GetZonesDataByZid(post.getZid()));

            }
            return list;
//            return PostsList;
        }catch (NullPointerException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<PostBean> GetHotPostsDataLimit(int size,int page) {
        try {
            List<PostBean> PostsList=postMapper.selectList(new QueryWrapper<PostBean>().select("*").orderByDesc("(plike_count-pdislike_count)").eq("status","1").last("Limit " +size*page+","+size));
            for(PostBean post:PostsList ){
                post.setFloorCount(queryDataService.GetPostFloorConoutByPid(post.getPid()));
                post.setAuthorData(queryDataService.GetUserdataByUid(post.getUid()));
                post.setFirstFloor(queryDataService.GetFloorContent(post.getPid(),1).get(0));
                post.setPostZoneData(queryDataService.GetZonesDataByZid(post.getZid()));
            }
            return PostsList;
        }catch (NullPointerException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<PostBean> GetNewestPostDataLimit(int size,int page) {
        try {
            List<PostBean> HotPostList =   postMapper.selectList(new QueryWrapper<PostBean>().select("*").orderByDesc("pcreate_time").eq("status","1").last("Limit " +size*page+","+size));
            List list = new ArrayList();
            for(PostBean post:HotPostList ){
                list.add(queryDataService.GetPostInfo(post.getPid()));
            }
            return list;
        }catch (NullPointerException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public UserFullDataBean GetUserdataByUid(long uid) {
        try{
            return userDataMapper.selectOne(new QueryWrapper<UserFullDataBean>().eq("uid",uid));

        }catch (NullPointerException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public PostBean GetPostInfo(long Pid) {
        PostBean PostInfo = postMapper.selectOne(new QueryWrapper<PostBean>().select("*").eq("pid", Pid));
        int PostFloorsCount = floorMapper.selectCount(new QueryWrapper<FloorBean>().eq("pid",Pid));
        UserFullDataBean Author = userDataMapper.selectOne(new QueryWrapper<UserFullDataBean>().select("*").eq("uid",PostInfo.getUid()));

        PostInfo.setFirstFloor(queryDataService.GetFloorContent(Pid,1).get(0));
        PostInfo.setPostZoneData(queryDataService.GetZonesDataByZid(PostInfo.getZid()));
        PostInfo.setFloorCount(PostFloorsCount);
        PostInfo.setAuthorData(Author);
        return PostInfo;
    }

    @Override
    public List<FloorBean> GetFloorList(long pid) {
        return floorMapper.selectList(new QueryWrapper<FloorBean>().select("*").eq("pid",pid));
    }

    @Override
    public long GetMaxPostCount() {
        return postMapper.selectOne(new QueryWrapper<PostBean>().select("pid").orderByDesc("pid").last("Limit 1")).getPid();
    }

    @Override
    public long GetZidByName(String zname) {
        try {
            return zonemapper.selectOne(new QueryWrapper<ZoneBean>().select("zid").eq("zurl",zname)).getZid();

        }catch (NullPointerException e){
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public long GetUserUidFromSession(HttpSession session) {
        long uid;
        try{
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            UserFullDataBean user = userDataMapper.selectOne(new QueryWrapper<UserFullDataBean>().select("uid").eq("username", username));
            uid = user.getUid();
            return uid;
        }catch (NullPointerException e){
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public List<Map<String, Object>> GetFormattedFloors(long pid) {
        List<Map<String, Object>> returnList = new ArrayList<>();
        int MaxFloor = 0;
        //楼层列表非空时
        try {
            List<FloorBean> FloorsList = GetFloorList(pid);

            //获取楼层最大值
            MaxFloor = floorMapper.selectOne(new QueryWrapper<FloorBean>().select("floor").orderByDesc("floor").eq("pid",pid).last("limit 1")).getFloor();

            //每层数据打包结构如下
            //"floori": {
            //    "userdata": {},
            //    "content": "{<html>} ",
            //    "replys": {
            //      "reply1": {
            //        "uerdata":{},
            //        "content": {}
            //      },
            //      "reply2": {
            //        "uerdata":{},
            //        "content": {}
            //      }
            //      ......
            //    }
            //
            //  }
            for(int i=1;i<=MaxFloor;i++){
                Map<String,Object> floor = new HashMap<>();
                List<Map<String,Object>> replys = new ArrayList<>();

                //是按type降序排列的，第一个就是楼层回复，其他的是楼中楼
                List<FloorBean> currentFloor = GetFloorContent(pid,i);

                floor.put("floor_index",i);
                floor.put("userdata",userDataMapper.selectOne(new QueryWrapper<UserFullDataBean>().select("nick,icon_url,sex,level,uintroduction,uid").eq("uid",currentFloor.get(0).getUid())));
                floor.put("content",currentFloor.get(0).getContent());
                floor.put("ftime",currentFloor.get(0).getFtime());
                floor.put("replys",replys);

                for(int j =1;j<currentFloor.size();j++){
                    Map<String,Object> reply = new HashMap<>();
                    reply.put("reply_index",j);
                    reply.put("userdata",userDataMapper.selectOne(new QueryWrapper<UserFullDataBean>().select("nick,icon_url,sex,level,uintroduction,uid").eq("uid",currentFloor.get(j).getUid())));
                    reply.put("content",currentFloor.get(j).getContent());
                    reply.put("ftime",currentFloor.get(j).getFtime());
                    replys.add(reply);
                }




                returnList.add(floor);
            }

        }catch (NullPointerException e){
            e.printStackTrace();
            returnList = null;

        }

        return returnList;
    }



    @Override
    public List<Map<String, Object>> GetFormattedFloorByIndex(long pid,int index) {
        Map<String, Object> stringObjectMap = GetFormattedFloors(pid).get(index-1);
        List<Map<String, Object>> list = new ArrayList<>();
        list.add(stringObjectMap);
        return list;
    }

    @Override
    public List<Map<String, Object>> GetFormattedRest(long pid) {
        List<Map<String, Object>> list = GetFormattedFloors(pid);
        list.remove(0);
        return list;
    }


    @Override
    public boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    @Override
    public List<UserFullDataBean> SimpleSearchUserByUidOrNick(String param) {
        Object[] array = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toArray();
        String authority = array[0].toString();
        List<UserFullDataBean> list = new ArrayList<>();
        long uid = -1;

        if(isInteger(param)){
            try{
                UserFullDataBean UserByUid = userDataMapper.selectOne(new QueryWrapper<UserFullDataBean>().eq("uid", param).lt("authority",authority));
                uid=UserByUid.getUid();
                list.add(UserByUid);
            }
           catch (NullPointerException e){
                e.printStackTrace();
               try {
                   List<UserFullDataBean> beanList = userDataMapper.selectList(new QueryWrapper<UserFullDataBean>().like("nick", param).ne("uid", uid).lt("authority",authority));
                   for(UserFullDataBean b:beanList){
                       list.add(b);
                   }
               }catch (NullPointerException f){
                   f.printStackTrace();
                   return null;
               }
           }
        }

        else {
            try {
                List<UserFullDataBean> beanList = userDataMapper.selectList(new QueryWrapper<UserFullDataBean>().like("nick", param).ne("uid", uid).lt("authority",authority));
                for (UserFullDataBean b : beanList) {
                    list.add(b);
                }
            } catch (NullPointerException f) {
                f.printStackTrace();
                return null;
            }

        }
        return list;
    }

    @Override
    public List<PostBean> SimpleSearchPostByUidOrTittle(String param, String zonename) {
        List<PostBean> list = new ArrayList<>();
        long pid= -1;
        long zid = queryDataService.GetZidByName(zonename);
        if(isInteger(param)){
            try{
                PostBean PostByPid = postMapper.selectOne(new QueryWrapper<PostBean>().eq("pid", param).eq("zid",zid).eq("status","1"));
                pid=PostByPid.getPid();
                list.add(PostByPid);
            }
            catch (NullPointerException e){
                e.printStackTrace();
                try {
                    List<PostBean> beanList = postMapper.selectList(new QueryWrapper<PostBean>().like("ptittle", param).ne("pid", pid).eq("zid",zid).eq("status","1"));
                    for(PostBean b:beanList){
                        list.add(b);
                    }
                }catch (NullPointerException f){
                    f.printStackTrace();
                    return null;
                }
            }
        }

        else {
            try {
                List<PostBean> beanList = postMapper.selectList(new QueryWrapper<PostBean>().like("ptittle", param).ne("pid", pid).eq("zid",zid).eq("status","1"));
                for (PostBean b : beanList) {
                    list.add(b);
                }
            } catch (NullPointerException f) {
                f.printStackTrace();
                return null;
            }

        }
        ArrayList<PostBean> list2 = new ArrayList();
        for(PostBean l:list){
            list2.add(queryDataService.GetPostInfo(l.getPid()));
        }
        return list2;
    }

    @Override
    public List<PostBean> getGoodPostListByZid(long zid) {
        ArrayList<PostBean> goodList = new ArrayList<>();
        try {
            List<PostBean> postBeans = postMapper.selectList(new QueryWrapper<PostBean>().eq("zid", zid).eq("is_good", true).eq("enabled",true));
            for(PostBean p:postBeans){
                goodList.add(queryDataService.GetPostInfo(p.getPid()));
            }
        }catch (NullPointerException e){
            e.printStackTrace();
            return null;
        }
        return goodList;
    }

    @Override
    public long getUidByUsername(String username) {
        try {
            userDataMapper.selectOne(new QueryWrapper<UserFullDataBean>().eq("username", username)).getUid();
            return userDataMapper.selectOne(new QueryWrapper<UserFullDataBean>().eq("username",username)).getUid();

        }catch (NullPointerException e){
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public Long getCuerrentUid() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return userDataMapper.selectOne(new QueryWrapper<UserFullDataBean>().eq("username", name)).getUid();
    }
}
