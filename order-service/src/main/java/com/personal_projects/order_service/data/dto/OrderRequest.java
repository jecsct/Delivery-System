package com.personal_projects.order_service.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents an order creation request containing customer and product details.
 * Used as input for creating new orders through the API.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    /** Name of the customer placing the order */
    private String customerName;

    /** Name of the product being ordered */
    private String productName;

    /** Quantity of the product ordered */
    private int quantity;

    /** Unit price of the product */
    private double price;
}
