package com.govind.inventory_service.producer;


import com.govind.inventory_service.event.InventoryFailedEvent;
import com.govind.inventory_service.event.InventoryReservedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class InventoryEventProducer {

    private final KafkaTemplate<String,Object> kafkaTemplate;

    public void publishInventoryReserved(InventoryReservedEvent event) {
        kafkaTemplate.send("inventory.reserved", event.getOrderId(), event);
    }

    public void publishInventoryFailed(InventoryFailedEvent event) {
        kafkaTemplate.send("inventory.failed", event.getOrderId(), event);
    }

}
