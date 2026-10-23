package org.pizzeria.service;

import org.pizzeria.model.Pizza;

import java.util.Map;

public interface OrderService {
    long orderPizza(String username, Map<Pizza, Integer> pizzas);
}
