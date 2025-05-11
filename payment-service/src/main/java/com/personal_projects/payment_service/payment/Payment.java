package com.personal_projects.payment_service.payment;


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
    private String clientId;

    /**
     * Amount paid.
     */
    private double amount;

    /**
     * Currency of the payment (e.g., EUR, USD).
     */
    private String currency;

    /**
     * Status of the payment (e.g., PENDING, COMPLETED).
     */
    private PaymentStatus status;

    /**
     * Timestamp when the payment was created.
     */
    private LocalDateTime createdAt;
}