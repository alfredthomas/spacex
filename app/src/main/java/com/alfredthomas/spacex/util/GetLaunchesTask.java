package com.alfredthomas.spacex.util;


import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alfredthomas.spacex.R;
import com.alfredthomas.spacex.launchinfo.Launch;
import com.alfredthomas.spacex.views.LaunchDashboardView;
import com.alfredthomas.spacex.views.QuickSelectorView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by AJ on 2/9/2018.
 */

public class GetLaunchesTask extends AsyncTask <Void,Void,List<Launch>>{
    String baseURL = "api.spacexdata.com/v2/launches";
    String launchTypeURL[] = {"latest","","upcoming","all"};
    Uri uri;
    Uri.Builder uribuilder;
    WeakReference<ViewPager> viewPagerWeakReference;
    public enum LaunchType{
        latest,past,upcoming,all
    }

    public GetLaunchesTask(LaunchType launchType, List<NameValuePair> filters, ViewPager viewPager)
    {
        viewPagerWeakReference = new WeakReference<ViewPager>(viewPager);
        //create the base URI
        uribuilder = new Uri.Builder();
        uribuilder.scheme("https");

        uribuilder.encodedAuthority(baseURL);
        uribuilder.appendEncodedPath(launchTypeURL[launchType.ordinal()]);
        //add any query strings selected
        setFilters(filters);
    }



    public void setFilters(List<NameValuePair> filters)
    {
        uribuilder.clearQuery();
        for(NameValuePair pair:filters)
        {
            uribuilder.appendQueryParameter(pair.name,pair.value);
        }
        uri = uribuilder.build();
    }

    @Override
    protected List<Launch> doInBackground(Void[] objects) {

        //todo: handle if no internet available
        JSONArray jsonArray = null;
        JSONObject jsonObject = null;
        boolean isArray = false;
        try {
            //send request
            URL url = new URL(uri.toString());
            HttpsURLConnection urlConnection = (HttpsURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* 10 seconds */ );
            urlConnection.setConnectTimeout(15000 /* 15 seconds */ );
            urlConnection.setDoOutput(false); //getting only
            urlConnection.connect();

            //read response
            InputStreamReader inputStreamReader = new InputStreamReader(urlConnection.getInputStream());

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder responseBuilder = new StringBuilder();

            String nextLine;
            while((nextLine= bufferedReader.readLine())!= null)
            {
                responseBuilder.append(nextLine);
            }

            //done, now convert to String
            String response = responseBuilder.toString();

            //simple check/hack for array or object by checking first character
            if(response.charAt(0)=='[')
            {
                isArray = true;
                jsonArray = new JSONArray(response);
            }
            else
            {
                jsonObject = new JSONObject(response);
            }
        }
        catch (Exception e)
        {
            Log.e("LaunchesRequest",e.getMessage());
        }
        if (isArray)
        {
            return FlightInfoParser.parseLaunchData(jsonArray);
        }
        else
        {
            List<Launch> launches = new ArrayList<>();
            launches.add(FlightInfoParser.parseLaunchData(jsonObject));
            return launches;
        }

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected void onProgressUpdate(Void[] values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(final List<Launch> launchesReturned) {
        final ViewPager viewPager= viewPagerWeakReference.get();
            viewPager.setAdapter(createPagerAdapter(viewPager,launchesReturned));

        //show toast to explain to user what to do
        Toast finished = Toast.makeText(viewPager.getContext(),"Please select a shortcut below, or navigate by swiping left",Toast.LENGTH_LONG);
        finished.setGravity(Gravity.CENTER,0,0);

        //give toast black background to make it easier to see
        View toastView= finished.getView();
        toastView.setBackgroundColor(Color.BLACK);
        TextView toastTextView= (TextView)((LinearLayout)toastView).getChildAt(0);
        toastTextView.setTextColor(Color.WHITE);
        toastTextView.setGravity(Gravity.CENTER);

        finished.show();
    }

    private PagerAdapter createPagerAdapter(final ViewPager viewPager, final List<Launch> launchList)
    {
        return new PagerAdapter() {
            List<Launch> launches = launchList;

            @Override
            public int getCount() {
                return launches.size()+1;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                //insert the quickselector view manually
                if(position==0)
                {
                    QuickSelectorView quickSelectorView = new QuickSelectorView(viewPager.getContext(),viewPagerWeakReference,launches);
                    container.addView(quickSelectorView);
                    return quickSelectorView;
                }
                else {
                    //position will always be one ahead because of insertion
                    Launch launch = launches.get(position-1);
                    LaunchDashboardView launchDashboardView = new LaunchDashboardView(viewPager.getContext(), launch, viewPagerWeakReference);

                    //depricating, since picasso will load faster
//                        GetPatchTask patchTask = new GetPatchTask(launch.links.missionPatch, launchDashboardView.getPatchView());
//                        patchTask.execute();

                    Picasso.with(viewPager.getContext()).load(launch.links.missionPatch).placeholder(R.drawable.rocket).into(launchDashboardView.getPatchView());

                    container.addView(launchDashboardView);
                    return launchDashboardView;
                }
            }

            @Override
            public void destroyItem(ViewGroup collection, int position, Object view) {
                collection.removeView((View) view);
            }
        };
    }

}
