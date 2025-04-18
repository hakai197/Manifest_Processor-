package com.techelevator.custom.model;

public class Customer {
    private int customerId;
    private String orderNumber;
    private String name;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private String doorNumber;
    private int trailerId;
    private int handlingUnit;
    private int weight;

    // Constructors
    public Customer() {
    }

    public Customer(int customerId, String orderNumber, String name, String address,
                    String city, String state, String zipCode, String doorNumber,
                    int trailerId, int handlingUnit, int weight) {
        this.customerId = customerId;
        this.orderNumber = orderNumber;
        this.name = name;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.doorNumber = doorNumber;
        this.trailerId = trailerId;
        this.handlingUnit = handlingUnit;
        this.weight = weight;
    }

    // Getters and Setters
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getDoorNumber() {
        return doorNumber;
    }

    public void setDoorNumber(String doorNumber) {
        this.doorNumber = doorNumber;
    }

    public int getTrailerId() {
        return trailerId;
    }

    public void setTrailerId(int trailerId) {
        this.trailerId = trailerId;
    }

    public int getHandlingUnit() {
        return handlingUnit;
    }

    public void setHandlingUnit(int handlingUnit) {
        this.handlingUnit = handlingUnit;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerId=" + customerId +
                ", orderNumber='" + orderNumber + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", doorNumber='" + doorNumber + '\'' +
                ", trailerId=" + trailerId +
                ", handlingUnit=" + handlingUnit +
                ", weight=" + weight +
                '}';
    }
}