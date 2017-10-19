package com.srinathv.servicebook;

/**
 * Created by Srinath on 18-10-2017.
 */

public class service {
    public String name = "";
    public String uid = "";
    public double latitude = 0.0;
    public double longitude = 0.0;
    public String desc = "";
    public String date = "";
    public String time = "";
    public String servid = "";
    public String landmark="";

    public void setname(String n){ name = n; }
    public void setuid(String id) {uid = id;}
    public void setdesc(String d) {desc = d;}
    public void setdate(String da) {date = da;}
    public void settime(String t) {time = t;}
    public void setsid(String sid) {servid = sid;}
    public void setlat(double l){ latitude = l;}
    public void setland(String la) {landmark = la;}
    public void setlong(double lo) { longitude = lo;}

}
