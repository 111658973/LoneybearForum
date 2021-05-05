package com.lbf.pack.Util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderUtil {
    public String encryptPasswrod(String password){
        return new BCryptPasswordEncoder().encode(password);
    }

}
