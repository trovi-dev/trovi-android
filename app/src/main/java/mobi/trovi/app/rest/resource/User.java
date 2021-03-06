package mobi.trovi.app.rest.resource;

import com.andtinder.model.CardModel;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by aidan on 2/7/15.
 */
public class User extends HyperlinkedResource {
    private String firstName;
    private URL profilePicture;
    private int age;

    public User(String url, String firstName, String profilePicture) throws MalformedURLException{
        super(url);
        this.firstName = firstName;
        this.profilePicture = new URL(profilePicture);
    }

    public URL getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) throws MalformedURLException {
        this.profilePicture = new URL(profilePicture);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getAge() {
        return age;
    }
}
