package com.personal_projects.common.Events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Represents an order event for Kafka messaging.
 * Contains essential order details for downstream processing.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderEvent {
    /** Unique identifier of the order */
    private long orderId;

    /** Name of the customer who placed the order */
    private String customerName;

    /** Total amount of the order */
    private double totalAmount;
}
