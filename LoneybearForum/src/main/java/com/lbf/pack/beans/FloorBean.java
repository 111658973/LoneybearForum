package com.lbf.pack.beans;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName(value = "floors")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FloorBean {
    @TableId(value = "fid",type = IdType.AUTO)

    private Long fid;
    private long pid;
    private long uid;
    private int favourite;
    private int opposite;
    private int floor;
    private int type;
    private String content;
    private String ftime;

    @TableField(exist = false)
    private int FloorReplysCount;

    @TableField(exist = false)
    private String Tittle;

    @TableField(exist = false)
    private String Author;

    @TableField(exist = false)
    private String Zname;

    @TableField(exist = false)
    private String Zurl;
}
