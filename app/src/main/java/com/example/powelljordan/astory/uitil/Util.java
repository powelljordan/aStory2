package com.example.powelljordan.astory.uitil;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

import java.io.InputStream;

/**
 * Created by jorda on 12/23/2017.
 */

public class Util {
    public static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    public static void loadFragment(Fragment fragment,
                              int container,
                              View containerView,
                              Intent intent,
                              FragmentManager fragmentManager,
                              Bundle savedInstanceState) {
        if (containerView != null) {
            //TODO Use Data Binding here
            if (savedInstanceState != null) {
                return;
            }
            fragment.setArguments(intent.getExtras());
            fragmentManager
                    .beginTransaction()
                    .add(container, fragment)
                    .commit();
        }
    }

    public static LatLng geoPointToLatLng(GeoPoint geoPoint){
        return new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude());
    }
}
