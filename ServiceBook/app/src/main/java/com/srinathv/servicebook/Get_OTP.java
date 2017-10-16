package com.srinathv.servicebook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.TextView;

public class Get_OTP extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get__otp);
        Intent i = getIntent();
        String name = i.getStringExtra("name");
        String email = i.getStringExtra("email");

        TextView text1 = (TextView) findViewById(R.id.txt1);
        TextView text2 = (TextView) findViewById(R.id.txt2);

        text1.setText(name);
        text2.setText(email);

    }
}
