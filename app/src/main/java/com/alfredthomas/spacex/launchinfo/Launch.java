package com.alfredthomas.spacex.launchinfo;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by AJ on 2/9/2018.
 */

//rootmost object that contains references to other objects parsed by flightinfoparser
public class Launch {
    public int flightNumber=-1;
    public String launchYear;
    public Long launchDateUnix;
    public String launchDateUTC;
    public String launchDateLocal;
    public Rocket rocket;
    //skip for now
    //public HashMap<String,String> telemetry;
    public String flightClub;
    public Reuse reuse;
    public Site site;
    public boolean success;
    public Links links;
    public String details;

    public String launchDate;
    public String launchTime;
    public boolean future = false;
    public Launch() {
        //skip for now
        //telemetry = new HashMap<>();

        //create objects to more easily replace values
        reuse = new Reuse();
        site = new Site();
        links = new Links();

    }
    public void generateLaunchDateTime()
    {
        //get launch date and time
        try{
            SimpleDateFormat launchDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            Date date= launchDateFormat.parse(launchDateLocal);

            //hide success if it doesn't make sense to show (is in the future)
            if(date.after(new Date()))
                future = true;

            SimpleDateFormat justDate= new SimpleDateFormat("MMM-dd-yyyy");
            SimpleDateFormat justTime= new SimpleDateFormat("hh:mm aaa z");

            launchDate = justDate.format(date);
            launchTime = justTime.format(date);

        }
        catch(Exception e)
        {
            Log.e("LaunchDateTimeParse",e.getMessage());

            //should only error out on the fallback of "TBA" or broken times
            launchDate= launchDateLocal;
            launchTime= launchDateLocal;
            //assume it's the future since results are untrustworthy
            future = true;
        }
    }
}
