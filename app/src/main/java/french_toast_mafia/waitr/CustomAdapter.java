package french_toast_mafia.waitr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by User on 5/5/2015.
 */
public class CustomAdapter extends ArrayAdapter<String> {
    public CustomAdapter(Context context, ArrayList<String> restaurants) {
        super(context, 0, restaurants);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        String restaurant = getItem(position);
        String[] strsplit = restaurant.split(",");
        ArrayList<String> coupons = new ArrayList<String>();
        coupons.add("$5.99 burger");
        coupons.add("$1.00 off burrito bowl");
        coupons.add("$1.25 off a taco meal");
        coupons.add("Buy one latte/macchiato get one free");
        coupons.add("Free mac & cheese bowl");
        coupons.add("Buy a pizza, get a side free");
        coupons.add("$5.50 for 10 wings");
        coupons.add("Free drink with purchase of sandwich");

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.coupons_page, parent, false);
        }
        // Lookup view for data population
        TextView restaurantText = (TextView) convertView.findViewById(R.id.restaurant);
        TextView couponText = (TextView) convertView.findViewById(R.id.coupon);
        // Populate the data into the template view using the data object
        restaurantText.setText(strsplit[0]);
        couponText.setText(coupons.get(position));


        // Return the completed view to render on screen
        return convertView;
    }

}
