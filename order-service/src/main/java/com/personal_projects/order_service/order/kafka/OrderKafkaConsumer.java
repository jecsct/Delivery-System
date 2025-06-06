package com.personal_projects.order_service.order.kafka;

import com.personal_projects.common.Events.PaymentEvent;
import com.personal_projects.common.Events.ShipmentEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration class for Kafka consumers in the Order Service.
 *
 * <p>This class defines beans to consume {@code PaymentEvent} and {@code ShipmentEvent} messages
 * from Kafka topics using Spring Kafka's listener containers.</p>
 *
 * <p>Each event type has its own {@code ConsumerFactory} and {@code KafkaListenerContainerFactory}
 * to support different payload deserialization types.</p>
 */
@Configuration
public class OrderKafkaConsumer {

    /**
     * Kafka bootstrap servers address, injected from application properties.
     */
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    /**
     * Basic Kafka consumer configuration.
     *
     * @return a map of Kafka consumer properties.
     */
    public Map<String, Object> consumerConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "groupId");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        return props;
    }

    /**
     * Creates a {@link ConsumerFactory} for {@code PaymentEvent} messages.
     *
     * @return a {@code ConsumerFactory} that deserializes values into {@code PaymentEvent}.
     */
    @Bean
    public ConsumerFactory<String, PaymentEvent> paymentConsumerFactory() {
        JsonDeserializer<PaymentEvent> deserializer = new JsonDeserializer<>(PaymentEvent.class);
        deserializer.addTrustedPackages("*");
        return new DefaultKafkaConsumerFactory<>(
                consumerConfig(),
                new StringDeserializer(),
                deserializer
        );
    }

    /**
     * Creates a {@link ConsumerFactory} for {@code ShipmentEvent} messages.
     *
     * @return a {@code ConsumerFactory} that deserializes values into {@code ShipmentEvent}.
     */
    @Bean
    public ConsumerFactory<String, ShipmentEvent> shipmentConsumerFactory() {
        JsonDeserializer<ShipmentEvent> deserializer = new JsonDeserializer<>(ShipmentEvent.class);
        deserializer.addTrustedPackages("*");
        return new DefaultKafkaConsumerFactory<>(
                consumerConfig(),
                new StringDeserializer(),
                deserializer
        );
    }

    /**
     * Creates a {@link ConcurrentKafkaListenerContainerFactory} for handling {@code PaymentEvent} messages.
     *
     * @return a Kafka listener container factory configured for {@code PaymentEvent}.
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PaymentEvent> paymentKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, PaymentEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(paymentConsumerFactory());
        return factory;
    }

    /**
     * Creates a {@link ConcurrentKafkaListenerContainerFactory} for handling {@code ShipmentEvent} messages.
     *
     * @return a Kafka listener container factory configured for {@code ShipmentEvent}.
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ShipmentEvent> shipmentKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ShipmentEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(shipmentConsumerFactory());
        return factory;
    }
}
