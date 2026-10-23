package org.pizzeria.model;


import lombok.Data;

@Data
public class Customer {
    private long customerId;
    private final String fullName;
    private final String username;

}
