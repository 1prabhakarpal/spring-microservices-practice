package com.prabhakar.ProductService;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import io.restassured.RestAssured;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class ProductServiceApplicationTests {

    @Container
    @ServiceConnection
    static MongoDBContainer mongoDBContainer =
            new MongoDBContainer("mongo:7.0.5").withExposedPorts(27017);

    @LocalServerPort
    private int port;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;

        // Add some logging to verify container is running
        System.out.println("MongoDB Container URL: " + mongoDBContainer.getReplicaSetUrl());
        System.out.println("Test running on port: " + port);
    }

    @Test
    void shouldCreateProduct() {
        String productRequest = """
                {
                    "name": "iPhone 16",
                    "description": "Latest model of iPhone",
                    "price": 999.99
                }
                """;

        RestAssured.given()
                .contentType("application/json")
                .body(productRequest)
                .when()
                .post("/api/products")
                .then()
                .statusCode(201)
                .body("id", Matchers.notNullValue())
                .body("name", Matchers.equalTo("iPhone 16"))
                .body("description", Matchers.equalTo("Latest model of iPhone"))
                .body("price", Matchers.equalTo(999.99f));
    }

    @Test
    void shouldGetAllProducts() {
        // First create a product to ensure there's data
        String productRequest = """
                {
                    "name": "Test Product",
                    "description": "Test Description",
                    "price": 100.0
                }
                """;

        RestAssured.given()
                .contentType("application/json")
                .body(productRequest)
                .when()
                .post("/api/products")
                .then()
                .statusCode(201);

        // Then get all products
        RestAssured.given()
                .when()
                .get("/api/products")
                .then()
                .statusCode(200)
                .body("size()", Matchers.greaterThan(0));
    }

    @Test
    void contextLoads() {
        // Simple test to verify context loads
        System.out.println("Application context loaded successfully!");
    }
}
