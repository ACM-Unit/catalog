package com.autoplus.entity;

import javax.sql.DataSource;
import java.util.List;
import java.util.Objects;

public class Car {
    private int id;
    private String brand;
    private String model;
    private String type;
    private String reference;

    public Car(String brand, String model, String type, String reference) {
        this.brand = brand;
        this.model = model;
        this.type = type;
        this.reference = reference;
    }
    public Car(Car defCar) {
        this.brand = defCar.brand;
        this.model = defCar.model;
        this.type = defCar.type;
        this.reference = defCar.reference;
    }
    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getType() {
        return type;
    }

    public void setType(String modification) {
        this.type = modification;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Car)) return false;
        Car car = (Car) o;
        return id == car.id &&
                Objects.equals(brand, car.brand) &&
                Objects.equals(model, car.model) &&
                Objects.equals(type, car.type) &&
                Objects.equals(reference, car.reference);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, brand, model, type, reference);
    }
}
