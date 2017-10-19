package com.srinathv.servicebook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.support.annotation.NonNull;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    public GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);
        final TextView txtstatus = (TextView) findViewById(R.id.txtstat);
        final SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        txtstatus.setVisibility(View.INVISIBLE);
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            //user has logged in to firebase with Google
            if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals("asdasdsad asdsadsa")){// <-- enter the mechanic's user id here
                Intent xyz = new Intent(getApplicationContext(),MechanicActivity.class);
                startActivity(xyz);
                finish();
            }
            else{
                txtstatus.setVisibility(View.VISIBLE);
                FirebaseDatabase.getInstance().getReference("users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                User us = snapshot.getValue(User.class);
                                if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(us.uid)) {
                                    if (us.phone.equals("")) {
                                        Intent pqr = new Intent(getApplicationContext(),PhoneOTP.class);
                                        startActivity(pqr);
                                        finish();
                                    }
                                    else{
                                        NormalUserActivity();
                                        finish();
                                    }
                                    break;
                                }
                            }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(MainActivity.this, "An error occurred. Try again later.", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        }
        else{
            // user has not signed in. He has to sign in.
            signInButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    switch(view.getId()){
                        case R.id.sign_in_button:signIn();
                            break;
                    }
                }
            });
        }

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();

        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener(){
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                Toast.makeText(MainActivity.this, "Check your internet connection.", Toast.LENGTH_SHORT).show();
            }
        }).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

    }

    private void NormalUserActivity(){
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            Intent abcd = new Intent(getApplicationContext(),LoggedIn.class);
            abcd.putExtra("done","done");
            startActivity(abcd);
            finish();
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestcode, int resultcode, Intent data){
        super.onActivityResult(requestcode,resultcode,data);

        if(requestcode == RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Signed in successfully, show authenticated UI.
                try {
                    GoogleSignInAccount acct = result.getSignInAccount();
                    firebaseAuthWithGoogle(acct);
                }
                catch(Exception e){
                    Log.v("happen",e.getMessage());
                }
            } else {
                Log.v("happen","Google account has signed out");
                // Signed out
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount ac){
        AuthCredential cred = GoogleAuthProvider.getCredential(ac.getIdToken(),null);
        mAuth.signInWithCredential(cred).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    //sign in success
                    if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals("ahdgasjgd adasd ")){
                        Intent xyz = new Intent(getApplicationContext(),MechanicActivity.class);
                        startActivity(xyz);
                        finish();
                    }
                    else {
                        FirebaseDatabase.getInstance().getReference("users").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    User us = snapshot.getValue(User.class);
                                    if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(us.uid)) {
                                        if (!us.phone.equals("")) {
                                            Intent pqr = new Intent(getApplicationContext(),PhoneOTP.class);
                                            startActivity(pqr);
                                            finish();
                                        }
                                        else{
                                            NormalUserActivity();
                                            finish();
                                        }
                                        break;
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(MainActivity.this, "An error occurred. Try again later.", Toast.LENGTH_SHORT).show();
                            }
                        });
                        NormalUserActivity();
                        finish();
                    }
                }
                else{
                    //sign in failed
                    Toast.makeText(MainActivity.this, "Sign in to firebase failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
