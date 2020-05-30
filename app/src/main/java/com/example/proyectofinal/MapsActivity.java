package com.example.proyectofinal;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.juang.jplot.PlotPlanitoXY;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener, View.OnClickListener {

    private GoogleMap mMap;
    Button btnList;
    InputStreamReader file;
    BufferedReader br;
    ArrayList<Coord> locationsList = new ArrayList<>();
    private PlotPlanitoXY plot;
    LinearLayout lyGraph;

    double[] X1, Y1;
    double[] X2, Y2;
    double[] X3, Y3;
    double[] X4, Y4;
    double[] X5, Y5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        link();
        setListeners();
        String sql = "SELECT * FROM Coords";
        DbHelper helper = new DbHelper(this, "BD", null, 1);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor c = db.rawQuery(sql, null);
        if (!c.moveToFirst()) {
           /* for (int i = 1; i <= 10; i++) {
                if (i >= 10) {
                    readInfo("BV_" + i + ".txt", i);
                } else {
                    readInfo("BV_0" + i + ".txt", i);
                }
            } */
            readCoords("coordenadas1.txt");
        }
        db.close();
        X1 = new double[]{2.6, 2.92, 3.24, 3.55, 3.87, 4.18, 4.5};
        Y1 = new double[]{0.37, 0.0929, 0.0321, 0.0097, 0.0062, 0.0038, 0};
        X2 = new double[]{2.72, 3.17, 3.62, 4.07, 4.52, 4.97, 5.42};
        Y2 = new double[]{0.2277, 0.0535, 0.0128, 0.0083, 0.0062, 0.0041, 0};
        X3 = new double[]{2.9, 3.43, 3.96, 4.49, 5.03, 5.56, 6.09};
        Y3 = new double[]{0.2203,0.0462,0.0134,0.0076,0.0055,0.0021,0};
        X4 = new double[]{3.28,3.88,4.48,5.07,5.67,6.26,6.86};
        Y4 = new double[]{0.2481,0.0574,0.0207,0.0086,0.0055,0.0028,0};
        X5 = new double[]{2.7,3.07,3.44,3.82,4.19,4.56,4.93};
        Y5 = new double[]{0.3287,0.1054,0.0346,0.0152,0.0066,0.0017,0};
        locationsList = getLocationsFromDB();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    ArrayList<Coord> getLocationsFromDB() {
        String sql = "SELECT * FROM Coords";
        int ID;
        Double lon, lat;
        String name, desc;
        ArrayList<Coord> data = new ArrayList<>();
        DbHelper helper = new DbHelper(this, "BD", null, 1);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {
            do {
                ID = c.getInt(0);
                lon = c.getDouble(1);
                lat = c.getDouble(2);
                data.add(new Coord(ID, lon, lat));
            } while (c.moveToNext());
        }
        db.close();
        return data;
    }

    void readCoords(String txt) {
        String[] files = this.fileList();
        String[] data;
        String line;
        if (this.existsFile(files, txt)) {
            try {
                file = new InputStreamReader(this.openFileInput(txt));
                br = new BufferedReader(file);
                while ((line = br.readLine()) != null) {
                    data = line.split(",");
                    addLocation(Double.parseDouble(data[0]), Double.parseDouble(data[1]));
                }
                br.close();
                file.close();
            } catch (IOException e) {
            }
        }
    }

    void readInfo(String txt, int i) {
        String[] files = this.fileList();
        String[] data;
        String line;
        if (this.existsFile(files, txt)) {
            try {
                file = new InputStreamReader(this.openFileInput(txt));
                br = new BufferedReader(file);
                while ((line = br.readLine()) != null) {
                    data = line.split(" ");
                    addInfo(Integer.valueOf(data[0]), Integer.valueOf(data[1]), Integer.valueOf(data[2]), Integer.valueOf(data[3]), Double.parseDouble(data[7].trim()), "Buoy" + i);
                }
                br.close();
                file.close();
            } catch (IOException e) {
            }
        }
    }

    void addLocation(Double lon, Double lat) {
        DbHelper helper = new DbHelper(this, "BD", null, 1);
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            ContentValues cv = new ContentValues();
            cv.put("Long", lon);
            cv.put("Lat", lat);
            db.insert("Coords", null, cv);
            db.close();
        } catch (Exception ex) {
            Toast.makeText(this, "Error. Verify the provided data or contact application's owner", Toast.LENGTH_SHORT).show();
        }
    }

    void addInfo(Integer year, Integer month, Integer day, Integer hour, Double height, String buoy) {
        DbHelper helper = new DbHelper(this, "BD", null, 1);
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            ContentValues cv = new ContentValues();
            cv.put("Year", year);
            cv.put("Month", month);
            cv.put("Day", day);
            cv.put("Hour", hour);
            cv.put("Height", height);
            db.insert(buoy, null, cv);
            db.close();
        } catch (Exception ex) {
            Toast.makeText(this, "Error. Verify the provided data or contact application's owner", Toast.LENGTH_SHORT).show();
        }
    }

    boolean existsFile(String[] files, String fileToFind) {
        for (int f = 0; f < files.length; ++f) {
            if (fileToFind.equals(files[f])) {
                return true;
            }
        }
        return false;
    }

    private void link() {
        lyGraph = findViewById(R.id.lyGraph);
    }

    private void setListeners() {
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
        Bitmap bit = BitmapFactory.decodeFile(String.valueOf(getDrawable(R.drawable.pin)));
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.pin);
        Bitmap smallMarker = Bitmap.createScaledBitmap(bm, 25, 25, false);

        // Add a marker in Sydney and move the camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(locationsList.get(4).getLocation()));
        mMap.setMinZoomPreference(7);
        for (int i = 0; i < locationsList.size(); i++) {
            mMap.addMarker(new MarkerOptions().position(getLocation(i)).title(getName(i)).flat(true).icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
        }
    }

    private LatLng getLocation(int i) {
        return locationsList.get(i).getLocation();
    }

    private String getName(int i) {
        return "Buoy " + locationsList.get(i).getID();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this, "Title clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        float[] X, Y;
        //tvClicked.setText(getDescription(marker.getTitle()));
        Toast.makeText(this, marker.getTitle(), Toast.LENGTH_SHORT).show();
        plot = new PlotPlanitoXY(this, "Titulo principal del grafico", "H(m)", "Probabilidad de excedencia(%)");
        switch (marker.getTitle()) {
            case "Buoy 1":
            case "Buoy 6":
                X = parseX(X1);
                Y = parseY(Y1);
                break;
            case "Buoy 2":
            case "Buoy 7":
                X = parseX(X2);
                Y = parseY(Y2);
                break;
            case "Buoy 3":
            case "Buoy 8":
                X = parseX(X3);
                Y = parseY(Y3);
                break;
            case "Buoy 4":
            case "Buoy 9":
                X = parseX(X4);
                Y = parseY(Y4);
                break;
            default:
                X = parseX(X5);
                Y = parseY(Y5);
                break;
        }

        plot.SetSerie1(X, Y, marker.getTitle(), 5, true);
        plot.SetGruesoLinea(2);
        plot.SetEscalaX(minX(X), maxX(X));
        plot.SetEscalaY1(0, maxY(Y));
        plot.SetHD(true); //ajustamos la calidad hd que suaviza bordes del grafico. por default esta desactivado
        plot.SetTouch(false);// activa el touch sobre el grafico no es necesario colocarlo ya que por default esta activado
        lyGraph.removeAllViews();
        lyGraph.addView(plot);
        return false;
    }

    private float[] parseY(double[] Yi) {
        float[] Y = new float[7];
        for (int i = 0; i < 7; i++) {
            Y[i] = (float) Yi[i];
        }
        return Y;
    }

    private float[] parseX(double[] Xi) {
        float[] X = new float[7];
        for (int i = 0; i < 7; i++) {
            X[i] = (float) Xi[i];
        }
        return X;
    }

    double maxX (float[] X){
        double max = 0;
        for(int i = 0; i < 7; i++){
            if(X[i] > max){
                max = X[i];
            }
        }
        return max;
    }

    double minX (float[] X){
        double min = maxX(X);
        for(int i = 0; i < 7; i++){
            if(X[i] < min){
                min = X[i];
            }
        }
        return min;
    }

    double maxY(float[] Y){
        double max = 0;
        for(int i = 0; i < 7; i++){
            if(Y[i] > max){
                max = Y[i];
            }
        }
        return max;
    }
}
