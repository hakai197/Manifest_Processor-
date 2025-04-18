package com.techelevator.custom.model;

public class Shipper {
    private int shipperId;
    private String name;
    private String address;
    private String city;
    private String state;
    private String zipCode;

    public Shipper(int shipperId, String name, String address, String city, String state, String zipCode) {
        this.shipperId = shipperId;
        this.name = name;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }

    public Shipper() {
    }

    public int getShipperId() {
        return shipperId;
    }

    public void setShipperId(int shipperId) {
        this.shipperId = shipperId;
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

    @Override
    public String toString() {
        return "Shipper ID: " + shipperId + "\n" +
                "Name: " + name + "\n" +
                "Address: " + address + "\n" +
                "City: " + city + "\n" +
                "State: " + state + "\n" +
                "Zip Code: " + zipCode;
    }
}