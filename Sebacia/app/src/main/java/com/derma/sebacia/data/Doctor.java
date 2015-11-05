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

    @Override
    public String toString() {
        return "\n\nname:" + name + " adress:" + adress + " id:" + id +
                " acneMin:" + acneMin + " acneMax:" + acneMax + " lat/long:" + lat +"/"+llong +"\n\n";
    }

    public Doctor() {

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


}