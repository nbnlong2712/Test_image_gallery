package com.longtraidep.imagegallery.Object;

import java.util.List;

public class DateImage {
    private String mDate;
    private List<Image> mImages;

    public DateImage(String date, List<Image> images) {
        mDate = date;
        mImages = images;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public List<Image> getImages() {
        return mImages;
    }

    public void setImages(List<Image> images) {
        mImages = images;
    }
}
