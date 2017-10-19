package com.srinathv.servicebook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final TextView txt1= (TextView) findViewById(R.id.txt1);
        final TextView txt2= (TextView) findViewById(R.id.txt2);
        final TextView txt3= (TextView) findViewById(R.id.txt3);

        final EditText edtname = (EditText) findViewById(R.id.edtname);
        final EditText edtmail = (EditText) findViewById(R.id.edtmail);
        final EditText edtphone = (EditText) findViewById(R.id.edtphone);

        final Button ok = (Button) findViewById(R.id.ok);
        final Button edit= (Button) findViewById(R.id.edit);
        final Button signout= (Button) findViewById(R.id.signout);
        edtname.setEnabled(false);
        edtmail.setEnabled(false);
        edtphone.setEnabled(false);
        ok.setVisibility(View.INVISIBLE);
        edtmail.setHorizontallyScrolling(true);
        edtname.setHorizontallyScrolling(true);
        edtphone.setHorizontallyScrolling(true);

        FirebaseDatabase.getInstance().getReference("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User us = snapshot.getValue(User.class);
                    if(us.uid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                        edtname.setText(us.name);
                        edtmail.setText(us.email);
                        edtphone.setText(us.phone);
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Profile.this, "An error occurred. Try again later.", Toast.LENGTH_SHORT).show();
            }
        });

        

    }
}
