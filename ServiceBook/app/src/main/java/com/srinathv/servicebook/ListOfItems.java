package com.srinathv.servicebook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ListOfItems extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_items);

        Button profile = (Button) findViewById(R.id.profile);
        Button bookings = (Button) findViewById(R.id.bookings);
        Button bookservices = (Button) findViewById(R.id.bookservices);
        Button rate = (Button) findViewById(R.id.rate);
        profile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent xyz = new Intent(getApplicationContext(),Profile.class);
                startActivity(xyz);
            }
        });

        bookings.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent xyz = new Intent(getApplicationContext(),Bookings.class);
                startActivity(xyz);
            }
        });

        bookservices.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent xyz = new Intent(getApplicationContext(),LoggedIn.class);
                xyz.putExtra("done","abcde");
                startActivity(xyz);
                finish();
            }
        });

        rate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent xyz = new Intent(getApplicationContext(),Rate.class);
                startActivity(xyz);
            }
        });

    }
}
