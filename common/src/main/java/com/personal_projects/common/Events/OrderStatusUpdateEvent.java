package com.personal_projects.common.Events;

import com.personal_projects.common.Enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusUpdateEvent {
    /** Unique identifier of the order */
    private long orderId;

    private OrderStatus orderStatus;

    public static OrderStatusUpdateEvent createEvent(long orderId, OrderStatus orderStatus) {
        return OrderStatusUpdateEvent.builder().orderId(orderId).orderStatus(orderStatus).build();
    }

    public boolean paymentWasSuccessful() {
        return this.orderStatus.equals(OrderStatus.PAID);
    }
}
