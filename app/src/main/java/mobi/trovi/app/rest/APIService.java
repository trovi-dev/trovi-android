package mobi.trovi.app.rest;
import mobi.trovi.app.rest.resource.Location;
import mobi.trovi.app.rest.resource.User;
import retrofit.http.GET;
import retrofit.http.POST;
import rx.Observable;
import java.util.List;

/**
 * Created by Greg Ziegan on 2/7/15.
 */
public interface APIService {
    @GET("api/users")
    Observable<List<User>> listUsers();

    @GET("api/locations")
    Observable<List<Location>> listLocations();

    @POST("api/users")
    Observable<User> createUser();
}