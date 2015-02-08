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
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.GridLayout;

import com.andtinder.model.CardModel;
import com.andtinder.model.Orientations;
import com.andtinder.view.CardContainer;
import com.andtinder.view.SimpleCardStackAdapter;
import mobi.trovi.app.rest.resource.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity {

    private List<User> carousel = new ArrayList<>();
    private int carouselIndex = 0;

    private User circleRight() {
        if (carouselIndex == (carousel.size() - 1))
            carouselIndex = 0;
        else
            carouselIndex++;
        return carousel.get(carouselIndex);
    }

    private User circleLeft() {
        if (carouselIndex == 0)
            carouselIndex = carousel.size() -1;
        else
            carouselIndex--;
        return carousel.get(carouselIndex);
    }

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

        //async callbacks are hella ugly.
        promptForResult(new PromptRunnable(){
            // put whatever code you want to run after user enters a result
            public void run() {
                // get the value we stored from the dialog
                String value = this.getValue();
                // do something with this value...
                SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("lastName", value);
                editor.apply();
            }
        }, "Last Name");

        promptForResult(new PromptRunnable(){
            // put whatever code you want to run after user enters a result
            public void run() {
                // get the value we stored from the dialog
                String value = this.getValue();
                // do something with this value...
                SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("firstName", value);
                editor.apply();
            }
        }, "First Name");

        getFirstPicture();
        editor.putString("profilePictureFilename", "profile_picture");

        editor.apply();//apply is better than commit. apply backgrounds the write.
    }

    void promptForResult(final PromptRunnable postrun, String message) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(message);
        alert.setMessage(message);
        // Create textbox to put into the dialog
        final EditText input = new EditText(this);
        // put the textbox into the dialog
        alert.setView(input);
        // procedure for when the ok button is clicked.
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                dialog.dismiss();
                // set value from the dialog inside our runnable implementation
                postrun.setValue(value);
                // ** HERE IS WHERE THE MAGIC HAPPENS! **
                // now that we have stored the value, lets run our Runnable
                postrun.run();
                return;
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                return;
            }
        });
        alert.show();
    }

    class PromptRunnable implements Runnable {
        private String v;
        void setValue(String inV) {
            this.v = inV;
        }
        String getValue() {
            return this.v;
        }
        public void run() {
            this.run();
        }
    }

    /**
     * queries the user to pick a profile picture
     * @return the chosen picture as a drawable
     */
    private void getFirstPicture(){
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            Bitmap bump = BitmapFactory.decodeFile(picturePath);
            try {
                // pictures and other media owned by the application, consider
                // Context.getExternalMediaDir().
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                File file = new File(path, "profilePicture.png");
                FileOutputStream outputStream = new FileOutputStream(file);
                bump.compress(Bitmap.CompressFormat.PNG, 9, outputStream);
                outputStream.close();
            } catch (IOException e){
                Log.e("ERROR","ERROR WHILE SAVING INITIAL IMAGE TO PICTURES/PROFILEPICTURE.PNG");
                e.printStackTrace();
            }

        }//end of selecting bottom image
    }


    /**
     * checks if it's the first run,
     * returns false by default (preference isn't found)
     */
    private boolean isFirstRun(){
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getBoolean("isFirstRun", false);
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