package com.srinathv.servicebook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        final EditText e1 = (EditText) findViewById(R.id.edt1);
        final EditText e2 = (EditText) findViewById(R.id.edt2);
        final EditText e3 = (EditText) findViewById(R.id.edt3);

        Button b = (Button) findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String name = e1.getText().toString();
                String email = e2.getText().toString();
                String phone = e3.getText().toString();
                if(name.length()!=0 && email.length()!=0 && phone.length()!=0){

                }
                else{
                    Toast.makeText(MainActivity.this, "Please enter all details.", Toast.LENGTH_SHORT).show();
                }
            }   
        });

    }
}
