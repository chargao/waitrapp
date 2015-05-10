package french_toast_mafia.waitr;

/**
 * Created by User on 5/5/2015.
 */

//import com.french_toast_mafia.waitr.RestaurantManager;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.app.Fragment;
import android.content.IntentSender;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.provider.Telephony;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONObject;

public class MapsActivity extends SupportMapFragment implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    public static final String TAG = MapsActivity.class.getSimpleName();
    private static final float DEFAULT_ZOOM = 13;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    public static Button btnSubmit;
    private final static double LOWER_LEFT_LAT = 30; //basically bounds on 78705
    private final static double LOWER_LEFT_LNG = -98;
    private final static double UPPER_RIGHT_LAT = 31;
    private final static double UPPER_RIGHT_LNG = -97;
    public static FragmentActivity fragmentActivity;
    public static RelativeLayout relativeLayout;

    String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=30.287702,-97.741569&radius=800&types=food|restaurants&key=AIzaSyDetgkaiV1zKHy4s168VqyGT9tIo8XBG8Q";

    public MapsActivity(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentActivity fa = (FragmentActivity) super.getActivity();
        RelativeLayout rl = (RelativeLayout) inflater.inflate(R.layout.map_page, container, false);
        fragmentActivity = fa;
        relativeLayout = rl;
        setUpMapIfNeeded();
        mGoogleApiClient = new GoogleApiClient.Builder(this.getActivity().getApplicationContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1000); // 1 second, in milliseconds

        //new JSONParse().execute();
        PlacesTask placesTask = new PlacesTask();

        // Invokes the "doInBackground()" method of the class PlaceTask
        placesTask.execute(url);

        rl.findViewById(R.id.map);
        return rl;

    }

    @Override
    public void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        mGoogleApiClient.connect();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     * <p/>
     * Sets up restaurant markers with different color markers indicating different line sizes
     */
    private void setUpMap() {

        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Test Marker").icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))); //test marker, get rid of this move rr markers to another method, can add "snippet" to markers for a lil description
        mMap.addMarker(new MarkerOptions().position(new LatLng(30.287864, -97.741881)).title("Qdoba").snippet("Line: Short").icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_GREEN))); //qdoba marker
        mMap.addMarker(new MarkerOptions().position(new LatLng(30.287381, -97.741808)).title("Which wich").snippet("Line: Medium").icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))); //which wich marker
        mMap.addMarker(new MarkerOptions().position(new LatLng(30.291225, -97.741473)).title("Kerbey Lane").snippet("Line: Long").icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_RED))); //kerbey lane marker

        // placeMarkers();
    }
