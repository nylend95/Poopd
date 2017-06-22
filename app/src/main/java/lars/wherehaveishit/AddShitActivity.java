package lars.wherehaveishit;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.LatLng;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddShitActivity extends AppCompatActivity
{

    // Used to determine if the data is saved properly
    protected boolean dataSaved = false;


    @Override
    protected void onCreate( Bundle savedInstanceState )
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shit);


    }

    protected void onResume( )
    {

        super.onResume();
        // Automaticly sets the variable to false
        dataSaved = false;

    }


    public void doneShitting( View view )
    {

        savingData();

        // Goes back to maps view if the data is saved properly, and dataSaved == true.
        if (dataSaved == true)
        {
            Intent Maps = new Intent(this, MainActivity.class);
            Maps.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(Maps);
            return;
        }

        // Displays a text saying that data was not saved properly, if dataSaved != true.
        Toast.makeText(this, "The data was not saved properly", Toast.LENGTH_SHORT).show();
    }

    public void savingData( )
    {
        // Saves the current location to a variable
        // noinspection deprecation
        Location currentLocation = MainActivity.mMap.getMyLocation();

        // Saves the Latitude and Longitude to a string variable
        String currentLocationLatFin = String.valueOf(currentLocation.getLatitude());
        String currentLocationLonFin = String.valueOf(currentLocation.getLongitude());


        // Getting info from etxt_shitName and saves it to a String
        EditText name = (EditText) findViewById(R.id.etxt_ShitName);
        if (name.getText().length() == 0)
        {
            return;
        }
        // Saves it to a string
        String shitName = name.getText().toString();


        // Getting info from ratingBar and saves it to a float
        RatingBar rating = (RatingBar) findViewById(R.id.ratingBar);
        String shitRating = String.valueOf(rating.getRating());

        // Getting todays date, month, year, hour and minute, Saves it to int values
        Calendar savingDate = Calendar.getInstance();
        int shitDate = savingDate.get(Calendar.DATE);
        int shitMonth = savingDate.get(Calendar.MONTH);
        int shitYear = savingDate.get(Calendar.YEAR);
        int shitHour = savingDate.get(Calendar.HOUR_OF_DAY);
        int shitMinute = savingDate.get(Calendar.MINUTE);

        // A alternative way of saving all the variables to a string using delimiters
        //String savingShitString = currentLocationSubstring + "|" + shitName + "|" + shitRating + "|" + shitDate + "/" + shitMonth + "/" + shitYear + "|" + shitHour + ":" + shitMinute;

        // Merging date, month and year
        String shitDateMonthYear = shitDate + "/" + shitMonth + "/" + shitYear;

        // Merging hour and minute
        String shitHourMinute = shitHour + ":" + shitMinute;

        // Makes a string array of current location, name of place, rating, date and time
        //String[] savingShitString = {currentLocationLatFin, currentLocationLonFin, shitName, shitRating, shitDateMonthYear, shitHourMinute, ";"};
        String savingShitString = currentLocationLatFin + (char) 182 + currentLocationLonFin + (char) 182 + shitName + (char) 182 + String.valueOf(shitRating) + (char) 182 + shitDateMonthYear + (char) 182 + shitHourMinute + ";";

        Log.i("lat", currentLocationLatFin);
        Log.i("Lon", currentLocationLonFin);
        Log.i("Name", shitName);
        Log.i("Rating", shitRating);
        Log.i("Date", shitDateMonthYear);
        Log.i("Time", shitHourMinute);
        Log.i("Saved data:", savingShitString.toString());


        try
        {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getApplicationContext().openFileOutput("savedShits",Context.MODE_PRIVATE));
            outputStreamWriter.write(savingShitString);
            outputStreamWriter.close();
            dataSaved = true;
            Toast.makeText(getApplicationContext(), "File saved!", Toast.LENGTH_SHORT).show();
        }
        catch (IOException e)
        {
            Log.e("Exception","File write failed: " + e.toString());
            e.printStackTrace();
        }
    }



    // Prompt the users and ask if the don't want to save their shit
    @Override
    public void onBackPressed( )
    {

        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage("Are you sure you dont want to save your shit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {

                    @Override
                    public void onClick( DialogInterface dialog, int which )
                    {

                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }


}
