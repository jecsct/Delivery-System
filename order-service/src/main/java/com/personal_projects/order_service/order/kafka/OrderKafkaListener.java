package com.personal_projects.order_service.order.kafka;

import com.personal_projects.common.Enums.OrderStatus;
import com.personal_projects.common.Events.OrderEvent;
import com.personal_projects.common.Events.OrderStatusUpdateEvent;
import com.personal_projects.order_service.order.OrderService;
import com.personal_projects.order_service.util.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static com.personal_projects.common.Configs.KafkaConfigs.ORDER_PAYMENT_REQUEST_TOPIC;
import static com.personal_projects.common.Configs.KafkaConfigs.ORDER_STATUS_UPDATES_TOPIC;

/**
 * Kafka listener for handling {@link OrderStatusUpdateEvent} messages.
 * <p>
 * This component listens to messages from the Kafka topic related to order status updates.
 * Based on the content of the event, it updates the order's status using the {@link OrderService}.
 * </p>
 */
@Component
public class OrderKafkaListener {

    private final OrderService orderService;

    /**
     * Constructs the Kafka listener with a reference to the {@link OrderService}.
     *
     * @param orderService the service used to update order statuses in the system.
     */
    @Autowired
    public OrderKafkaListener(final OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Handles incoming {@link OrderStatusUpdateEvent} messages from Kafka.
     * <p>
     * If the payment was successful, it sets the order status to {@link OrderStatus#PAID}.
     * Otherwise, it sets the order status to {@link OrderStatus#FAILED}.
     * </p>
     *
     * @param orderStatusUpdateEvent the event containing order ID and payment status.
     */
    @KafkaListener(
            topics = ORDER_STATUS_UPDATES_TOPIC,
            groupId = "groupId",
            containerFactory = "kafkaListenerContainerFactory"
    )
    void listener(OrderStatusUpdateEvent orderStatusUpdateEvent) {
        System.out.println("Listener Received: " + orderStatusUpdateEvent);

        if (orderStatusUpdateEvent.paymentWasSuccessful()) {
            orderService.updateOrderStatusById(orderStatusUpdateEvent.getOrderId(), OrderStatus.PAID);
        } else {
            orderService.updateOrderStatusById(orderStatusUpdateEvent.getOrderId(), OrderStatus.FAILED);
        }
    }
}