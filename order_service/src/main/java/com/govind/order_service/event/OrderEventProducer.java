package com.govind.order_service.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventProducer {
    private final KafkaTemplate<String,Object> kafkaTemplate;

    public void publishOrderCreated(OrderCreatedEvent event){
        try{
            kafkaTemplate.send("order.created",event.getOrderId(),event);
        }catch(Exception ex){
            log.error("Failed to publish order.created event for orderId={}", event.getOrderId(), ex);
            throw ex;
        }
    }
}
