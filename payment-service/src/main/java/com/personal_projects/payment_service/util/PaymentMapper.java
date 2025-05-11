package com.personal_projects.payment_service.util;

import com.personal_projects.common.Enums.OrderStatus;
import com.personal_projects.common.Enums.PaymentStatus;
import com.personal_projects.common.Events.OrderEvent;
import com.personal_projects.payment_service.data.entity.Payment;

import java.time.LocalDateTime;

public class PaymentMapper {


    public static Payment toPayment(OrderEvent event) {
        return Payment.builder()
                .clientName(event.getCustomerName())
                .amount(event.getTotalAmount())
                .status(PaymentStatus.PENDING)  // Default status
                .createdAt(LocalDateTime.now())
                .orderId(event.getOrderId())
                .build();
    }


//    public static OrderEvent toPaymentEvent(Order order) {
//        return OrderEvent.builder()
//                .orderId(order.getId())
//                .customerName(order.getCustomerName())
//                .totalAmount(order.getTotalAmount())
//                .build();
//    }

}
