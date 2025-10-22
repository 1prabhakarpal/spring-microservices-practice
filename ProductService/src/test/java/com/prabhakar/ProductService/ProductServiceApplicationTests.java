package com.prabhakar.ProductService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mongodb.MongoDBContainer;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prabhakar.ProductService.dto.ProductRequest;
import com.prabhakar.ProductService.model.Product;
import com.prabhakar.ProductService.repository.ProductRepository;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

  @Container
  static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0.13").withReuse(true);

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private ProductRepository productRepository;

  @DynamicPropertySource
  static void setProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
  }

  @AfterEach
  void tearDown() {
    productRepository.deleteAll();
  }

  @Test
  void createProduct() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.post("/api/products")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(getProductRequest())))
        .andExpect(status().isCreated());

    assertEquals(1, productRepository.findAll().size(), "Product should be saved in MongoDB");
  }

  @Test
  void getAllProducts() throws Exception {
    productRepository.save(Product.builder()
        .name("Test Product")
        .description("Sample product for GET test")
        .price(BigDecimal.valueOf(123.45))
        .build());

    MvcResult result =
        mockMvc.perform(MockMvcRequestBuilders.get("/api/products").contentType("application/json"))
            .andExpect(status().isOk())
            .andReturn();

    String jsonResponse = result.getResponse().getContentAsString();
    List<Product> products =
        objectMapper.readValue(jsonResponse, new TypeReference<List<Product>>() {});
    assertEquals(1, products.size(), "Should return one product");
    assertEquals("Test Product", products.get(0).getName(), "Product name should match");
  }

  private ProductRequest getProductRequest() {
    return ProductRequest.builder()
        .name("Test Product")
        .description("This is a test product.")
        .price(BigDecimal.valueOf(99.99))
        .build();
  }
}
