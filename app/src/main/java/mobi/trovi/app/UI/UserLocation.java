package mobi.trovi.app.UI;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import java.io.IOException;

/**
 * retrieve and convert the location of the user
 * Created by aidan on 2/7/15.
 */
public class UserLocation {
    /**
 private float latitude;
 private float longitude;
 private String countryName;
 private String locality;
 private String postalCode;
 private URL user;
 */

    private double latitude;
    private double longitude;
    private Geocoder geocoder = null;
    private Address address;
    private String locality;
    private String postalCode;

    public void updateLocation(Context context){
        updateLatLon(context);
        this.geocoder = new Geocoder(context);
        try {
            this.address = geocoder.getFromLocation(this.latitude, this.longitude, 1).get(0);
            this.locality = address.getLocality();
            this.postalCode = address.getPostalCode();
        } catch(IOException e){
            e.printStackTrace();
            Log.e("LOCATION","ERROR WHILE GETTING ADDRESS FROM GEOCODER");
        }
    }

    private void updateLatLon(Context context){
        LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        this.longitude = location.getLongitude();
        this.latitude = location.getLatitude();
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Geocoder getGeocoder() {
        return geocoder;
    }

    public Address getAddress() {
        return address;
    }

    public String getLocality() {
        return locality;
    }

    public String getPostalCode() {
        return postalCode;
    }
}
