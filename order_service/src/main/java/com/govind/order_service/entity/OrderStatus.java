package com.govind.order_service.entity;

public enum OrderStatus {
    CREATED,
    INVENTORY_RESERVED,
    PAYMENT_SUCCESS,
    SHIPPING_STARTED,
    COMPLETED,
    FAILED
}
