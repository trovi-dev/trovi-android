package mobi.trovi.app.rest.resource;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Greg Ziegan on 2/7/15.
 */
public class Location extends HyperlinkedResource {
    private float latitude;
    private float longitude;
    private String countryName;
    private String locality;
    private String postalCode;
    private URL user;

    public Location(String url, float latitude, float longitude, String countryName,
                    String locality, String postalCode, String user) throws MalformedURLException {
        super(url);
        this.latitude = latitude;
        this.longitude = longitude;
        this.countryName = countryName;
        this.locality = locality;
        this.postalCode = postalCode;
        this.user = new URL(user);
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getLocality() {
        return locality;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public URL getUser() {
        return user;
    }
}
