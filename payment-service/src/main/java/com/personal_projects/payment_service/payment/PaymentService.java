package com.personal_projects.payment_service.payment;


import com.personal_projects.common.Enums.OrderStatus;
import com.personal_projects.common.Enums.PaymentStatus;
import com.personal_projects.common.Events.OrderStatusUpdateEvent;
import com.personal_projects.common.Events.ShipmentRequestEvent;
import com.personal_projects.payment_service.data.dto.PaymentRequest;
import com.personal_projects.payment_service.data.entity.Payment;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.personal_projects.common.Configs.KafkaConfigs.ORDER_STATUS_UPDATES_TOPIC;
import static com.personal_projects.common.Configs.KafkaConfigs.SHIPMENT_REQUESTS_TOPIC;
import static com.personal_projects.common.Events.OrderStatusUpdateEvent.createEvent;

/**
 * Service class responsible for handling business logic related to payments.
 * <p>
 * This includes creating, saving, retrieving, and processing payments.
 * It interacts with the {@link PaymentRepository} for database operations and communicates
 * with other services via Kafka by publishing {@link OrderStatusUpdateEvent} and {@link ShipmentRequestEvent}.
 * </p>
 */
@Service
public class PaymentService {
    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    private final PaymentRepository paymentRepository;
    private final KafkaTemplate<String, OrderStatusUpdateEvent> orderStatusUpdateKafkaTemplate;
    private final KafkaTemplate<String, ShipmentRequestEvent> shipmentRequestKafkaTemplate;

    /**
     * Constructs a new {@code PaymentService} with the specified dependencies.
     *
     * @param paymentRepository              the repository used for accessing payment data
     * @param orderStatusUpdateKafkaTemplate Kafka template used to send payment status updates
     * @param shipmentRequestKafkaTemplate   Kafka template used to trigger shipment processing
     */
    public PaymentService(final PaymentRepository paymentRepository,
                          KafkaTemplate<String, OrderStatusUpdateEvent> orderStatusUpdateKafkaTemplate,
                          KafkaTemplate<String, ShipmentRequestEvent> shipmentRequestKafkaTemplate) {
        this.paymentRepository = paymentRepository;
        this.orderStatusUpdateKafkaTemplate = orderStatusUpdateKafkaTemplate;
        this.shipmentRequestKafkaTemplate = shipmentRequestKafkaTemplate;
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
     * @param paymentRequest the payment details submitted by the client
     * @throws EntityNotFoundException if no payment is found for the given order ID
     */
    @Transactional
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
            sendFailedOrderPaymentUpdate(orderId);
            return;
        }

        markPaymentAsCompleted(payment);
        sendSuccessfulOrderPaymentUpdate(orderId);
        sendShipmentRequest(ShipmentRequestEvent.builder().orderId(orderId).build());
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
    public void sendFailedOrderPaymentUpdate(long orderId) {
        sendOrderStatusUpdate(new OrderStatusUpdateEvent(orderId, OrderStatus.FAILED));
    }

    /**
     * Sends a Kafka event indicating that payment has been successfully completed.
     *
     * @param orderId the order ID related to the completed payment
     */
    public void sendSuccessfulOrderPaymentUpdate(long orderId) {
        sendOrderStatusUpdate(new OrderStatusUpdateEvent(orderId, OrderStatus.PAID));
    }

    /**
     * Publishes a given order status update event to Kafka.
     *
     * @param event the order status update event
     */
    public void sendOrderStatusUpdate(OrderStatusUpdateEvent event) {
        sendEvent(ORDER_STATUS_UPDATES_TOPIC, event, orderStatusUpdateKafkaTemplate);
    }

    /**
     * Publishes a shipment request event to Kafka.
     *
     * @param event the shipment request event
     */
    public void sendShipmentRequest(ShipmentRequestEvent event) {
        sendEvent(SHIPMENT_REQUESTS_TOPIC, event, shipmentRequestKafkaTemplate);
    }

    /**
     * Generic method to send a Kafka event to a specific topic using the given KafkaTemplate.
     *
     * @param topic         the topic to which the event should be published
     * @param event         the event payload
     * @param kafkaTemplate the KafkaTemplate used to publish the event
     * @param <T>           the type of the event
     */
    private <T> void sendEvent(String topic, T event, KafkaTemplate<String, T> kafkaTemplate) {
        kafkaTemplate.send(topic, event);
    }
}
