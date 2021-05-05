package com.lbf.pack.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public interface CustomUserDetailService extends UserDetailsService {
    UserDetails loadUserByUsername(String s) throws UsernameNotFoundException;
}
