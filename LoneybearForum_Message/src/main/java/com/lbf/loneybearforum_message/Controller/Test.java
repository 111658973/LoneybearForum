package com.lbf.loneybearforum_message.Controller;

import com.lbf.loneybearforum_message.Beans.ResBean;
import com.lbf.loneybearforum_message.Kafka.Consumers.consumerTest;
import com.lbf.loneybearforum_message.Kafka.Producers.producerTest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Controller;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
@Slf4j
@Controller
public class Test {
    @GetMapping("/Kafka/send/{msg}")
    @ResponseBody
    public ResBean kafka1(@PathVariable String msg){
        producerTest<ResBean> producerTest = new producerTest<>();
        producerTest.send(new ResBean(200,msg));
        return new ResBean(200,"yes");
    }


    @GetMapping("/Kafka/get")
    @ResponseBody
    public ResBean kafka2(@PathVariable String msg){
        consumerTest consumerTest =new consumerTest();
        return new ResBean(200,"yes");
    }

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    // 发送消息
    @GetMapping("/kafka/normal/{message}")
    @ResponseBody
    public void sendMessage1(@PathVariable String message) throws InterruptedException {
        kafkaTemplate.send("topic123", message).addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onFailure(Throwable ex) {
                System.out.println("发送消息失败："+ex.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, Object> result) {
                System.out.println("发送消息成功：" + result.getRecordMetadata().topic() + "-"
                        + result.getRecordMetadata().partition() + "-" + result.getRecordMetadata().offset());
            }
        });


    }
}
