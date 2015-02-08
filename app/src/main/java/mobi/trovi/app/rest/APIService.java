package mobi.trovi.app.rest;
import mobi.trovi.app.rest.resource.Location;
import mobi.trovi.app.rest.resource.User;
import retrofit.http.GET;
import retrofit.http.PATCH;
import retrofit.http.POST;
import retrofit.http.Path;
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

    @PATCH("api/users/{phone}")
    Observable<User> updateUserField(@Path("phone") String phone);

    @GET("/api/users/list_within_radius")
    Observable<List<User>> listNearbyUser();
}