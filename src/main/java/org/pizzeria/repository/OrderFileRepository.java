package org.pizzeria.repository;

import org.pizzeria.common.JsonWriter;
import org.pizzeria.model.Order;

import java.util.HashMap;
import java.util.Map;

public final class OrderFileRepository implements OrderRepository {

    private static final OrderFileRepository INSTANCE = new OrderFileRepository();
    private final Map<Long, Order> orders = new HashMap<>();
    private long orderId = 0;
    private final JsonWriter jsonWriter = JsonWriter.getInstance();

    private OrderFileRepository() {
        loadOrders();
    }

    public static OrderFileRepository getInstance() {
        return INSTANCE;
    }

    private void loadOrders() {
        for (Order order : jsonWriter.readOrders()) {
            orders.put(order.getOrderId(), order);
            if (order.getOrderId() != null && order.getOrderId() >= orderId) {
                orderId = order.getOrderId() + 1;
            }
        }
    }

    @Override
    public long save(Order order) {
        long id = generateOrderNumber();
        order.setOrderId(id);
        orders.put(id, order);
        jsonWriter.writeOrder(order);
        return id;
    }

    private long generateOrderNumber() {
        return orderId++;
    }
}
