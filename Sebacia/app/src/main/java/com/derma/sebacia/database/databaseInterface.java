package com.derma.sebacia.database;

import com.derma.sebacia.data.AcneLevel;
import com.derma.sebacia.data.Doctor;
import com.derma.sebacia.data.Patient;

import com.derma.sebacia.data.Picture;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by Daniel on 9/12/2015.
 * Interface that allows us to switch between local and external db
 *
 */
public interface databaseInterface {

    //GETS
    Picture getPicture(int PatientID, String FilePath);

    List<Picture> getPatientPics(Patient patient);

    List<Doctor> getDoctors(AcneLevel Severity);

    Doctor getDoctor(int DocID);

    Patient getPatient(Patient patient);

    Iterator<Integer>[] getSurveyPictures();

    //POSTS
    boolean addPatient(Patient patient);

    boolean addDoctor(Doctor doctor);

    boolean addPicture(Picture picture);

    boolean setPictureSeverity(String filename, AcneLevel sevLevel);
}
