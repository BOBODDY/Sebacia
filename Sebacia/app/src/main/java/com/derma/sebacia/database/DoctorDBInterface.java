package com.derma.sebacia.database;

import com.derma.sebacia.data.AcneLevel;
import com.derma.sebacia.data.Coordnate;
import com.derma.sebacia.data.Doctor;

import java.util.List;
import java.util.Map;

/**
 * Created by Daniel on 10/3/2015.
 */
public interface DoctorDBInterface {

    public Doctor getDoctor(int DocID);

    //list of docs given list of IDs. If a ID is not there, will return NULL
    public List<Doctor> getDoctorList(List<Integer> docIDs);

    //returns Doc_ID with their geographical coordinates.
    public Map<Integer, Coordnate> getDoctors(AcneLevel Severity);

    public boolean addDoctor(Doctor doctor);

}
