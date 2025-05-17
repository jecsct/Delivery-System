package com.personal_projects.payment_service.payment.kafka;

import com.personal_projects.common.Events.OrderEvent;
import com.personal_projects.payment_service.payment.PaymentService;
import com.personal_projects.payment_service.util.PaymentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static com.personal_projects.common.Configs.KafkaConfigs.ORDER_PAYMENT_REQUEST_TOPIC;


/**
 * Kafka listener component for the Payment service.
 * <p>
 * Listens to incoming {@link OrderEvent} messages on the Kafka topic specified by {@code ORDER_PAYMENT_REQUEST_TOPIC}
 * and delegates processing to the {@link PaymentService}.
 * </p>
 */
@Component
public class PaymentKafkaListener {

    private final PaymentService paymentService;

    /**
     * Constructs a new {@code PaymentKafkaListener} with the provided {@link PaymentService}.
     *
     * @param paymentService the service responsible for handling payment logic
     */
    @Autowired
    public PaymentKafkaListener(final PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * Kafka listener method that is triggered whenever a new {@link OrderEvent} is published
     * to the {@code ORDER_PAYMENT_REQUEST_TOPIC} Kafka topic.
     * <p>
     * Converts the event into a {@code Payment} entity and persists it using {@link PaymentService}.
     * </p>
     *
     * @param orderEvent the order event received from Kafka
     */
    @KafkaListener(
            topics = ORDER_PAYMENT_REQUEST_TOPIC,
            groupId = "groupId",
            containerFactory = "kafkaListenerContainerFactory"
    )
    void listener(OrderEvent orderEvent) {
        System.out.println("Listener Received: " + orderEvent);
        paymentService.savePayment(PaymentMapper.toPayment(orderEvent));
    }
}