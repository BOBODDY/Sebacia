package com.derma.sebacia.database;

import com.derma.sebacia.data.Doctor;

import java.util.List;

/**
 * Created by Daniel on 10/3/2015.
 */
public interface DoctorDBInterface {

    //list of docs given list of IDs. If a ID is not there, will return NULL
    public List<Doctor> getDoctorList(Integer[] docIDs);

    //list of doctor that treat a specified severity.
    public List<Doctor> getDoctorBySeverity(Integer Severity);

    //returns doctor list that is withing a certain range of coordinates
    public List<Doctor> getDoctorsByCoordinate(int maxLat, int minLat, int minLong, int maxLong);

    public boolean addDoctor(Doctor doctor);

}
