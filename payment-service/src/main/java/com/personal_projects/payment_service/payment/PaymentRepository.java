package com.personal_projects.payment_service.payment;


import org.springframework.data.mongodb.repository.MongoRepository;

public interface PaymentRepository extends MongoRepository<Payment, Long> {
}
