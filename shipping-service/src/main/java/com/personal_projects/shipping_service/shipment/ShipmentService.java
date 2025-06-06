package com.personal_projects.shipping_service.shipment;

import com.personal_projects.common.Enums.ShipmentStatus;
import com.personal_projects.common.Events.ShipmentEvent;
import com.personal_projects.shipping_service.data.entity.Shipment;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.personal_projects.common.Configs.KafkaConfigs.SHIPMENT_TOPIC;

/**
 * Service class responsible for handling business logic related to shipments.
 */
@Service
public class ShipmentService {

    private static final Logger logger = LoggerFactory.getLogger(ShipmentService.class);

    private final ShipmentRepository shipmentRepository;
    private final KafkaTemplate<String, ShipmentEvent> shipmentKafkaTemplate;


    /**
     * Constructs the ShipmentService with required dependencies.
     *
     * @param shipmentRepository Repository for interacting with shipment data
     * @param shipmentKafkaTemplate Kafka template to publish order status updates
     */
    public ShipmentService(ShipmentRepository shipmentRepository,
                           KafkaTemplate<String, ShipmentEvent> shipmentKafkaTemplate) {
        this.shipmentRepository = shipmentRepository;
        this.shipmentKafkaTemplate = shipmentKafkaTemplate;
    }

    /**
     * Retrieves all shipments from the database.
     *
     * @return list of all shipments
     */
    public List<Shipment> getAllShipments() {
        return shipmentRepository.findAll();
    }

    /**
     * Persists a new shipment document.
     *
     * @param shipment the shipment to be stored
     */
    public void createShipment(Shipment shipment){
        shipmentRepository.save(shipment);
    }

    /**
     * Ships an order by changing its status to IN_TRANSIT and sending an ORDER_STATUS_UPDATES_TOPIC Kafka event.
     *
     * @param orderId the ID of the order to ship
     */
    public void shipOrder(final long orderId) {
        Shipment shipment = shipmentRepository.getShipmentsByOrderId(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Shipment not found for orderId: " + orderId));

        if ( shipment.isInTransit() ) {
            logger.warn("Shipment with orderId {} is not in PENDING status. Current status: {}", orderId, shipment.getShipmentStatus());
            return;
        }

        updateShipment(orderId, ShipmentStatus.IN_TRANSIT, shipment);
        publishShipmentEvent(shipment.getShippingId(), orderId);
    }

    /**
     * Sends a Kafka message to notify that the order has been shipped.
     *
     * @param orderId the ID of the order whose shipment status is updated
     */
    public void publishShipmentEvent(final String shipmentId, final long orderId) {
        shipmentKafkaTemplate.send(SHIPMENT_TOPIC, new ShipmentEvent(shipmentId, orderId, ShipmentStatus.IN_TRANSIT));
    }




    /**
     * Updates the shipping status of a shipment.
     *
     * @param orderId        the order ID whose shipment should be updated
     * @param shipmentStatus new shipping status to set
     * @param shipment       the current shipment object
     */
    public void updateShipment(
            final long orderId, final ShipmentStatus shipmentStatus, final Shipment shipment
    ) {
        shipmentRepository.updateStatusByOrderId(
                orderId,
                shipmentStatus,
                shipment.getTrackingNumber(),
                LocalDateTime.now()
        );

    }

}
