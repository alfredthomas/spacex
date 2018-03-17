package com.alfredthomas.spacex.views;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

import android.widget.TextView;

import com.alfredthomas.spacex.launchinfo.Launch;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by AJ on 2/19/2018.
 */

public class QuickSelectorView extends ImprovedViewGroup {
    WeakReference<ViewPager> viewPagerWeakReference;
    Button newQueryButton;
    TextView noDataToDisplay;
    GridView gridView;
    List<Launch> launches;
    int width;
    int height;
    int columns;

    /*
        Used as a table of contents type page. Show mission patches and allow for quick jumping.
        Also used as a catch-all if queries return no data
     */

    public QuickSelectorView(Context context, WeakReference<ViewPager> viewPager, List<Launch> launches)
    {
        super(context);
        viewPagerWeakReference = viewPager;
        this.launches = launches;

        noDataToDisplay = createTextView(context,"No Data To Display");
        noDataToDisplay.setTextColor(Color.RED);
        addView(noDataToDisplay);

        int padding= (int)(5f*getResources().getDisplayMetrics().density);

        gridView = new GridView(context);
        gridView.setAdapter(createGridAdapter());
        columns = 3;
        gridView.setNumColumns(columns);

        gridView.setVerticalSpacing(padding);
        gridView.setHorizontalSpacing(padding*2);

        addView(gridView);

        //no data
        if(launches.isEmpty())
        {
            gridView.setVisibility(GONE);
            noDataToDisplay.setVisibility(VISIBLE);
        }
        //have data to show in grid
        else
        {
            noDataToDisplay.setVisibility(GONE);
            gridView.setVisibility(VISIBLE);
        }

        newQueryButton= new Button(context);
        newQueryButton.setText("Create a new query.");
        newQueryButton.setGravity(Gravity.CENTER);
        newQueryButton.setOnClickListener( createNewQueryPopup(context,viewPagerWeakReference));

        DisplayMetrics displaymetrics = getResources().getDisplayMetrics();
        width = (((displaymetrics.widthPixels-(padding*2))-(padding*columns*2))/columns);
        height = displaymetrics.heightPixels/4;
        //height = gridView.getMeasuredHeight()/4;
        addView(newQueryButton);
    }
    private BaseAdapter createGridAdapter()
    {
        return new BaseAdapter() {
            @Override
            public int getCount() {
                //return launches.size();
                return launches.size();
            }

            @Override
            public Object getItem(int i) {
                return launches.get(i);
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                if(view == null) {
                    view = new GridCell(viewGroup.getContext());
                    view.setLayoutParams(new GridView.LayoutParams(width, height));

                }
                //let picasso handle cancelling tasks
//                if(((GridCell)view).getPatchTask!=null && ((GridCell)view).getPatchTask.getStatus()== AsyncTask.Status.RUNNING)
//                {
//                    ((GridCell)view).getPatchTask.cancel(true);
//                }
                ((GridCell)view).setData(viewGroup.getContext(),(Launch)getItem(i));

                view.setOnClickListener(createOnClickListener(i));
                return view;
            }
        };
    }

    public OnClickListener createOnClickListener(final int index)
    {
        return new OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPagerWeakReference.get().setCurrentItem(index+1);
            }
        };
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int padding= (int)(5f*getResources().getDisplayMetrics().density);



        measureView(noDataToDisplay,padding,height/4,width-padding-padding,height/4);
        measureView(gridView,padding,padding,width-padding-padding,height/5*4);

        measureView(newQueryButton,padding,height/5*4,width-padding-padding,height/4-padding-padding);
    }
}
