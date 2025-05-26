package com.personal_projects.shipping_service.util;

import com.personal_projects.common.Enums.OrderStatus;
import com.personal_projects.common.Enums.ShippingStatus;
import com.personal_projects.common.Events.OrderStatusUpdateEvent;
import com.personal_projects.common.Events.ShipmentRequestEvent;
import com.personal_projects.shipping_service.data.dto.ShipmentRequest;
import com.personal_projects.shipping_service.data.entity.Shipment;

import java.time.LocalDateTime;
import java.util.UUID;

public class ShipmentMapper {

    public static Shipment mapToShipment(ShipmentRequestEvent shipmentRequestEvent) {
        LocalDateTime now = LocalDateTime.now();

        return Shipment.builder()
                .orderId(shipmentRequestEvent.getOrderId())
                .paymentId(shipmentRequestEvent.getPaymentId())
                .customerName(shipmentRequestEvent.getCustomerName())
                .customerAddress(shipmentRequestEvent.getCustomerAddress())
                .carrier("DEFAULT_CARRIER")
                .trackingNumber(generateTrackingNumber())
                .estimatedDeliveryDate(estimateDeliveryDate())
                .shippingStatus(ShippingStatus.PENDING)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }



    public static OrderStatusUpdateEvent mapToOrderStatusUpdateEvent(Shipment shipment) {
        return OrderStatusUpdateEvent.builder()
                .orderId(shipment.getOrderId())
                .orderStatus(OrderStatus.SHIPPED)
                .build();
    }

    private static LocalDateTime estimateDeliveryDate() {
        return LocalDateTime.now().plusDays(3);
    }

    private static String generateTrackingNumber() {
        return "TRK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

}
