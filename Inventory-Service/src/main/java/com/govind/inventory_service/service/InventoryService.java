package com.govind.inventory_service.service;

import com.govind.inventory_service.entity.Inventory;
import com.govind.inventory_service.entity.InventoryReservation;
import com.govind.inventory_service.entity.ReservationStatus;
import com.govind.inventory_service.event.InventoryFailedEvent;
import com.govind.inventory_service.event.InventoryReservedEvent;
import com.govind.inventory_service.producer.InventoryEventProducer;
import com.govind.inventory_service.repository.InventoryRepository;
import com.govind.inventory_service.repository.InventoryReservationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryReservationRepository reservationRepository;
    private final InventoryEventProducer eventProducer;

    @Transactional
    public void processOrder(String orderId, String productId, int quantity) {

        reservationRepository.findByOrderId(orderId).ifPresent(reservation -> {
            log.info("Reservation already exists for orderId={}", orderId);
            return;
        });

        Inventory inventory = inventoryRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (inventory.getAvailableQuantity() < quantity) {

            reservationRepository.save(
                    InventoryReservation.builder()
                            .orderId(orderId)
                            .productId(productId)
                            .quantity(quantity)
                            .status(ReservationStatus.FAILED)
                            .createdAt(Instant.now())
                            .build()
            );

            eventProducer.publishInventoryFailed(
                    new InventoryFailedEvent(orderId, productId, "Insufficient stock")
            );
            return;
        }

        inventory.setAvailableQuantity(
                inventory.getAvailableQuantity() - quantity
        );
        inventoryRepository.save(inventory);

        reservationRepository.save(
                InventoryReservation.builder()
                        .orderId(orderId)
                        .productId(productId)
                        .quantity(quantity)
                        .status(ReservationStatus.RESERVED)
                        .createdAt(Instant.now())
                        .build()
        );

        eventProducer.publishInventoryReserved(
                new InventoryReservedEvent(orderId, productId, quantity)
        );
    }
}
