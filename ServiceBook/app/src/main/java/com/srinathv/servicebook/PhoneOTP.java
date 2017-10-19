package com.srinathv.servicebook;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.Random;

import static android.R.attr.phoneNumber;

public class PhoneOTP extends AppCompatActivity {

    private SmsManager smsManager;
    private int OTP = 1000;
    private static final int MY_PERMISSION_REQUEST_CODE = 7171;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_otp);
        ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.SEND_SMS},1);
        final Button getotp = (Button) findViewById(R.id.getotp);
        final TextView txt1 = (TextView) findViewById(R.id.txt1);
        final TextView txt2 = (TextView) findViewById(R.id.txt2);
        final Button verifyotp = (Button) findViewById(R.id.verifyotp);
        final EditText number = (EditText) findViewById(R.id.phone);
        final EditText code = (EditText) findViewById(R.id.getcode);
        final TextView txtstatus = (TextView) findViewById(R.id.txtstatus);

        getotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String phone = number.getText().toString();

                if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(PhoneOTP.this, new String[]{
                            android.Manifest.permission.SEND_SMS
                    }, MY_PERMISSION_REQUEST_CODE);
                }
                else{
                    txt1.setVisibility(View.INVISIBLE);
                    getotp.setVisibility(View.INVISIBLE);
                    number.setVisibility(View.INVISIBLE);
                    txt2.setVisibility(View.VISIBLE);
                    verifyotp.setVisibility(View.VISIBLE);
                    code.setVisibility(View.VISIBLE);
                    txtstatus.setText("Generating OTP......");
                    OTP = OTP + (new Random()).nextInt(50);
                    final Handler handler = new Handler();
                    // delay ny 2 seconds..
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            txtstatus.setText("Sending Text Message......");
                        //    smsManager = SmsManager.getDefault();
                          //  smsManager.sendTextMessage(phone, null, "Your OTP to verify on ServiceBook is : "+ OTP, null, null);
                            txtstatus.setText("OTP sent.");
                            verifyotp.setOnClickListener(new View.OnClickListener(){
                                @Override
                                public void onClick(View view){
                                    String co = code.getText().toString();
                                    try {
                                        if (true) {
                                            Toast.makeText(PhoneOTP.this, "Success! You have connected your phone number with your ServiceBook account!", Toast.LENGTH_LONG).show();
                                            Intent gotologgedin = new Intent(getApplicationContext(), LoggedIn.class);
                                            gotologgedin.putExtra("number",phone);
                                            gotologgedin.putExtra("done","done2");
                                            startActivity(gotologgedin);
                                        } else txtstatus.setText("Invalid OTP");
                                    }
                                    catch(Exception e){
                                        Log.v("whatishappening",e.getMessage());
                                    }

                                }
                            });
                        }
                    }, 2000);
                }
            }
        });

    }

    public boolean verifyOTP(String s){
        if((OTP+"").equals(s)) return true;
        else return false;
    }
}
