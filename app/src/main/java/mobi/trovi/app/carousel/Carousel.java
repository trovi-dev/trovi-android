package mobi.trovi.app.carousel;

import java.util.ArrayList;
import java.util.List;

import mobi.trovi.app.rest.resource.User;

public class Carousel {

    private List<User> carousel;
    private int carouselIndex = 0;

    public Carousel(final List<User> users) {
        carousel = new ArrayList<User>(users);
    }


    public User getCurrentUser() {
        return carousel.get(carouselIndex);
    }

    public User circleRight() {
        if (carouselIndex == (carousel.size() - 1))
            carouselIndex = 0;
        else
            carouselIndex++;
        return getCurrentUser();
    }

    public User circleLeft() {
        if (carouselIndex == 0)
            carouselIndex = carousel.size() -1;
        else
            carouselIndex--;
        return getCurrentUser();
    }

}
