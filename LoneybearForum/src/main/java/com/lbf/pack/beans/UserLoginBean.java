package com.lbf.pack.beans;


import com.baomidou.mybatisplus.annotation.TableField;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
//
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "user")
@Component
@Data
@ToString
@TableName("users")
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginBean implements Serializable,UserDetails {
    @TableField(exist = false)
    private String verifyCode;

    @TableField(exist = false)
    private Map<String,String> headers;


    private String authority;

    private String nick;
    private String email;
    private String username;
    private String password;
    private String createDate;
    private String lastVisitedTime;
    private String phoneNumber;
    private String uuid;
    private String lastVisitedIp;

    @TableField(exist = false)
    private List<GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        //会话并发生效，使用username判断是否是同一个用户

        if (obj instanceof UserLoginBean){
            //字符串的equals方法是已经重写过的
            return ((UserLoginBean) obj).getUsername().equals(this.username);
        }else {
            return false;
        }
    }

}
