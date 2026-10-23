package org.pizzeria.repository;

import org.pizzeria.common.AbstractRepositoryFactory;
import org.pizzeria.common.RepositoryTypes;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class OrderRepositoryFactory extends AbstractRepositoryFactory {

    private static final Map<RepositoryTypes, OrderRepository> ORDER_REPOSITORY_MAP = new HashMap<>();

    static {
        ORDER_REPOSITORY_MAP.put(RepositoryTypes.IN_MEMORY, InMemoryOrderRepository.getInstance());
        ORDER_REPOSITORY_MAP.put(RepositoryTypes.FILE_REPOSITORY, OrderFileRepository.getInstance());
    }

    public OrderRepository getRepositoryByType(RepositoryTypes type) {

        OrderRepository orderRepository = ORDER_REPOSITORY_MAP.get(type);
        if (Objects.isNull(orderRepository)) {
            throw new IllegalStateException("No order repository found for type " + type);
        }

        return orderRepository;
    }


}
