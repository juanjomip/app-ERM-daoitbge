package com.erm.erm_debug;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Juan José on 10-01-2017.
 */

public class SamplesManager extends ActionBarActivity {

    List<Map<String, Double>> samplesList = new ArrayList<>();
    List<Map<String, Double>> matchedSamples = new ArrayList<>();


    public void defaultList() {
        Map<String, Double> map = new HashMap<>();
        map.put("value", 25.3);
        samplesList.add(map);
    }


    // Retorna el tamaño de la lista de muestras.
    Integer getSizeOfSamples(){
        return samplesList.size();
    }

    // Retorna el tamaño de la lista de muestras.
    Integer getSizeOfMatchedSamples() {
        return matchedSamples.size();
    }

    List getMatchedSamples(){
        return matchedSamples;
    }

    public void makeMatch(List coordenadas) {
        matchedSamples = coordenadas;
    }

    /*String bowlingJson() {

        // Lista generica
        Map<String, Double> map = new HashMap<>();
        map.put("value", 25.3);

        SamplesList.add(map);











        //Map<String, List> map = new HashMap<>();
        //map.put("samples", locationList);

        //JSONObject json = new JSONObject(map);
        return json.toString();


    }*/
}
