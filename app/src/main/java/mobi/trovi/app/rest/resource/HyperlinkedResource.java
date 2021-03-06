package mobi.trovi.app.rest.resource;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Greg Ziegan on 2/7/15.
 */
public abstract class HyperlinkedResource {
    private URL url;

    public HyperlinkedResource(String url) throws MalformedURLException {
        this.url = new URL(url);
    }

    public URL getUrl() {
        return url;
    }
}
