package com.srinathv.servicebook;

/**
 * Created by Srinath on 16-10-2017.
 */

public class User {
    public String phone = " ";
    public String name = "";
    public String email = "";
    public String bookings = "";
    public String uid = "";

    public void setmail(String m){
        email = m;
    }

    public void setname(String n){
        name = n;
    }

    public void setphone(String p) { phone = p; }

    public void setbookings(String s) { bookings = s; }

    public void setuid(String u) {uid = u;}

}
