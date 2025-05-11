package com.personal_projects.order_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import static com.personal_projects.common.Configs.KafkaConfigs.ORDERS_TOPIC;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic orderServiceTopic() {
        return TopicBuilder.name(ORDERS_TOPIC).build();
    }
}
