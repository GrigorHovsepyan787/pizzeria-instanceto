package org.pizzeria.repository;

import org.pizzeria.exceptions.DuplicateUsernameException;
import org.pizzeria.model.Customer;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class InMemoryCustomerRepository implements CustomerRepository {

    private final Map<String, Customer> customers = new HashMap<>();
    private long customerId = 0;
    private static InMemoryCustomerRepository INSTANCE;


    private InMemoryCustomerRepository() {
        System.out.println("Initialize only once");
    }


    public static InMemoryCustomerRepository getInstance() {
        if (Objects.isNull(INSTANCE)) {
            INSTANCE = new InMemoryCustomerRepository();
        }

        return INSTANCE;
    }

    public String registerCustomer(Customer customer) {
        boolean isCustomerExist = customers.containsKey(customer.getUsername());
        if (isCustomerExist) {
            throw new DuplicateUsernameException("Username: " + customer.getUsername() + " already exists");
        }
        long customerId = generateCustomerNumber();
        customer.setCustomerId(customerId);
        String username = customer.getUsername();
        customers.put(username, customer);
        return username;
    }


    public Optional<Customer> findCustomerByUsername(String username) {
        return Optional.ofNullable(customers.get(username));
    }

    private long generateCustomerNumber() {
        return customerId++;
    }


}
