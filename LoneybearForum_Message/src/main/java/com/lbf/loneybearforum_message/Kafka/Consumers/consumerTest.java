package com.lbf.loneybearforum_message.Kafka.Consumers;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;

import java.util.Optional;

@Slf4j
@KafkaListener
public class consumerTest {
    @KafkaListener(id = "tut", topics = "test")

    public void listen(ConsumerRecord<?, ?> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
//判断是否NULL
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
        //获取消息
            Object message = kafkaMessage.get();
            log.info("Receive： +++++++++++++++ Topic:" + topic);
            log.info("Receive： +++++++++++++++ Record:" + record);
            log.info("Receive： +++++++++++++++ Message:" + message);
        }
    }
}
