package com.lbf.loneybearforum_message;

import com.lbf.loneybearforum_message.Beans.MessageBean;
import com.lbf.loneybearforum_message.Controller.Message;
import com.lbf.loneybearforum_message.Kafka.Producers.producerTest;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;

@SpringBootTest
class LoneybearForumMessageApplicationTests  {
    @Test
    void contextLoads() {
    }

    @Test
    void Kafka() throws InterruptedException{
//        producerTest<MessageBean> producerTest = new producerTest<>();
//        MessageBean message = new MessageBean();
//        message.setTime(new Date().toString());
//        message.setUid(1L);
//        message.setReceiverId(1L);
//        producerTest.send(message);
        System.out.println("123");
        
    }
}
