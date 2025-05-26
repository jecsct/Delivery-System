package com.personal_projects.shipping_service.shipment;

import com.personal_projects.common.Enums.OrderStatus;
import com.personal_projects.common.Enums.ShippingStatus;
import com.personal_projects.common.Events.OrderStatusUpdateEvent;
import com.personal_projects.shipping_service.data.entity.Shipment;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.personal_projects.common.Configs.KafkaConfigs.ORDER_STATUS_UPDATES_TOPIC;

/**
 * Service class responsible for handling business logic related to shipments.
 */
@Service
public class ShipmentService {

    private static final Logger logger = LoggerFactory.getLogger(ShipmentService.class);

    private final ShipmentRepository shipmentRepository;
    private final KafkaTemplate<String, OrderStatusUpdateEvent> orderStatusUpdateKafkaTemplate;


    /**
     * Constructs the ShipmentService with required dependencies.
     *
     * @param shipmentRepository Repository for interacting with shipment data
     * @param orderStatusUpdateKafkaTemplate Kafka template to publish order status updates
     */
    public ShipmentService(ShipmentRepository shipmentRepository,
                           KafkaTemplate<String, OrderStatusUpdateEvent> orderStatusUpdateKafkaTemplate) {
        this.shipmentRepository = shipmentRepository;
        this.orderStatusUpdateKafkaTemplate = orderStatusUpdateKafkaTemplate;
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
            logger.warn("Shipment with orderId {} is not in PENDING status. Current status: {}", orderId, shipment.getShippingStatus());
            return;
        }

        updateShipment(orderId, ShippingStatus.IN_TRANSIT, shipment);
        sendOrderShipmentStatusUpdateMessage(orderId);
    }

    /**
     * Sends a Kafka message to notify that the order has been shipped.
     *
     * @param orderId the ID of the order whose shipment status is updated
     */
    public void sendOrderShipmentStatusUpdateMessage(final long orderId) {
        orderStatusUpdateKafkaTemplate.send(ORDER_STATUS_UPDATES_TOPIC, new OrderStatusUpdateEvent(orderId, OrderStatus.SHIPPED));
    }

    /**
     * Updates the shipping status of a shipment.
     *
     * @param orderId        the order ID whose shipment should be updated
     * @param shippingStatus new shipping status to set
     * @param shipment       the current shipment object
     */
    public void updateShipment(
            final long orderId, final ShippingStatus shippingStatus, final Shipment shipment
    ) {
        shipmentRepository.updateStatusByOrderId(
                orderId,
                shippingStatus,
                shipment.getTrackingNumber(),
                LocalDateTime.now()
        );

    }

}
