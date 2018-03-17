package com.alfredthomas.spacex.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.view.ViewPager;
import android.text.util.Linkify;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.alfredthomas.spacex.R;
import com.alfredthomas.spacex.util.GetLaunchesTask;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class ImprovedViewGroup extends ViewGroup
{
    public ImprovedViewGroup(Context context)
    {
        super(context);
    }

    public ImprovedViewGroup(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public ImprovedViewGroup(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        this.layoutChildren();
    }

    //layout all views sequentially, since we properly measure them in onMeasure
    public void layoutChildren()
    {
        int count = this.getChildCount();

        for (int i = 0; i < count; i++)
        {
            View child = this.getChildAt(i);

            layoutView(child);
        }
    }

    public void layoutView(View view)
    {
        LayoutParams params = (LayoutParams) view.getLayoutParams();
        view.layout(params.left, params.top, params.right, params.bottom);
    }

    public class LayoutParams extends ViewGroup.LayoutParams
    {
        public int left, right, top, bottom;

        public LayoutParams(int width, int height)
        {
            super(width, height);
        }

        public LayoutParams()
        {
            super(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    private void updateLayoutParams(View child, int left, int top, int width, int height)
    {
        ImprovedViewGroup.LayoutParams params = (ImprovedViewGroup.LayoutParams) child.getLayoutParams();

        params.height = height;
        params.width = width;
        params.bottom = top + height;
        params.right = left + width;
        params.left = left;
        params.top = top;
    }

    public void measureView(View child)
    {
        ImprovedViewGroup.LayoutParams params = (ImprovedViewGroup.LayoutParams) child.getLayoutParams();

        child.measure(MeasureSpec.makeMeasureSpec(params.width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(params.height, MeasureSpec.EXACTLY));
    }

    public void measureView(View child, int left, int top, int width, int height)
    {
        this.updateLayoutParams(child, left, top, width, height);
        this.measureView(child);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p)
    {
        return p instanceof LayoutParams;
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams()
    {
        return new LayoutParams();
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p)
    {
        return new LayoutParams(p.width, p.height);
    }

    //get String from id
    public String getString(int id)
    {
        return this.getContext().getString(id);
    }
    //create the filterview that will ultimately update the viewpager
    public OnClickListener createNewQueryPopup(final Context context, final WeakReference<ViewPager> viewPagerWeakReference)
    {
        return new OnClickListener() {
            @Override
            public void onClick(View view) {
                final FilterView filterView = new FilterView(context);
                AlertDialog.Builder newQueryPopup = new AlertDialog.Builder(context);
                newQueryPopup.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        GetLaunchesTask getLaunchesTask = new GetLaunchesTask(filterView.getLaunchType(),filterView.getFiltersSelected(),viewPagerWeakReference.get());
                        getLaunchesTask.execute();
                    }
                });
                newQueryPopup.setNegativeButton("Cancel",null);
                newQueryPopup.setView(filterView);
                newQueryPopup.show();

            }
        };
    }
    //create button that will show a simple list of strings when clicked
    public Button createButton(Context context, String text, List<String> data)
    {
        Button button = new Button(context);
        button.setText(text);
        button.setOnClickListener(createPopupMenu(context,data));
        return button;

    }

    //set textview text color depending on passed in boolean value
    public void setConditionalFormat(TextView textView, boolean value)
    {
        if(value)
            textView.setTextColor(Color.GREEN);
        else
            textView.setTextColor(Color.RED);
    }

    //create simple text view with set gravity and font size. Also set text and cond formatting
    public TextView createTextView(Context context, String value)
    {
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
        textView.setText(value);

        checkForConditionalFormatting(textView,value);
        //applies hyperlinks to text if applicable
        Linkify.addLinks(textView,Linkify.WEB_URLS);
        return textView;
    }

    //create simple spinner based on an unlimited amount of string arguments
    public Spinner createSpinner(Context context, String... values)
    {
        List<String> spinnerArray = Arrays.asList(values);
        return createSpinner(context,spinnerArray);

    }

    //create spinner based on list of strings as the adapter
    public Spinner createSpinner(Context context, List<String> spinnerArray)
    {
        Spinner spinner = new Spinner(context,Spinner.MODE_DROPDOWN);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(context, R.layout.spinner_dropdown_item,spinnerArray);

        spinner.setAdapter(spinnerAdapter);
        return spinner;
    }

    //create a list of years from 2006-now+1 and then create spinner from it
    public Spinner createYearSpinner(Context context)
    {
        //since years for Space-X start in 2006, we can start there and go to current year +1
        List<String> years= new ArrayList<>();
        //workaround for empty spinner at start
        years.add("-");
        for(int i = 2006; i<= Calendar.getInstance().get(Calendar.YEAR)+1;i++) {
            years.add(Integer.toString(i));
        }
        return createSpinner(context, years);
    }

    //create simple radiobutton with text
    public RadioButton createRadioButton(Context context, String text) {
        RadioButton radioButton = new RadioButton(context);
        radioButton.setText(text);

        return radioButton;
    }

    //change text colors based on certain words
    public void checkForConditionalFormatting(TextView textView, String value)
    {
        //@todo come up with a more clever way to find these
        //since all the booleans are displayed as ": true/false" we can check the last few characters
        //if we want to use yes/no instead of true/false, the process is similar. Convert to boolean and format
        if(value.endsWith("true")||value.endsWith("false")||value.endsWith(" no")||value.endsWith("yes")||
                value.endsWith("Success")||value.endsWith("Failure")) {
            setConditionalFormat(textView, getBoolean(textView.getText().toString()));
        }
        else {
            //fixing a bug where text color could be shown wrong if reused
            textView.setTextColor(Color.BLACK);
        }
    }

    //create a boolean value based on some substrings
    public boolean getBoolean(String endsWithBoolean)
    {
        //@todo come up with a more clever way of handling this
        //for "yes"," true","Success"
        if(endsWithBoolean.endsWith(" yes")||endsWithBoolean.endsWith(" true")||endsWithBoolean.endsWith("Success"))
            return true;

        //for " no","false","Failure"
        return false;

    }

    //set border on view
    public void setBorder(View view)
    {
        GradientDrawable border = new GradientDrawable();

        border.setColor(Color.TRANSPARENT);
        border.setStroke(1, Color.BLACK);
        border.setCornerRadius(50);

        view.setBackground(border);
    }

    //create a dialog for showing simple list of strings
    public OnClickListener createPopupMenu(final Context context, final List<String> data)
    {
        return new OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder popup = new AlertDialog.Builder(context);
                popup.setPositiveButton("OK", null);


                popup.setTitle(((Button)view).getText());

                ListView listView = new ListView(context);
                listView.setAdapter(createBaseAdapter(data));
                popup.setView(listView);
                popup.show();

            }
        };
    }

    //create simple base adapter for a list of strings to be shown as text views
    private BaseAdapter createBaseAdapter(final List<String> data)
    {
        return new BaseAdapter() {
            @Override
            public int getCount() {
                return data.size();
            }

            @Override
            public Object getItem(int i) {
                return data.get(i);
            }

            @Override
            public long getItemId(int i) {
                return 0;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                //use textview creator and set the value
                if(view == null) {
                    view = createTextView(viewGroup.getContext(), (String) getItem(i));
                    ((TextView) view).setGravity(Gravity.LEFT);
                    ((TextView) view).setPadding(15,5,5,5);
                }
                //set text on reuse
                else {
                    ((TextView) view).setText((String) getItem(i));
                    //fixes a bug where reused textview may have wrong text color
                    checkForConditionalFormatting((TextView)view,(String)getItem(i));
                }
                return view;
            }
        };
    }
    
}