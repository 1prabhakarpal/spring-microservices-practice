package com.prabhakar.OrderService.dto;

import java.math.BigDecimal;

public record OrderRequest(String skuCode, BigDecimal price, Integer quantity) {

}
