package mobi.trovi.app.UI;

import com.andtinder.model.CardModel;

import mobi.trovi.app.rest.resource.User;

/**
 * creates an AndTinder card from a User object
 * Created by aidan on 2/7/15.
 */
public class UserCard {

    private CardModel card;//the card representation of the user

    /**
     * Constructor to create the card
     * @param user User to create card of
     */
    public UserCard(User user){

        //TODO: get Drawable from User's profilePicture
        //card = new CardModel(user.getFirstName(), ((Integer)user.getAge()).toString(),);
    }

    /**
     * @return the card value of the user
     */
    public CardModel getcard(){
        return this.card;
    }

}
