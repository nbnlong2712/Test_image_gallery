package com.longtraidep.imagegallery.Object;

import java.util.Date;

public class Video {
    private String id;
    private String path;
    private String thumb;
    private Date dateAdded;
    private String duration;
    private long size;

    public Video(String id, String path, String thumb, Date dateAdded, String duration, long size) {
        this.id = id;
        this.path = path;
        this.thumb = thumb;
        this.dateAdded = dateAdded;
        this.duration = duration;
        this.size = size;
    }


    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
}
