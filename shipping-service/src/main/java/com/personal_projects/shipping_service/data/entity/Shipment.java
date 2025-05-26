package com.personal_projects.shipping_service.data.entity;


import com.personal_projects.common.Enums.ShippingStatus;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


/**
 * Represents a shipment entity stored in the "shipment" collection in MongoDB.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "shipment")
public class Shipment {

    @Id
    private String shippingId;

    private long orderId;
    private long paymentId;

    private String customerName;
    private String customerAddress;

    private ShippingStatus shippingStatus; // Consider using an enum

    private String carrier;
    private String trackingNumber;

    private LocalDateTime estimatedDeliveryDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    /**
     * Checks whether the shipment is currently in transit.
     *
     * @return true if the shipping status is IN_TRANSIT, false otherwise
     */
    public boolean isInTransit() {
        return this.shippingStatus.equals(ShippingStatus.IN_TRANSIT);
    }
}
