package com.golecdriver;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth mAuth;
    DatabaseReference databaseReference, databaseReference1, Driver;
    String uuid;
    TripData tripData;
    ListView list;
    ArrayList<TripData> arrayList = new ArrayList<>();
    TextView name,email;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

            //FirebaseDatabase.getInstance().setPersistenceEnabled(true);


        mAuth = FirebaseAuth.getInstance();
        uuid = mAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("tripHistory").child(uuid);
        Driver = FirebaseDatabase.getInstance().getReference().child("DriverUser").child(uuid);
        list = findViewById(R.id.list);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

       // System.out.print(truncatedDouble);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
      name= (TextView) headerView.findViewById(R.id.name);
        email= (TextView) headerView.findViewById(R.id.email);


        Location mylocation = new Location("");
        Location dest_location = new Location("");
        //26.4435861,76.2017557
        String lat = "26.4435861";
        String lon = "76.2017557";

        //26.4412299,76.1894111
        String lata = "26.4412299";
        String lona= "76.1894111";
        //String lata = "";
      //  String lona = "";
        dest_location.setLatitude(Double.parseDouble(lat));
        dest_location.setLongitude(Double.parseDouble(lon));
        mylocation.setLatitude(Double.parseDouble(lata));
        mylocation.setLongitude(Double.parseDouble(lona));


        Float distance = mylocation.distanceTo(dest_location);//in meters
        //86394.988
        float km=distance/100;
        Log.e("kmnew",Double.toString(km));
        //86394.9765625
        int newkm=(int)km;
        Log.e("kmnewtest",convertMeterToKilometer(newkm));

        Geocoder geocoder;

        geocoder = new Geocoder(MainActivity.this, Locale.getDefault());


        try {
            //26.4413303,76.1895582
          List<Address> addresses = geocoder.getFromLocation(26.4415885,76.189538, 1);
          // Here 1 represent max location result to returned, by documents it recommended 1 to 5
          String  address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            Log.e("address",address);

        } catch (IOException e) {
            e.printStackTrace();
        }



      //  String
        //Log.w("km",convertMeterToKilometer(distance));

        if (mAuth.getCurrentUser() != null) {
            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        tripData = new TripData("");

                        String logs = ds.child("logs").getValue().toString();
                        String startAddress = ds.child("startAddress").getValue().toString();

                        String km = ds.child("km").getValue().toString();
                        String endAddress = ds.child("endAddress").getValue().toString();
                        String endLocation = ds.child("endLocation").getValue().toString();
                        if(ds.hasChild("lo")){
                            String lo = ds.child("lo").getValue().toString();

                        }

                       // String lo = ds.child("loss").getValue().toString();

                      //  Log.w("", logs.toString());
                        tripData.setTmie(convertDate(logs, "dd/MM/yyyy hh:mm"));
                        tripData.setKm(km);
                        tripData.setEndAddress(endAddress);
                        tripData.setStartAddress(startAddress);
                        //tripData.setPickLo(lo);
                        tripData.setDropLo(endLocation);


                        arrayList.add(tripData);
                    }

                    // Get Post object and use the values to update the UI
                    // ...
                    Collections.reverse(arrayList);
                    MapList customAdapter = new MapList(MainActivity.this, arrayList,MainActivity.this);
                    list.setAdapter(customAdapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    Log.w("", "loadPost:onCancelled", databaseError.toException());
                    // ...
                }
            };
            databaseReference.addValueEventListener(postListener);
        }

        allclint();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            mAuth.signOut();
            startActivity(new Intent(this,LoginActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera ac

            startActivity(new Intent(this,StartTrip.class));



        } else if (id == R.id.nav_gallery) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public static String convertDate(String dateInMilliseconds,String dateFormat) {
        return DateFormat.format(dateFormat, Long.parseLong(dateInMilliseconds)).toString();
    }

    void  allclint(){
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               Log.e("nsjcbd",dataSnapshot.child("name").getValue().toString());
              if(dataSnapshot.hasChild("name"))
              {
                  name.setText(dataSnapshot.child("name").getValue().toString());

              }
                if(dataSnapshot.hasChild("email"))
                {
                    email.setText(dataSnapshot.child("email").getValue().toString());

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        Driver.addListenerForSingleValueEvent(eventListener);
    }
    public static String convertMeterToKilometer(int totalDistance){
        double ff = totalDistance / 1000.0;
        BigDecimal bd = BigDecimal.valueOf(ff);
        bd = bd.setScale(1, RoundingMode.HALF_UP);
        return String.valueOf(bd.doubleValue());
    }




}
