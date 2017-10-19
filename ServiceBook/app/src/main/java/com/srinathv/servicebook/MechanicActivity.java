package com.srinathv.servicebook;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MechanicActivity extends AppCompatActivity {
    private service serv;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder =
            new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.notif)
                    .setContentTitle("Service")
                    .setContentText("Services have been booked");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic);

        final TextView t = (TextView) findViewById(R.id.txtimbooked);
        t.setMovementMethod(new ScrollingMovementMethod());
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String id = "Services have been booked.";
        t.setText("No immediate services");
        FirebaseDatabase.getInstance().getReference("services").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int serviceno = 1;
                if(dataSnapshot == null){
                   // t.setText("No immediate services");
                }
                else {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        serv = snapshot.getValue(service.class);
                        if ((serv.servid).substring(0, 2).equals("IM")) {
                            t.setText(serviceno + ".");
                            serviceno++;
                            t.append("\nType of service: " + serv.name + "\n");
                            FirebaseDatabase.getInstance().getReference("users").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        User u = snapshot.getValue(User.class);
                                        if (serv.uid.equals(u.uid)) {
                                            t.append("Name of person : " + u.name + "\n");
                                            if(u.phone.length()!=0) t.append("Phone number of person : " + u.phone + "\n");
                                            t.append("Email of person : " + u.email + "\n");

                                            try {
                                                Geocoder geocoder;
                                                List<Address> addresses;
                                                geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                                                addresses = geocoder.getFromLocation(serv.latitude, serv.longitude, 1);
                                                String address = addresses.get(0).getAddressLine(0);
                                                String city = addresses.get(0).getLocality();
                                                String state = addresses.get(0).getAdminArea();
                                                String country = addresses.get(0).getCountryName();
                                                String postalCode = addresses.get(0).getPostalCode();
                                                t.append("Address: \n" + address + " " + city + " " + state + " " + country + " " + postalCode + "\n\n");
                                            } catch (Exception e) {
                                                Log.v("enterhere", e.getMessage());
                                            }
                                            Uri sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.notifsound);
                                            mBuilder.setSound(sound);
                                            Intent xyz = getIntent();
                                            PendingIntent resultPendingIntent =
                                                    PendingIntent.getActivity(
                                                            getApplicationContext(),
                                                            0,
                                                            getIntent(),
                                                            PendingIntent.FLAG_UPDATE_CURRENT
                                                    );
                                            mBuilder.setContentIntent(resultPendingIntent);
                                            int mNotificationId = 001;
                                            mNotificationManager.notify(mNotificationId, mBuilder.build());
                                            break;
                                        }

                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final TextView txts = (TextView) findViewById(R.id.txtscheduled);
        txts.setMovementMethod(new ScrollingMovementMethod());
        txts.setText("No scheduled services.");
        FirebaseDatabase.getInstance().getReference("services").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int serviceno = 1;
                if(dataSnapshot == null) txts.setText("No services scheduled");
                else {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        serv = snapshot.getValue(service.class);
                        if (!(serv.servid).substring(0, 2).equals("IM")) {
                            txts.setText(serviceno++ + ".\n");
                            txts.append("\nType of service: " + serv.name + "\n");
                            txts.append("Date of service : " + serv.date + "\n");
                            txts.append("Time of service : " + serv.time + "\n");
                            FirebaseDatabase.getInstance().getReference("users").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        User u = snapshot.getValue(User.class);
                                        if (serv.uid.equals(u.uid)) {
                                            txts.append("Name of person : " + u.name + "\n");
                                            if(u.phone.length()!=0) txts.append("Phone number of person : " + u.phone + "\n");
                                            txts.append("Email of person : " + u.email + "\n\n");
                                            Intent xyz = getIntent();
                                            PendingIntent resultPendingIntent =
                                                    PendingIntent.getActivity(
                                                            getApplicationContext(),
                                                            0,
                                                            getIntent(),
                                                            PendingIntent.FLAG_UPDATE_CURRENT
                                                    );
                                            mBuilder.setContentIntent(resultPendingIntent);
                                            int mNotificationId = 001;
                                            mNotificationManager.notify(mNotificationId, mBuilder.build());
                                            break;
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
