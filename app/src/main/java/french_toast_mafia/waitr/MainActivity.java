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

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class MainActivity extends FragmentActivity {

  public static ArrayAdapter<String> arrayAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

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
}
