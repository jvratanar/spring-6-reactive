package guru.springframework.spring6reactive.controllers;

import guru.springframework.spring6reactive.model.CustomerDTO;
import guru.springframework.spring6reactive.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class CustomerController {
    public static final String CUSTOMER_PATH = "/api/v2/customer";
    public static final String CUSTOMER_PATH_ID = CUSTOMER_PATH + "/{customerId}";

    private final CustomerService customerService;

    @DeleteMapping(CUSTOMER_PATH_ID)
    public Mono<ResponseEntity<Void>> deleteById(@PathVariable("customerId") Integer customerId) {
        return this.customerService
                .getCustomerById(customerId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .map(customerDto -> this.customerService.deleteCustomerById(customerDto.getId()))
                .thenReturn(ResponseEntity.noContent().build());
    }

    @PatchMapping(CUSTOMER_PATH_ID)
    public Mono<ResponseEntity<Void>> patchExistingCustomer(@PathVariable("customerId") Integer customerId,
                                                            @Validated @RequestBody CustomerDTO customerDTO) {
        return this.customerService.patchCustomer(customerId, customerDTO)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .map(savedDto -> {
                    return ResponseEntity.ok().build();
                });
    }

    @PutMapping(CUSTOMER_PATH_ID)
    public Mono<ResponseEntity<Void>> updateExistingCustomer(@PathVariable("customerId") Integer customerId,
                                                             @Validated @RequestBody CustomerDTO customerDTO) {
        return this.customerService.updateCustomer(customerId, customerDTO)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .map(savedDto -> {
                    return ResponseEntity.noContent().build();
                });
    }

    @PostMapping(CUSTOMER_PATH)
    public Mono<ResponseEntity<Void>> createNewCustomer(
            @Validated @RequestBody CustomerDTO customerDto) {
        return this.customerService.saveNewCustomer(customerDto)
                .map(savedDto -> {
                    return ResponseEntity.created(
                            UriComponentsBuilder.fromHttpUrl(
                                            "http://localhost:8080/" + CUSTOMER_PATH + "/" + savedDto.getId())
                                    .build().toUri()
                    ).build();
                });
    }

    @GetMapping(CUSTOMER_PATH_ID)
    public Mono<CustomerDTO> getCustomerById(@PathVariable("customerId") Integer customerId) {
        return this.customerService.getCustomerById(customerId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @GetMapping(CUSTOMER_PATH)
    public Flux<CustomerDTO> listCustomers() {
        return this.customerService.listCustomers();
    }
}
