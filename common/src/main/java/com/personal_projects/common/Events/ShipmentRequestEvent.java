package com.personal_projects.common.Events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShipmentRequestEvent {

    private long paymentId;

    private long orderId;

    private String customerName;

    private String customerAddress;


}
