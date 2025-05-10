package com.personal_projects.payment_service.payment;


import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
     * Processes a payment.
     *
     * @param paymentId the {@link Payment}
     */
    @Operation(summary = "")
    @PostMapping("{id}")
    public void processPayment(@PathVariable("id") Long paymentId){

        paymentService.processPayment(paymentId);
    }

}
