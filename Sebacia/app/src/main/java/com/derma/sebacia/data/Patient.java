package com.derma.sebacia.data;

/**
 * Created by Daniel on 9/12/2015.
 */
public class Patient {

    int PatientID;

    //FUNCTIONALITY



    //CONSTRUCTOR
    public Patient(int patientID) {
        PatientID = patientID;
    }

    //SETTERS
    public void setPatientID(int patientID) {
        PatientID = patientID;
    }

    //GETTERS
    public int getPatientID() {
        return PatientID;
    }
}
