package com.personal_projects.common.Events;

import com.personal_projects.common.Enums.ShipmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShipmentEvent {

    private String shipmentId;

    private long orderId;

    private ShipmentStatus shipmentStatus;
}
