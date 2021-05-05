package com.lbf.pack.beans;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;



@TableName("visithistory")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Component
public class VisitHistoryBean {

    @TableId(value = "vid",type = IdType.AUTO)
    private Long vid;
    private long uid;
    private long times;
    private String url;
    private String lastVisitTime;
    private String method;
    private boolean enable;

}
