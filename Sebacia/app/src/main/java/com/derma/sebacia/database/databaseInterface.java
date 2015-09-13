package com.derma.sebacia.database;

import com.derma.sebacia.data.AcneLevel;
import com.derma.sebacia.data.Doctor;
import com.derma.sebacia.data.Patient;

import com.derma.sebacia.data.Picture;

import java.util.List;

/**
 * Created by Daniel on 9/12/2015.
 */
public interface databaseInterface {

    //GETS
    public Picture getPicture(int PatientID, String FilePath);

    public List<Picture> getPatientPics(Patient patient);

    public List<Doctor> getDoctors(AcneLevel Severity);

    public Doctor getDoctor(int DocID);

    public Patient getPatient(Patient patient);

    //POSTS
    public boolean addPatient(Patient patient);

    public boolean addDoctor(Doctor doctor);

    public boolean addPicture(Picture picture);
}
