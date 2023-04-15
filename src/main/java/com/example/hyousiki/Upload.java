package com.example.hyousiki;

import android.location.Location;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.DialogFragment;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.Exclude;
import com.google.firebase.storage.UploadTask;

public class Upload {


    private String mName;
    private String mImageUrl;
    private String mKey;
    private double mLatitude;
    private double mLongitude;

    public Upload() {
        //empty constructor needed
    }

    public Upload(String name, String imageUrl, LatLng location) {
        if (name.trim().equals("")) {
            name = "No Name";
        }

        mName = name;
        mImageUrl = imageUrl;
        mLatitude = location.latitude;
        mLongitude = location.longitude;
    }

    public String getName() {
        return mName;
    }
    public void setName(String name) {
        mName = name;
    }

    public String getImageUrl() {
        return mImageUrl;
    }
    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public double getLatitude() {return mLatitude;}
    public void setLatitude(double v) {mLatitude = v;}
    public double getLongitude() {return mLongitude;}
    public void setLongitude(double v) {mLongitude = v;}


    @Exclude
    public String getKey() {
        return mKey;
    }

    @Exclude
    public void setKey(String key) {
        mKey = key;
    }
}