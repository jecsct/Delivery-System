package com.personal_projects.shipping_service.shipment;

import com.personal_projects.common.Enums.ShipmentStatus;
import com.personal_projects.shipping_service.data.entity.Shipment;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

import java.time.LocalDateTime;
import java.util.Optional;


public interface ShipmentRepository extends MongoRepository<Shipment, Long> {

    /**
     * Find the latest shipment by orderId.
     *
     * @param orderId the order ID
     * @return an optional Shipment
     */
    Optional<Shipment> getShipmentsByOrderId(long orderId);


    /**
     * Updates the shipment status, tracking number, and updatedAt timestamp for a given orderId.
     *
     * @param orderId       the order ID to update
     * @param status        the new shipping status
     * @param trackingNumber the tracking number
     * @param updatedAt     the updated time
     * @return the number of documents updated
     */
    @Modifying
    @Query("{ 'orderId' : ?0 }")
    @Update("{$set: { shippingStatus: ?1, trackingNumber: ?2, updatedAt: ?3 }}")
    long updateStatusByOrderId(
            long orderId,
            ShipmentStatus status,
            String trackingNumber,
            LocalDateTime updatedAt
    );
}
