package com.personal_projects.shipping_service.util;

import com.personal_projects.common.Enums.ShipmentStatus;
import com.personal_projects.common.Events.PaymentEvent;
import com.personal_projects.common.Events.ShipmentEvent;
import com.personal_projects.shipping_service.data.entity.Shipment;

import java.time.LocalDateTime;
import java.util.UUID;

public class ShipmentMapper {

    public static Shipment mapPaymentEventToShipment(PaymentEvent paymentEvent) {
        LocalDateTime now = LocalDateTime.now();

        return Shipment.builder()
                .orderId(paymentEvent.getOrderId())
                .paymentId(paymentEvent.getPaymentId())
                .carrier("DEFAULT_CARRIER")
                .trackingNumber(generateTrackingNumber())
                .estimatedDeliveryDate(estimateDeliveryDate())
                .shipmentStatus(ShipmentStatus.PENDING)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    public static ShipmentEvent mapShipmentToShipmentEvent(Shipment shipment) {
        return ShipmentEvent.builder()
                .shipmentId(shipment.getShippingId())
                .orderId(shipment.getOrderId())
                .shipmentStatus(shipment.getShipmentStatus())
                .build();
    }

    private static LocalDateTime estimateDeliveryDate() {
        return LocalDateTime.now().plusDays(3);
    }

    private static String generateTrackingNumber() {
        return "TRK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

}
