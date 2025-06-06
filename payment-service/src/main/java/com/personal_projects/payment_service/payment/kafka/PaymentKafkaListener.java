package com.personal_projects.payment_service.payment.kafka;

import com.personal_projects.common.Events.OrderEvent;
import com.personal_projects.payment_service.payment.PaymentService;
import com.personal_projects.payment_service.util.PaymentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static com.personal_projects.common.Configs.KafkaConfigs.ORDER_TOPIC;



/**
 * Kafka listener component for the Payment service.
 *
 * <p>This class listens for {@link OrderEvent} messages from Kafka and triggers the appropriate
 * business logic in the {@link PaymentService}.</p>
 */
@Component
public class PaymentKafkaListener {

    private final PaymentService paymentService;

    /**
     * Constructs a new {@code PaymentKafkaListener} with the given {@link PaymentService}.
     *
     * @param paymentService the service used to handle payment-related operations
     */
    @Autowired
    public PaymentKafkaListener(final PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * Listens to the Kafka topic defined by {@code ORDER_TOPIC} and processes incoming {@link OrderEvent} messages.
     * <p>
     * When an order event is received, it is transformed into a {@code Payment} object and saved using the {@link PaymentService}.
     * </p>
     *
     * @param orderEvent the order event received from Kafka
     */
    @KafkaListener(
            topics = ORDER_TOPIC,
            groupId = "payment-service-group",
            containerFactory = "paymentKafkaListenerContainerFactory"
    )
    void listener(OrderEvent orderEvent) {
        System.out.println("Listener Received: " + orderEvent);
        paymentService.savePayment(PaymentMapper.toPayment(orderEvent));
    }
}