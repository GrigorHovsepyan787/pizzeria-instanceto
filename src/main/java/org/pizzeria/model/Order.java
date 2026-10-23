package org.pizzeria.model;

import lombok.Data;

import java.util.Map;

@Data
public class Order {

    private Long orderId;
    private final Long customerId;
    private final Map<Pizza, Integer> pizzas;

    public void addPizza(Pizza pizza, int count) {
        pizzas.put(pizza, count);
    }
}
