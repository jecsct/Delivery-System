package com.personal_projects.order_service.data.entity;

import com.personal_projects.common.Enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entity representing an order in the system.
 * Stores customer and product details, as well as order status and creation timestamp.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="orders")
public class Order {
    /**
     * Unique identifier for the order (auto-generated).
     */
    @Id
    @SequenceGenerator(
            name = "order_sequence",
            sequenceName = "order_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "order_sequence"
    )
    private long id;

    /**
     * Name of the customer placing the order.
     */
    private String customerName;

    /**
     * Name of the product ordered.
     */
    private String productName;

    /**
     * Quantity of product ordered.
     */
    private int quantity;

    /**
     * Price per unit of the product.
     */
    private double price;

    /**
     * Total price calculated as quantity × unit price.
     */
    private double totalAmount;

    /**
     * Status of the order (e.g., CREATED, PAID, SHIPPED).
     */
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    /**
     * Timestamp when the order was created.
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