/*
    private void placeMarkers() {
        double lat;
        double lng;
        String title;
        float hue;
        RestaurantManager rm = new RestaurantManager(MapsActivity.this, 0); //0 is not right!! i dont have a text view so idk...
        ArrayList<String> restaurants = rm.findRestaurants();
      /*  Iterator i = restaurants.iterator();
        while(i.hasNext()){
            Object r = i.next();
            String rr= r.toString();

            mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Test Marker").icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
        }
    }*/

    /*centers camera around devices current location and adds a marker and animated circle at it*/
    private void handleNewLocation(Location location, float zoom) {
        Log.d(TAG, location.toString());
        double currentLat = location.getLatitude();
        double currentLng = location.getLongitude();
        LatLng latLng = new LatLng(currentLat, currentLng);
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title("You are here!");
        mMap.addMarker(options);
        CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
        mMap.moveCamera(cu);

        /*adds animated circle around marker*/
        final Circle newLocationCircle = mMap.addCircle(new CircleOptions()
                .center(new LatLng(currentLat, currentLng))
                .radius(100)
                .strokeColor(Color.CYAN).radius(1000));
        ValueAnimator vAnimator = new ValueAnimator();
        vAnimator.setRepeatCount(ValueAnimator.INFINITE);
        vAnimator.setRepeatMode(ValueAnimator.RESTART);  /* PULSE */
        vAnimator.setIntValues(0, 100);
        vAnimator.setDuration(1000);
        vAnimator.setEvaluator(new IntEvaluator());
        vAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        vAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatedFraction = valueAnimator.getAnimatedFraction();
                newLocationCircle.setRadius(animatedFraction * 1000);
            }
        });
        vAnimator.start();
    }

    private void goToLocation(double addressLat, double addressLng, String locality, float zoom) throws IOException {
        LatLng latLng = new LatLng(addressLat, addressLng);
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(locality);
        mMap.addMarker(options);
        CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
        mMap.moveCamera(cu);

        /*adds animated circle around marker*/
        final Circle newLocationCircle = mMap.addCircle(new CircleOptions()
                .center(new LatLng(addressLat, addressLng))
                .radius(100)
                .strokeColor(Color.CYAN).radius(1000));
        ValueAnimator vAnimator = new ValueAnimator();
        vAnimator.setRepeatCount(ValueAnimator.INFINITE);
        vAnimator.setRepeatMode(ValueAnimator.RESTART);  /* PULSE */
        vAnimator.setIntValues(0, 100);
        vAnimator.setDuration(1000);
        vAnimator.setEvaluator(new IntEvaluator());
        vAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        vAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatedFraction = valueAnimator.getAnimatedFraction();
                newLocationCircle.setRadius(animatedFraction * 1000);
            }
        });
        vAnimator.start();
    }


    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Location services connected.");
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } else {
            handleNewLocation(location, DEFAULT_ZOOM);
        }
    }


    @Override
    public void onConnectionSuspended(int i) {
        //  Log.i(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this.getActivity(), CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        handleNewLocation(location, DEFAULT_ZOOM);

    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);


            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
           // Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }

        return data;
    }

    /** A class, to download Google Places */
    private class PlacesTask extends AsyncTask<String, Integer, String>{

        String data = null;

        // Invoked by execute() method of this object
        @Override
        protected String doInBackground(String... url) {
            try{
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(String result){
            ParserTask parserTask = new ParserTask();

            // Start parsing the Google places in JSON format
            // Invokes the "doInBackground()" method of the class ParseTask
            parserTask.execute(result);
        }

    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>>{

        JSONObject jObject;

        // Invoked by execute() method of this object
        @Override
        protected List<HashMap<String,String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;
            PlaceJSONParser placeJsonParser = new PlaceJSONParser();

            try{
                jObject = new JSONObject(jsonData[0]);

                /** Getting the parsed data as a List construct */
                places = placeJsonParser.parse(jObject);

            }catch(Exception e){
                Log.d("Exception",e.toString());
            }
            return places;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(List<HashMap<String,String>> list){

            // Clears all the existing markers
            mMap.clear();

            for(int i=0;i<list.size();i++){

                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();

                // Getting a place from the places list
                HashMap<String, String> hmPlace = list.get(i);

                // Getting latitude of the place
                double lat = Double.parseDouble(hmPlace.get("lat"));

                // Getting longitude of the place
                double lng = Double.parseDouble(hmPlace.get("lng"));

                // Getting name
                String name = hmPlace.get("place_name");

                // Getting vicinity
                String vicinity = hmPlace.get("vicinity");

                LatLng latLng = new LatLng(lat, lng);

                // Setting the position for the marker
                markerOptions.position(latLng);

                // Setting the title for the marker.
                //This will be displayed on taping the marker
                markerOptions.title(name + " : " + vicinity);

                // Placing a marker on the touched position
                mMap.addMarker(markerOptions);
            }
        }
    }

    public void checkIn(View view) {
        Toast.makeText(view.getContext(), "Checked In", Toast.LENGTH_LONG).show();
    }

}

    //geoLocateAddress locates address entered by user
    /*
    public void geoLocateAddress(View view) throws IOException {
        hideSoftKeyboard(view);
        EditText et = (EditText) findViewById(R.id.et_place);
        String addressLocation = et.getText().toString();
        Geocoder geocoder = new Geocoder(this);
        List<Address> list = geocoder.getFromLocationName(addressLocation, 1);
        Address address = list.get(0);
        String locality = address.getLocality();
        // Toast.makeText(this, locality, Toast.LENGTH_LONG).show();
        double lat = address.getLatitude();
        double lng = address.getLongitude();
        goToLocation(lat, lng, locality, DEFAULT_ZOOM);
        String zip = getZip(lat, lng);
        if(zip != "78705") {
            // inOurZone = false;
            Toast.makeText(this, "You are in "+zip, Toast.LENGTH_LONG).show();
        }
        if(zip == "78705") {
            // inOurZone = false;
            Toast.makeText(this,"You are in "+zip, Toast.LENGTH_LONG).show();
        }
        // Spinner spinner = (Spinner) findViewById(R.id.action_bar_spinner);
    }

    //return location zip code
    private String getZip(double lat, double lng) throws IOException{
        Locale current = getResources().getConfiguration().locale;
        Geocoder geocoder = new Geocoder(this, current.getDefault());
// lat,lng, your current location
        List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
        String zip = addresses.get(0).getPostalCode();
        return zip;
    }

    //hideSoftKeyboard hides keyboard on display
    private void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}*/
