package com.alfredthomas.spacex.views;


import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.alfredthomas.spacex.R;
import com.alfredthomas.spacex.launchinfo.Launch;
import com.squareup.picasso.Picasso;

/**
 * Created by AJ on 2/19/2018.
 */

public class GridCell extends ImprovedViewGroup {
    ImageView patch;
    TextView date;
//    GetPatchTask getPatchTask;
    public GridCell(Context context)
    {
        super(context);

        patch = new ImageView(context);
        addView(patch);

        date = createTextView(context,"");
        addView(date);

        setBorder(this);
    }
    public void setData(Context context, Launch launch)
    {
        date.setText(launch.launchDate);

        //depricating patch task since picasso is faster
//        getPatchTask = new GetPatchTask(launch.links.missionPatch,patch);
//        getPatchTask.execute();

        //loading picasso image with a placeholder/fallback and resizing for performance
        Picasso.with(context).load(launch.links.missionPatch).placeholder(R.drawable.rocket).resize(400,400).into(patch);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        int padding= (int)(5f*getResources().getDisplayMetrics().density);

        //operate on a 20/80 height ratio of textview to imageview

        measureView(date,padding,padding,width-padding-padding,(int)(height*0.2)-padding);
        measureView(patch,padding,(int)(height*0.2),width-padding-padding,(int)(height*0.8)-padding);
    }
}
