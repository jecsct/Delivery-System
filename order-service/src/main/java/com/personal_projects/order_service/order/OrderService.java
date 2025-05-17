package com.personal_projects.order_service.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.personal_projects.common.Enums.OrderStatus;
import com.personal_projects.common.Events.OrderEvent;
import com.personal_projects.order_service.data.dto.OrderRequest;
import com.personal_projects.order_service.data.entity.Order;
import com.personal_projects.order_service.util.OrderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.personal_projects.common.Configs.KafkaConfigs.ORDER_PAYMENT_REQUEST_TOPIC;

/**
 * Service class for managing orders.
 * Handles business logic and communicates with the repository and Kafka.
 */
@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private KafkaTemplate<String, OrderEvent> kafkaTemplate;


    /**
     * Constructs the OrderService with dependencies.
     *
     * @param orderRepository the repository to perform CRUD operations
     * @param kafkaTemplate   the Kafka template used to send messages
     */
    public OrderService(OrderRepository orderRepository,
                        KafkaTemplate<String, OrderEvent> kafkaTemplate) {
        this.orderRepository = orderRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Retrieves all orders from the database.
     *
     * @return a list of all {@link Order} objects
     */
    public List<Order> getAllOrders() {
        logger.info("Fetching all orders from the database");
        return orderRepository.findAll();
    }

    /**
     * Retrieves an order by its ID.
     *
     * @param orderId the ID of the order to retrieve
     * @return the {@link Order} object
     * @throws IllegalStateException if no order is found with the given ID
     */
    public Order getOrderById(Long orderId) {
        logger.info("Fetching order with ID: {}", orderId);
        return orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    logger.error("Order with ID {} not found", orderId);
                    return new IllegalStateException(orderId + "not found");
                });
    }

    /**
     * Creates and saves an order from the request, then publishes it to Kafka.
     *
     * @param orderRequest the order creation request
     */
    public void createOrder(OrderRequest orderRequest) {
        logger.info("Creating order: {}", orderRequest);
        Order order = orderRepository.save(OrderMapper.toOrder(orderRequest));
        logger.info("Order saved to the database");
        logger.debug("Order created: {}", order);
        kafkaTemplate.send(ORDER_PAYMENT_REQUEST_TOPIC, OrderMapper.toOrderEvent(order));
        logger.info("Order published to Kafka topic: {}", ORDER_PAYMENT_REQUEST_TOPIC);
    }

    /**
     * Updates the status of an order by its ID.
     * <p>
     * Delegates to a custom repository query that performs a direct update in the database
     * </p>
     *
     * @param orderId      the ID of the order to update
     * @param orderStatus  the new status to assign to the order
     */
    public void updateOrderStatusById(long orderId, OrderStatus orderStatus) {
        orderRepository.updateOrderStatusById(orderId, orderStatus);
    }
}
