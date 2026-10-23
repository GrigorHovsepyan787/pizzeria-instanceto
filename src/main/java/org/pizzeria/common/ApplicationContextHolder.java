package org.pizzeria.common;


import org.pizzeria.repository.CustomerRepository;
import org.pizzeria.repository.CustomerRepositoryFactory;
import org.pizzeria.repository.OrderRepository;
import org.pizzeria.repository.OrderRepositoryFactory;

import java.util.Map;
import java.util.NoSuchElementException;

public final class ApplicationContextHolder {

    private RepositoryTypes repositoryType = RepositoryTypes.IN_MEMORY;

    public void setRepositoryType(RepositoryTypes repositoryType) {
        this.repositoryType = repositoryType;
    }

    private static final Map<Class<?>, Object> CONTEXT = Map.of(
            CustomerRepository.class, new CustomerRepositoryFactory(),
            OrderRepository.class, new OrderRepositoryFactory()
    );


    public <T> T getObject(Class<T> clazz) {
        Object entry = CONTEXT.get(clazz);

        if (entry == null) {
            throw new NoSuchElementException("No object registered for type: " + clazz.getName());
        }

        if (entry instanceof AbstractRepositoryFactory factory) {
            Object repo = factory.getRepositoryByType(repositoryType);
            return clazz.cast(repo);
        }
        return clazz.cast(entry);
    }
}