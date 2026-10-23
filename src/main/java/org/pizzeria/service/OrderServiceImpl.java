package org.pizzeria.service;

import org.pizzeria.common.ApplicationContextHolder;
import org.pizzeria.exceptions.UserNotFoundException;
import org.pizzeria.model.Customer;
import org.pizzeria.model.Order;
import org.pizzeria.model.Pizza;
import org.pizzeria.repository.CustomerRepository;
import org.pizzeria.repository.OrderRepository;

import java.util.Map;

public class OrderServiceImpl implements OrderService {


    private final CustomerRepository customerRepository;


    private final OrderRepository orderRepository;

    public OrderServiceImpl(ApplicationContextHolder contextHolder) {
        this.customerRepository = contextHolder.getObject(CustomerRepository.class);
        this.orderRepository = contextHolder.getObject(OrderRepository.class);
    }

    @Override
    public long orderPizza(String username, Map<Pizza, Integer> pizzas) {

        Customer customer = customerRepository.findCustomerByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));

        Order order = new Order(customer.getCustomerId(), pizzas);
        return orderRepository.save(order);
    }
}
