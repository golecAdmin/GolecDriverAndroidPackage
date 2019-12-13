package com.golecdriver;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.golecdriver.directionhelpers.FetchURL;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;

import java.util.ArrayList;

public class MapList implements ListAdapter, OnMapReadyCallback, TaskLoadedCallback {
    ArrayList<TripData> arrayList;
    Context context;
    Activity activity;
     TripData subjectData;
    private MapView mapView,mapViews;
    private MarkerOptions place1, place2;
    private static final String MAP_VIEW_BUNDLE_KEY ="";
    private GoogleMap gmap; TextView km;
    private Polyline currentPolyline;

    ArrayList markerPoints= new ArrayList();
    public MapList(Context context, ArrayList<TripData> arrayList,Activity activity) {
        this.arrayList=arrayList;
        this.context=context;
        this.activity=activity;
    }
    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }
    @Override
    public boolean isEnabled(int position) {
        return true;
    }
    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
    }
    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }
    @Override
    public Object getItem(int position) {
        return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public boolean hasStableIds() {
        return false;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       subjectData  =arrayList.get(position);
        if(convertView==null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView=layoutInflater.inflate(R.layout.mylist, null);

            Bundle bundle=new Bundle();
            bundle.putString("MAP_VIEW_BUNDLE_KEY",context.getString(R.string.key));

            mapView = convertView.findViewById(R.id.map_view);
            mapView.onCreate(bundle);
            mapView.getMapAsync(this);
            mapView.onSaveInstanceState(bundle);
            mapView.onStart();

            TextView time=convertView.findViewById(R.id.date);
          km=convertView.findViewById(R.id.km);
            TextView   end=convertView.findViewById(R.id.endAdd);
            TextView  startAdd=convertView.findViewById(R.id.startAdd);
            time.setText(subjectData.getTmie());
            km.setText(subjectData.getKm());
            startAdd.setText(subjectData.getStartAddress());
            end.setText(subjectData.getEndAddress());
           // String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + subjectData.getStartAddress() + "&destinations=" +subjectData.getEndAddress()+ "&mode=driving&language=fr-FR&avoid=tolls&key=AIzaSyBlyhD0vrhYc7DmiIEGqI6zh5o1F-oNZtQ";
            //new GeoTask(activity).execute(url);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // Intent i = new Intent(context, SingalTripActivity.class);
                   // i.putExtra("sampleObject", subjectData);
                  //0  context.startActivity(i);
                }
            });

        }
        return convertView;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public int getViewTypeCount() {
        int count;
        if (arrayList.size() > 0) {
            count = getCount();
        } else {
            count = 1;
        }
        return count;    }
    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
    /* gmap = googleMap;
        GoogleMapOptions options = new GoogleMapOptions().liteMode(true);
        gmap.setIndoorEnabled(true);
        UiSettings uiSettings = gmap.getUiSettings();
        uiSettings.setIndoorLevelPickerEnabled(false);
        uiSettings.setMyLocationButtonEnabled(false);
        uiSettings.setMapToolbarEnabled(false);
        uiSettings.setCompassEnabled(false);
        uiSettings.setZoomControlsEnabled(false);uiSettings.setScrollGesturesEnabled(false);
        String currentString = subjectData.getPickLo();
        String[] separated = currentString.split(",");
        double lat= Double.parseDouble(separated[0]);
        double longs= Double.parseDouble(separated[1]);

        String currentStrings = subjectData.getDropLo();
        String[] separateds = currentStrings.split(",");
        double lat2= Double.parseDouble(separateds[0]);
        double longs2= Double.parseDouble(separateds[1]);

        LatLng latLng=new LatLng(lat,longs);
        LatLng streetLocation=new LatLng(lat2,longs2);


        /*LatLngBounds zoom = new LatLngBounds(
                new LatLng(lat, longs), new LatLng(26.8037, 75.8085));*/

// Set the camera to the greatest possible zoom level that includes the
// bounds
/*LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(latLng).include(streetLocation);
        gmap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 0));*/

        place1 = new MarkerOptions().position(new LatLng(27.658143, 85.3199503)).title("Location 1");
        place2 = new MarkerOptions().position(new LatLng(27.667491, 85.3208583)).title("Location 2");
        new FetchURL(context.getApplicationContext()).execute(getUrl(place1.getPosition(), place2.getPosition(), "driving"), "driving");


    }
    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + context.getString(R.string.key);
        return url;
    }


    @Override
    public void onTaskDone(Object... values) {

    }
}