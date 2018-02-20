package com.alfredthomas.spacex.util;

import android.util.Log;

import com.alfredthomas.spacex.launchinfo.Core;
import com.alfredthomas.spacex.launchinfo.Launch;
import com.alfredthomas.spacex.launchinfo.Payload;
import com.alfredthomas.spacex.launchinfo.Rocket;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by AJ on 2/9/2018.
 */

public class FlightInfoParser {

/*EXAMPLE LAUNCH JSON
    "flight_number":56,
    "launch_year":"2018",
    "launch_date_unix":1518272520,
    "launch_date_utc":"2018-02-16T14:22:00Z",
    "launch_date_local":"2018-02-16T06:22:00-08:00",
    "rocket": (see parse rocket)
    "telemetry":{
        "flight_club":"https:\/\/www.flightclub.io\/results\/?code=JSN3"
    },
    "reuse":{
    "core":true,
    "side_core1":false,
    "side_core2":false,
    "fairings":false,
    "capsule":false
    },
    "launch_site":{
        "site_id":"vafb_slc_4e",
        "site_name":"VAFB SLC 4E",
        "site_name_long":"Vandenberg Air Force Base Space Launch Complex 4E"
    },
    "launch_success":null,
    "links":{
        "mission_patch":"http:\/\/i.imgur.com\/u4Y0ifE.png",
        "reddit_campaign":null,
        "reddit_launch":"https:\/\/www.reddit.com\/r\/spacex\/comments\/417weg",
        "reddit_recovery":null,
        "reddit_media":"https:\/\/www.reddit.com\/r\/spacex\/comments\/41cvdm",
        "presskit":"http:\/\/www.spacex.com\/sites\/spacex\/files\/spacex_jason3_press_kit.pdf",
        "article_link":"https:\/\/en.wikipedia.org\/wiki\/Jason-3",
        "video_link":"https:\/\/www.youtube.com\/watch?v=ivdKRJzl6y0"
    },
    "details":"sample text"

*/

    //had to break out to account for "Latest" which returns only one launch object not in array
    //going through XML and parsing fields (with fallbacks) to proper objects
    public static Launch parseLaunchData(JSONObject launchJson)
    {

        Launch launch = new Launch();
            try {


                launch.flightNumber = launchJson.optInt("flight_number",-1);
                launch.launchYear = launchJson.optString("launch_year","TBA");
                launch.launchDateUnix = launchJson.optLong("launch_date_unix");
                launch.launchDateUTC = launchJson.optString("launch_date_utc","TBA");
                launch.launchDateLocal = launchJson.optString("launch_date_local","TBA");

                launch.rocket = parseRocket(launchJson.getJSONObject("rocket"));
                //skip for now and just populate flightclub
                //launch.telemetry =
                launch.flightClub=launchJson.getJSONObject("telemetry").optString("flight_club","N/A");

                //@todo account for unknown other reuses
                JSONObject reuseJson = launchJson.getJSONObject("reuse");
                launch.reuse.core = reuseJson.optBoolean("core");
                launch.reuse.side_core1 = reuseJson.optBoolean("side_core1");
                launch.reuse.side_core2 = reuseJson.optBoolean("side_core2");
                launch.reuse.fairings = reuseJson.optBoolean("fairings");
                launch.reuse.capsule = reuseJson.optBoolean("capsule");


                JSONObject launchSiteJson = launchJson.getJSONObject("launch_site");
                launch.site.id = launchSiteJson.optString("site_id");
                launch.site.name = launchSiteJson.optString("site_name");
                launch.site.nameLong= launchSiteJson.optString("site_name_long");

                launch.success = launchJson.optBoolean("launch_success");

                JSONObject linksJson = launchJson.getJSONObject("links");
                //setting fallback to a free rights rocket ship: https://pixabay.com/en/rocket-spaceship-launch-space-528071/ which we set locally if the download fails
                launch.links.missionPatch = linksJson.optString("mission_patch","N/A");
                launch.links.redditCampaign = linksJson.optString("reddit_campaign","N/A");
                launch.links.redditLaunch = linksJson.optString("reddit_launch","N/A");
                launch.links.redditRecovery = linksJson.optString("reddit_recovery","N/A");
                launch.links.redditMedia = linksJson.optString("reddit_media","N/A");
                launch.links.pressKit = linksJson.optString("presskit","N/A");
                launch.links.articleLink = linksJson.optString("article_link","N/A");
                launch.links.videoLink = linksJson.optString("video_link","N/A");

                launch.details = launchJson.optString("details");
            }
            catch(Exception e)
            {
                Log.e("LaunchParser",e.getMessage());
            }
        launch.generateLaunchDateTime();

        return launch;
    }

