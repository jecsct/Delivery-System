package com.personal_projects.payment_service.mongo;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class MongoDBInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private final MongoTemplate mongoTemplate;

    public MongoDBInitializer(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        System.out.println("Initializing MongoDB...");
        mongoTemplate.getDb().drop();
    }
}
