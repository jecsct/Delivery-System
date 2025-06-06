package com.personal_projects.shipping_service.shipment.kafka;

import com.personal_projects.common.Events.PaymentEvent;
import com.personal_projects.common.Events.ShipmentEvent;
import com.personal_projects.shipping_service.data.entity.Shipment;
import com.personal_projects.shipping_service.shipment.ShipmentService;
import com.personal_projects.shipping_service.util.ShipmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static com.personal_projects.common.Configs.KafkaConfigs.PAYMENT_TOPIC;

/**
 * Kafka listener for handling events related to shipments.
 *
 * <p>This listener consumes {@link PaymentEvent} messages from the Kafka topic
 * defined by {@code PAYMENT_TOPIC} and processes them using the {@link ShipmentService}.</p>
 */
@Component
public class ShipmentKafkaListener {

    private final ShipmentService shipmentService;

    /**
     * Constructs a new {@code ShipmentKafkaListener} with the given {@link ShipmentService}.
     *
     * @param shipmentService the service responsible for shipment business logic
     */
    @Autowired
    public ShipmentKafkaListener(final ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    /**
     * Kafka listener method that is invoked when a {@link PaymentEvent} is received from the configured topic.
     * It processes the payment event and creates a shipment using the shipment service.
     *
     * @param paymentEvent the payment event received from Kafka
     */
    @KafkaListener(
            topics = PAYMENT_TOPIC,
            groupId = "shipment-service-group",
            containerFactory = "shipmentKafkaListenerContainerFactory"
    )
    void listener(PaymentEvent paymentEvent) {
        System.out.println("Listener Received: " + paymentEvent);
        shipmentService.createShipment(ShipmentMapper.mapPaymentEventToShipment(paymentEvent));
    }
}
