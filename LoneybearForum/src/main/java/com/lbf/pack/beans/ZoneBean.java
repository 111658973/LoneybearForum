package com.lbf.pack.beans;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
@Slf4j
@Data
@ToString
@TableName(value = "zones",autoResultMap = true)
@AllArgsConstructor
@NoArgsConstructor
public class ZoneBean {
    private String zname;
    private String zintroduction;
    private String ziconUrl;
    private String zurl;
    private int likeNumber;

    @TableId(value = "zid",type = IdType.AUTO)
    private Long zid;

    @TableField(exist = false)
    private int postcount;


    /**
     * 分区里的管理员uid列表
     */
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private List<ZoneManagerRelation> zmanagerId;



    @Data
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ZoneManagerRelation {
        private long uid;
    }


}
