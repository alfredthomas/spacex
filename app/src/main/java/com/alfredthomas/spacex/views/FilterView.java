package com.alfredthomas.spacex.views;


import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.alfredthomas.spacex.util.GetLaunchesTask;
import com.alfredthomas.spacex.util.NameValuePair;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by AJ on 2/15/2018.
 */

public class FilterView extends ImprovedViewGroup {
    TextView description;
    TextView startDateTV;
    EditText startDateET;
    TextView endDateTV;
    EditText endDateET;
    TextView launchYearTV;
    Spinner launchYearSpinner;
    TextView launchSuccessText;
    Spinner launchSuccessSpinner;
    TextView landSuccessText;
    Spinner landSuccessSpinner;
    RadioButton latest;
    RadioButton past;
    RadioButton upcoming;
    RadioButton all;
    RadioGroup launchTypeGroup;
    List<NameValuePair> filters = new ArrayList<>();

    /*
    handle filters for new queries. Change query url to point to latest/recent/upcoming/all.
    Support for following filters:
        start & final
        launch_year
        launch_success
        land_success
        @todo add more filters
     */
    public FilterView(Context context) {
        super(context);

        description = createTextView(context,"Please enter any filters. Any text entered will be added to query string");
        addView(description);

        startDateTV = createTextView(context,"start");
        startDateET = createEditText(context,"YYYY-MM-dd");
        addView(startDateTV);
        addView(startDateET);


        endDateTV = createTextView(context,"final");
        endDateET = createEditText(context,"YYYY-MM-dd");
        addView(endDateTV);
        addView(endDateET);


        launchYearTV = createTextView(context,"launch_year");
        launchYearSpinner = createYearSpinner(context);
        addView(launchYearTV);
        addView(launchYearSpinner);

        launchTypeGroup = new RadioGroup(context);
        launchTypeGroup.setOrientation(LinearLayout.HORIZONTAL);
        launchTypeGroup.setGravity(Gravity.CENTER);

        latest = createRadioButton(context,"Latest");
        past= createRadioButton(context,"Past");
        upcoming= createRadioButton(context,"Upcoming");
        all= createRadioButton(context,"All");

        launchTypeGroup.addView(latest,0);
        launchTypeGroup.addView(past,1);
        launchTypeGroup.addView(upcoming,2);
        launchTypeGroup.addView(all,3);

        launchTypeGroup.check(all.getId());
        addView(launchTypeGroup);

        launchSuccessText = createTextView(context,"launch_success");
        launchSuccessSpinner = createSpinner(context,"","true","false");
        addView(launchSuccessText);
        addView(launchSuccessSpinner);

        landSuccessText = createTextView(context,"land_success");
        landSuccessSpinner = createSpinner(context,"","true","false");
        addView(landSuccessText);
        addView(landSuccessSpinner);
    }
    public EditText createEditText(Context context, String format)
    {
        EditText editText = new EditText(context);
        editText.setOnClickListener(datePickerOnClickListener(format));
        editText.setFocusable(false);
        editText.setCompoundDrawables(null,null, getResources().getDrawable(android.R.drawable.ic_menu_my_calendar),null);
        return editText;
    }
    public GetLaunchesTask.LaunchType getLaunchType()
    {
        return GetLaunchesTask.LaunchType.values()[launchTypeGroup.indexOfChild(findViewById(launchTypeGroup.getCheckedRadioButtonId()))];
    }
    public List<NameValuePair> getFiltersSelected(){
        filters.clear();

        //only true if both start and end have values
        if(startDateET.getText().length()>0 && endDateET.getText().length()>0)
        {
            filters.add(new NameValuePair(startDateTV.getText().toString(),startDateET.getText().toString()));
            filters.add(new NameValuePair(endDateTV.getText().toString(),endDateET.getText().toString()));
        }

        //if launch year has a value
        if(!launchYearSpinner.getSelectedItem().toString().isEmpty() )
        {
            filters.add(new NameValuePair(launchYearTV.getText().toString(),launchYearSpinner.getSelectedItem().toString()));
        }

        //if launch success has a value
        if(!launchSuccessSpinner.getSelectedItem().toString().isEmpty())
        {
            filters.add(new NameValuePair(launchSuccessText.getText().toString(),launchSuccessSpinner.getSelectedItem().toString()));
        }

        //if land success has a value
        if(!landSuccessSpinner.getSelectedItem().toString().isEmpty())
        {
            filters.add(new NameValuePair(landSuccessText.getText().toString(),landSuccessSpinner.getSelectedItem().toString()));
        }
        return filters;
    }

    //create calendar picker to show on touch of the edit text
    private OnClickListener datePickerOnClickListener(final String format)
    {
        return new OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar myCalendar = Calendar.getInstance();
                new DatePickerDialog(getContext(), dateSetListener((EditText)v,format,myCalendar), myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        };
    }

    //wait for a date to be set on the calendar picker and pass it to the formatter
    private DatePickerDialog.OnDateSetListener dateSetListener(final EditText editText, final String format, final Calendar myCalendar)
    {
        return new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(editText,format,myCalendar);

            }

        };
    }
    //get formatted data from the calendar object
    private void updateLabel( EditText editText, String format, Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        editText.setText(sdf.format(calendar.getTime()));
    }



    /*
    Layout in this style:   ____________________________
                           |filtername|    filterval    |
                            ____________________________
                           |filtername|    filterval    |

                           etc


     */

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        int padding= (int)(5f*getResources().getDisplayMetrics().density);

        int tvWidth = width/5*2;
        int otherWidth = width-tvWidth;
        int lineHeight = height/10;
        int y = padding*2;
        measureView(launchTypeGroup,padding,y,width,lineHeight);

        y+=lineHeight;

        measureView(description,0,y,width,lineHeight);
        y+=lineHeight;

        measureView(startDateTV,0,y,tvWidth,lineHeight);
        measureView(startDateET,tvWidth,y,otherWidth,lineHeight);
        y+=lineHeight;
        measureView(endDateTV,0,y,tvWidth,lineHeight);
        measureView(endDateET,tvWidth,y,otherWidth,lineHeight);
        y+=lineHeight;
        measureView(launchYearTV,0,y,tvWidth,lineHeight);
//        measureView(launchYearET,tvWidth,y,otherWidth,lineHeight);
        measureView(launchYearSpinner,tvWidth,y,otherWidth,lineHeight);
        y+=lineHeight;
        measureView(launchSuccessText,0,y,tvWidth,lineHeight);
        measureView(launchSuccessSpinner,tvWidth,y,otherWidth,lineHeight);
        y+=lineHeight;
        measureView(landSuccessText,0,y,tvWidth,lineHeight);
        measureView(landSuccessSpinner,tvWidth,y,otherWidth,lineHeight);
        y+=lineHeight;



    }
}
