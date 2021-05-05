package com.lbf.pack.beans;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@TableName("post_recycle")
public class PostRecycleBean {
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    private Long pid;
    private String operator;
    private Long zid;
    private String reason;
    private String time;

    @TableField(exist = false)
    private PostBean post;
    @TableField(exist = false)
    private UserFullDataBean user;
}
