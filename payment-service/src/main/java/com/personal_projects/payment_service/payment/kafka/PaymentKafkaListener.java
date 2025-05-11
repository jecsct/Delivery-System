package com.personal_projects.payment_service.payment.kafka;

import com.personal_projects.common.Events.OrderEvent;
import com.personal_projects.payment_service.payment.PaymentService;
import com.personal_projects.payment_service.util.PaymentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static com.personal_projects.common.Configs.KafkaConfigs.ORDERS_TOPIC;


/**
 * Kafka listener component that listens for order events and processes the payment accordingly.
 * <p>
 * This listener receives order events from Kafka topics and maps them to {@link Payment} entities.
 * It then saves the payment using the {@link PaymentService}. The listener method listens to
 * events from a specific Kafka topic and processes them asynchronously.
 */
@Component
public class PaymentKafkaListener {

    private final PaymentService paymentService;

    /**
     * Constructor to inject the {@link PaymentService} dependency.
     *
     * @param paymentService the service to handle payment operations
     */
    @Autowired
    public PaymentKafkaListener(final PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * Listener method that processes the received order events from Kafka.
     * <p>
     * This method listens for incoming messages (order events) from a specified Kafka topic.
     * Upon receiving an order event, it maps the event to a {@link Payment} object and saves it
     * using the {@link PaymentService}.
     *
     * @param orderEvent the order event received from Kafka
     */
    @KafkaListener(topics = ORDERS_TOPIC, groupId = "groupId", containerFactory = "kafkaListenerContainerFactory")
    void listener(OrderEvent orderEvent) {
        System.out.println("Listener Received: " + orderEvent);
        paymentService.savePayment(PaymentMapper.toPayment(orderEvent));
    }
}
