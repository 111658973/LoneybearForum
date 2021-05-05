package com.lbf.pack.beans;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

@Component
@Data
@ToString
@TableName(value = "users",autoResultMap = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserFullDataBean implements Serializable {
    private String email;
    private String username;
//    private String password;
    private String displayImgUrl;
    private String createDate;
    private String lastVisitedTime;
    private String phoneNumber;
    private String uuid;
    private String lastVisitedIp;
    private String nick;
    private String sex;
    private String status;
    private String uintroduction;
    private String iconUrl;

    @TableId(value = "uid",type = IdType.AUTO)
    private Long uid;
    private int authority;
    private int level;
    private int exp;
    private boolean enabled;


    //ArrayList
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private List<favouriteZoneIdList> favouriteZoneIdList;

    @TableField(typeHandler = FastjsonTypeHandler.class)
    private List<favorPostsIdList> favorPostsIdList;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class favorPostsIdList implements Serializable {
        private int pid;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class favouriteZoneIdList implements Serializable {
        private int zid;

    }
}
