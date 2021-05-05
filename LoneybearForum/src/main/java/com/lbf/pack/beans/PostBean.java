package com.lbf.pack.beans;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@TableName(value = "posts",autoResultMap = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class PostBean {
    @TableId(value = "pid",type = IdType.AUTO)

    private Long pid;
    private String ptittle;
    private String pcreateTime;
    private String plastVisitedTime;
    private String classfications;
    private int plikeCount;
    private int pdislikeCount;
    private long zid;
    private long uid;
    private String status;
    private Boolean isGood;
    private Boolean enabled;

    //楼层总计
    @TableField(exist = false)
    private int FloorCount;

    //存发帖人信息
    @TableField(exist = false)
    private UserFullDataBean AuthorData;

    @TableField(exist = false)
    private FloorBean firstFloor;

    @TableField(exist = false)
    private ZoneBean PostZoneData;




}
