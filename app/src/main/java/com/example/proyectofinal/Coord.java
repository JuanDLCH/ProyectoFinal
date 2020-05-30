package com.example.proyectofinal;

import com.google.android.gms.maps.model.LatLng;

public class Coord {
    private LatLng location;
    private int ID;

    public Coord(int ID, Double lon, Double lat) {
        setLocation(new LatLng(lat*(-1), lon*(-1)));
        setID(ID);
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
