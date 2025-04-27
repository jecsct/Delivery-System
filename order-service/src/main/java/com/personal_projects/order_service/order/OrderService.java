package com.personal_projects.order_service.order;

import com.personal_projects.common.Enums.OrderStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.personal_projects.common.Configs.KafkaConfigs.ORDERS_TOPIC;

@Service
public class OrderService {


    private KafkaTemplate<String, String> kafkaTemplate;

    public OrderService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public List<Order> getOrders(){
        return List.of(Order.builder()
                .id("1")
                .customerName("Joao")
                .productName("Tennis")
                .quantity(1)
                .price(50.0)
                .totalAmount(1.0)
                .status(OrderStatus.CREATED)
                .createdAt(LocalDateTime.now())
                .build());
    }

    public void createOrder(Order order) {
        System.out.println("[LOG] Sent message to kafka: " + order.toString());
        kafkaTemplate.send(ORDERS_TOPIC, order.toString());
    }
}
