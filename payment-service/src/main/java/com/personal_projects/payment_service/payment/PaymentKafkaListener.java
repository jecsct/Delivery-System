package com.personal_projects.payment_service.payment;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static com.personal_projects.common.Configs.KafkaConfigs.ORDERS_TOPIC;


@Component
public class PaymentKafkaListener {

    @KafkaListener(topics = ORDERS_TOPIC, groupId = "groupId")
    void listener(String data) {
        System.out.println("Listener Received: " + data);
    }
}
