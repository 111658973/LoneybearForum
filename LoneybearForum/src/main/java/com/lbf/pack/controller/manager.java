package com.lbf.pack.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lbf.pack.Util.TimeUtil;
import com.lbf.pack.beans.*;
import com.lbf.pack.mapper.*;
import com.lbf.pack.service.FileHandlerService;
import com.lbf.pack.service.PostService;
import com.lbf.pack.service.QueryDataService;
import com.lbf.pack.service.RedisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "管理员功能")
@Controller
public class manager {
    @Autowired
    PostService postService;
    @Autowired
    PostMapper postMapper;
    @Autowired
    UserDataMapper userDataMapper;
    @Autowired
    QueryDataService queryDataService;
    @Autowired
    Zonemapper zonemapper;
    @Autowired
    FileHandlerService fileHandlerService;
    @Autowired
    PostRecycleMapper postRecycleMapper;
    @Autowired
    RedisService redisService;
    @Autowired
    AdminMapper adminMapper;

    @ApiOperation(value = "帖子回收站")
    @GetMapping("/zoneadmin/postcycle")
    public String showDeletePostCyclePage(){
        return "postCycle";


    }

    @ApiOperation(value = "删帖,加入回收站")
    @PostMapping("/zoneadmin/tocycle")
    @ResponseBody
    public Map<String,Object> DeletePost(){
        Map<String,Object> returnJson = new HashMap<>();
        return returnJson;
    }

    /**
     * 帖子加精或者取消加精
     * @return 状态码code和消息msg
     */
    @ApiOperation("加精")
    @PostMapping("/zoneadmin/addgood")
    @ResponseBody
    public Map<String,Object> GoodHandle(MultipartHttpServletRequest request){
        PostBean post =  new PostBean();
        Map<String,Object> returnJson = new HashMap<>();
        String hint;
        int op = Integer.parseInt(request.getParameter("op"));
        String checked = request.getParameter("checked");
        JSONArray objects = JSONArray.parseArray(checked);
        List<PostBean> PostBeans = JSON.parseArray(checked, PostBean.class);


        switch (op){
            case 1:
                for(PostBean p:PostBeans){
                    p.setIsGood(true);
                    postMapper.update(p,new QueryWrapper<PostBean>().eq("pid",p.getPid()));
                }

                hint = "加精";

                break;
            case 2:
                for(PostBean p:PostBeans){
                    p.setIsGood(false);
                }
                hint = "取消加精";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + op);
        }

