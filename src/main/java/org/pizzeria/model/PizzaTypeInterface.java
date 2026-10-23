package org.pizzeria.model;

public interface PizzaTypeInterface {

    String CALZONE = "CALZONE";
    String REGULAR = "REGULAR";

    static String[] getTypes() {
        return new String[]{CALZONE, REGULAR};
    }
}
