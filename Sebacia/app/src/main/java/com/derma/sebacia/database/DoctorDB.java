package com.derma.sebacia.database;

import android.os.AsyncTask;

import com.derma.sebacia.data.Doctor;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 10/3/2015.
 */
public class DoctorDB extends AsyncTask<Integer, Void, List<Doctor>> implements DoctorDBInterface {

    final String baseURL = "http://52.33.9.125:8080/sebacia/doctor";
    RestTemplate restTemplate;

    public enum request {IDS, SEVERITY, COORDINATE};

    public DoctorDB() {

    }

    @Override
    protected List<Doctor> doInBackground(Integer... params) {
        List<Doctor> ret = new ArrayList<Doctor>();
        int count = params.length;
        if(count == 1) {
            //ERROR
        }
        else {
            switch (params[0]) {
                case 0:
                    ret = getDoctorList(params);
                    break;
                case 1:
                    ret = getDoctorBySeverity(params[1]);
                    break;
                case 2:
                    ret = getDoctorsByCoordinate(params[1], params[2], params[3], params[4]);
                    break;
            }
        }
        return ret;
    }

    @Override
    public List<Doctor> getDoctorList(Integer[] docIDs) {
        //sane as above by repeat and make list. Or add contitions to WHERE statement
        String url;
        int count = docIDs.length;
        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        List<Doctor> doc = new ArrayList<Doctor>();
        for(int i = 1; i <count; i++) {
            url = baseURL +"?Doc_ID="+docIDs[i];
            doc.addAll(restTemplate.getForObject(url, ArrayList.class)); //change to Doc.class?
        }
        return doc;
    }

    @Override
    public List<Doctor> getDoctorBySeverity(Integer Severity) {
        //SELECT Doctors_ID, Doctors_Latitude, Doctors_Longitude FROM Doctors WHERE Severity >= Doctors_AnceMin && Severity <= Doctors_AnceMax
        String url = baseURL + "?Severity="+Severity;
        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        List<Doctor> doc = restTemplate.getForObject(url, ArrayList.class);
        return doc;
    }

    @Override
    public List<Doctor> getDoctorsByCoordinate(int maxLat, int minLat, int minLong, int maxLong) {
        String url = baseURL + "?MaxLat="+maxLat+"&&MinLat="+minLat+"&&MaxLong="+maxLong+"&&MinLong="+minLong;
        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        List<Doctor> doc = restTemplate.getForObject(url, ArrayList.class);
        return doc;
    }

    //add statement for getting doctors witha specific Lat.Long?

    @Override
    public boolean addDoctor(Doctor doctor) {
        //Dont need to implement here. Belongs in a small webapp.
        return false;
    }
}
