package com.personal_projects.order_service.util;

import com.personal_projects.common.Enums.OrderStatus;
import com.personal_projects.common.Events.OrderEvent;
import com.personal_projects.order_service.data.dto.OrderDTO;
import com.personal_projects.order_service.data.entity.Order;

import java.time.LocalDateTime;

/**
 * Utility class for mapping between order-related data transfer objects and entities.
 */
public class OrderMapper {

    /**
     * Converts an OrderRequest to an Order entity with calculated fields.
     * Sets default status (CREATED) and creation timestamp.
     *
     * @param request the order request data
     * @return mapped Order entity with calculated totalAmount
     */
    public static Order toOrder(OrderDTO request) {
        return Order.builder()
                .customerName(request.getCustomerName())
                .productName(request.getProductName())
                .quantity(request.getQuantity())
                .price(request.getPrice())
                .totalAmount(request.getQuantity() * request.getPrice())  // Auto-calculate
                .status(OrderStatus.CREATED)  // Default status
                .createdAt(LocalDateTime.now())
                .build();
    }

    /**
     * Converts an Order entity to an OrderEvent for Kafka messaging.
     * Includes only essential fields for event processing.
     *
     * @param order the order entity to convert
     * @return simplified OrderEvent for publishing
     */
    public static OrderEvent toOrderEvent(Order order) {
        return OrderEvent.builder()
                .orderId(order.getId())
                .customerName(order.getCustomerName())
                .totalAmount(order.getTotalAmount())
                .build();
    }
}
