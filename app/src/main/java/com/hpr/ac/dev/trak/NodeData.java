package com.hpr.ac.dev.trak;

public class NodeData {
    public String busnumber;
    public String licence_no;
    public String type;
    public String floor_type;
    public String busdest;
    public double lat, lon;
    public int cond;

    public NodeData() {
    }

    public NodeData(String busnumber, String licence_no, String type, String floor_type, String busdest,
                    double lat, double lon, int cond) {
        this.busnumber = busnumber;
        this.licence_no = licence_no;
        this.type = type;
        this.floor_type = floor_type;
        this.busdest = busdest;
        this.lat = lat;
        this.lon = lon;
        this.cond = cond;
    }
}
