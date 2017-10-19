package com.srinathv.servicebook;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ScheduleService extends AppCompatActivity {
    public String entireday = "";
    public String time = "";
    public int ddd = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_service);
        final TextView txtdate = (TextView) findViewById(R.id.txtdate);
        final TextView txtbrief = (TextView) findViewById(R.id.txtbrief);
        final TextView txttime = (TextView) findViewById(R.id.txttime);
        final EditText e = (EditText) findViewById(R.id.edt);
        final Button settime = (Button) findViewById(R.id.butttime);
        final Button schedule = (Button) findViewById(R.id.buttbook);

        final CalendarView v = (CalendarView) findViewById(R.id.calendarView);
        v.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                String dd = "", mm = "", yyyy = "";
                Calendar c = Calendar.getInstance();
                c.set(year, month, dayOfMonth);
                dd = dayOfMonth + "";
                mm = (month + 1) + "";
                yyyy = year + "";
                entireday = dd + "-" + mm + "-" + yyyy;
                txtdate.setText(entireday);
            }
        });

        settime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final TimePicker tp = (TimePicker) findViewById(R.id.picktime);
                final Button oktime = (Button) findViewById(R.id.oktime);
                txttime.setVisibility(GONE);
                txtdate.setVisibility(GONE);
                settime.setVisibility(GONE);
                v.setVisibility(GONE);
                tp.setVisibility(VISIBLE);
                oktime.setVisibility(VISIBLE);
                schedule.setVisibility(GONE);
                e.setVisibility(GONE);
                txtbrief.setVisibility(GONE);

                tp.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                    public void onTimeChanged(TimePicker t, int hourofday, int minute) {
                        String AM_PM = "AM";
                        if (hourofday >= 12) {
                            hourofday = hourofday - 12;
                            AM_PM = "PM";
                        }
                        time = hourofday + " : " + minute + " " + AM_PM;
                        txttime.setText(time);

                        oktime.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                txttime.setVisibility(VISIBLE);
                                txtdate.setVisibility(VISIBLE);
                                settime.setVisibility(VISIBLE);
                                schedule.setVisibility(VISIBLE);
                                v.setVisibility(VISIBLE);
                                tp.setVisibility(GONE);
                                oktime.setVisibility(GONE);
                                e.setVisibility(VISIBLE);
                                txtbrief.setVisibility(VISIBLE);
                            }
                        });
                    }
                });
            }
        });

        schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText desc = (EditText) findViewById(R.id.edt);
                final String description = desc.getText().toString();

                if(description.length()!=0 && time.length()!=0 && entireday.length()!=0){
                    Intent i = getIntent();
                    final String serv = i.getStringExtra("service");
                    final service s = new service();
                    FirebaseDatabase.getInstance().getReference("services").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                service s = snapshot.getValue(service.class);
                                if(s.servid.substring(0,2).equals("IM")) ddd = Integer.parseInt(s.servid.substring(2));
                                else ddd = Integer.parseInt(s.servid);
                            }

                            if(ddd==0)
                                ddd = (new Random()).nextInt(50);
                            else
                                ddd = ddd +(10 + (new Random()).nextInt(50) );

                            s.setdesc(description);
                            s.setname(serv);
                            s.setdate(entireday);
                            s.settime(time);
                            s.setsid(ddd+"");
                            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid()+"";
                            s.setuid(uid);
                            FirebaseDatabase.getInstance().getReference("services").child(ddd+"").setValue(s);
                            final int abcdef = ddd;

                            FirebaseDatabase.getInstance().getReference("users").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        User us = snapshot.getValue(User.class);
                                        if(us.uid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                                            String allbookings = us.bookings+","+abcdef;
                                            Log.v("whatishappening",abcdef+"");
                                            FirebaseDatabase.getInstance().getReference("users/"+FirebaseAuth.getInstance().getCurrentUser().getUid()).child("bookings").setValue(allbookings);
                                            break;
                                        }
                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Toast.makeText(ScheduleService.this, "An error occurred. Try again later.", Toast.LENGTH_SHORT).show();
                                }
                            });

                            Toast.makeText(ScheduleService.this, "Service has been booked.", Toast.LENGTH_SHORT).show();
                            Intent xyz = new Intent(getApplicationContext(),LoggedIn.class);
                            xyz.putExtra("done","notdone");
                            startActivity(xyz);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(ScheduleService.this, "An error occurred. Try again later.", Toast.LENGTH_SHORT).show();
                        }
                    });


                }
                else{
                    Toast.makeText(ScheduleService.this, "Please enter all details.", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }
}
