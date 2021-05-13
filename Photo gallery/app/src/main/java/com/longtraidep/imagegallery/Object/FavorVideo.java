package com.longtraidep.imagegallery.Object;

public class FavorVideo {
    String mPathVideo;
    String mThumbnail;

    public FavorVideo(String pathVideo, String thumbnail) {
        mPathVideo = pathVideo;
        mThumbnail = thumbnail;
    }

    public String getPathVideo() {
        return mPathVideo;
    }

    public void setPathVideo(String pathVideo) {
        mPathVideo = pathVideo;
    }

    public String getThumbnail() {
        return mThumbnail;
    }

    public void setThumbnail(String thumbnail) {
        mThumbnail = thumbnail;
    }
}
