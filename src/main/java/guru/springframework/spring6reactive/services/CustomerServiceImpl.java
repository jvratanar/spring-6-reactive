package guru.springframework.spring6reactive.services;

import guru.springframework.spring6reactive.mappers.CustomerMapper;
import guru.springframework.spring6reactive.model.CustomerDTO;
import guru.springframework.spring6reactive.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public Flux<CustomerDTO> listCustomers() {
        return this.customerRepository.findAll()
                .map(this.customerMapper::customerToCustomerDTO);
    }

    @Override
    public Mono<CustomerDTO> getCustomerById(Integer customerId) {
        return this.customerRepository.findById(customerId)
                .map(this.customerMapper::customerToCustomerDTO);
    }

    @Override
    public Mono<CustomerDTO> saveNewCustomer(CustomerDTO customerDto) {
        return this.customerRepository.save(
                this.customerMapper.customerDTOToCustomer(customerDto)
        ).map(this.customerMapper::customerToCustomerDTO);
    }

    @Override
    public Mono<CustomerDTO> updateCustomer(Integer customerId, CustomerDTO customerDTO) {
        return this.customerRepository.findById(customerId)
                .map(foundCustomer -> {
                    foundCustomer.setCustomerName(customerDTO.getCustomerName());

                    return foundCustomer;
                }).flatMap(this.customerRepository::save)
                .map(this.customerMapper::customerToCustomerDTO);
    }

    @Override
    public Mono<CustomerDTO> patchCustomer(Integer customerId, CustomerDTO customerDTO) {
        return this.customerRepository.findById(customerId)
                .map(foundCustomer -> {
                    if (StringUtils.hasText(customerDTO.getCustomerName())) {
                        foundCustomer.setCustomerName(customerDTO.getCustomerName());
                    }

                    return foundCustomer;
                }).flatMap(this.customerRepository::save)
                .map(this.customerMapper::customerToCustomerDTO);
    }

    @Override
    public Mono<Void> deleteCustomerById(Integer customerId) {
        return this.customerRepository.deleteById(customerId);
    }
}
