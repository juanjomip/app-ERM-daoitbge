package com.erm.erm_debug;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Juan José on 10-01-2017.
 */

public class SamplesManager extends ActionBarActivity {

    List<Map<String, String>> samplesList = new ArrayList<>();
    List<Map<String, String>> matchedSamples = new ArrayList<>();


    public void defaultList() {
        Map<String, String> map = new HashMap<>();
        map.put("value", "25.3");
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

    public void readSamplesFile(AssetManager assets) throws IOException {

        InputStream is = assets.open("samples.csv");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] RowData = line.split(",");
                //System.out.println(RowData[0] + RowData[1] + "noe gomez");
                //date = RowData[0];
                //value = RowData[1];
                // do something with "data" and "value"

                Map<String, String> map = new HashMap<>();
                map.put("value", String.valueOf(RowData[0]));
                map.put("timestampt", String.valueOf(RowData[1]));
                samplesList.add(map);
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        finally {
            try {
                is.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }









}
