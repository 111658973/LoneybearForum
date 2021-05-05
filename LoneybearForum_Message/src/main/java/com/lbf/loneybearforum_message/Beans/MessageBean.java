package com.lbf.loneybearforum_message.Beans;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@TableName(value = "message")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class MessageBean {
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    private Long uid;
    private Long senderId;
    private Long receiverId;
    private String tittle;
    private String time;
    private int classificationId;
    private String content;
    private String html;
    private Boolean enabled;
    private Boolean checked;
}
