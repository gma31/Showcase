package org.educama.services.flightinformation;

import org.springframework.data.annotation.Id;

public class Airport {
    @Id
    private String id;


    private String name;
    private String city;
    private String country;
    private String iataCode;
    private String icaoCode;
    private double latitude;
    private double longitude;


    public Airport() {
    }

    public Airport(String name, String iataCode) {
        this.name = name;
        this.iataCode = iataCode;
    }

    public String getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getIataCode() {
        return iataCode;
    }

    public void setIataCode(String iataCode) {
        this.iataCode = iataCode;
    }

    public String getIcaoCode() {
        return icaoCode;
    }

    public void setIcaoCode(String icaoCode) {
        this.icaoCode = icaoCode;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Airport withName(String name) {
        this.name = name;
        return this;
    }

    public Airport withCity(String city) {
        this.city = city;
        return this;
    }

    private Airport withIataCode(String iataCode) {
        this.iataCode = iataCode;
        return this;
    }

    private Airport withIcaoCode(String icaoCode) {
        this.icaoCode = icaoCode;
        return this;
    }

    private Airport withLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    private Airport withLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    @Override
    public String toString() {
        return String.format("Airport[id=%s, name='%s', IATA='%s']", id, name, iataCode);
    }

}
