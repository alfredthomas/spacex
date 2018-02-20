package com.alfredthomas.spacex.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.alfredthomas.spacex.R;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;

/**
 * Created by AJ on 2/15/2018.
 */

/*
    DEPRICATING TASK since picasso will improve load speed
 */
public class GetPatchTask extends AsyncTask<Void,Void,Bitmap> {

    WeakReference<ImageView> imageView;
    String urlString;
    public GetPatchTask(String urlString, ImageView imageView)
    {
        //weak reference to avoid leaking context
        this.imageView = new WeakReference<ImageView>(imageView);
        this.urlString = urlString;


    }

    @Override
    protected void onPreExecute() {
        //our local fallback in case the image can't be loaded
        ImageView iv = imageView.get();
        Drawable placeholder = iv.getContext().getResources().getDrawable(R.drawable.rocket);
        iv.setImageDrawable(placeholder);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        ImageView iv = imageView.get();

        if (iv != null) {
            if (bitmap != null) {
                iv.setImageBitmap(bitmap);
            }
        }
       // super.onPostExecute(bitmap);
    }

    @Override
    protected Bitmap doInBackground(Void... voids) {
        Bitmap bitmap = null;
        try {
            URL url = new URL(urlString);
            InputStream inputStream = url.openConnection().getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
        }
        catch(Exception e)
        {
            Log.e("GetPatchTask",e.getMessage());
        }
        return bitmap;
    }
}
