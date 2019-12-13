package com.golecdriver;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Splesh extends AppCompatActivity {
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splesh);
        mFirebaseAuth = FirebaseAuth.getInstance();



    }

    @Override
    protected void onStart() {
        super.onStart();
        EnableRuntimePermission();
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mAuthStateListener.onAuthStateChanged(mFirebaseAuth);

            }
        },2000);

    }

    public void EnableRuntimePermission() {

        ActivityCompat.requestPermissions(Splesh.this, new String[]
                {


                        Manifest.permission.ACCESS_FINE_LOCATION,



                }, 1);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case 1:

                if (grantResults.length > 0) {



                    boolean ACCESS_FINE_LOCATION= grantResults[0] == PackageManager.PERMISSION_GRANTED;



                    if ( ACCESS_FINE_LOCATION ) {

                        //Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();
                        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
                            @Override
                            public void onAuthStateChanged( FirebaseAuth firebaseAuth) {
                                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                                if( mFirebaseUser != null ){
                                    Intent intToHome = new Intent(Splesh.this,MainActivity.class);
                                    startActivity(intToHome);
                                    finish();
                                }
                                else{
                                    Intent intToHome = new Intent(Splesh.this,LoginActivity.class);
                                    startActivity(intToHome);
                                    finish();
                                }
                            }
                        };
                    }
                    else {
                        //  Toast.makeText(MainActivity.this,"Permission Denied",Toast.LENGTH_LONG).show();

                    }
                }

                break;
        }
    }


}
