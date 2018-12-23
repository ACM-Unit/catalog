package com.autoplus.entity;

import java.util.Objects;

public class Modification {
    private Car parent;
    private String name;
    private String engineType;
    private String engineModel;
    private String engineCapacity;
    private String power;
    private String drive;
    private String date;
    private String reference;
    private int id;
    private String description;
    private String startYear;
    private String endYear;
    private String bodyType;
    private String driveType;
    private String capacity;
    private String capacityTax;
    private String capacityTech;
    private String fuelMixture;
    private String fuelType;
    private String numberOfCylinders;
    private String numberOfValves;
    private String constructionInterval;
    private String engineCode;
    private String startYearMonth;
    private String endYearMonth;

    public Modification(Car parent, String name, String engineType, String engineModel, String engineCapacity, String power, String drive, String date, String reference, int id, String description, String startYear, String endYear, String bodyType, String driveType, String capacity, String capacityTax, String capacityTech, String fuelMixture, String fuelType, String numberOfCylinders, String numberOfValves, String constructionInterval, String engineCode, String startYearMonth, String endYearMonth) {
        this.parent = parent;
        this.name = name;
        this.engineType = engineType;
        this.engineModel = engineModel;
        this.engineCapacity = engineCapacity;
        this.power = power;
        this.drive = drive;
        this.date = date;
        this.reference = reference;
        this.id = id;
        this.description = description;
        this.startYear = startYear;
        this.endYear = endYear;
        this.bodyType = bodyType;
        this.driveType = driveType;
        this.capacity = capacity;
        this.capacityTax = capacityTax;
        this.capacityTech = capacityTech;
        this.fuelMixture = fuelMixture;
        this.fuelType = fuelType;
        this.numberOfCylinders = numberOfCylinders;
        this.numberOfValves = numberOfValves;
        this.constructionInterval = constructionInterval;
        this.engineCode = engineCode;
        this.startYearMonth = startYearMonth;
        this.endYearMonth = endYearMonth;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Car getParent() {
        return parent;
    }

    public void setParent(Car parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEngineType() {
        return engineType;
    }

    public void setEngineType(String engineType) {
        this.engineType = engineType;
    }

    public String getEngineModel() {
        return engineModel;
    }

    public void setEngineModel(String engineModel) {
        this.engineModel = engineModel;
    }

    public String getEngineCapacity() {
        return engineCapacity;
    }

    public void setEngineCapacity(String engineCapacity) {
        this.engineCapacity = engineCapacity;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getDrive() {
        return drive;
    }

    public void setDrive(String drive) {
        this.drive = drive;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartYear() {
        return startYear;
    }

    public void setStartYear(String startYear) {
        this.startYear = startYear;
    }

    public String getEndYear() {
        return endYear;
    }

    public void setEndYear(String endYear) {
        this.endYear = endYear;
    }

    public String getBodyType() {
        return bodyType;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }

    public String getDriveType() {
        return driveType;
    }

    public void setDriveType(String driveType) {
        this.driveType = driveType;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getCapacityTax() {
        return capacityTax;
    }

    public void setCapacityTax(String capacityTax) {
        this.capacityTax = capacityTax;
    }

    public String getCapacityTech() {
        return capacityTech;
    }

    public void setCapacityTech(String capacityTech) {
        this.capacityTech = capacityTech;
    }

    public String getFuelMixture() {
        return fuelMixture;
    }

    public void setFuelMixture(String fuelMixture) {
        this.fuelMixture = fuelMixture;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getNumberOfCylinders() {
        return numberOfCylinders;
    }

    public void setNumberOfCylinders(String numberOfCylinders) {
        this.numberOfCylinders = numberOfCylinders;
    }

    public String getNumberOfValves() {
        return numberOfValves;
    }

    public void setNumberOfValves(String numberOfValves) {
        this.numberOfValves = numberOfValves;
    }

    public String getConstructionInterval() {
        return constructionInterval;
    }

    public void setConstructionInterval(String constructionInterval) {
        this.constructionInterval = constructionInterval;
    }

    public String getEngineCode() {
        return engineCode;
    }

    public void setEngineCode(String engineCode) {
        this.engineCode = engineCode;
    }

    public String getStartYearMonth() {
        return startYearMonth;
    }

    public void setStartYearMonth(String startYearMonth) {
        this.startYearMonth = startYearMonth;
    }

    public String getEndYearMonth() {
        return endYearMonth;
    }

    public void setEndYearMonth(String endYearMonth) {
        this.endYearMonth = endYearMonth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Modification)) return false;
        Modification that = (Modification) o;
        return id == that.id &&
                Objects.equals(parent, that.parent) &&
                Objects.equals(name, that.name) &&
                Objects.equals(engineType, that.engineType) &&
                Objects.equals(engineModel, that.engineModel) &&
                Objects.equals(engineCapacity, that.engineCapacity) &&
                Objects.equals(power, that.power) &&
                Objects.equals(drive, that.drive) &&
                Objects.equals(date, that.date) &&
                Objects.equals(reference, that.reference) &&
                Objects.equals(description, that.description) &&
                Objects.equals(startYear, that.startYear) &&
                Objects.equals(endYear, that.endYear) &&
                Objects.equals(bodyType, that.bodyType) &&
                Objects.equals(driveType, that.driveType) &&
                Objects.equals(capacity, that.capacity) &&
                Objects.equals(capacityTax, that.capacityTax) &&
                Objects.equals(capacityTech, that.capacityTech) &&
                Objects.equals(fuelMixture, that.fuelMixture) &&
                Objects.equals(fuelType, that.fuelType) &&
                Objects.equals(numberOfCylinders, that.numberOfCylinders) &&
                Objects.equals(numberOfValves, that.numberOfValves) &&
                Objects.equals(constructionInterval, that.constructionInterval) &&
                Objects.equals(engineCode, that.engineCode) &&
                Objects.equals(startYearMonth, that.startYearMonth) &&
                Objects.equals(endYearMonth, that.endYearMonth);
    }

    @Override
    public int hashCode() {

        return Objects.hash(parent, name, engineType, engineModel, engineCapacity, power, drive, date, reference, id, description, startYear, endYear, bodyType, driveType, capacity, capacityTax, capacityTech, fuelMixture, fuelType, numberOfCylinders, numberOfValves, constructionInterval, engineCode, startYearMonth, endYearMonth);
    }
}
