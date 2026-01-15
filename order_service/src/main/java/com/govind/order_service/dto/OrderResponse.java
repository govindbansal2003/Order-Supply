package com.govind.order_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class OrderResponse {
    private String orderId;
    private String status;
    private Instant createdAt;
}
