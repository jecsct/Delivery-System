package com.personal_projects.shipping_service.data.dto;

import com.personal_projects.common.Enums.ShipmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating or requesting shipment details.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShipmentDTO {

    private long orderId;

    private ShipmentStatus shipmentStatus;

}
