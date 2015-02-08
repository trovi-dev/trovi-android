package mobi.trovi.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.andtinder.model.CardModel;
import com.andtinder.model.Orientations;
import com.andtinder.view.CardContainer;
import com.andtinder.view.SimpleCardStackAdapter;
import mobi.trovi.app.rest.resource.User;

import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        boolean isUserInitialized = isFirstRun();

        //User user = new User();

        if(!isUserInitialized){
            //user isn't initialized. Make a profile
            initializeUser();
        }
        //user is initialized. Load their details into User object
        //TODO: load their details into the User object

        initCards();
    }

    /**
     * gets phone number, first/last name, and picture.
     * Persists them into the sharedPreferences
     */
    private void initializeUser(){
        TelephonyManager tMgr = (TelephonyManager)getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneNumber = tMgr.getLine1Number();
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("phoneNumber", mPhoneNumber);

        String firstName, lastName;
        firstName = getResponseFromQuery("First Name");
        editor.putString("firstName", firstName);
        lastName = getResponseFromQuery("Last Name");
        editor.putString("lastName", lastName);

        getFirstPicture();
        editor.putString("profilePictureFilename", "profile_picture");

        editor.apply();//apply is better than commit. apply backgrounds the write.
    }

    /**
     * TODO: the return value isn't working because of inheritance or something.
     * Queries the user with 'query' in a popup and returns the response
     * @param query the query to prompt the user with
     * @return the String response of the user
     */
    private String getResponseFromQuery(final String query){
        String returnValue;

        final EditText et = new EditText(this);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle(query);
        alert.setMessage(query);
        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                //TODO: Do something with value!
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
        return null;
    }

    /**
     * queries the user to pick a profile picture
     * @return the chosen picture as a drawable
     */
    private Drawable getFirstPicture(){
        Drawable picture;
        Intent data = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        Bitmap bump = BitmapFactory.decodeFile(picturePath);
        picture =  new BitmapDrawable(getResources(), bump);
        return picture;

    }

    /**
     * saves the given drawable to on-device storage
     * It's stored to "profile_picture" as a PNG
     * @param picture the Drawable picture to store as profile picture
     * @return whether the persistence was successful.
     */
    private boolean persistPicture(Drawable picture){
        Bitmap bmpPicture = ((BitmapDrawable)picture).getBitmap();
        FileOutputStream fos;
        try {
            String FILENAME = "profile_picture";
            fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            bmpPicture.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * checks if it's the first run,
     * returns false by default (preference isn't found)
     */
    private boolean isFirstRun(){
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getBoolean("isFirstRun", false);
    }

    /**
     * sets up the andTinder cards with the given varargs of cardModels stuff.
     */
    private void initCards(CardModel... cardModels){
        CardContainer mCardContainer = (CardContainer) findViewById(R.id.layoutview);
        mCardContainer.setOrientation(Orientations.Orientation.Disordered);
        SimpleCardStackAdapter adapter = new SimpleCardStackAdapter(this);
        for(CardModel c: cardModels) adapter.add(c);
        mCardContainer.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_screen, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}