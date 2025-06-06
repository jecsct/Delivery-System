package com.personal_projects.payment_service.payment;


import com.personal_projects.payment_service.data.dto.PaymentDTO;
import com.personal_projects.payment_service.data.entity.Payment;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
     * Processes a pending payment using the associated order ID and payment details.
     *
     * @param orderId    The ID of the order whose payment is to be processed.
     * @param paymentDTO The payment details submitted by the user.
     */
    @Operation(summary = "Process a pending payment using the associated order ID and payment details")
    @PostMapping("{order_id}")
    public void processPayment(
            @PathVariable("order_id") long orderId,
            @RequestBody PaymentDTO paymentDTO) {
        paymentService.processPayment(orderId, paymentDTO);
    }

}
