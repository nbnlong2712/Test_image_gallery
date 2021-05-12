package com.longtraidep.imagegallery.Object;

import java.util.Date;
import java.util.UUID;

public class Image {
    private String mId;
    private String mPath;
    private Date mDateAdded;
    private float latitude;
    private float longitude;
    private String mLocation;
    private long mSize;

    public long getSize() {
        return mSize;
    }

    public void setSize(long size) {
        mSize = size;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        mLocation = location;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public Image(String id, String path, Date date, float latitude, float longitude, String location, long size) {
        mId = id;
        mPath = path;
        mDateAdded = date;
        this.latitude = latitude;
        this.longitude = longitude;
        mLocation = location;
        mSize = size;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getPath() {
        return mPath;
    }

    public void setPath(String path) {
        mPath = path;
    }

    public Date getDate() {
        return mDateAdded;
    }

    public void setDate(Date date) {
        mDateAdded = date;
    }
}
