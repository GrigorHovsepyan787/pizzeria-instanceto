package org.pizzeria.repository;

import org.pizzeria.model.Order;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class InMemoryOrderRepository implements OrderRepository {

    private final Map<Long, Order> orders = new HashMap<>();
    private static InMemoryOrderRepository INSTANCE = null;

    private InMemoryOrderRepository() {
    }

    public static InMemoryOrderRepository getInstance() {
        if (Objects.isNull(INSTANCE)) {
            INSTANCE = new InMemoryOrderRepository();
        }

        return INSTANCE;
    }


    private Long initialOrderId = 0L;


    public long save(Order order) {
        Long orderId = generateOrderNumber();
        order.setOrderId(orderId);
        orders.put(orderId, order);
        return orderId;
    }


    private Long generateOrderNumber() {
        return initialOrderId++;
    }

}
