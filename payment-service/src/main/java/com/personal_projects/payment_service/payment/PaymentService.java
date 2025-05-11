package com.personal_projects.payment_service.payment;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing payments.
 * Handles business logic and communicates with the repository and Kafka.
 */
@Service
public class PaymentService {
    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    private final PaymentRepository paymentRepository;


    public PaymentService(final PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public void createPayment(final Payment payment) {
        logger.info("Creating payment: {}", payment);
        paymentRepository.save(payment);
        logger.info("Payment saved to the database");
    }

    public void processPayment(final Long paymentId) {
        logger.info("Processing payment: {}", paymentId);

    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public void savePayment(Payment payment) {
        logger.info("Saving Payment: {}", payment.getId());
        paymentRepository.save(payment);
    }
}
