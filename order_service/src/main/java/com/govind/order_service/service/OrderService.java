package com.govind.order_service.service;

import com.govind.order_service.dto.CreateOrderRequest;
import com.govind.order_service.dto.OrderResponse;
import com.govind.order_service.entity.Order;
import com.govind.order_service.entity.OrderStatus;
import com.govind.order_service.event.OrderCreatedEvent;
import com.govind.order_service.event.OrderEventProducer;
import com.govind.order_service.exception.InvalidOrderException;
import com.govind.order_service.exception.OrderCreationException;
import com.govind.order_service.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;


@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderEventProducer eventProducer;

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request){

        if(request.getQuantity() <= 0){
            throw new InvalidOrderException("Quantity must be greater than zero");
        }

        try {
            Order order = Order.builder()
                    .userId(request.getUserId())
                    .productId(request.getProductId())
                    .quantity(request.getQuantity())
                    .status(OrderStatus.CREATED)
                    .createdAt(Instant.now())
                    .build();

            Order savedOrder = orderRepository.save(order);

            OrderCreatedEvent event = new OrderCreatedEvent(
                    savedOrder.getOrderId(),
                    request.getProductId(),
                    request.getQuantity()
            );

            eventProducer.publishOrderCreated(event);

            return new OrderResponse(
                    savedOrder.getOrderId(),
                    savedOrder.getStatus().name(),
                    savedOrder.getCreatedAt()
            );

        }catch(Exception ex){
            log.error("Order Creation Failed",ex);
            throw new OrderCreationException("Failed to create order", ex);
        }

    }
}
