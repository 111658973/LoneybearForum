package com.lbf.pack.beans;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;

@TableName(value = "blacklist")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Component
public class BlackListBean {
    @TableId(value = "bid",type = IdType.AUTO)

    private Long bid;
    private long uid;
    private long zid;
    private String status;
    private int operatorId;
    private String reason;
    private String time;
    private long lastTime;
    private boolean enabled;


    @TableField(exist = false)
    private UserFullDataBean userinfo;

    @TableField(exist = false)
    private UserFullDataBean operatorinfo;
}
