package com.personal_projects.shipping_service.data.dto;

import com.personal_projects.common.Enums.ShippingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for creating or requesting shipment details.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShipmentRequest {

    private long orderId;

    private ShippingStatus shippingStatus;

}
