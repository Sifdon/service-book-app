package com.srinathv.servicebook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ServiceSelected extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_selected);

        Intent i = getIntent();

        final String service = i.getStringExtra("service");
        TextView txtser = (TextView) findViewById(R.id.txtservice);
        txtser.setText(service);

        ImageView img = (ImageView) findViewById(R.id.imgservice);

        if(service.equals("Electrician")){
            img.setMaxWidth(83);
            img.setMaxHeight(83);
            img.setImageDrawable(getResources().getDrawable(R.drawable.electrician, getApplicationContext().getTheme()));
        }

        else if(service.equals("Plumber")){
            img.setMaxWidth(85);
            img.setMaxHeight(89);
            img.setImageDrawable(getResources().getDrawable(R.drawable.plumber, getApplicationContext().getTheme()));
        }

        else if(service.equals("Car Service")){
            img.setMaxWidth(100);
            img.setMaxHeight(93);
            img.setImageDrawable(getResources().getDrawable(R.drawable.car, getApplicationContext().getTheme()));
        }

        else if(service.equals("Cleaning Service")){
            img.setMaxWidth(89);
            img.setMaxHeight(85);
            img.setImageDrawable(getResources().getDrawable(R.drawable.cleaning, getApplicationContext().getTheme()));
        }

        else if(service.equals("AC, fridge, and other services")){
            img.setMaxWidth(145);
            img.setMaxHeight(98);
            img.setImageDrawable(getResources().getDrawable(R.drawable.other , getApplicationContext().getTheme()));
        }

        else if(service.equals("DJ booking")){
            img.setMaxWidth(136);
            img.setMaxHeight(96);
            img.setImageDrawable(getResources().getDrawable(R.drawable.dj, getApplicationContext().getTheme()));
        }

        else if(service.equals("Photography")){
            img.setMaxWidth(82);
            img.setMaxHeight(84);
            img.setImageDrawable(getResources().getDrawable(R.drawable.photos, getApplicationContext().getTheme()));
        }

        else if(service.equals("Bike Service")){
            img.setMaxWidth(102);
            img.setMaxHeight(86);
            img.setImageDrawable(getResources().getDrawable(R.drawable.bike, getApplicationContext().getTheme()));
        }

        Button schedule = (Button) findViewById(R.id.butt1);
        Button booknow = (Button) findViewById(R.id.butt2);

        schedule.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent i = new Intent(getApplicationContext(), ScheduleService.class);
                i.putExtra("service",service);
                startActivity(i);
                finish();
            }
        });

        booknow.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent i = new Intent(getApplicationContext(), BookNow.class);
                i.putExtra("service",service);
                startActivity(i);
                finish();
            }
        });

    }
}
