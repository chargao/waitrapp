/*
* Copyright 2013 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package french_toast_mafia.waitr;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


public class MainActivity extends FragmentActivity {

  public static ArrayAdapter<String> arrayAdapter;
  private static final int GPSERROR = 9001;
  GoogleMap mMap;
  private static final float DEFAULTZOOM = 15;
  Marker mapMarker;
  String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=30.287702,-97.741569&radius=800&types=food|restaurants&key=AIzaSyDetgkaiV1zKHy4s168VqyGT9tIo8XBG8Q";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

      setContentView(R.layout.activity_main);


    /*final Button button = (Button) findViewById(R.id.btn_check_in);
    button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
          Toast.makeText(v.getContext(), "Checked In", Toast.LENGTH_LONG).show();
      }
    });
*/
    // Construct the data source
    ArrayList<String> restaurants = new ArrayList<String>();
    restaurants.add("Whataburger");
    restaurants.add("Chipotle Mexican Grill");
    restaurants.add("Torchy's Tacos");
    restaurants.add("Starbucks");
    restaurants.add("Noodles and Company");
    restaurants.add("Mellow Mushroom");
    restaurants.add("Pluckers Wing Bar");
    restaurants.add("Jimmy John's");
// Create the adapter to convert the array to views
    CustomAdapter adapter = new CustomAdapter(this, restaurants);
    arrayAdapter = adapter;

    if (savedInstanceState == null) {
      FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
      SlidingTabsBasicFragment fragment = new SlidingTabsBasicFragment();
      transaction.replace(R.id.sample_content_fragment, fragment);
      transaction.commit();
      new GcmRegistrationAsyncTask(this).execute();
    }



  }

  @Override
  protected void onStart() {
    super.onStart();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    return super.onPrepareOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    return super.onOptionsItemSelected(item);
  }



  /** A method to download json data from url */
  private String downloadUrl(String strUrl) throws IOException {
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



}
