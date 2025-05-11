package com.personal_projects.order_service.order;
import com.personal_projects.order_service.data.dto.OrderRequest;
import com.personal_projects.order_service.data.entity.Order;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Order Controller", description = "Manages order operations")
@RestController
@RequestMapping(path = "api/v1/order")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);


    private final OrderService orderService;

    /**
     * Constructs the OrderController with the required OrderService dependency.
     *
     * @param orderService the service that handles order business logic
     */
    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Retrieves a list of all existing orders.
     *
     * @return a list of {@link Order} objects
     */
    @Operation(summary = "Retrieves a list of all existing orders.")
    @GetMapping()
    public List<Order> getOrders(){
        return orderService.getAllOrders();
    }

    /**
     * Retrieves a specific order by its ID.
     *
     * @param orderId the ID of the order to retrieve
     * @return the {@link Order} object with the specified ID
     * "@throws OrderNotFoundException" if no order exists with the given ID (if applicable)
     */
    @Operation(summary = "Retrieves a specific order by its ID.")
    @GetMapping("{id}")
    public Order getOrderById(@PathVariable("id") Long orderId){
        logger.info("Received request for Order by id: {} ", orderId);
        return orderService.getOrderById(orderId);
    }

    /**
     * Creates a new order.
     *
     * @param orderRequest the {@link OrderRequest} object to be created
     */
    @Operation(summary = "Creates a new order")
    @PostMapping
    public void createOrder(@RequestBody OrderRequest orderRequest){
        orderService.createOrder(orderRequest);
    }

}
