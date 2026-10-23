package org.pizzeria.model;


public enum PizzaType {

    REGULAR(1d),
    CALZONE(1.5);

    PizzaType(double value) {
        price = value;
    }

    private double price;

    public double getPrice() {
        return price;
    }
}
