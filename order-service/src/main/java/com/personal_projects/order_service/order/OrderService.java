package com.personal_projects.order_service.order;

import com.personal_projects.common.Enums.OrderStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

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

}
