package org.pizzeria.model;

import java.util.Objects;

public class Pizza {
    private Long id;
    private String name;
    private String size;
    private double price;
    private boolean vegetarian;

    public Pizza(Long id, String name, String size, double price, boolean vegetarian) {
        this.id = id;
        this.name = name;
        this.size = size;
        this.price = price;
        this.vegetarian = vegetarian;
    }

    public Pizza(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isVegetarian() {
        return vegetarian;
    }

    public void setVegetarian(boolean vegetarian) {
        this.vegetarian = vegetarian;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Pizza pizza = (Pizza) o;
        return Double.compare(price, pizza.price) == 0 && vegetarian == pizza.vegetarian && Objects.equals(id, pizza.id) && Objects.equals(name, pizza.name) && Objects.equals(size, pizza.size);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, size, price, vegetarian);
    }
}
