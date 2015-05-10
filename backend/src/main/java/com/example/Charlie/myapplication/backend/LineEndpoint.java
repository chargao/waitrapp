package com.example.Charlie.myapplication.backend;

import java.util.ArrayList;
import java.util.Hashtable;

import static com.example.Charlie.myapplication.backend.OfyService.ofy;

/**
 * Created by nacorti on 5/5/2015.
 */

public class LineEndpoint {
    private Hashtable<String, RestaurantRecord> theList = new Hashtable<String, RestaurantRecord>();
    public final RestaurantRecord thaiHowAreYou = new RestaurantRecord("Thai How Are You", 30.283959, -97.742249);
    public final RestaurantRecord tejis = new RestaurantRecord("Teji's", 30.284014, -97.742226);
    public final RestaurantRecord medici = new RestaurantRecord("Cafe Medici", 30.285460, -97.742168);
    public final RestaurantRecord chipotle = new RestaurantRecord("Chipotle", 30.285665, -97.741991);
    public final RestaurantRecord jambaJuice = new RestaurantRecord("Jamba Juice", 30.286595, -97.741991);
    public final RestaurantRecord potbelly = new RestaurantRecord("Potbelly Sandwich Shop", 30.286844, -97.741953);
    public final RestaurantRecord austinPizza = new RestaurantRecord("Austin's Pizza", 30.287028, -97.741915);
    public final RestaurantRecord whichWich = new RestaurantRecord("Which Wich", 30.287321, -97.741805);
    public final RestaurantRecord pitaPit = new RestaurantRecord("Pita Pit", 30.287440, -97.741920);
    public final RestaurantRecord noodlesandCo = new RestaurantRecord("Noodles & Company", 30.287867, -97.741939);
    public final RestaurantRecord einsteinBros = new RestaurantRecord("Einstein Bros Bagels", 30.288016, -97.741770);
    public final RestaurantRecord fuzzys = new RestaurantRecord("Fuzzy Taco Shop", 30.289844, -97.741597);
    public final RestaurantRecord verts = new RestaurantRecord("VertsKebap", 30.289873, -97.741602);

    public LineEndpoint() {
        theList = constructTheList();
    }

    public Hashtable<String, RestaurantRecord> constructTheList() {
        Hashtable<String, RestaurantRecord> protoList = new Hashtable<String, RestaurantRecord>();
        protoList.put(thaiHowAreYou.getName(), thaiHowAreYou);
        protoList.put(tejis.getName(), tejis);
        protoList.put(medici.getName(), medici);
        protoList.put(chipotle.getName(), chipotle);
        protoList.put(jambaJuice.getName(), jambaJuice);
        protoList.put(potbelly.getName(), potbelly);
        protoList.put(austinPizza.getName(), austinPizza);
        protoList.put(whichWich.getName(), whichWich);
        protoList.put(pitaPit.getName(), pitaPit);
        protoList.put(noodlesandCo.getName(), noodlesandCo);
        protoList.put(einsteinBros.getName(), einsteinBros);
        protoList.put(fuzzys.getName(), fuzzys);
        protoList.put(verts.getName(),verts);
        ofy().save().entity(thaiHowAreYou).now();
        ofy().save().entity(tejis).now();
        ofy().save().entity(medici).now();
        ofy().save().entity(chipotle).now();
        ofy().save().entity(jambaJuice).now();
        ofy().save().entity(potbelly).now();
        ofy().save().entity(austinPizza).now();
        ofy().save().entity(whichWich).now();
        ofy().save().entity(pitaPit).now();
        ofy().save().entity(noodlesandCo).now();
        ofy().save().entity(einsteinBros).now();
        ofy().save().entity(fuzzys).now();
        ofy().save().entity(verts).now();
        return protoList;
    }

    public void processCheckIn(String restaurantName) {
        if(theList.contains(restaurantName)) {
            theList.get(restaurantName).incrementLength();
        }
    }

    public void processCheckOut(String restaurantName) {
        if(theList.contains(restaurantName)) {
            if(theList.get(restaurantName).getLineLength() > 0) {
                theList.get(restaurantName).decrementLength();
            }
        }
    }

    public Hashtable<String, RestaurantRecord> getTheList() {
        return theList;
    }
}
