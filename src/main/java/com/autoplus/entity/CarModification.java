package com.autoplus.entity;

import java.util.Date;
import java.util.Objects;

public class CarModification {
    private Car parent;
    private String name;
    private String engineType;
    private String engineModel;
    private String engineCapacity;
    private String power;
    private String drive;
    private Date date;

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CarModification)) return false;
        CarModification that = (CarModification) o;
        return Objects.equals(parent, that.parent) &&
                Objects.equals(name, that.name) &&
                Objects.equals(engineType, that.engineType) &&
                Objects.equals(engineModel, that.engineModel) &&
                Objects.equals(engineCapacity, that.engineCapacity) &&
                Objects.equals(power, that.power) &&
                Objects.equals(drive, that.drive) &&
                Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {

        return Objects.hash(parent, name, engineType, engineModel, engineCapacity, power, drive, date);
    }
}
