package com.lbf.pack.serviceImpl;

import com.lbf.pack.service.CustomAuthenticationProvider;
import com.lbf.pack.service.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomAuthenticationProviderImpl implements CustomAuthenticationProvider {
    @Autowired
    CustomUserDetailService customUserDetailService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        UserDetails userDetails = customUserDetailService.loadUserByUsername();
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }
}
