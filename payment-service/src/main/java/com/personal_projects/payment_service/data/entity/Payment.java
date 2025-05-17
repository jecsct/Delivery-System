package com.personal_projects.payment_service.data.entity;


import com.personal_projects.common.Enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "payments")  // MongoDB collection name
public class Payment {

    /**
     * MongoDB ObjectId — automatically generated if null.
     */
    @Id
    private String id;

    /**
     * ID of the client making the payment.
     */
    private String clientName;

    /**
     * Amount paid.
     */
    private double amount;

    /**
     * Status of the payment (e.g., PENDING, COMPLETED).
     */
    private PaymentStatus status;

    /**
     * Timestamp when the payment was created.
     */
    private LocalDateTime createdAt;

    /**
     * Timestamp when the payment was paid.
     */
    private LocalDateTime paidAt;

    /**
     * ID of the order referent to this payment.
     */
    private long orderId;

    /**
     * Checks if the payment has already been processed.
     * <p>
     * A payment is considered processed if its status is not {@link PaymentStatus#PENDING}.
     *
     * @return {@code true} if the payment status is not {@code PENDING}, otherwise {@code false}
     */
    public boolean isAlreadyProcessed() {
        return this.getStatus() != PaymentStatus.PENDING;
    }

    /**
     * Compares the provided amount with the payment's amount.
     * <p>
     * This method is used to check if the provided amount matches the expected amount for the payment.
     *
     * @param amount the amount to compare against the payment's amount
     * @return {@code true} if the amounts match, otherwise {@code false}
     */
    public boolean isAmountMatching(double amount) {
        return this.amount == amount;
    }

    public void completePayment(){
        this.setStatus(PaymentStatus.COMPLETED);
        this.setPaidAt(LocalDateTime.now());
    }

    public void failPayment() {
        this.setStatus(PaymentStatus.FAILED);
        this.setPaidAt(LocalDateTime.now());
    }
}