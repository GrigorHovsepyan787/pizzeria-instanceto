package org.pizzeria.repository;

import org.pizzeria.common.BaseRepository;
import org.pizzeria.model.Customer;

import java.util.Optional;

public interface CustomerRepository extends BaseRepository {

    String registerCustomer (Customer customer);

    Optional<Customer> findCustomerByUsername(String username);
}
