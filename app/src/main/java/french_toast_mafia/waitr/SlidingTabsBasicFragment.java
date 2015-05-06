/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package french_toast_mafia.waitr;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import french_toast_mafia.view.SlidingTabLayout;

public class SlidingTabsBasicFragment extends Fragment {

  private SlidingTabLayout mSlidingTabLayout;
  private ViewPager mViewPager;
  private static final int PAGE_COUNT = 2; // change this to change number of pages in app
  public ListView listView;
  private GoogleMap mMap;
    String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=30.287702,-97.741569&radius=800&types=food|restaurants&key=AIzaSyDetgkaiV1zKHy4s168VqyGT9tIo8XBG8Q";

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

      return inflater.inflate(R.layout.fragment_sample, container, false);
  }

  /**
   * This is called after the {@link #onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)} has finished.
   * Here we can pick out the {@link android.view.View}s we need to configure from the content view.
   * <p/>
   * We set the {@link android.support.v4.view.ViewPager}'s adapter to be an instance of {@link SamplePagerAdapter}. The
   * {@link SlidingTabLayout} is then given the {@link android.support.v4.view.ViewPager} so that it can populate itself.
   *
   * @param view View created in {@link #onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)}
   */
  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    // BEGIN_INCLUDE (setup_viewpager)
      // Get the ViewPager and set it's PagerAdapter so that it can display items
    mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
    mViewPager.setAdapter(new SamplePagerAdapter());
    // END_INCLUDE (setup_viewpager)

    // BEGIN_INCLUDE (setup_slidingtablayout)
    // Give the SlidingTabLayout the ViewPager, this must be done AFTER the ViewPager has had
    // it's PagerAdapter set.
    mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
    mSlidingTabLayout.setViewPager(mViewPager);
    // END_INCLUDE (setup_slidingtablayout)



  }
  // END_INCLUDE (fragment_onviewcreated)

  /**
   * The {@link android.support.v4.view.PagerAdapter} used to display pages in this sample.
   * The individual pages are simple and just display two lines of text. The important section of
   * this class is the {@link #getPageTitle(int)} method which controls what is displayed in the
   * {@link SlidingTabLayout}.
   */
  class SamplePagerAdapter extends PagerAdapter {

    /**
     * @return the number of pages to display
     */
    @Override
    public int getCount() {
      return PAGE_COUNT;
    }

    /**
     * @return true if the value returned from {@link #instantiateItem(android.view.ViewGroup, int)} is the
     * same object as the {@link android.view.View} added to the {@link android.support.v4.view.ViewPager}.
     */
    @Override
    public boolean isViewFromObject(View view, Object o) {
      return o == view;
    }

    // BEGIN_INCLUDE (pageradapter_getpagetitle)

    /**
     * Return the title of the item at {@code position}. This is important as what this method
     * returns is what is displayed in the {@link SlidingTabLayout}.
     * <p/>
     * Here we construct one using the position value, but for real application the title should
     * refer to the item's contents.
     */
    @Override
    public CharSequence getPageTitle(int position) {
      switch (position) {
        case 0:
          return "Lines";
        case 1:
          return "Coupons";
        default:
          return "Oops";
      }
    }
    // END_INCLUDE (pageradapter_getpagetitle)

    /**
     * Instantiate the {@link android.view.View} which should be displayed at {@code position}. Here we
     * inflate a layout from the apps resources and then change the text view to signify the position.
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
      View view;
      TextView title;
      view = getActivity().getLayoutInflater().inflate(R.layout.pager_item,
              container, false);
      // Add the newly created View to the ViewPager
      container.addView(view);
      // Retrieve a TextView from the inflated View, and update it's text
      title = (TextView) view.findViewById(R.id.item_title);

      if(position == 0) { //lines page
        view = getActivity().getLayoutInflater().inflate(R.layout.map_page,
                container, false);
        // Add the newly created View to the ViewPager
        container.addView(view);
        // Retrieve a TextView from the inflated View, and update it's text
        title = (TextView) view.findViewById(R.id.item_title);
        mMap = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();
          LatLng ll = new LatLng(30.287702, -97.7414569);
          CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, 15);
          mMap.moveCamera(update);

        mMap.addMarker(new MarkerOptions().position(new LatLng(30.291218, -97.741535)).title("Kerbey Lane Cafe : 8m").icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
        mMap.addMarker(new MarkerOptions().position(new LatLng(30.286446, -97.745193)).title("Pluckers Wing Bar : 1m").icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
        mMap.addMarker(new MarkerOptions().position(new LatLng(30.28197, -97.740174)).title("The Carillon : 0m").icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
        mMap.addMarker(new MarkerOptions().position(new LatLng(30.287959, -97.74264)).title("Starbucks : 7m").icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
        mMap.addMarker(new MarkerOptions().position(new LatLng(30.285459, -97.742166)).title("Caffe Medici : 9m").icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
        mMap.addMarker(new MarkerOptions().position(new LatLng(30.293647, -97.741713)).title("Torchy's Tacos : 0m").icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
        mMap.addMarker(new MarkerOptions().position(new LatLng(30.289669 , -97.7416542)).title("Madam Mam's : 8m").icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
        mMap.addMarker(new MarkerOptions().position(new LatLng(30.291604 , -97.735006)).title(" Sao Paulo's Restaurante : 3m").icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
        mMap.addMarker(new MarkerOptions().position(new LatLng(30.281331 , -97.742235)).title("Papa John's Pizza : 9m").icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
        mMap.addMarker(new MarkerOptions().position(new LatLng(30.282138 , -97.74008)).title("Jimmy John's : 4m").icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
        mMap.addMarker(new MarkerOptions().position(new LatLng(30.282443 , -97.742923)).title("McDonald's : 8m").icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
        mMap.addMarker(new MarkerOptions().position(new LatLng(30.282155 , -97.742452)).title("Domino's Pizza : 0m").icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
        mMap.addMarker(new MarkerOptions().position(new LatLng(30.287861 , -97.741942)).title("Noodles and Company : 2m").icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
        mMap.addMarker(new MarkerOptions().position(new LatLng(30.292046 , -97.73432)).title("Double Dave's Pizza : 0m").icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
        mMap.addMarker(new MarkerOptions().position(new LatLng(30.293446 , -97.742133)).title("Whataburger : 9m").icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
        mMap.addMarker(new MarkerOptions().position(new LatLng(30.28129 , -97.741468)).title("Pizza Hut : 6m").icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
        mMap.addMarker(new MarkerOptions().position(new LatLng(30.285654 , -97.741983)).title("Chipotle Mexican Grill : 8m").icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
        mMap.addMarker(new MarkerOptions().position(new LatLng(30.282484 , -97.742298)).title("Pho Thaison : 5m").icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
        mMap.addMarker(new MarkerOptions().position(new LatLng(30.288553, -97.741737)).title("Mellow Mushroom : 0m").icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));


        final Button button = (Button) view.findViewById(R.id.btn_check_in);
        button.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Toast.makeText(v.getContext(), "Checked In", Toast.LENGTH_LONG).show();
          }
        });

      }
      if(position == 1) {
          LinearLayout layoutOfPopup;
          PopupWindow popupMessage;
          Button popupButton, insidePopupButton;
          TextView popupText;
        view = getActivity().getLayoutInflater().inflate(R.layout.coupons_page,
                container, false);
        // Add the newly created View to the ViewPager
        container.addView(view);
        // Retrieve a TextView from the inflated View, and update it's text
        title = (TextView) view.findViewById(R.id.item_subtitle);

        //Trying to get list view stuff to work...
        // Get ListView object from xml
        listView = (ListView) view.findViewById(R.id.list);

        // Attach the adapter to a ListView
        final ListView listView = (ListView) view.findViewById(R.id.list);
        // Assign adapter to ListView
        listView.setAdapter(MainActivity.arrayAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        /*   @Override
            public void onClick(View arg0) {
                LayoutInflater layoutInflater
                        = (LayoutInflater)getBaseContext()
                        .getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = layoutInflater.inflate(R.layout.popup, null);
                final PopupWindow popupWindow = new PopupWindow(
                        popupView,
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT);

                Button btnDismiss = (Button)popupView.findViewById(R.id.dismiss);
                btnDismiss.setOnClickListener(new Button.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        popupWindow.dismiss();
                    }});

                popupWindow.showAsDropDown(btnOpenPopup, 50, -30);

            }});
      }*/

          @Override
          public void onItemClick(AdapterView<?> parent, View view,
                                  int position, long id) {

            // ListView Clicked item index
            int itemPosition = position;

            // ListView Clicked item value
           String itemValue = (String) listView.getItemAtPosition(position);

            // Show Alert
            Toast.makeText(view.getContext(),
                    "You've got a coupon for " + itemValue, Toast.LENGTH_LONG)
                    .show();

          }
        });

       /*
         // Retrieve a ListView from the inflated View, and update it's list
        listView = (ListView) view.findViewById(R.id.list);
        // Defined Array values to show in ListView

        //listView.setAdapter(rm);
        // Retrieve a TextView from the inflated View, and update it's text
        title = (TextView) view.findViewById(R.id.item_title);
        title.setText(x);
        */
      }
      if (position > (PAGE_COUNT - 1)) { //not a page
        view = getActivity().getLayoutInflater().inflate(R.layout.pager_item,
                container, false);
        // Add the newly created View to the ViewPager
        container.addView(view);
        // Retrieve a TextView from the inflated View, and update it's text
        title = (TextView) view.findViewById(R.id.item_title);
        title = oopsView(title);
      }

    //  TextView title = (TextView) view.findViewById(R.id.item_title);
   //   title.setText(String.valueOf(position + 1));

      // Return the View
      return view;
    }

    private TextView linesView(TextView title) {
      title.setText("Lines");
      return title;
    }

    private TextView couponsView(TextView title) {
      title.setText("Coupons");
      return title;
    }

    private TextView oopsView(TextView title) {
      title.setText("Oops");
      return title;
    }

    /**
     * Destroy the item from the {@link android.support.v4.view.ViewPager}. In our case this is simply removing the
     * {@link android.view.View}.
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
      container.removeView((View) object);
    }

    public void checkIn(View view) {
      Toast.makeText(view.getContext(), "Checked In", Toast.LENGTH_LONG).show();
    }

  }


}
