package com.prabhakar.ProductService.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.prabhakar.ProductService.dto.ProductRequest;
import com.prabhakar.ProductService.dto.ProductResponse;
import com.prabhakar.ProductService.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Product Management", description = "APIs for managing products")
public class ProductController {

  private final ProductService productService;

  @Operation(summary = "Create a new product", description = "Add a new product to the system")
  @PostMapping
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Product created successfully",
          content = @Content(schema = @Schema(implementation = ProductResponse.class))),
      @ApiResponse(responseCode = "400", description = "Invalid request data",
          content = @Content(schema = @Schema()))})
  public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest productRequest) {
    return new ResponseEntity<>(productService.createProduct(productRequest),
        org.springframework.http.HttpStatus.CREATED);
  }

  @Operation(summary = "Get all products", description = "Retrieve a list of all products")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "List of products retrieved successfully",
          content = @Content(schema = @Schema(implementation = ProductResponse.class))),
      @ApiResponse(responseCode = "500", description = "Internal server error",
          content = @Content(schema = @Schema()))})
  @GetMapping
  public ResponseEntity<List<ProductResponse>> getAllProducts() {
    return ResponseEntity.ok(productService.getAllProducts());
  }

}
