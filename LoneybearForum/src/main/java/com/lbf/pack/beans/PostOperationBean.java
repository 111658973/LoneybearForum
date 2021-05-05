package com.lbf.pack.beans;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@TableName("post_opration")
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostOperationBean {
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    private Long uid;
    private Long pid;
    private String operation;
    private String time;
    private Boolean enable;
}
