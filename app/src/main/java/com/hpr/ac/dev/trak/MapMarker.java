package com.hpr.ac.dev.trak;

import java.util.ArrayList;

/**
 * Created by barbara on 4/24/16.
 */
public class MapMarker {
    private int id;
    private double lat;
    private double lng;
    private String description;


    // TODO, make async
    public static ArrayList<MapMarker> getMapMarkers() {
        ArrayList<MapMarker> list = new ArrayList<MapMarker>();
        list.add(new MapMarker(1, 47.4502, -122.3088, "SEA-TAC Airport"));
        list.add(new MapMarker(1, 37.6213, -122.3790, "SFO Airport"));
        list.add(new MapMarker(1, 39.8561, -104.6737, "Denver Airport"));
        return list;
    }

    public MapMarker(int id, double lat, double lng, String description) {
        this.id = id;
        this.lat = lat;
        this.lng = lng;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}