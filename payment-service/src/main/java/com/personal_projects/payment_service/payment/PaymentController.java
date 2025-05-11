package com.personal_projects.payment_service.payment;


import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/payment")
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);


    private final PaymentService paymentService;

    @Autowired
    public PaymentController(final PaymentService paymentService) {
        this.paymentService = paymentService;
    }


    /**
     * Endpoint to get all payments.
     * @return List of all payments.
     */
    @GetMapping
    public List<Payment> getAllPayments() {
        return paymentService.getAllPayments();
    }

    /**
     * Processes a payment.
     *
     * @param paymentId the {@link Payment}
     */
    @Operation(summary = "")
    @PostMapping("{id}")
    public void processPayment(@PathVariable("id") Long paymentId){
//        paymentService.processPayment(paymentId);
        paymentService.savePayment(Payment.builder()
                .clientId("client-dfehjkfgwn")
                .amount(199.99)
                .currency("EUR")
                .status(PaymentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build());
    }

}
