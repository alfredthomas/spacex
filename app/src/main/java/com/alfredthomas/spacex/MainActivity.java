package com.alfredthomas.spacex;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;


import com.alfredthomas.spacex.util.GetLaunchesTask;
import com.alfredthomas.spacex.util.NameValuePair;

import java.util.ArrayList;

/**
 * Created by AJ on 2/15/2018.
 */

public class MainActivity extends AppCompatActivity {
    ViewPager viewPager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //todo: create a more informative first screen or help page

        //todo: unlock app from portrait (redesign launch dashboard for landscape)
        viewPager= new ViewPager(this);
        GetLaunchesTask getLaunchesTask = new GetLaunchesTask(GetLaunchesTask.LaunchType.all,new ArrayList<NameValuePair>(), viewPager);
        getLaunchesTask.execute();
        setContentView(viewPager);
    }

    @Override
    public void onBackPressed() {

        //catch back pressed to handle navigation back to the quick selector
        if(viewPager!=null && viewPager.getCurrentItem()!=0)
        {
            viewPager.setCurrentItem(0);
        }
        else{

            new AlertDialog.Builder(this)
                    .setTitle("Exiting the App!")
                    .setMessage("Are you sure?")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            finish();
                            dialog.dismiss();
                        }})
                    .setNegativeButton("No",null)
                    .show();
        }

    }
}
