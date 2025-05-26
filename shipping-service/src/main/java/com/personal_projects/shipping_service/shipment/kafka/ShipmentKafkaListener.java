package com.personal_projects.shipping_service.shipment.kafka;

import com.personal_projects.common.Enums.OrderStatus;
import com.personal_projects.common.Events.OrderStatusUpdateEvent;
import com.personal_projects.common.Events.ShipmentRequestEvent;
import com.personal_projects.shipping_service.data.entity.Shipment;
import com.personal_projects.shipping_service.shipment.ShipmentService;
import com.personal_projects.shipping_service.util.ShipmentMapper;
import jakarta.persistence.SequenceGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static com.personal_projects.common.Configs.KafkaConfigs.ORDER_STATUS_UPDATES_TOPIC;
import static com.personal_projects.common.Configs.KafkaConfigs.SHIPMENT_REQUESTS_TOPIC;

/**
 * Kafka listener component for processing {@link ShipmentRequestEvent} messages.
 * <p>
 * When a shipment request event is received, a new {@link Shipment} is created in the database.
 * </p>
 */
@Component
public class ShipmentKafkaListener {

    private final ShipmentService shipmentService;

    /**
     * Constructs the Kafka listener with a reference to the {@link ShipmentService}.
     *
     * @param shipmentService the service used to handle shipment-related logic.
     */
    @Autowired
    public ShipmentKafkaListener(final ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    /**
     * Kafka listener method for handling {@link ShipmentRequestEvent} messages.
     *
     * @param shipmentServiceEvent the incoming shipment request event from Kafka.
     */
    @KafkaListener(
            topics = SHIPMENT_REQUESTS_TOPIC,
            groupId = "groupId",
            containerFactory = "kafkaListenerContainerFactory"
    )
    void listener(ShipmentRequestEvent shipmentServiceEvent) {
        System.out.println("Listener Received: " + shipmentService);

        shipmentService.createShipment(ShipmentMapper.mapToShipment(shipmentServiceEvent));
    }
}
