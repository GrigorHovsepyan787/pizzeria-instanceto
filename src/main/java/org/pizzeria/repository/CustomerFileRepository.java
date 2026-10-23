package org.pizzeria.repository;

import org.pizzeria.common.JsonWriter;
import org.pizzeria.exceptions.DuplicateUsernameException;
import org.pizzeria.model.Customer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class CustomerFileRepository implements CustomerRepository {

    private static final CustomerFileRepository INSTANCE = new CustomerFileRepository();
    private final Map<String, Customer> customers = new HashMap<>();
    private long customerId = 0;
    private final JsonWriter jsonWriter = JsonWriter.getInstance();

    private CustomerFileRepository() {
        loadCustomers();
    }

    public static CustomerFileRepository getInstance() {
        return INSTANCE;
    }

    private void loadCustomers() {
        for (Customer customer : jsonWriter.readCustomers()) {
            customers.put(customer.getUsername(), customer);
            if (customer.getCustomerId() >= customerId) {
                customerId = customer.getCustomerId() + 1;
            }
        }
    }

    @Override
    public String registerCustomer(Customer customer) {
        if (customers.containsKey(customer.getUsername())) {
            throw new DuplicateUsernameException("Username: " + customer.getUsername() + " already exists");
        }
        long id = generateCustomerNumber();
        customer.setCustomerId(id);
        customers.put(customer.getUsername(), customer);
        jsonWriter.writeCustomer(customer);
        return customer.getUsername();
    }

    @Override
    public Optional<Customer> findCustomerByUsername(String username) {
        return Optional.ofNullable(customers.get(username));
    }

    private long generateCustomerNumber() {
        return customerId++;
    }
}
