package com.prabhakar.OrderService;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import io.restassured.RestAssured;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class OrderServiceApplicationTests {

    @Container
    @ServiceConnection
    static MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.3.0");

    @LocalServerPort
    private int port;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    static {
        mysqlContainer.start();
    }

    @Test
    void shouldPlaceOrder() {
        String orderRequest = """
                {
                    "skuCode": "iphone_16",
                    "quantity": 2,
                    "price": 1000
                }
                """;

        given().contentType("application/json")
                .body(orderRequest)
                .when()
                .post("/api/order")
                .then()
                .statusCode(200)
                .body(containsString("Order placed successfully with Order Number:"));
    }
}
