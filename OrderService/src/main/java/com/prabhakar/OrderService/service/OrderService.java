package com.prabhakar.OrderService.service;

import java.util.UUID;
import org.springframework.stereotype.Service;
import com.prabhakar.OrderService.Order;
import com.prabhakar.OrderService.dto.OrderRequest;
import com.prabhakar.OrderService.dto.OrderResponse;
import com.prabhakar.OrderService.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderResponse placeOrder(OrderRequest orderRequest) {
        Order order = Order.builder()
                .orderNumber(UUID.randomUUID().toString())
                .skuCode(orderRequest.skuCode())
                .price(orderRequest.price())
                .quantity(orderRequest.quantity())
                .build();
        Order savedOrder = orderRepository.save(order);
        log.info("Order {} is saved", savedOrder.getId());
        return new OrderResponse(savedOrder.getId(), savedOrder.getOrderNumber(),
                savedOrder.getSkuCode(), savedOrder.getPrice(), savedOrder.getQuantity());

    }


}
