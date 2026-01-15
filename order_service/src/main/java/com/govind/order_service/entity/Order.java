package com.govind.order_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String orderId;

    private String userId;

    private String productId;

    private int quantity;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private Instant createdAt;
}
