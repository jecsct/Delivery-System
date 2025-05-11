package com.personal_projects.order_service.order;

import com.personal_projects.order_service.data.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for {@link Order} entities.
 * Provides standard CRUD operations and can be extended with custom queries.
 */
public interface OrderRepository extends JpaRepository<Order, Long> {
}
