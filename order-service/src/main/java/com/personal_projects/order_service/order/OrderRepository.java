package com.personal_projects.order_service.order;

import com.personal_projects.common.Enums.OrderStatus;
import com.personal_projects.order_service.data.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repository interface for {@link Order} entities.
 * Provides standard CRUD operations and can be extended with custom queries.
 */
public interface OrderRepository extends JpaRepository<Order, Long> {
    /**
     * Updates the status of an order by its ID.
     * <p>
     * This is a custom modifying query that directly executes an update operation in the database
     * without loading the entity into the persistence context.
     * </p>
     *
     * @param orderId the ID of the order to update
     * @param status the new status to set
     * @return the number of rows affected (should be 0 or 1)
     */
    @Modifying
    @Transactional
    @Query("UPDATE Order o SET o.status = :status WHERE o.id = :orderId")
    int updateOrderStatusById(@Param("orderId") Long orderId, @Param("status") OrderStatus status);
}