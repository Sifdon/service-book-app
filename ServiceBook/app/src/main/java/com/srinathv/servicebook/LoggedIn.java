package com.srinathv.servicebook;

import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

public class LoggedIn extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);

        mAuth = FirebaseAuth.getInstance();

        String name = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        User u = new User();
        u.setmail(email);
        u.setname(name);

        FirebaseDatabase.getInstance().getReference("users").child(uid).setValue(u);



        ((ImageView) findViewById(R.id.imgmenu)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

            }
        });

        // SET LISTENERS FOR ALL IMAGE BUTTONS AND FOLLOW PDF

        final Intent i = new Intent(getApplicationContext(),ServiceSelected.class);

        ((ImageView) findViewById(R.id.imgelectrician)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                i.putExtra("service","Electrician");
                startActivity(i);
            }
        });

        ((ImageView) findViewById(R.id.imgplumber)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                i.putExtra("service","Plumber");
                startActivity(i);
            }
        });

        ((ImageView) findViewById(R.id.imgcar)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                i.putExtra("service","Car Service");
                startActivity(i);
            }
        });

        ((ImageView) findViewById(R.id.imgclean)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                i.putExtra("service","Cleaning Service");
                startActivity(i);
            }
        });

        ((ImageView) findViewById(R.id.imgother)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                i.putExtra("service","AC, fridge, and other services");
                startActivity(i);
            }
        });

        ((ImageView) findViewById(R.id.imgdj)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                i.putExtra("service","DJ booking");
                startActivity(i);
            }
        });

        ((ImageView) findViewById(R.id.imgphoto)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                i.putExtra("service","Photography");
                startActivity(i);
            }
        });

        ((ImageView) findViewById(R.id.imgbike)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                i.putExtra("service","Bike Service");
                startActivity(i);
            }
        });

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount ac){
        AuthCredential cred = GoogleAuthProvider.getCredential(ac.getIdToken(),null);
        mAuth.signInWithCredential(cred).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    //sign in success
                    Log.v("TAG2","Sign in with credentials success");
                    FirebaseUser us = mAuth.getCurrentUser();

                    Log.v("TAG2", FirebaseAuth.getInstance().getCurrentUser().getUid()+"");
                    Log.v("TAG2",FirebaseAuth.getInstance().getCurrentUser().getEmail()+"");
                    Log.v("TAG2",FirebaseAuth.getInstance().getCurrentUser().getDisplayName()+"");
                }
                else{
                    //sign in failed
                    Log.v("TAG3","Sign in with credentials failed :(");
                }
            }
        });
    }
}
