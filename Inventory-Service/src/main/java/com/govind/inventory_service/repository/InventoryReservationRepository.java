package com.govind.inventory_service.repository;

import com.govind.inventory_service.entity.InventoryReservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryReservationRepository
        extends JpaRepository<InventoryReservation, String> {

    Optional<InventoryReservation> findByOrderId(String orderId);
}
