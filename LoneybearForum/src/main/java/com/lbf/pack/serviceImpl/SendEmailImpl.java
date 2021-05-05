package com.lbf.pack.serviceImpl;

import com.lbf.pack.Util.RandomUtils;
import com.lbf.pack.service.SendEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class SendEmailImpl implements SendEmailService {
    @Autowired
    private JavaMailSenderImpl mailSender;
    @Autowired
    RedisTemplate redisTemplate;

    @Value("${spring.mail.username}")
    private String username;

    public  void sendVerifycodeMail(String adress,String verifycode)  throws MessagingException
    {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,true);
        helper.setFrom(new InternetAddress(username));
        helper.setTo(adress);
        helper.setSubject("LoneybeatForum论坛邮箱验证");
        //点击链接验证
        String html = new String("<p>您的验证码是"+"<a style=\"text-decoration: underline;color: #2C8DFB;\n:\n\">"+verifycode+"</a>"+"</p><p>请在五分钟之内验证</p>");
        helper.setText(html,true);
        mailSender.send(message);



//        mailSender.send();

    }

    @Override
    public void storeVerifycodeInRedis(String username,String code) {
        Map<String,Object> map =new HashMap<>();
        map.put("code",code);
        map.put("time","300");
        redisTemplate.opsForHash().putAll(username,map);
        redisTemplate.expire(username,300, TimeUnit.SECONDS);
    }


}
