package com.personal_projects.shipping_service.mongo;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;


/**
 * Initializes the MongoDB database when the Spring application context is refreshed.
 * <p>
 * This component listens for {@link ContextRefreshedEvent} and drops the entire MongoDB
 * database configured in {@code application.yml} or {@code application.properties}.
 * </p>
 */
@Component
public class MongoDBInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private final MongoTemplate mongoTemplate;

    /**
     * Constructs a new MongoDBInitializer with the provided {@link MongoTemplate}.
     *
     * @param mongoTemplate the MongoTemplate used to interact with the MongoDB instance
     */
    public MongoDBInitializer(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * Handles the {@link ContextRefreshedEvent}, triggered when the Spring application context is initialized or refreshed.
     *
     * @param event the context refreshed event
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        System.out.println("Initializing MongoDB...");
        mongoTemplate.getDb().drop();
    }
}
