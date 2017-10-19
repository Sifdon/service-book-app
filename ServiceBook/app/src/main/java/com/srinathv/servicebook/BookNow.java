package com.srinathv.servicebook;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.Locale;
import java.util.Random;


public class BookNow extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    LocationManager lm;
    public double latitude = 0.0, longitude = 0.0;
    public LatLng latlng;

    private static final int MY_PERMISSION_REQUEST_CODE = 7171;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 7172;
    public GoogleApiClient mGoogleApiClient;
    private FusedLocationProviderClient mFusedLocationClient;
    public LocationRequest mLocationRequest;
    private static int UPDATE_INTERVAL = 5000;
    private static int FASTEST_INTERVAL = 3000;
    private static int DISPLACEMENT = 10;
    private TextView t ;
    private Geocoder geo;
    public String desc = "";
    public String land = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_now);
        t = (TextView) findViewById(R.id.txtloc);
        t.setMovementMethod(new ScrollingMovementMethod());
        try {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                }, MY_PERMISSION_REQUEST_CODE);
            }
            else{
                if(checkPlayServices()){
                    buildGoogleApiClient();
                    createLocationRequest();
                }
            }

            Button setloc = (Button) findViewById(R.id.buttloc);
            setloc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((TextView) findViewById(R.id.txtloc)).setText("Getting location....");
                    //delay by 2 seconds
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            displayLocation();
                        }
                    }, 2000);
                }
            });
        }
        catch(Exception e){
            Log.v("oncreate",e.getMessage());
        }

        Button book = (Button) findViewById(R.id.book);
        book.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                try {
                    Intent i = getIntent();
                    final String name = i.getStringExtra("service");
                    desc = ((EditText) findViewById(R.id.edtdesc)).getText().toString();
                    land = ((EditText) findViewById(R.id.edtland)).getText().toString();
                    String loc = ((TextView) findViewById(R.id.txtloc)).getText().toString();
                    if (desc.length() != 0 && land.length() != 0 && !(loc.equals("Click on the above button to set your location."))) {
                        FirebaseDatabase.getInstance().getReference("services").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                try {
                                    int ddd = 0;
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        service s = snapshot.getValue(service.class);
                                        String imm = s.servid;
                                        String imme = "";
                                        for (int i=0;i<imm.length();i++)
                                            imme = imme + imm.charAt(i);
                                        if (imme.substring(0, 2).equals("IM")) {
                                            imme = "";
                                            for (int i = 2; i < imm.length(); i++) {
                                                imme = imme + imm.charAt(i);
                                            }
                                            ddd = Integer.parseInt(imme);
                                        }
                                        else ddd = Integer.parseInt(s.servid);
                                    }
                                    if (ddd == 0)
                                        ddd = (new Random()).nextInt(50);
                                    else
                                        ddd = ddd + ((new Random()).nextInt(50));
                                    service is = new service();
                                    is.setdesc(desc);
                                    is.setname(name);
                                    is.setsid("IM" + ddd);
                                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid() + "";
                                    is.setuid(uid);
                                    is.setlat(latitude);
                                    is.setlong(longitude);
                                    is.setland(land);
                                    FirebaseDatabase.getInstance().getReference("services").child("IMMEDIATE" + ddd).setValue(is);
                                    final int abcdef = ddd;
                                    FirebaseDatabase.getInstance().getReference("users").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                User us = snapshot.getValue(User.class);
                                                if(us.uid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                                                    String allbookings = "";
                                                    allbookings = allbookings + us.bookings+","+abcdef;
                                                    FirebaseDatabase.getInstance().getReference("users/"+FirebaseAuth.getInstance().getCurrentUser().getUid()).child("bookings").setValue(allbookings);
                                                    break;
                                                }
                                            }
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            Toast.makeText(BookNow.this, "An error occurred. Try again later.", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    Toast.makeText(BookNow.this, "Service has been booked.", Toast.LENGTH_SHORT).show();
                                    Intent xyz = new Intent(getApplicationContext(), LoggedIn.class);
                                    xyz.putExtra("done","done");
                                    startActivity(xyz);
                                }
                                catch (Exception e){
                                    Log.v("bookservice",e.getMessage());
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(BookNow.this, "An error occurred. Try again later.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else
                        Toast.makeText(BookNow.this, "Please enter all details.", Toast.LENGTH_SHORT).show();
                }
                catch(Exception e){
                    Log.v("bookservice",e.getMessage());
                }
            }
        });

    }

    private void displayLocation(){
        try {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                List<Address> addresses;
                                geo = new Geocoder(getApplicationContext(), Locale.getDefault());
                                try {
                                    addresses = geo.getFromLocation(latitude, longitude, 1);
                                    String address = addresses.get(0).getAddressLine(0);
                                    String city = addresses.get(0).getLocality();
                                    String state = addresses.get(0).getAdminArea();
                                    String country = addresses.get(0).getCountryName();
                                    String postalCode = addresses.get(0).getPostalCode();
                                    t.setText(address+"\n"+city+"\n"+state+"\n"+country+"\n"+postalCode);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    });

        }
        catch(Exception e){
            Log.v("displaylocation",e.getMessage());
        }

    }

    private boolean checkPlayServices() {
        try {
            int rescode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
            if (rescode != ConnectionResult.SUCCESS) {
                if (GooglePlayServicesUtil.isUserRecoverableError(rescode)) {
                    GooglePlayServicesUtil.getErrorDialog(rescode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
                } else {
                    Toast.makeText(this, "This device is not supported", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return false;
            }
        }
        catch(Exception e){
            Log.v("checkplayservices",e.getMessage());
        }

        return true;
    }

    private void buildGoogleApiClient(){
        try {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API).build();
        }
        catch(Exception e){
            Log.v("buildgoogleapiclient",e.getMessage());
        }
    }

    private void createLocationRequest(){
        try {
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(UPDATE_INTERVAL);
            mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
        }
        catch(Exception e){
            Log.v("createlocationrequest",e.getMessage());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        try {
            switch (requestCode) {
                case MY_PERMISSION_REQUEST_CODE:
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        if (checkPlayServices()) {
                            buildGoogleApiClient();
                            createLocationRequest();
                        }
                    }
                    break;
            }
        }
        catch(Exception e){
            Log.v("requestpermres",e.getMessage());
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}

