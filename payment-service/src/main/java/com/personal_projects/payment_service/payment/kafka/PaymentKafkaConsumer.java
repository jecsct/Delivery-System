package com.personal_projects.payment_service.payment.kafka;

import com.personal_projects.common.Events.OrderEvent;
import jakarta.persistence.criteria.Order;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Kafka consumer configuration for the Payment service.
 * <p>
 * Sets up beans required to consume {@link OrderEvent} messages from a Kafka topic.
 * This configuration uses JSON deserialization to automatically convert message payloads to Java objects.
 * </p>
 */
@Configuration
public class PaymentKafkaConsumer {

    /**
     * The address of the Kafka broker, injected from application properties.
     */
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    /**
     * Builds a map of Kafka consumer configuration properties.
     *
     * @return a map of Kafka consumer configuration settings
     */
    public Map<String, Object> consumerConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "groupId");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*"); // Trust all packages for deserialization
        return props;
    }

    /**
     * Creates a {@link ConsumerFactory} for Kafka consumers that handle {@link OrderEvent} messages.
     *
     * @return a ConsumerFactory configured for {@code OrderEvent}
     */
    @Bean
    public ConsumerFactory<String, OrderEvent> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                consumerConfig(),
                new StringDeserializer(),
                new JsonDeserializer<>(OrderEvent.class)
        );
    }

    /**
     * Creates a Kafka listener container factory for handling {@link OrderEvent} messages.
     * <p>
     * This factory is used by {@code @KafkaListener} methods in the Payment service to consume Kafka messages.
     * </p>
     *
     * @return a configured Kafka listener container factory
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderEvent> paymentKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, OrderEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}

