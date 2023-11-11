package guru.springframework.spring6reactive.controllers;

import guru.springframework.spring6reactive.domain.Customer;
import guru.springframework.spring6reactive.model.CustomerDTO;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureWebTestClient
class CustomerControllerTest {
    @Autowired
    WebTestClient webTestClient;

    public static Customer getTestCustomer() {
        return Customer.builder()
                .customerName("Customer Test")
                .build();
    }

    @Test
    @Order(999)
    void testDeleteCustomer() {
        this.webTestClient.delete().uri(CustomerController.CUSTOMER_PATH_ID, 1)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @Order(3)
    void testUpdateCustomer() {
        this.webTestClient.put().uri(CustomerController.CUSTOMER_PATH_ID, 1)
                .body(Mono.just(getTestCustomer()), CustomerDTO.class)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void testCreateCustomer() {
        this.webTestClient.post().uri(CustomerController.CUSTOMER_PATH)
                .body(Mono.just(getTestCustomer()), CustomerDTO.class)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().location("http://localhost:8080/api/v2/customer/4");
    }

    @Test
    @Order(2)
    void testGetById() {
        this.webTestClient.get().uri(CustomerController.CUSTOMER_PATH_ID, 1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Type", "application/json")
                .expectBody(CustomerDTO.class);
    }

    @Test
    @Order(1)
    void testListCustomers() {
        this.webTestClient.get().uri(CustomerController.CUSTOMER_PATH)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Type", "application/json")
                .expectBody().jsonPath("$.size()").isEqualTo(3);
    }
}