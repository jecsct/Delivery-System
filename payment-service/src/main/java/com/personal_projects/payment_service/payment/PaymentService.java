package com.personal_projects.payment_service.payment;


import com.personal_projects.common.Enums.PaymentStatus;
import com.personal_projects.payment_service.data.dto.PaymentRequest;
import com.personal_projects.payment_service.data.entity.Payment;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service class responsible for handling business logic related to payments.
 * <p>
 * This includes creating, saving, retrieving, and processing payments. It communicates with the {@link PaymentRepository}
 * and performs necessary validations before marking payments as completed or failed.
 */
@Service
public class PaymentService {
    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    private final PaymentRepository paymentRepository;

    /**
     * Constructs a new {@code PaymentService} with the specified repository.
     *
     * @param paymentRepository the repository used for accessing payment data
     */
    public PaymentService(final PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }


    /**
     * Persists a new payment in the database.
     *
     * @param payment the payment to be created and stored
     */
    public void createPayment(final Payment payment) {
        logger.info("Creating payment: {}", payment);
        paymentRepository.save(payment);
        logger.info("Payment saved to the database");
    }


    /**
     * Processes a payment associated with a specific order ID using the provided payment details.
     * <p>
     * If the payment does not exist, an {@link EntityNotFoundException} is thrown.
     * If the payment is already processed (i.e., not in PENDING status), the method logs a warning and exits.
     * If the provided payment amount does not match the expected amount, the payment is marked as FAILED.
     * If all validations pass, the payment is marked as COMPLETED and saved to the database.
     *
     * @param orderId        the ID of the order associated with the payment
     * @param paymentRequest the payment details submitted by the client (e.g., amount)
     * @throws EntityNotFoundException if no payment is found for the given order ID
     */
    public void processPayment(final Long orderId, final PaymentRequest paymentRequest) {
        logger.info("Processing payment with orderId: {}", orderId);
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found for orderId: " + orderId));

        if (payment.isAlreadyProcessed()) {
            logger.warn("Payment with orderId {} is not in PENDING status. Current status: {}", orderId, payment.getStatus());
            return;
        }

        if (!payment.isAmountMatching(paymentRequest.getAmount())) {
            markPaymentAsFailed(payment, "Amount mismatch");
            logger.warn("Payment FAILED for orderId {} due to amount mismatch. Expected: {}, Received: {}",
                    orderId, payment.getAmount(), paymentRequest.getAmount());
            return;
        }

        markPaymentAsCompleted(payment);
    }

    /**
     * Retrieves all payment records stored in the database.
     *
     * @return a list of all payments
     */
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    /**
     * Saves or updates the provided payment entity in the database.
     *
     * @param payment the payment to be saved
     */
    public void savePayment(Payment payment) {
        logger.info("Saving Payment: {}", payment.toString());
        paymentRepository.save(payment);
    }


    /**
     * Marks the given payment as failed with a failure reason, sets the timestamp,
     * persists the change, and logs the failure.
     *
     * @param payment the payment to mark as failed
     * @param reason  the reason for the failure
     */
    private void markPaymentAsFailed(Payment payment, String reason) {
        payment.setStatus(PaymentStatus.FAILED);
        payment.setPaidAt(LocalDateTime.now());
        paymentRepository.save(payment);
        logger.warn("Payment failed: {}", reason);
    }

    /**
     * Marks the given payment as completed, sets the timestamp,
     * persists the change, and logs the completion.
     *
     * @param payment the payment to mark as completed
     */
    private void markPaymentAsCompleted(Payment payment) {
        payment.setStatus(PaymentStatus.COMPLETED);
        payment.setPaidAt(LocalDateTime.now());
        paymentRepository.save(payment);
        logger.info("Payment completed for orderId {}", payment.getOrderId());
    }

}
