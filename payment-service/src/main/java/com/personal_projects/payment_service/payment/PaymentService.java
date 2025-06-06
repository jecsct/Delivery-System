package com.personal_projects.payment_service.payment;


import com.personal_projects.common.Enums.PaymentStatus;
import com.personal_projects.common.Events.PaymentEvent;
import com.personal_projects.common.Events.ShipmentEvent;
import com.personal_projects.payment_service.data.dto.PaymentDTO;
import com.personal_projects.payment_service.data.entity.Payment;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.personal_projects.common.Configs.KafkaConfigs.PAYMENT_TOPIC;

/**
 * Service class responsible for handling business logic related to payments.
 * <p>
 * This includes creating, saving, retrieving, and processing payments.
 * It interacts with the {@link PaymentRepository} for database operations and communicates
 * with other services via Kafka by publishing {@link PaymentEvent} and {@link ShipmentEvent}.
 * </p>
 */
@Service
public class PaymentService {
    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    private final PaymentRepository paymentRepository;
    private final KafkaTemplate<String, PaymentEvent> paymentKafkaTemplate;

    /**
     * Constructs a new {@code PaymentService} with the specified dependencies.
     *
     * @param paymentRepository              the repository used for accessing payment data
     * @param paymentKafkaTemplate Kafka template used to send payment status updates
     */
    public PaymentService(final PaymentRepository paymentRepository,
                          KafkaTemplate<String, PaymentEvent> paymentKafkaTemplate) {
        this.paymentRepository = paymentRepository;
        this.paymentKafkaTemplate = paymentKafkaTemplate;
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
     * - If the payment is not found, throws {@link EntityNotFoundException}. <br>
     * - If the payment is already processed, the method exits early. <br>
     * - If the amount does not match, the payment is marked as failed. <br>
     * - On success, marks the payment as completed, sends a status update, and triggers shipment.
     * </p>
     *
     * @param orderId        the ID of the order associated with the payment
     * @param paymentDTO the payment details submitted by the client
     * @throws EntityNotFoundException if no payment is found for the given order ID
     */
    @Transactional
    public void processPayment(final Long orderId, final PaymentDTO paymentDTO) {
        logger.info("Processing payment with orderId: {}", orderId);
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found for orderId: " + orderId));

        if (payment.isAlreadyProcessed()) {
            logger.warn("Payment with orderId {} is not in PENDING status. Current status: {}", orderId, payment.getStatus());
            return;
        }

        if (!payment.isAmountMatching(paymentDTO.getAmount())) {
            markPaymentAsFailed(payment, "Amount mismatch");
            logger.warn("Payment FAILED for orderId {} due to amount mismatch. Expected: {}, Received: {}",
                    orderId, payment.getAmount(), paymentDTO.getAmount());
            publishPaymentFailedEvent(payment.getId(), orderId);
            return;
        }

        markPaymentAsCompleted(payment);
        publishPaymentCompleteEvent(payment.getId(), orderId);
    }

    /**
     * Retrieves all payment records stored in the database.
     *
     * @return a list of all {@link Payment} entities
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
        logger.info("Saving Payment: {}", payment);
        paymentRepository.save(payment);
    }

    /**
     * Marks the given payment as failed with a failure reason,
     * persists the change, and logs the failure.
     *
     * @param payment the payment to mark as failed
     * @param reason  the reason for the failure
     */
    private void markPaymentAsFailed(Payment payment, String reason) {
        payment.failPayment();
        paymentRepository.save(payment);
        logger.warn("Payment failed: {}", reason);
    }

    /**
     * Marks the given payment as completed,
     * persists the change, and logs the success.
     *
     * @param payment the payment to mark as completed
     */
    private void markPaymentAsCompleted(Payment payment) {
        payment.completePayment();
        paymentRepository.save(payment);
        logger.info("Payment completed for orderId {}", payment.getOrderId());
    }

    /**
     * Sends a Kafka event indicating that payment has failed.
     *
     * @param orderId the order ID related to the failed payment
     */
    public void publishPaymentFailedEvent(String paymentId, long orderId) {
        publishPaymentEvent(new PaymentEvent(paymentId, orderId, PaymentStatus.FAILED));
    }


    public void publishPaymentCompleteEvent(String paymentId, long orderId) {
        publishPaymentEvent(new PaymentEvent(paymentId, orderId, PaymentStatus.COMPLETED));
    }

    public void publishPaymentEvent(PaymentEvent event) {
        sendEvent(event, paymentKafkaTemplate);
    }


    /**
     * Generic method to send a Kafka event to a specific topic using the given KafkaTemplate.
     *
     * @param <T>           the type of the event
     * @param event         the event payload
     * @param kafkaTemplate the KafkaTemplate used to publish the event
     */
    private <T> void sendEvent(T event, KafkaTemplate<String, T> kafkaTemplate) {
        kafkaTemplate.send(PAYMENT_TOPIC, event);
    }
}
