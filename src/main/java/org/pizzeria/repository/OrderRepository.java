package org.pizzeria.repository;

import org.pizzeria.common.BaseRepository;
import org.pizzeria.model.Order;

public interface OrderRepository extends BaseRepository {

    long save(Order order);
}
