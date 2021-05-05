package com.lbf.pack.beans;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "users")
public class UserEditDataBean {
    private String email;
    private String phoneNumber;
    private String nick;
    private String sex;
    private String uintroduction;
    private String iconUrl;
    private String displayImgUrl;
}
