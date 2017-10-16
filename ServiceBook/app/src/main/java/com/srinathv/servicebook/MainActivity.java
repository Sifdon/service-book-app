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
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    public GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener(){
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                Toast.makeText(MainActivity.this, "Check your internet connection.", Toast.LENGTH_SHORT).show();
            }
        }).addApi(Auth.GOOGLE_SIGN_IN_API).build();

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

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestcode, int resultcode, Intent data){
        super.onActivityResult(requestcode,resultcode,data);

        if(requestcode == RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        //Log.v(TAG, "handleSignInResult:" + result.isSuccess());
        Log.v("TAG1",result.isSuccess()+"");
        String name = "",email="";
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            Toast.makeText(this, "SIGN IN SUCCESS!!!!", Toast.LENGTH_SHORT).show();
            GoogleSignInAccount acct = result.getSignInAccount();
            name = acct.getDisplayName();
            email = acct.getEmail();
            Toast.makeText(this, acct.getDisplayName()+"   "+acct.getEmail(), Toast.LENGTH_SHORT).show();
       //     updateUI(true);
        } else {
            // Signed out
        }
    }

}
