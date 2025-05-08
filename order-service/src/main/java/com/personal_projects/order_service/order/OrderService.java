package com.personal_projects.order_service.order;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.personal_projects.common.Configs.KafkaConfigs.ORDERS_TOPIC;

@Service
public class OrderService {


    private final OrderRepository orderRepository;
    private KafkaTemplate<String, String> kafkaTemplate;


    public OrderService(OrderRepository orderRepository,
            KafkaTemplate<String, String> kafkaTemplate) {
        this.orderRepository = orderRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }

    public Order getAllOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalStateException(orderId + "not found"));
    }

    public void createOrder(Order order) {
        System.out.println("[LOG] Sent message to kafka: " + order.toString());
        kafkaTemplate.send(ORDERS_TOPIC, order.toString());
        orderRepository.save(order);
    }

}
