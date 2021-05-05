package com.lbf.pack.service;


import com.lbf.pack.beans.FloorBean;
import com.lbf.pack.beans.PostBean;
import com.lbf.pack.beans.UserFullDataBean;
import com.lbf.pack.beans.ZoneBean;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Service
public interface QueryDataService{



    public List<PostBean> getGoodPostListByZid(long pid);

    public Map<String,Object> GetUserdataFromSession(HttpSession session,Boolean isPostsNeed);

    public Map<String,Object> getUserPageDataByUid(Long uid);

    public long GetUserUidFromSession(HttpSession session);


    /**
     * 根据名字获取分区资料，最好在url里传参数
     * @param zonename 分区名字
     * @return
     */
    public Map<String,Object> GetZonesDataByZname(String zonename);

    public ZoneBean GetZonesDataByZid(long zoneId);


    /**
     * 获取热度最高的帖子
     * @param size 取size条
     * @return Map
     * 第一页0
     */
    public List<PostBean> GetHotPostsDataLimit(int size,int page);


    /**
     * 获取最新的帖子
     * @param size 取size条
     * @return Map<String,Object>
     */
    public List<PostBean> GetNewestPostDataLimit(int size,int page);


    /**
     * 获取帖子的资料
     * @param Pid 帖子id
     * @return PostBean
     */
    @ApiOperation(value = "获取一个帖子的资料")
    public PostBean GetPostInfo(long Pid);

    /**
     * 根据帖子id获取一个所有的楼层
     * @param pid 帖子id
     * @return  List<FloorBean>
     */
    @ApiOperation(value = "根据帖子id获取未处理的所有楼层，包括首楼和楼层和楼中楼")
    public List<FloorBean> GetFloorList(long pid);

    /**
     * 获取经过处理过的整个帖子的内容，前端收到的是一个json可以直接遍历
     * @param pid 帖子id
     * @return 一个json存储整理好的楼层信息
     */
    @ApiOperation(value = "获取经过处理过的整个帖子的内容，前端收到的是一个json可以直接遍历")
    public List<Map<String,Object>> GetFormattedFloors(long pid);

    @ApiOperation(value = "获取经过处理过的整个帖子的内容，取一楼")
    public List<Map<String,Object>>GetFormattedFloorByIndex(long pid,int index);

    @ApiOperation(value = "获取经过处理过的整个帖子的内容，取其余楼层")
    public List<Map<String,Object>> GetFormattedRest(long pid);

    @ApiOperation(value = "根据uid获取用户的信息")
    public UserFullDataBean GetUserdataByUid(long pid);

    /**
     * 根据用户id获取用户最喜欢的分区资料
     * @param uid 用户id
     * @return ZoneBean列表
     */
    public List<ZoneBean> GetUserFavouriteZonesData(int uid);

    /**
     * 查最受欢迎的分区
     * @param index 查询几个
     * @return List<Zonebean>
     */
    public List<ZoneBean> GetHotZones(int index);


    /**
     * 获取指令楼层的所有FloorBean对象列表,按type类型从大到小排列
     * @param Floor 楼层
     * @return List<FloorBean>
     */
    public List<FloorBean> GetFloorContent(long pid,int Floor);


    /**
     * 获取用户的简略信息
     * @param uid
     * @return
     */
    public UserFullDataBean GetBriefUserData(int uid);

    /**
     * 根据名字查id
     * @param zname
     * @return
     */
    public long GetZidByName(String zname);


    /**
     * 查pid的最大值，用于创建新帖子的时候
     * @return
     */
    public long GetMaxPostCount();

    /**
     * 根据zid 查分区中的所有帖子，每次显示index条，第page页
     * @param zid 分区id
     * @param Limit 每页数量
     * @param page 页码
     * @param order 排序方式，1是按时间，2是按热度
     * @return List
     */
    public List<PostBean> GetZonePostDataDescBy(long zid,int Limit,int page,int order);

    /**
     * 根据pid查帖子的回复数量
     * @return
     */
    public int GetPostFloorConoutByPid(long pid);


    /**
     * 获取帖子的收藏点赞点踩信息
     * @param pid
     * @param session
     * @return
     */
    public Map<String,Object> GetPostCollectInfo(long pid,HttpSession session);

    /**
     * 获取分区的关注信息
     * @param zid
     * @param session
     * @return
     */
    public Map<String,Object> GetZoneFollowInfo(long zid,HttpSession session);

    /**
     * 按排序获取分区的帖子信息
     * @Param zid 分区id
     * @param type 类型，1是按热度，2是按时间先后
     * @param size 显示数量
     * @param page 页数，第一页是0
     * @return List<Zonebean>
     */
    public List<PostBean> GetZoneSortedPosts(long zid,int type,int size,int page);

    /**
     * 根据用户名或者id模糊搜索
     * @param param
     * @return
     */
    public List<UserFullDataBean> SimpleSearchUserByUidOrNick(String param);


    /**
     * 根据pid或者tittle查帖子列表
     * @param param 参数
     * @return
     */
    public List<PostBean> SimpleSearchPostByUidOrTittle(String param, String zonename);

    /**
     * 传入一个字符串判断是否是整数
     * @param str
     * @return
     */
    public boolean isInteger(String str);

    /**
     * 从Redis获取黑名单信息
     * @return
     */
    public List<Map<String,Object>> getBlackList();

    public List<Map<String,Object>> ParseToMapList(List objects);

    public FloorBean GetFloorInfoById(long fid);

    /**
     * 根据登陆的账号获取uid
     * @param username
     * @return
     */
    public long getUidByUsername(String username);


    public Long getCuerrentUid();
}
