package com.lbf.pack.beans;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName(value = "users",autoResultMap = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserJsonStoreBean {

        private String favor_posts_id_list;
        private String favourite_zone_id_list;

}
