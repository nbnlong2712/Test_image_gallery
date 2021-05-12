package com.longtraidep.imagegallery.Object;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Album {
    private UUID mId;
    private String mName;
    private List<String> mPathImageList;
    private String mPassword;

    public Album() {
        this(UUID.randomUUID());
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public Album(UUID id)
    {
        mId = id;
        mName = "";
        mPathImageList = new ArrayList<>();
        mPassword = "";
    }

    public Album(UUID id, String name, List<String> pathImageList, String password) {
        mId = id;
        mName = name;
        mPathImageList = pathImageList;
        mPassword = password;
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public List<String> getPathImageList() {
        return mPathImageList;
    }

    public void setPathImageList(List<String> pathImageList) {
        mPathImageList = pathImageList;
    }
}
