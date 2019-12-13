package com.golecdriver;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;



import com.golecdriver.Model.User;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class StartTrip extends AppCompatActivity implements AdapterView.OnItemSelectedListener, GeoTask.Geo, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, OnMapReadyCallback {
    List<User> userList = new ArrayList<>();
    DatabaseReference databaseReference, Driver, myRef, tripHistory,databaseReference1,databaseReference2;
    Spinner spinner;
    String dataotp = "";
    String otp, clientId, clientName;
    double latitude, longitude;
    LinearLayout otpLay, veri, l, endLey;
    TextView otpsend, txveri, end, cancel, lo;
    LatLng startLatLng;
    private FirebaseAuth mAuth;
    EditText et_Otp;
    double logs;
    JSONObject jsonObjects;
    JSONObject jsonObject = new JSONObject();
    LocationManager locationManager;
    String subscriberID;
    List<Address> addresses;
    String address = "";
    private static final int REQUEST_LOCATION_PERMISSION = 1;


    private String provider;
    private MyLocationListener mylistener;
    private Criteria criteria;
     String llo;



    private GoogleApiClient googleApiClient;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 0;
    private LocationRequest locationRequest;
    private static final long UPDATE_INTERVAL = 0, FASTEST_INTERVAL = 0; // = 5 seconds
    // lists for permissions
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    // integer for permissions results request
    private static final int ALL_PERMISSIONS_RESULT = 1011;


    private GoogleMap gmap;
    LocationListener locationListener;

    Marker marker;


    private MapView mapView, mapViews;
    private MarkerOptions place1, place2;
    private static final String MAP_VIEW_BUNDLE_KEY = "";
    private static final String TAG = MainActivity.class.getSimpleName();

    /**
     * Code used in requesting runtime permissions.
     */
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    /**
     * Constant used in the location settings dialog.
     */
    private static final int REQUEST_CHECK_SETTINGS = 0x1;

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    // Keys for storing activity state in the Bundle.
    private final static String KEY_REQUESTING_LOCATION_UPDATES = "requesting-location-updates";
    private final static String KEY_LOCATION = "location";
    private final static String KEY_LAST_UPDATED_TIME_STRING = "last-updated-time-string";

    /**
     * Provides access to the Fused Location Provider API.
     */
    private FusedLocationProviderClient mFusedLocationClient;

    /**
     * Provides access to the Location Settings API.
     */
    private SettingsClient mSettingsClient;

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    private LocationRequest mLocationRequest;

    /**
     * Stores the types of location services the client is interested in using. Used for checking
     * settings to determine if the device has optimal location settings.
     */
    private LocationSettingsRequest mLocationSettingsRequest;

    /**
     * Callback for Location events.
     */
    private LocationCallback mLocationCallback;

    /**
     * Represents a geographical location.
     */
    private Location mCurrentLocation;

    // UI Widgets.
    private Button mStartUpdatesButton;
    private Button mStopUpdatesButton;
    private TextView mLastUpdateTimeTextView;
    private TextView mLatitudeTextView;
    private TextView mLongitudeTextView;

    // Labels.
    private String mLatitudeLabel;
    private String mLongitudeLabel;
    private String mLastUpdateTimeLabel;

    Marker mCurrLocationMarker;


    /**
     * Tracks the status of the location updates request. Value changes when the user presses the
     * Start Updates and Stop Updates buttons.
     */
    private Boolean mRequestingLocationUpdates;

    /**
     * Time when the location was updated represented as a String.
     */
    private String mLastUpdateTime;


    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_trip);
        spinner = (Spinner) findViewById(R.id.spinner);
        otpLay = findViewById(R.id.otp);
        otpsend = findViewById(R.id.sendotp);
        txveri = findViewById(R.id.veris);
        et_Otp = findViewById(R.id.otpedi);
        l = findViewById(R.id.l1);
        endLey = findViewById(R.id.endtrip);
        end = findViewById(R.id.end);
        lo = findViewById(R.id.lo);

        cancel = findViewById(R.id.can);

        Bundle bundle = new Bundle();
        bundle.putString("MAP_VIEW_BUNDLE_KEY", this.getString(R.string.key));

        mapView = findViewById(R.id.map);
        mapView.onCreate(bundle);
        mapView.getMapAsync(this);
        mapView.onSaveInstanceState(bundle);
        mapView.onStart();


        // we build google api client
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }



        mAuth = FirebaseAuth.getInstance();
        veri = findViewById(R.id.veri);
        databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Trip");
        databaseReference2= FirebaseDatabase.getInstance().getReference().child("Trip");


        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the location provider
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);   //default

        // user defines the criteria

        criteria.setCostAllowed(false);
        // get the best provider depending on the criteria
        provider = locationManager.getBestProvider(criteria, false);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
        }

        // the last known location of this provider
        // locationnew = locationManager.getLastKnownLocation(provider);

        mylistener = new MyLocationListener();


        // location updates: at least 1 meter and 200millsecs change
        locationManager.requestLocationUpdates(provider, 60000, 0, mylistener);
        if (locationManager != null) {
            if (mCurrentLocation != null) {

            }
        }
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            ;
        } else {
            showGPSDisabledAlertToUser();
        }
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Trip");
        myRef = FirebaseDatabase.getInstance().getReference().child("GeoLecClient");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(mAuth.getCurrentUser().getUid())) {
                    databaseReference.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                                Object object = dataSnapshot.getValue(Object.class);
                                String json = new Gson().toJson(object);
                                Log.w("", json.toString());
                                try {
                                    JSONObject jsonObject = new JSONObject(json.toString());
                                    if (jsonObject.has("clientId")) {
                                        clientId = jsonObject.getString("clientId");
                                    }

                                    if (jsonObject.has("status")) {
                                        String statss = jsonObject.getString("status");
                                        if (statss.equalsIgnoreCase("pending")) {
                                            viswHelper();
                                            veri.setVisibility(View.VISIBLE);
                                        } else if (statss.equalsIgnoreCase("ONGoing")) {
                                            viswHelper();
                                            end.setText("End  trip with" + "  " + jsonObject.getString("clientName"));

                                            endLey.setVisibility(View.VISIBLE);

                                        }

                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.w("ff", databaseError.getMessage());
                        }
                    });

                } else {
                    viswHelper();
                    l.setVisibility(View.VISIBLE);
                    otpLay.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        allclint();
        spinner.setOnItemSelectedListener(this);
        // construct a new instance of SimpleLocation


        otpsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mCurrentLocation != null) {
                    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {


                        //this.shared = new PrefrenceShared(this.context);

                        Geocoder geocoder;

                        geocoder = new Geocoder(StartTrip.this, Locale.getDefault());


                        try {
                            addresses = geocoder.getFromLocation(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                            address = addresses.get(0).getAddressLine(0);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                        // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        //String city = addresses.get(0).getLocality();
                        // String state = addresses.get(0).getAdminArea();
                        // String country = addresses.get(0).getCountryName();
                        //String postalCode = addresses.get(0).getPostalCode();
                        // String knownName = addresses.get(0).getFeatureName();

                        logs = System.currentTimeMillis();
                        HashMap map = new HashMap();
                        map.put("status", "pending");
                        map.put("clientId", clientId);
                        map.put("clientName", clientName);
                        map.put("logs", logs);
                        dataotp = otp;


                        map.put("DriverId", mAuth.getCurrentUser().getUid());
                        map.put("addrs", address);
                        map.put("otp", otp);
                        map.put("lo", mCurrentLocation.getLatitude() + "," + mCurrentLocation.getLongitude());
                        databaseReference.child(clientId).updateChildren(map);
                        databaseReference.child(mAuth.getCurrentUser().getUid().toString()).updateChildren(map).addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                otpLay.setVisibility(View.GONE);
                                l.setVisibility(View.GONE);
                                veri.setVisibility(View.VISIBLE);
                            }
                        });

                    } else {
                        showGPSDisabledAlertToUser();
                    }

                } else {
                    Toast.makeText(StartTrip.this, "No  location ",
                            Toast.LENGTH_SHORT).show();
                }


            }
        });

        txveri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    //  Toast.makeText(StartTrip.this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();

                    if(mCurrentLocation!=null){
                        otp();}
                    else {
                        Toast.makeText(StartTrip.this, "No  location ",
                                Toast.LENGTH_SHORT).show();
                    }

                } else {
                    showGPSDisabledAlertToUser();
                }

            }
        });
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    //  Toast.makeText(StartTrip.this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();
                     if(mCurrentLocation!=null){
                        endTrip();}
                        else {
                         Toast.makeText(StartTrip.this, "No  location ",
                                 Toast.LENGTH_SHORT).show();
                     }


                } else {
                    showGPSDisabledAlertToUser();
                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child(clientId).removeValue();
                databaseReference.child(mAuth.getCurrentUser().getUid().toString()).removeValue();
            }
        });


        mRequestingLocationUpdates = false;
        mLastUpdateTime = "";

        // Update values using data stored in the Bundle.
        updateValuesFromBundle(savedInstanceState);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);

        // Kick off the process of building the LocationCallback, LocationRequest, and
        // LocationSettingsRequest objects.
        createLocationCallback();
        createLocationRequest();
        buildLocationSettingsRequest();
        startLocationUpdates();





    }

    void allclint() {
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    User user1 = new User("", "", "");
                    String name = "";
                    String uuid = "";

                    if (ds.hasChild("name")) {
                        name = ds.child("name").getValue(String.class);

                    }

                    if (ds.hasChild("uuid")) {
                        uuid = ds.child("uuid").getValue(String.class);

                        Log.e("", name + "" + uuid);

                    }
                    user1.setName(name);
                    user1.setid(uuid);


                    userList.add(user1);

                }
                ArrayAdapter<User> adapter = new ArrayAdapter<User>(StartTrip.this,
                        android.R.layout.simple_spinner_item, userList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinner.setAdapter(adapter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        myRef.addListenerForSingleValueEvent(eventListener);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        User user = (User) parent.getSelectedItem();
        clientName = user.getName();

        clientId = user.getid();
        Log.e("clientId", clientId);
        Random rand = new Random();
        otp = String.format("%04d", rand.nextInt(10000));
        Log.e("clientId", otp);




        end.setText("End  trip with" + "  " + clientName);

        //  DownloadTask downloadTask = new DownloadTask();

        // Start downloading json data from Google Directions API
        // downloadTask.execute(url);
        //  Log.w("distance", String.valueOf(getDistance(latitude,longitude,endLatLng.latitude,endLatLng.longitude)));
        otpLay.setVisibility(View.VISIBLE);


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("My Current loction ", strReturnedAddress.toString());
            } else {
                Log.w("My Current loction ", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current loction ", "Canont get Address!");
        }
        return strAdd;
    }


    void otp() {
        String otp = et_Otp.getText().toString();

        if (TextUtils.isEmpty(otp)) {
            return;
        }else if(mCurrentLocation==null){
            Toast.makeText(this, "No Location", Toast.LENGTH_LONG).show();

        }


        databaseReference.child(clientId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    Object object = dataSnapshot.getValue(Object.class);
                    String json = new Gson().toJson(object);
                    Log.w("", json.toString());
                    try {
                        JSONObject jsonObject = new JSONObject(json.toString());
                        dataotp = jsonObject.getString("otp");
                        Log.w("dataotp", dataotp);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        if (otp.equalsIgnoreCase(dataotp)) {
            //startActivity(new Intent(StartTrip.this,MainActivity.class));
            logs = System.currentTimeMillis();
            Geocoder geocoder;

            geocoder = new Geocoder(StartTrip.this, Locale.getDefault());


            try {
                addresses = geocoder.getFromLocation(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            } catch (IOException e) {
                e.printStackTrace();
            }


            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

            HashMap map = new HashMap();
            map.put("status", "ONGoing");
            map.put("clientId", clientId);
            map.put("clientName", clientName);
            map.put("logs", logs);

            map.put("DriverId", mAuth.getCurrentUser().getUid());
            map.put("startAddress", address);
            map.put("otp", otp);
            map.put("startLocation", mCurrentLocation.getLatitude() + "," + mCurrentLocation.getLongitude());
            databaseReference.child(clientId).updateChildren(map);
            databaseReference.child(mAuth.getCurrentUser().getUid().toString()).updateChildren(map);

        } else {
            Toast.makeText(this, "invalid otp", Toast.LENGTH_LONG).show();

        }


    }

    void viswHelper() {
        l.setVisibility(View.GONE);
        otpLay.setVisibility(View.GONE);
        veri.setVisibility(View.GONE);
        endLey.setVisibility(View.GONE);
    }


    void endTrip() {
        end.setEnabled(false);
       llo = String.valueOf(System.currentTimeMillis());

        logs = System.currentTimeMillis();

        tripHistory = FirebaseDatabase.getInstance().getReference().child("tripHistory");
        databaseReference1.child(clientId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    Object object = dataSnapshot.getValue(Object.class);
                    String json = new Gson().toJson(object);
                    try {
                        jsonObjects = new JSONObject(json.toString());
                    } catch (Exception e) {
                    }
                }

                Log.w("", jsonObjects.toString());
                jsonObject = jsonObjects;
                Geocoder geocoder;

                geocoder = new Geocoder(StartTrip.this, Locale.getDefault());


                try {
                    addresses = geocoder.getFromLocation(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()


                } catch (IOException e) {
                    e.printStackTrace();
                    LatLng a=new LatLng(26.4059501,76.1792614);

                }

                try {


                    try {

                        Location locationB = new Location("26.4059501,76.1792614");
                        String currentString = jsonObject.getString("startLocation");
                        String[] separated = currentString.split(",");
                        // separated[0]; // this will contain "Fruit"
                        //  separated[1];
                        Location mylocation = new Location("");
                        Location dest_location = new Location("");
                        //26.4062776,76.1834614

                        String lata =separated[0];
                        String lona=separated[1];


                        StringBuilder sb = new StringBuilder();
                        sb.append("https://maps.googleapis.com/maps/api/distancematrix/json?origins=");
                        sb.append(lata+","+ lona);
                        sb.append("&destinations=");
                        sb.append(mCurrentLocation.getLatitude()+","+mCurrentLocation.getLongitude());
                        sb.append("&mode=driving&language=fr-FR&avoid=tolls&key=AIzaSyBlyhD0vrhYc7DmiIEGqI6zh5o1F-oNZtQ");
                        String url = sb.toString();
                        new GeoTask(StartTrip.this).execute(new String[]{url});

                        Map<String, Object> map = new Gson().fromJson(jsonObject.toString(), new TypeToken<HashMap<String, Object>>() {
                        }.getType());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                } catch (Exception e) {
                    e.printStackTrace();
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("ff", databaseError.getMessage());
            }
        });

    }


    @Override
    public void setDouble(String min) {


        String km = "0 Km";
        String dists = "0";
        String kms = "0 km";
        String res[] = min.split(",");


        Log.e("km", min);


        if (res.length == 2) {


            int s = Integer.parseInt(res[1]);

            kms = "" + convertMeterToKilometer(s) + " " + "km";

        }


        try {

            Geocoder geocoder;

            geocoder = new Geocoder(StartTrip.this, Locale.getDefault());


            try {
                addresses = geocoder.getFromLocation(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            } catch (IOException e) {
                e.printStackTrace();
            }


            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

            jsonObject.put("endAddress", address);
            jsonObject.put("endLocation", mCurrentLocation.getLatitude() + "," + mCurrentLocation.getLongitude());
            jsonObject.put("endlogs", llo);
            this.jsonObject.put("km", kms);




            jsonObject.put("status", "camplite");
            Map<String, Object> map = new Gson().fromJson(jsonObject.toString(), new TypeToken<HashMap<String, Object>>() {
            }.getType());
            //26.1624° N, 75.6208° E




            tripHistory.child(mAuth.getCurrentUser().getUid()).child(llo).updateChildren(map);
            tripHistory.child(clientId).child(llo).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    databaseReference1.child(clientId).removeValue();
                    databaseReference1.child(mAuth.getCurrentUser().getUid()).removeValue();
                    end.setEnabled(true);
                    dataotp = "";
                    et_Otp.setText("");

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String convertMeterToKilometer(int totalDistance) {
        double ff = totalDistance / 1000.0;
        BigDecimal bd = BigDecimal.valueOf(ff);
        bd = bd.setScale(1, RoundingMode.HALF_UP);
        return String.valueOf(bd.doubleValue());
    }

    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    @Override
    public void onLocationChanged(Location location) {
        //locationnew=location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        gmap = googleMap;
      //  GoogleMapOptions options = new GoogleMapOptions().liteMode(true);
       // options.Set
        googleMap.setMyLocationEnabled(true);
        if (googleMap != null) {
            googleMap.setOnMyLocationClickListener(new GoogleMap.OnMyLocationClickListener() {
                @Override
                public void onMyLocationClick(@NonNull Location location) {
                    StartTrip.this.mCurrentLocation=location;
                   LatLng coordinate = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                    CameraUpdate locations = CameraUpdateFactory.newLatLngZoom(coordinate,16);
                    gmap.animateCamera(locations);

                }
            });}
      /*  LatLng origin = new LatLng(-7.788969, 110.338382);
        LatLng destination = new LatLng(-7.781200, 110.349709);
        DrawRouteMaps.getInstance(this)
                .draw(origin, destination, gmap);
        DrawMarker.getInstance(this).draw(gmap, origin, R.drawable.marker, "Origin Location");
        DrawMarker.getInstance(this).draw(gmap, destination, R.drawable.marker, "Destination Location");

        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(origin)
                .include(destination).build();
        Point displaySize = new Point();
        getWindowManager().getDefaultDisplay().getSize(displaySize);
        gmap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, displaySize.x, 250, 30));*/


       /* if (googleMap == null) {
            gmap=googleMap;
           mapView.onStart();

            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
        if(googleMap!=null){
            googleMap.setMyLocationEnabled(true);
            if (googleMap == null) {
                // Try to obtain the map from the SupportMapFragment.
                mapView.onStart();
                googleMap.setMyLocationEnabled(true);
                locationnew = googleMap.getMyLocation();

                if (locationnew != null) {
                    LatLng coordinates = new LatLng(locationnew.getLatitude(),
                            locationnew.getLongitude());
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates
                            ,
                            15));
                }
                LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                String provider = locationManager.getBestProvider(criteria, true);
                Location location = locationManager.getLastKnownLocation(provider);


                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    LatLng latLng = new LatLng(latitude, longitude);
                    LatLng myPosition = new LatLng(latitude, longitude);


                    LatLng coordinate = new LatLng(latitude, longitude);
                    CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 19);
                    googleMap.animateCamera(yourLocation);
                }

                // Check if we were successful in obtaining the map.

                    });
                }
            }
        }
*/


    }


    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {

            //  locationnew=location;

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Toast.makeText(StartTrip.this, provider + "'s status changed to " + status + "!",
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(StartTrip.this, "Provider " + provider + " enabled!",
                    Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(StartTrip.this, "Provider " + provider + " disabled!",
                    Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();


    }


    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST);
            } else {
                finish();
            }

            return false;
        }

        return true;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION

        ) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000); // Update location every second

         mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        if (mCurrentLocation != null) {
            Geocoder geocoder;

            geocoder = new Geocoder(StartTrip.this, Locale.getDefault());


            try {
                addresses = geocoder.getFromLocation(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            } catch (IOException e) {
                e.printStackTrace();
            }


            // address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            // lo.setText("Address : " +address);
        }

    }


    @Override
    protected void onStop() {
        super.onStop();
      //  locationManager.removeUpdates(locationListener);
    }


    /**
     * Updates fields based on data stored in the bundle.
     *
     * @param savedInstanceState The activity state saved in the Bundle.
     */
    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            // Update the value of mRequestingLocationUpdates from the Bundle, and make sure that
            // the Start Updates and Stop Updates buttons are correctly enabled or disabled.
            if (savedInstanceState.keySet().contains(KEY_REQUESTING_LOCATION_UPDATES)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                        KEY_REQUESTING_LOCATION_UPDATES);
            }

            // Update the value of mCurrentLocation from the Bundle and update the UI to show the
            // correct latitude and longitude.
            if (savedInstanceState.keySet().contains(KEY_LOCATION)) {
                // Since KEY_LOCATION was found in the Bundle, we can be sure that mCurrentLocation
                // is not null.
               // mCurrentLocation = savedInstanceState.getParcelable(KEY_LOCATION);

            }

            // Update the value of mLastUpdateTime from the Bundle and update the UI.
            if (savedInstanceState.keySet().contains(KEY_LAST_UPDATED_TIME_STRING)) {
                mLastUpdateTime = savedInstanceState.getString(KEY_LAST_UPDATED_TIME_STRING);
            }
           // updateUI();
        }
    }

    /**
     * Sets up the location request. Android has two location request settings:
     * {@code ACCESS_COARSE_LOCATION} and {@code ACCESS_FINE_LOCATION}. These settings control
     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
     * the AndroidManifest.xml.
     * <p/>
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused Location Provider API returns location updates that are
     * accurate to within a few feet.
     * <p/>
     * These settings are appropriate for mapping applications that show real-time location
     * updates.
     */
    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Creates a callback for receiving location events.
     */
    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                mCurrentLocation = locationResult.getLastLocation();
              //  MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title("Hello Maps");
                if (mCurrLocationMarker != null)
                {
                    mCurrLocationMarker.remove();
                }

                //Place current location marker
                LatLng latLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
                markerOptions.title("Current Position");
              //  mCurrLocationMarker = gmap.addMarker(markerOptions);


               // gmap.addMarker(new MarkerOptions().position(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude())).title(""));
                LatLng coordinate = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                CameraUpdate location = CameraUpdateFactory.newLatLngZoom(

                        coordinate,16);
                gmap.animateCamera(location);
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
                updateLocationUI();
            }
        };
    }

    /**
     * Uses a {@link com.google.android.gms.location.LocationSettingsRequest.Builder} to build
     * a {@link com.google.android.gms.location.LocationSettingsRequest} that is used for checking
     * if a device has the needed location settings.
     */
    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.i(TAG, "User agreed to make required location settings changes.");
                        // Nothing to do. startLocationupdates() gets called in onResume again.
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i(TAG, "User chose not to make required location settings changes.");
                       // mRequestingLocationUpdates = false;
                      //  updateUI();
                        break;
                }
                break;
        }
    }

    /**
     * Handles the Start Updates button and requests start of location updates. Does nothing if
     * updates have already been requested.
     */
    public void startUpdatesButtonHandler(View view) {
        if (!mRequestingLocationUpdates) {
            mRequestingLocationUpdates = true;
           // setButtonsEnabledState();
           // startLocationUpdates();
        }
    }

    /**
     * Handles the Stop Updates button, and requests removal of location updates.
     */
    public void stopUpdatesButtonHandler(View view) {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
       // stopLocationUpdates();
    }

    /**
     * Requests location updates from the FusedLocationApi. Note: we don't call this unless location
     * runtime permission has been granted.
     */
    private void startLocationUpdates() {
        // Begin by checking if the device has the necessary location settings.
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(TAG, "All location settings are satisfied.");

                        //noinspection MissingPermission
                        if (ActivityCompat.checkSelfPermission(StartTrip.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(StartTrip.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());

                      //  updateUI();
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(StartTrip.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);
                                Toast.makeText(StartTrip.this, errorMessage, Toast.LENGTH_LONG).show();
                                mRequestingLocationUpdates = false;
                        }

                       // updateUI();
                    }
                });
    }

    /**
     * Updates all UI fields.
     */
   /* private void updateUI() {
        setButtonsEnabledState();
        updateLocationUI();
    }

    /**
     * Disables both buttons when functionality is disabled due to insuffucient location settings.
     * Otherwise ensures that only one button is enabled at any time. The Start Updates button is
     * enabled if the user is not requesting location updates. The Stop Updates button is enabled
     * if the user is requesting location updates.
     */
   /* private void setButtonsEnabledState() {
        if (mRequestingLocationUpdates) {
            mStartUpdatesButton.setEnabled(false);
            mStopUpdatesButton.setEnabled(true);
        } else {
            mStartUpdatesButton.setEnabled(true);
            mStopUpdatesButton.setEnabled(false);
        }
    }*/

    /**
     * Sets the value of the UI fields for the location latitude, longitude and last update time.
     */
    private void updateLocationUI() {
       /* if (mCurrentLocation != null) {
            mLatitudeTextView.setText(String.format(Locale.ENGLISH, "%s: %f", mLatitudeLabel,
                    mCurrentLocation.getLatitude()));
            mLongitudeTextView.setText(String.format(Locale.ENGLISH, "%s: %f", mLongitudeLabel,
                    mCurrentLocation.getLongitude()));
            mLastUpdateTimeTextView.setText(String.format(Locale.ENGLISH, "%s: %s",
                    mLastUpdateTimeLabel, mLastUpdateTime));
        }*/
    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    private void stopLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            Log.d(TAG, "stopLocationUpdates: updates never requested, no-op.");
            return;
        }

        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mRequestingLocationUpdates = false;
                      //  setButtonsEnabledState();
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Within {@code onPause()}, we remove location updates. Here, we resume receiving
        // location updates if the user has requested them.
        if (mRequestingLocationUpdates && checkPermissions()) {
            startLocationUpdates();
        } else if (!checkPermissions()) {
          //  requestPermissions();
        }

       // updateUI();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Remove location updates to save battery.
       // stopLocationUpdates();
    }

    /**
     * Stores activity data in the Bundle.
     */
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(KEY_REQUESTING_LOCATION_UPDATES, mRequestingLocationUpdates);
        savedInstanceState.putParcelable(KEY_LOCATION, mCurrentLocation);
        savedInstanceState.putString(KEY_LAST_UPDATED_TIME_STRING, mLastUpdateTime);
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * Shows a {@link Snackbar}.
     *
     * @param mainTextStringId The id for the string resource for the Snackbar text.
     * @param actionStringId   The text of the action item.
     * @param listener         The listener associated with the Snackbar action.
     */
    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(
                findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    /**
     * Return the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            showSnackbar(R.string.permission_rationale,
                    android.R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(StartTrip.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    });
        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(StartTrip.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (mRequestingLocationUpdates) {
                    Log.i(TAG, "Permission granted, updates requested, starting location updates");
                  //  startLocationUpdates();
                }
            } else {
                // Permission denied.

                // Notify the user via a SnackBar that they have rejected a core permission for the
                // app, which makes the Activity useless. In a real app, core permissions would
                // typically be best requested during a welcome-screen flow.

                // Additionally, it is important to remember that a permission might have been
                // rejected without asking the user for permission (device policy or "Never ask
                // again" prompts). Therefore, a user interface affordance is typically implemented
                // when permissions are denied. Otherwise, your app could appear unresponsive to
                // touches or interactions which have required permissions.
                showSnackbar(R.string.permission_denied_explanation,
                        R.string.settings, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
            }
        }
    }

    public void back(View v){
    onBackPressed();
    }

}