        returnJson.put("code",200);
        returnJson.put("msg",hint+"成功");
        return returnJson;
    }

    @ApiOperation("取消加精")
    @PostMapping("/zoneadmin/removegood")
    @ResponseBody
    public ResponseBean cancelGood(MultipartHttpServletRequest request){
        String row = request.getParameter("row");
        String zname = request.getParameter("zname");
        long zid = queryDataService.GetZidByName(zname);
        JSONObject obj = JSONObject.parseObject(row);
        int pid = Integer.parseInt(obj.get("pid").toString());

        PostBean postBean = postMapper.selectOne(new QueryWrapper<PostBean>().eq("pid",pid));

        postBean.setIsGood(false);

        postMapper.update(postBean,new QueryWrapper<PostBean>().eq("pid",pid));

        return new ResponseBean(200,"取消加精成功",null);
    }

    @ApiOperation("加黑名单")
    @PostMapping("/zoneadmin/addblacklist")
    @ResponseBody
    public Map<String,Object> BlackList(MultipartHttpServletRequest request,Authentication authentication){
        String check = request.getParameter("check");
        String deal = request.getParameter("deal");
        String time_minute = request.getParameter("time");
        String zname = request.getParameter("zname");
        long duration = 0;
        if(!time_minute.equals("永久")){
            duration = Long.parseLong(time_minute);
        }


        String reason = request.getParameter("reason");
        List<UserFullDataBean> Users = JSON.parseArray(check, UserFullDataBean.class);
        String Operator = userDataMapper.selectOne(new QueryWrapper<UserFullDataBean>().eq("username",authentication.getName())).getNick();
        for(UserFullDataBean user:Users){
            Map<String,Object> hash = new HashMap<>();
            hash.put("uid",user.getUid()+"");
            hash.put("username",user.getUsername());
            hash.put("deal",deal);
            hash.put("reason",reason);
            hash.put("operator",Operator);
            hash.put("time",new TimeUtil().getCuerrent_time());
            hash.put("duration", duration +"分钟");
            hash.put("zname",zname);

            switch (deal){
                case "禁言":
                    UserFullDataBean u1 = userDataMapper.selectOne(new QueryWrapper<UserFullDataBean>().eq("uid", user.getUid()));
                    u1.setAuthority(-1);
                    break;
                case "封禁":
                    UserFullDataBean u2 = userDataMapper.selectOne(new QueryWrapper<UserFullDataBean>().eq("uid", user.getUid()));
                    u2.setAuthority(-2);
                    break;
            }
            redisService.insertMap(2,"blacklist-"+user.getUsername(),hash,duration);

        }

        return null;
    }

    @ApiOperation("移除黑名单")
    @PostMapping("/zoneadmin/removeblacklist")
    @ResponseBody
    public ResponseBean removeBlciklist(MultipartHttpServletRequest request){
        String row = request.getParameter("row");
        String zname = request.getParameter("zname");
        long zid = queryDataService.GetZidByName(zname);
        JSONObject obj = JSONObject.parseObject(row);
        String username = obj.get("username").toString();
        try{
            redisService.deleteByName("blacklist-"+username);
        }catch (NullPointerException e){
            e.printStackTrace();
            return  new ResponseBean(-1,"操作失败","未找到数据");
        }



        return new ResponseBean(200,"移除黑名单成功",null);
    }

    @ApiOperation("编辑黑名单")
    @PostMapping("/zoneadmin/editblacklist")
    @ResponseBody
    public ResponseBean editBlciklist(MultipartHttpServletRequest request){
        String username = request.getParameter("username");
        long uid = userDataMapper.selectOne(new QueryWrapper<UserFullDataBean>().eq("username",username)).getUid();
        String zname = request.getParameter("zname");
        String deal = request.getParameter("deal");
        int duration =Integer.parseInt(request.getParameter("duration"));
        String reason = request.getParameter("reason");
        String operator = SecurityContextHolder.getContext().getAuthentication().getName();
        String time = new TimeUtil().getCuerrent_time();
        HashMap<String, String> map = new HashMap<>();
        map.put("duration", ""+ duration);
        map.put("uid",""+uid);
        map.put("reason",reason);
        map.put("deal",deal);
        map.put("operator",operator);
        map.put("time",time);
        map.put("username",username);
        map.put("zname",zname);

        redisService.insertMap("blacklist-"+username,map,duration);

        long zid = queryDataService.GetZidByName(zname);




        return new ResponseBean(200,"更新黑名单成功",null);
    }


    @ApiOperation("加吧主")
    @PostMapping("/senior/addzonemananger")
    @ResponseBody
    public Map<String,Object> addzonemananger(MultipartHttpServletRequest request,HttpSession session){
        Map<String,Object> returnJson = new HashMap<>();
        String checked = request.getParameter("checked");
        JSONArray objects = JSONArray.parseArray(checked);
        List<UserFullDataBean> UserBeans = JSON.parseArray(checked, UserFullDataBean.class);

        long zid = queryDataService.GetZidByName(request.getParameter("zname"));
        ZoneBean zone = zonemapper.selectOne(new QueryWrapper<ZoneBean>().eq("zid",zid));
        List<ZoneBean.ZoneManagerRelation> zmanagerId = JSON.parseArray(zone.getZmanagerId().toString(),ZoneBean.ZoneManagerRelation.class);
        for(UserFullDataBean u:UserBeans){
            long uid = u.getUid();
            boolean isStored = false;
            for(ZoneBean.ZoneManagerRelation z:zmanagerId){
                if(z.getUid() == uid){
                    isStored = true;
                    break;
                }
            }

            if(!isStored){
                long uid1 = u.getUid();
                zmanagerId.add(new ZoneBean.ZoneManagerRelation(uid1));
                UserFullDataBean user = userDataMapper.selectOne(new QueryWrapper<UserFullDataBean>().eq("uid",uid1));
                user.setAuthority(2);
                userDataMapper.update(user,new QueryWrapper<UserFullDataBean>().eq("uid",uid1));

                try {
                    AdminBean admin = adminMapper.selectOne(new QueryWrapper<AdminBean>().eq("uid",uid1).eq("zid",zid));
                    admin.setAuthority(2);
                    admin.setUid(uid1);
                    admin.setOperator(SecurityContextHolder.getContext().getAuthentication().getName());
                    admin.setZid(zid);
                    admin.setTime(new TimeUtil().getCuerrent_time());
                    adminMapper.update(admin,new QueryWrapper<AdminBean>().eq("uid",uid1).eq("zid",zid));
                }catch (NullPointerException e){
                    AdminBean admin = new AdminBean();
                    admin.setAuthority(2);
                    admin.setUid(uid1);
                    admin.setOperator(SecurityContextHolder.getContext().getAuthentication().getName());
                    admin.setZid(zid);
                    admin.setTime(new TimeUtil().getCuerrent_time());
                    adminMapper.insert(admin);
                }

            }
        }
        zone.setZmanagerId(zmanagerId);
        zonemapper.update(zone,new QueryWrapper<ZoneBean>().eq("zid",zid));
        returnJson.put("code",200);
        returnJson.put("msg",zone.getZname()+"分区添加管理员成功");
        return returnJson;
    }

    @ApiOperation("取消吧主")
    @PostMapping("/senior/removemanager")
    @ResponseBody
    public ResponseBean RemoveManager(MultipartHttpServletRequest request){
        String row = request.getParameter("row");
        String zname = request.getParameter("zname");
        long zid = queryDataService.GetZidByName(zname);
        JSONObject obj = JSONObject.parseObject(row);

        int authority = Integer.parseInt(obj.get("authority").toString());
        int uid = Integer.parseInt(obj.get("uid").toString());
        if(authority==3){
            return new ResponseBean(501,"操作禁止","总管理无法被取消");
        }
        try{
            UserFullDataBean user = userDataMapper.selectOne(new QueryWrapper<UserFullDataBean>().eq("uid", uid));
            user.setAuthority(1);

            userDataMapper.update(user,new QueryWrapper<UserFullDataBean>().eq("uid",uid));

            ZoneBean zone = zonemapper.selectOne(new QueryWrapper<ZoneBean>().eq("zid",zid));
            List<ZoneBean.ZoneManagerRelation> zmanagerId = JSON.parseArray(zone.getZmanagerId().toString(),ZoneBean.ZoneManagerRelation.class);
            zmanagerId.removeIf(z -> z.getUid() == uid);
            zone.setZmanagerId(zmanagerId);
            zonemapper.update(zone,new QueryWrapper<ZoneBean>().eq("zid",zid));

            adminMapper.delete(new QueryWrapper<AdminBean>().eq("uid", uid).eq("zid",zid));


            return new ResponseBean(200,"操作成功","取消吧主成功");
        }catch (NullPointerException e){
            e.printStackTrace();
            return new ResponseBean(-1,"操作失败","找不到用户");
        }

    }

    @ApiOperation("修改分区信息")
    @PostMapping("/zoneadmin/saveZoneInfo/{zonename}")
    @ResponseBody
    public Map<String,Object> SaveZoneInfo(MultipartHttpServletRequest request,@PathVariable String zonename) throws IOException {
        long zid = queryDataService.GetZidByName(zonename);
        String NewIntro = request.getParameter("intro");
        Map<String,Object> returnJson = new HashMap<>();

        ZoneBean zone = new ZoneBean();
        zone.setZintroduction(NewIntro);
        zonemapper.update(zone,new QueryWrapper<ZoneBean>().eq("zid",zid));
        MultipartFile file = request.getFile("file");
        String filename = file.getOriginalFilename();

        String[] list = filename.split("\\.");
        String suffix = list[list.length-1];
        String fileAdress ="./src/main/resources/static/images/page_icons/ZoneIcons/"+zonename+"."+suffix;
        fileHandlerService.OverwriteMultiPartFile(file,fileAdress);



        returnJson.put("code",200);
        returnJson.put("msg","修改分区资料成功");
        return returnJson;
    }

    @ApiOperation("把列表中的帖子放进回收站")
    @PostMapping("/admin/deletePostList")
    @ResponseBody
    public Map<String,Object> DeletePostList(MultipartHttpServletRequest request, Authentication authentication){
        Map<String,Object> returnJson = new HashMap<>();
        String checked = request.getParameter("checked");
        JSONArray objects = JSONArray.parseArray(checked);
        List<PostBean> postBeans = JSON.parseArray(checked, PostBean.class);

        String operator = authentication.getName();
//        String reason



        for(PostBean b:postBeans){
            postService.moveToRecycle(b.getPid());

            PostRecycleBean recycleBean = new PostRecycleBean();
            recycleBean.setPid(b.getPid());
            recycleBean.setZid(b.getZid());
            recycleBean.setOperator(operator);
            recycleBean.setReason("test");
            recycleBean.setTime(new TimeUtil().getCuerrent_time());
            postRecycleMapper.insert(recycleBean);

        }

        returnJson.put("code",200);
        returnJson.put("msg","删除选中表格成功成功");
        return returnJson;
    }

    @ApiOperation("恢复帖子")
    @PostMapping("/zoneadmin/recoverpost")
    @ResponseBody
    public ResponseBean Recoverpost(MultipartHttpServletRequest request){
        String row = request.getParameter("row");
        String zname = request.getParameter("zname");
        long zid = queryDataService.GetZidByName(zname);
        JSONObject obj = JSONObject.parseObject(row);
        int pid =Integer.parseInt(obj.get("pid").toString());

        postRecycleMapper.delete(new QueryWrapper<PostRecycleBean>().eq("pid",pid).eq("zid",zid));

        PostBean postBean = postMapper.selectOne(new QueryWrapper<PostBean>().eq("pid", pid));
        postBean.setEnabled(true);
        postMapper.update(postBean,new QueryWrapper<PostBean>().eq("pid",pid));


        return new ResponseBean(200,"恢复帖子成功",null);
    }

}