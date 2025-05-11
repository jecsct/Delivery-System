package com.personal_projects.order_service.order.kafka;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration class for setting up Kafka producer beans.
 * This defines how messages are serialized and which Kafka server to connect to.
 */
@Configuration
public class OrderKafkaProducer {

    /**
     * Kafka bootstrap servers, injected from application.properties.
     */
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;


    /**
     * Defines the Kafka producer configuration settings.
     *
     * @return a map containing Kafka producer properties
     */
    public Map<String, Object> producerConfig(){
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        return props;
    }

    /**
     * Creates a {@link ProducerFactory} bean that is used to create Kafka producers.
     *
     * @return a configured ProducerFactory
     */
    @Bean
    public ProducerFactory<String, String> producerFactory()
    {
        return new DefaultKafkaProducerFactory<>(producerConfig());
    }

    /**
     * Creates a {@link KafkaTemplate} bean for sending messages to Kafka topics.
     *
     * @param producerFactory the producer factory used by the KafkaTemplate
     * @return a KafkaTemplate for String key-value pairs
     */
    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> producerFactory )    {
        return new KafkaTemplate<>(producerFactory);
    }

}
