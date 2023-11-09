package guru.springframework.spring6reactive.services;

import guru.springframework.spring6reactive.model.CustomerDTO;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface CustomerService {
    Flux<CustomerDTO> listCustomers();

    Mono<CustomerDTO> getCustomerById(Integer customerId);

    Mono<CustomerDTO> saveNewCustomer(CustomerDTO customerDto);

    Mono<CustomerDTO> updateCustomer(Integer id, CustomerDTO customerDTO);

    Mono<CustomerDTO> patchCustomer(Integer customerId, CustomerDTO customerDTO);

    Mono<Void> deleteCustomerById(Integer customerId);
}
