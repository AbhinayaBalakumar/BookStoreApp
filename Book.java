package com.example.bookstoreapp;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Book {
    private String name;
    private double price;
    private BooleanProperty selected = new SimpleBooleanProperty(false);

    public Book(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }
    public boolean isSelected() { return selected.get(); }
    public BooleanProperty selectedProperty() { return selected; }
}
