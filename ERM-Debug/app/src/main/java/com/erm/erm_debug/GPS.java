package com.erm.erm_debug;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;


import java.io.IOException;


public class GPS extends ActionBarActivity implements LocationListener {

    List<Map<String, String>> locationList = new ArrayList<>();
    public static final int MY_PERMISSIONS = 0;
    private Switch mySwitch;
    private TextView samplesSize;
    private TextView locationSize;
    private TextView matchedSize;
    private TextView lat;
    private TextView lng;
    private TextView txtGps;
    private TextView datetime;
    private Switch switch_ubicacion;
    public LocationManager handle;
    private String provider;
    SamplesManager samplesManager = new SamplesManager();

    // CONSTANTES
    private static final int MIN_TIME_COORDS = 15;

    protected void onCreate(Bundle savedInstanceState) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gps);
        txtGps = (TextView) findViewById(R.id.proveedor);
        mySwitch = (Switch) findViewById(R.id.switch_ubicacion);
        lat = (TextView) findViewById(R.id.lat);
        lng = (TextView) findViewById(R.id.lng);
        datetime = (TextView) findViewById(R.id.datetime);

        locationSize = (TextView) findViewById(R.id.locationsSize);
        samplesSize = (TextView) findViewById(R.id.SamplesSize);
        matchedSize = (TextView) findViewById(R.id.MatchedSize);

        samplesManager = new SamplesManager();
        samplesManager.defaultList();
        try {
            AssetManager assets = getAssets();
            samplesManager.readSamplesFile(assets);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setEstadoSwitch(isChecked);
            }
        });
    }

    void setEstadoSwitch(boolean x) {
        if(x) {
            IniciarServicio();
            //MuestraPosicionActual();
        } else {
            PararServicio();
        }
    }


    public void IniciarServicio() {
        Toast.makeText(this, "Iniciado", Toast.LENGTH_SHORT).show();
        handle = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria c = new Criteria();
        c.setAccuracy(Criteria.ACCURACY_FINE);

        // Assume thisActivity is the current activity
        int internetPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET);
        int finePermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int networkPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_NETWORK_STATE);
        int coarsePermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        // Si no tiene los permisos
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_NETWORK_STATE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ) {

            // Pedir permisos
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.INTERNET,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_NETWORK_STATE
                    },
                    MY_PERMISSIONS);
        } else {
            provider = handle.getBestProvider(c, true);
            txtGps.setText("Proveedor:" + provider);

            handle.requestLocationUpdates(provider, MIN_TIME_COORDS, 0, this);

            Location location = handle.getLastKnownLocation(provider);
            this.onLocationChanged(location);


            MuestraPosicionActual(location);
        }

        //txtGps.setText(String.valueOf(permissionCheck));


    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "daaaaaaado", Toast.LENGTH_SHORT).show();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    Toast.makeText(this, "denegado", Toast.LENGTH_SHORT).show();

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void MuestraPosicionActual(Location location){

        if (location==null) {
            lat.setText("Lat Desconocida");
            lng.setText("Lng Desconocida");
        } else {
            lat.setText(String.valueOf(location.getLatitude()));
            lng.setText(String.valueOf(location.getLongitude()));
        }
    }



    public void PararServicio(){
        handle.removeUpdates(this);
        lat.setText("Detenido");
        lng.setText("Detenido");
        Toast.makeText(this, "Iniciado", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location==null) {
            lat.setText("Lat Desconocida");
            lng.setText("Lng Desconocida");
        } else {
            // Guardamos las coordenadas en una matriz.
            Map<String, String> map = new HashMap<>();
            map.put("lat", String.valueOf(location.getLatitude()));
            map.put("lng", String.valueOf(location.getLongitude()));

            // Se añade la fecha a la matriz en formato yyyy-MM.dd HH:mm:ss.
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDate = df.format(c.getTime());
            map.put("datetime", formattedDate);

            // Agregamos la matriz a nuestra lista que contiene todas las coordenadas.
            locationList.add(map);

            // Si hay alguna muestra y hay 10 o más coordenadas se hace el match.
            System.out.println(String.valueOf(samplesManager.getSizeOfSamples()) + " " + String.valueOf(locationList.size()));
            if(samplesManager.getSizeOfSamples() > 0 && locationList.size() >= 10) {
                samplesManager.makeMatch(locationList);
            }
            // Si hay muchas coordenadas y aún no hay samples se limpia la lista de coordenadas.
            else if(locationList.size() >= 50) {
                Toast.makeText(this, "Limpiando exceso de coordenadas", Toast.LENGTH_SHORT).show();
                this.clearList();
            }

            System.out.println(String.valueOf(locationList.size()));
            if(samplesManager.getSizeOfMatchedSamples() > 10) {
                Toast.makeText(this, "Enviando a servidor", Toast.LENGTH_SHORT).show();
                try {
                    this.makeRequest();
                    locationList.clear();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            lat.setText(String.valueOf(location.getLatitude()));
            lng.setText(String.valueOf(location.getLongitude()));
            datetime.setText(formattedDate);


            System.out.println(locationList.size());
            locationSize.setText(String.valueOf("cant. coordenadas: " + locationList.size()));
            samplesSize.setText(String.valueOf("cant. muestras: " + samplesManager.getSizeOfSamples()));
            matchedSize.setText(String.valueOf("cant. matched: " + samplesManager.getSizeOfMatchedSamples()));
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    String post(String url, String json) throws IOException {
        System.out.println(json);
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    String bowlingJson() {

        //JSONArray json = new JSONArray(locationList);

        Map<String, List> map = new HashMap<>();
        map.put("samples", samplesManager.getMatchedSamples());

        JSONObject json = new JSONObject(map);
        return json.toString();


    }

    public void makeRequest() throws IOException {

        String json = this.bowlingJson();
        String response = this.post("http://104.236.92.253/api/samples", json);
        System.out.println(response);
        Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
    }

    // Se limpia la lista de coordenadas. en el futuro la idea es eliminar solo las
    // mas antiguas progresivamente.
    public void clearList(){
        locationList.clear();

    }
}