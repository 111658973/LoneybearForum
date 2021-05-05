package com.lbf.pack.beans;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@TableName("admins")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Component
public class AdminBean {
    @TableId(value = "aid",type = IdType.AUTO)
    private Long aid;
    private long zid;
    private long uid;
    private int authority;
    private String time;
    private String operator;
}
