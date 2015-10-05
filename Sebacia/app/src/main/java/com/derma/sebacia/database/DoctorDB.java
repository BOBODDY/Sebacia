package com.derma.sebacia.database;

import com.derma.sebacia.data.AcneLevel;
import com.derma.sebacia.data.Coordnate;
import com.derma.sebacia.data.Doctor;

import java.util.List;
import java.util.Map;

/**
 * Created by Daniel on 10/3/2015.
 */
public class DoctorDB implements DoctorDBInterface {



    public DoctorDB() {

    }

    @Override
    public Doctor getDoctor(int DocID) {
        return null;
    }

    @Override
    public List<Doctor> getDoctorList(List<Integer> docIDs) {
        return null;
    }

    @Override
    public Map<Integer, Coordnate> getDoctors(AcneLevel Severity) {
        return null;
    }

    @Override
    public boolean addDoctor(Doctor doctor) {
        return false;
    }
}
