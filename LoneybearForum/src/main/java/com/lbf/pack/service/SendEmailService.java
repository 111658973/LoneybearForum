package com.lbf.pack.service;


import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@Service
public interface SendEmailService{
    public  void sendVerifycodeMail(String adress,String verifycode)throws MessagingException;

    public  void storeVerifycodeInRedis(String username,String code);


};