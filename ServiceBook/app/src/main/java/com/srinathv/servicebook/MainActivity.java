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

        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            Intent abcd = new Intent(getApplicationContext(),LoggedIn.class);
            startActivity(abcd);
            finish();
        }

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();

        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener(){
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                Toast.makeText(MainActivity.this, "Check your internet connection.", Toast.LENGTH_SHORT).show();
            }
        }).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

        final SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);

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

                    Intent i = new Intent(getApplicationContext(),LoggedIn.class);
                    startActivity(i);
                    finish();
                    firebaseAuthWithGoogle(acct);
                }
                catch(Exception e){
                    Toast.makeText(this, e.getMessage()+" THIS ONE", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Google account has signed out.", Toast.LENGTH_SHORT).show();
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
                    Log.v("TAG2","Sign in with credentials success");
                    FirebaseUser us = mAuth.getCurrentUser();

              //      Log.v("TAG2",FirebaseAuth.getInstance().getCurrentUser().getUid()+"");
              //      Log.v("TAG2",FirebaseAuth.getInstance().getCurrentUser().getEmail()+"");
              //      Log.v("TAG2",FirebaseAuth.getInstance().getCurrentUser().getDisplayName()+"");
                }
                else{
                    //sign in failed
                    Log.v("TAG3","Sign in with credentials failed :(");
                }
            }
        });
    }

}
