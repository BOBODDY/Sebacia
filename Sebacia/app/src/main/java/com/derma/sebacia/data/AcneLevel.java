package com.derma.sebacia.data;

/**
 * Created by Daniel on 9/12/2015.
 */
public class AcneLevel {

    int Level;
    String Name; //do we need this?

    //FUNCTIONALITY



    //CONSTRUCTOR
    public AcneLevel(int level, String name) {
        Level = level;
        Name = name;
    }

    //SETTERS
    public void setLevel(int level) {
        Level = level;
    }

    public void setName(String name) {
        Name = name;
    }

    //GETTERS
    public String getName() {
        return Name;
    }

    public int getLevel() {
        return Level;
    }
}
