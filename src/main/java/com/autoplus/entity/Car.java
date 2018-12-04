package com.autoplus.entity;

public class Car implements Entity{
    private int id;
    private String brand;
    private String model;
    private String type;
    private String reference;
    private String year;

    public Car(String brand, String model, String type, String reference, String year) {
        this.brand = brand;
        this.model = model;
        this.type = type;
        this.reference = reference;
        this.year = year;
    }
    public Car(Car defCar) {
        this.brand = defCar.brand;
        this.model = defCar.model;
        this.type = defCar.type;
        this.reference = defCar.reference;
        this.year = defCar.year;
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

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
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

        if (getId() != car.getId()) return false;
        if (getBrand() != null ? !getBrand().equals(car.getBrand()) : car.getBrand() != null) return false;
        if (getModel() != null ? !getModel().equals(car.getModel()) : car.getModel() != null) return false;
        if (getType() != null ? !getType().equals(car.getType()) : car.getType() != null) return false;
        if (getReference() != null ? !getReference().equals(car.getReference()) : car.getReference() != null)
            return false;
        return getYear() != null ? getYear().equals(car.getYear()) : car.getYear() == null;
    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + (getBrand() != null ? getBrand().hashCode() : 0);
        result = 31 * result + (getModel() != null ? getModel().hashCode() : 0);
        result = 31 * result + (getType() != null ? getType().hashCode() : 0);
        result = 31 * result + (getReference() != null ? getReference().hashCode() : 0);
        result = 31 * result + (getYear() != null ? getYear().hashCode() : 0);
        return result;
    }
}
