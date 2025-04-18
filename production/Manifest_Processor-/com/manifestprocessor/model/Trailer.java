package com.techelevator.custom.model;

public class Trailer {
    private int trailerId;
    private String trailerNumber;
    private String trailerType;
    private int shipperId;


    public int getTrailerId() {
        return trailerId;
    }

    public void setTrailerId(int trailerId) {
        this.trailerId = trailerId;
    }

    public String getTrailerNumber() {
        return trailerNumber;
    }

    public void setTrailerNumber(String trailerNumber) {
        this.trailerNumber = trailerNumber;
    }

    public String getTrailerType() {
        return trailerType;
    }

    public void setTrailerType(String trailerType) {
        this.trailerType = trailerType;
    }

    public int getShipperId() {
        return shipperId;
    }

    public void setShipperId(int shipperId) {
        this.shipperId = shipperId;
    }

    @Override
    public String toString() {
        return "Trailer{" +
                "trailerId=" + trailerId +
                ", trailerNumber='" + trailerNumber + '\'' +
                ", trailerType='" + trailerType + '\'' +
                '}';
    }
}
