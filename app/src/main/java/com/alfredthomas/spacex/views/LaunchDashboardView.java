package com.alfredthomas.spacex.views;

import android.content.Context;
import android.support.v4.view.ViewPager;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alfredthomas.spacex.launchinfo.Core;
import com.alfredthomas.spacex.launchinfo.Launch;
import com.alfredthomas.spacex.launchinfo.Payload;
import com.alfredthomas.spacex.util.Utils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by AJ on 2/16/2018.
 */

public class LaunchDashboardView extends ImprovedViewGroup {

    Launch launch;
    ImageView missionPatch;
    TextView flightNumber;
    TextView launchDate;
    TextView launchTime;
    TextView rocketName;
    TextView rocketType; //not currently shown
    TextView success;
    TextView launchSite;
    Button firstStage;
    Button secondStage;
    Button reuse;
    Button launchSiteAdditional;
    Button links;
    Button details;

    WeakReference<ViewPager> viewPagerWeakReference;
    Button newQueryButton;

    public LaunchDashboardView(Context context, Launch launch, WeakReference<ViewPager> viewPagerWeakReference)
    {
        super(context);
        this.launch = launch;
        this.viewPagerWeakReference = viewPagerWeakReference;

        missionPatch = new ImageView(context);
        addView(missionPatch);

        flightNumber = createTextView(context,"#"+ Utils.removeNull(String.valueOf(launch.flightNumber)));
        addView(flightNumber);

        //set success/failure based on variable
        success = createTextView(context,launch.success?"Launch Success":"Launch Failure");
        addView(success);

        launchDate = createTextView(context,launch.launchDate);
        launchTime = createTextView(context,launch.launchTime);
        addView(launchDate);
        addView(launchTime);

        //if the launch is in the future there's no point showing launch success/failure
        if(launch.future)
            success.setVisibility(GONE);

        rocketName = createTextView(context,Utils.removeNull(launch.rocket.name) + "("+Utils.removeNull(launch.rocket.id)+")");
        addView(rocketName);
        rocketType = createTextView(context,Utils.removeNull(launch.rocket.type));
        addView(rocketType);

        launchSite = createTextView(context,Utils.removeNull(launch.site.nameLong));
        addView(launchSite);

        //popup menu of first stage data
        List<String> firstStageData = new ArrayList<>();
        for(String key:launch.rocket.firstStage.keySet())
        {
            Core core = launch.rocket.firstStage.get(key);
            if(!firstStageData.isEmpty())
                firstStageData.add(" ");

            firstStageData.addAll(core.asStringList());
        }
        firstStage = createButton(context,"First Stage Data",firstStageData);
        addView(firstStage);

        //popup menu of second stage data
        List<String> secondStageData = new ArrayList<>();
        for(String key:launch.rocket.secondStage.keySet())
        {
            Payload payload = launch.rocket.secondStage.get(key);
            if(!secondStageData.isEmpty())
                secondStageData.add(" ");

            secondStageData.addAll(payload.asStringList());
        }
        secondStage = createButton(context,"Second Stage Data",secondStageData);
        addView(secondStage);

        //popup menu of reuse booleans
        List<String> reuseData = launch.reuse.asStringList();
        reuse = createButton(context,"Rocket Reuse Data",reuseData);
        addView(reuse);

        //popup menu of additional site data
        List<String> siteData = launch.site.asStringList();
        launchSiteAdditional = createButton(context,"Launch Site Additional",siteData);
        addView(launchSiteAdditional);

        //popup menu of extra links + flightclub data
        List<String> linksData = launch.links.asStringList();
        linksData.add("Flight Club: "+Utils.removeNull(launch.flightClub));
        links = createButton(context,"Additional Links", linksData);
        addView(links);

        //popup menu showing details string (can be rather large)
        List<String> detailsData = new ArrayList<>(1);
        detailsData.add(Utils.removeNull(launch.details));
        details = createButton(context,"Details",detailsData);
        addView(details);

        //create a new query based on filter data
        newQueryButton = new Button(context);
        newQueryButton.setText("New Query");
        newQueryButton.setOnClickListener(createNewQueryPopup(context,viewPagerWeakReference));
        addView(newQueryButton);

    }

    //image view to be set based on web url of patch or fallback
    public ImageView getPatchView()
    {
        return missionPatch;
    }

    /*
            Layout in following  __________________________________
                                | ID |  Rocket Name  |  New Query  |
                                |   Launch Date   |   Launch Time  |
                                |          Launch site name..      |
                                |              (cont)              |
                                |           | Success |            |
                                |       ___________________        |
                                |      |                   |       |
                                |      |                   |       |
                                |      |       <img>       |       |
                                |      |                   |       |
                                |      |                   |       |
                                |      |___________________|       |
                                | |First stage |   | Second stage| |
                                | |   reuse    |   | Launch site | |
                                | |   links    |   |   details   | |
                                |__________________________________|

    */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);

        int padding= (int)(5f*getResources().getDisplayMetrics().density);
        int top = 0;
        int lineHeight = height/15;
        int buttonWidth = (width/2)-padding;

        measureView(missionPatch,width/4,height/4,width/2,height/2);

        measureView(flightNumber,padding,top,(width/8)-padding,(int)(lineHeight*1.5));
        measureView(rocketName,(width/8),top,(width/8)*4,(int)(lineHeight*1.5));
        measureView(newQueryButton,(width/8)*5,top,(buttonWidth/4)*3,(int)(lineHeight*1.5));
        top+=(int)(lineHeight*1.5);

        measureView(launchDate,padding,top,(width/2)-padding,(int)(lineHeight*0.75));
        measureView(launchTime,width/2,top,(width/2),(int)(lineHeight*0.75));
        top+=(lineHeight*0.75);

        launchSite.measure(MeasureSpec.makeMeasureSpec(width,MeasureSpec.AT_MOST),MeasureSpec.makeMeasureSpec(0,MeasureSpec.UNSPECIFIED));

        measureView(launchSite,padding,top,launchSite.getMeasuredWidth(),launchSite.getMeasuredHeight());
        top+= launchSite.getMeasuredHeight();

        measureView(success,width/4,top,(width/2),lineHeight);


        top = (height/4)*3;
        measureView(firstStage,padding,top,buttonWidth,lineHeight);
        measureView(secondStage,buttonWidth+(padding*2),top,buttonWidth,lineHeight);
        top+=lineHeight+padding;
        measureView(reuse,padding,top,buttonWidth,lineHeight);
        measureView(launchSiteAdditional,buttonWidth+(padding*2),top,buttonWidth,lineHeight);
        top+=lineHeight+padding;
        measureView(links,padding,top,buttonWidth,lineHeight);
        measureView(details,buttonWidth+(padding*2),top,buttonWidth,lineHeight);
    }

}
