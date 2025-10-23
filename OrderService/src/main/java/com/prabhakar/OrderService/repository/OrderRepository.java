package com.prabhakar.OrderService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.prabhakar.OrderService.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}