    public static List<Launch> parseLaunchData(JSONArray jsonResult)
    {
        List<Launch> launches = new ArrayList<Launch>();
        for(int i = 0; i<jsonResult.length();i++)
        {
            try {
                JSONObject launchJson = jsonResult.getJSONObject(i);
                launches.add(parseLaunchData(launchJson));
            }
            catch (Exception e)
            {
                Log.e("LaunchArrayParser",e.getMessage());
            }
        }
        return  launches;
    }


/* Example rocket JSON
{
    "rocket_id":"falcon9",
    "rocket_name":"Falcon 9",
    "rocket_type":"FT",
    "first_stage":{
        "cores":[
            {
            "core_serial":"B1038",
            "reused":true,
            "land_success":null,
            "landing_type":null,
            "landing_vehicle":null
            }
         ]
    },
    "second_stage":{
    "payloads":[
    {
        "payload_id":"Paz",
        "reused":false,
        "customers":[
            "HisdeSAT"
        ],
        "payload_type":"Satellite",
        "payload_mass_kg":1350,
        "payload_mass_lbs":2976.2,
        "orbit":"LEO"
    },
    {
        "payload_id":"Microsat-2a, -2b",
        "reused":false,
        "customers":[
            "SpaceX"
        ],
        "payload_type":"Satellite",
        "payload_mass_kg":800,
        "payload_mass_lbs":1763.7,
        "orbit":"LEO"
    }
    ]
}
     */
    //parsing rocket with applicable data from json with fallbacks
    public static Rocket parseRocket(JSONObject rocketJson)
    {
        Rocket rocket = new Rocket();
        try {
            rocket.id = rocketJson.optString("rocket_id");
            rocket.name = rocketJson.optString("rocket_name");
            rocket.type = rocketJson.optString("rocket_type");

            JSONObject first_stage = rocketJson.getJSONObject("first_stage");
            //assume for the time being there is only one element in first stage we care about: cores
                JSONArray cores = first_stage.getJSONArray("cores");
                for(int i = 0; i<cores.length();i++)
                {
                    JSONObject coreJson = cores.getJSONObject(i);
                    Core core = new Core();
                    core.serial = coreJson.optString("core_serial",Integer.toString(i));
                    core.reused = coreJson.optBoolean("reused",false);
                    core.landSuccess = coreJson.optBoolean("land_success");
                    core.landingType =coreJson.optString("landing_type","N/A");
                    core.landingVehicle =coreJson.optString("landing_vehicle","N/A");

                    rocket.firstStage.put(core.serial,core);
                }

            JSONObject second_stage = rocketJson.getJSONObject("second_stage");
            //assume for the time being there is only one element in second stage we care about: payloads
            JSONArray payloads = second_stage.getJSONArray("payloads");
            for(int i = 0; i<payloads.length();i++)
            {
                JSONObject payloadJson = payloads.getJSONObject(i);
                Payload payload = new Payload();
                payload.id = payloadJson.optString("payload_id",Integer.toString(i));
                payload.reused = payloadJson.optBoolean("reused",false);
                JSONArray customersJson = payloadJson.optJSONArray("customers");
                for(int k= 0;k<customersJson.length();k++)
                {
                    payload.customers.add(customersJson.getString(k));
                }

                payload.type = payloadJson.optString("payload_type","N/A");
                payload.massKG =payloadJson.optInt("payload_mass_kg",0);
                payload.massLBS =payloadJson.optDouble("payload_mass_lbs",0);
                payload.orbit = payloadJson.optString("orbit","N/A");
                rocket.secondStage.put(payload.id,payload);
            }
//            }


        }
        catch(Exception e)
        {
            Log.e("LaunchParser",e.getMessage());
        }


        return rocket;

    }
}
