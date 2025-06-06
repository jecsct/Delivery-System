package com.personal_projects.common.Events;

import com.personal_projects.common.Enums.OrderStatus;
import com.personal_projects.common.Enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentEvent {
    private String paymentId;

    /** Unique identifier of the order */
    private long orderId;

    private PaymentStatus paymentStatus;

    public static PaymentEvent createEvent(String paymentId, long orderId, PaymentStatus paymentStatus) {
        return PaymentEvent.builder().paymentId(paymentId).orderId(orderId).paymentStatus(paymentStatus).build();
    }

    public boolean paymentWasSuccessful() {
        return this.paymentStatus.equals(PaymentStatus.COMPLETED);
    }
}
