package com.prabhakar.ProductService.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.prabhakar.ProductService.dto.ProductRequest;
import com.prabhakar.ProductService.dto.ProductResponse;
import com.prabhakar.ProductService.model.Product;
import com.prabhakar.ProductService.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;

  public ProductResponse createProduct(ProductRequest productRequest) {
    Product product = Product.builder()
        .name(productRequest.getName())
        .description(productRequest.getDescription())
        .price(productRequest.getPrice())
        .build();

    Product product1 = productRepository.save(product);
    log.info("Product {} is saved", product1.getId());
    return mapToProductResponse(product1);
  }

  public List<ProductResponse> getAllProducts() {
    List<Product> products = productRepository.findAll();
    return products.stream().map(product -> mapToProductResponse(product)).toList();
  }

  public ProductResponse mapToProductResponse(Product product) {
    return ProductResponse.builder()
        .id(product.getId())
        .name(product.getName())
        .description(product.getDescription())
        .price(product.getPrice())
        .build();
  }
}
