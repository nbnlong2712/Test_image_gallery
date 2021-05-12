package com.longtraidep.imagegallery.Object;

import java.util.List;

public class DateVideo {
    private String mDate;
    private List<Video> mVideos;

    public DateVideo(String date, List<Video> videos) {
        mDate = date;
        mVideos = videos;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public List<Video> getVideos() {
        return mVideos;
    }

    public void setVideos(List<Video> videos) {
        mVideos = videos;
    }
}
