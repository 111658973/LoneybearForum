package com.lbf.loneybearforum_message.Kafka.Consumers;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;

@Slf4j
public class c {
    @KafkaListener(id="test-consumer-group",topics = {"topic123"})
    public void onMessage(ConsumerRecord<?, ?> record,String message) {
        // 消费的哪个topic、partition的消息,打印出消息内容
        log.info("简单消费：" + record.topic() + "-" + record.partition() + "-" + record.value()+message);
    }


}
