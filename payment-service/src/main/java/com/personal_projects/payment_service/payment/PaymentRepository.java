package com.personal_projects.payment_service.payment;


import com.personal_projects.payment_service.data.entity.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * Repository interface for managing {@link Payment} entities in MongoDB.
 * <p>
 * This interface extends {@link MongoRepository} and provides methods for performing CRUD operations
 * on {@link Payment} entities. It also defines custom queries, such as searching for payments by their
 * associated order ID.
 */
public interface PaymentRepository extends MongoRepository<Payment, Long> {
    /**
     * Finds a payment by its associated order ID.
     * <p>
     * This method returns an {@link Optional} containing the {@link Payment} associated with the specified
     * order ID, or an empty {@link Optional} if no payment is found.
     *
     * @param orderId the ID of the order associated with the payment
     * @return an {@link Optional} containing the payment if found, or an empty {@link Optional}
     */
    Optional<Payment> findByOrderId(long orderId);
}
