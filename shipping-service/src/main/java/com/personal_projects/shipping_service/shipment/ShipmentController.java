package com.personal_projects.shipping_service.shipment;

import com.personal_projects.shipping_service.data.dto.ShipmentRequest;
import com.personal_projects.shipping_service.data.entity.Shipment;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for handling shipment-related operations.
 */
@RestController
@RequestMapping("api/v1/shipment")
public class ShipmentController {

    private static final Logger logger = LoggerFactory.getLogger(ShipmentController.class);

    private ShipmentService shipmentService;

    /**
     * Constructor for injecting the ShipmentService dependency.
     *
     * @param shipmentService the service handling business logic for shipments
     */
    @Autowired
    public ShipmentController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    /**
     * Retrieve all shipments in the system.
     *
     * @return a list of all {@link Shipment} records
     */
    @Operation(summary = "Get all shipments", description = "Retrieve a list of all existing shipments")
    @GetMapping
    public List<Shipment> getAllShipments() {
        logger.info("Fetching all shipments");
        return shipmentService.getAllShipments();
    }

    /**
     * Trigger the shipment process for an order by order ID.
     *
     * @param orderId the unique identifier of the order to be shipped
     */
    @Operation(summary = "")
    @PostMapping("{order_id}/ship_order")
    public void shipOrder(
            @PathVariable("order_id") long orderId) {
        shipmentService.shipOrder(orderId);
    }

}
