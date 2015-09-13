package com.derma.sebacia.data;

/**
 * Created by Daniel on 9/12/2015.
 */
public class Doctor {

    String Name;
    String Adress;
    int DocID;
    AcneLevel Min;
    AcneLevel Max;

    //FUNCTIONALITY



    //CONSTRUCTOR
    public Doctor(String name, String adress, int DocID, AcneLevel min, AcneLevel max) {
        Name = name;
        Adress = adress;
        this.DocID = DocID;
        Min = min;
        Max = max;
    }

    //SETTERS
    public void setName(String name) {
        Name = name;
    }

    public void setMax(AcneLevel max) {
        Max = max;
    }

    public void setMin(AcneLevel min) {
        Min = min;
    }

    public void setID(int DocID) {
        this.DocID = DocID;
    }

    public void setAdress(String adress) {
        Adress = adress;
    }

    //GETTERS
    public String getName() {
        return Name;
    }

    public AcneLevel getMax() {
        return Max;
    }

    public AcneLevel getMin() {
        return Min;
    }

    public int getID() {
        return DocID;
    }

    public String getAdress() {
        return Adress;
    }
}
