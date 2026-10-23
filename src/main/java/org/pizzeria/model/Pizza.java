package org.pizzeria.model;

import lombok.Data;

@Data
public class Pizza {

    public static final Pizza REGULAR = new Pizza("Regular", PizzaType.REGULAR.name(), new String[7]);

    private String name;
    private String type;
    private String[] ingredients;
    private int ingredientsCount;

    public Pizza(String name, String type, String[] ingredients) {
        this.name = name;
        this.type = type;
        this.ingredients = ingredients;
        for (String ingredient : ingredients) {
            if (ingredient != null) {
                ingredientsCount++;
            }
        }
    }

    public void addIngredient(String ingredient) {
        ingredients[8] = ingredient;
    }
}
