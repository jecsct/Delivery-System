package com.personal_projects.payment_service.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a payment processing request.
 * Used as input for processing a payment through the API.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {
    /** The ID of the order associated with the payment */
    private long orderId;

    /** The payment method chosen by the customer (e.g., CREDIT_CARD, PAYPAL, etc.) */
    private String paymentMethod;

    /** The amount to be paid (can be validated server-side based on order) */
    private double amount;
}