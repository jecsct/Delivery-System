package com.personal_projects.order_service.order.kafka;

import com.personal_projects.common.Enums.OrderStatus;
import com.personal_projects.common.Events.PaymentEvent;
import com.personal_projects.common.Events.ShipmentEvent;
import com.personal_projects.order_service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static com.personal_projects.common.Configs.KafkaConfigs.*;


/**
 * Kafka listener component for processing events related to order payments and shipments.
 *
 * <p>This class listens to Kafka topics for {@code PaymentEvent} and {@code ShipmentEvent} messages
 * and updates the order status accordingly using the {@link OrderService}.</p>
 */
@Component
public class OrderKafkaListener {

    private final OrderService orderService;

    /**
     * Constructor for dependency injection of {@link OrderService}.
     *
     * @param orderService the service responsible for handling order-related business logic
     */
    @Autowired
    public OrderKafkaListener(final OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Kafka listener for processing {@link PaymentEvent} messages from the {@code PAYMENT_TOPIC}.
     *
     * <p>If the payment was successful, the corresponding order's status is set to {@code PAID}.
     * Otherwise, it is marked as {@code FAILED}.</p>
     *
     * @param paymentEvent the payment event consumed from Kafka
     */
    @KafkaListener(
            topics = PAYMENT_TOPIC,
            groupId = "order-service-group",
            containerFactory = "paymentKafkaListenerContainerFactory"
    )
    void listener(PaymentEvent paymentEvent) {
        System.out.println("Listener Received: " + paymentEvent);

        if (paymentEvent.paymentWasSuccessful()) {
            orderService.updateOrderStatusById(paymentEvent.getOrderId(), OrderStatus.PAID);
        } else {
            orderService.updateOrderStatusById(paymentEvent.getOrderId(), OrderStatus.FAILED);
        }
    }

    /**
     * Kafka listener for processing {@link ShipmentEvent} messages from the {@code SHIPMENT_TOPIC}.
     *
     * <p>When a shipment event is received, the order status is updated to {@code SHIPPED}.</p>
     *
     * @param shipmentEvent the shipment event consumed from Kafka
     */
    @KafkaListener(
            topics = SHIPMENT_TOPIC,
            groupId = "order-service-group",
            containerFactory = "shipmentKafkaListenerContainerFactory"
    )
    void listener(ShipmentEvent shipmentEvent) {
        System.out.println("Listener Received: " + shipmentEvent);

        orderService.updateOrderStatusById(shipmentEvent.getOrderId(), OrderStatus.SHIPPED);
    }
}
