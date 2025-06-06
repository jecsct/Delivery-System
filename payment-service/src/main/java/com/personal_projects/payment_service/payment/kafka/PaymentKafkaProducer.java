package com.personal_projects.payment_service.payment.kafka;

import com.personal_projects.common.Events.PaymentEvent;
import com.personal_projects.common.Events.ShipmentEvent;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Kafka producer configuration for the Payment service.
 *
 * <p>This configuration sets up Kafka producers specifically for the {@link PaymentEvent} type.
 * It defines the necessary beans and serialization settings used for publishing messages to Kafka.</p>
 */
@Configuration
public class PaymentKafkaProducer {

    /**
     * Kafka broker address, injected from application properties.
     */
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    /**
     * Builds a map of configuration properties common to all Kafka producers.
     *
     * @return a map of Kafka producer configuration properties
     */
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    /**
     * Creates a {@link ProducerFactory} for producing {@link PaymentEvent} messages.
     *
     * @return a producer factory configured for {@link PaymentEvent}
     */
    @Bean
    public ProducerFactory<String, PaymentEvent> paymentProducerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    /**
     * Creates a {@link KafkaTemplate} for sending {@link PaymentEvent} messages.
     *
     * @return a Kafka template for {@link PaymentEvent}
     */
    @Bean
    public KafkaTemplate<String, PaymentEvent> paymentKafkaTemplate() {
        return new KafkaTemplate<>(paymentProducerFactory());
    }
}