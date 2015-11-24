package com.derma.sebacia.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Doctor {

    private String name;
    private String adress;
    private int id;
    private int acneMin;
    private int acneMax;
    private double lat;
    private double llong;
    private String email;

    @Override
    public String toString() {
        return "name:" + name + "\naddress:" + adress;
    }

    public Doctor() {
    }

    //CONSTRUCTOR
    public Doctor(String name, String adress, double lat, double llong, int id, int min, int max, String email) {
        this.name = name;
        this.adress = adress;
        this.email = email;
        this.lat = lat;
        this.llong = llong;
        this.id = id;
        acneMin = min;
        acneMax = max;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAcneMin(int acneMin) {
        this.acneMin = acneMin;
    }

    public void setAcneMax(int acneMax) {
        this.acneMax = acneMax;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLong(double llong) {
        this.llong = llong;
    }

    public void setEmail(String email) { this.email = email; }

    public String getName() {
        return name;
    }

    public String getAdress() {
        return adress;
    }

    public int getId() {
        return id;
    }

    public int getAcneMin() {
        return acneMin;
    }

    public int getAcneMax() {
        return acneMax;
    }

    public double getLat() {
        return lat;
    }

    public double getLong() {
        return llong;
    }

    public String getEmail() { return email; }


}