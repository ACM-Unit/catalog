package com.autoplus.entity;

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

    public Modification(Car parent, String name, String engineType, String engineModel, String engineCapacity, String power, String drive, String date, String reference) {
        this.parent = parent;
        this.name = name;
        this.engineType = engineType;
        this.engineModel = engineModel;
        this.engineCapacity = engineCapacity;
        this.power = power;
        this.drive = drive;
        this.date = date;
        this.reference = reference;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Modification)) return false;

        Modification that = (Modification) o;

        if (getParent() != null ? !getParent().equals(that.getParent()) : that.getParent() != null) return false;
        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) return false;
        if (getEngineType() != null ? !getEngineType().equals(that.getEngineType()) : that.getEngineType() != null)
            return false;
        if (getEngineModel() != null ? !getEngineModel().equals(that.getEngineModel()) : that.getEngineModel() != null)
            return false;
        if (getEngineCapacity() != null ? !getEngineCapacity().equals(that.getEngineCapacity()) : that.getEngineCapacity() != null)
            return false;
        if (getPower() != null ? !getPower().equals(that.getPower()) : that.getPower() != null) return false;
        if (getDrive() != null ? !getDrive().equals(that.getDrive()) : that.getDrive() != null) return false;
        if (getDate() != null ? !getDate().equals(that.getDate()) : that.getDate() != null) return false;
        return reference != null ? reference.equals(that.reference) : that.reference == null;
    }

    @Override
    public int hashCode() {
        int result = getParent() != null ? getParent().hashCode() : 0;
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getEngineType() != null ? getEngineType().hashCode() : 0);
        result = 31 * result + (getEngineModel() != null ? getEngineModel().hashCode() : 0);
        result = 31 * result + (getEngineCapacity() != null ? getEngineCapacity().hashCode() : 0);
        result = 31 * result + (getPower() != null ? getPower().hashCode() : 0);
        result = 31 * result + (getDrive() != null ? getDrive().hashCode() : 0);
        result = 31 * result + (getDate() != null ? getDate().hashCode() : 0);
        result = 31 * result + (reference != null ? reference.hashCode() : 0);
        return result;
    }
}
