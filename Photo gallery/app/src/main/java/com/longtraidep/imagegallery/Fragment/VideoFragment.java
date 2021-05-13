package com.longtraidep.imagegallery.Fragment;

import android.Manifest;
import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.longtraidep.imagegallery.Adapter.DateVideoAdapter;
import com.longtraidep.imagegallery.Object.DateImage;
import com.longtraidep.imagegallery.Object.DateVideo;
import com.longtraidep.imagegallery.Object.Image;
import com.longtraidep.imagegallery.R;
import com.longtraidep.imagegallery.Object.Video;
import com.longtraidep.imagegallery.Adapter.VideoAdapter;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VideoFragment extends Fragment {
    private RecyclerView mRecyclerViewVideo;
    private List<Video> mVideoList;
    private List<DateVideo> mDateVideosList;
    private DateVideoAdapter mDateVideoAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_video, container, false);

        mRecyclerViewVideo = (RecyclerView) v.findViewById(R.id.rcv_video);
        mDateVideoAdapter = new DateVideoAdapter(getActivity());

        mRecyclerViewVideo.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL,false));

        checkPermission();

        return v;
    }

    private void checkPermission()
    {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                getAllVideosFromGallery();
            }
            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
            }
        };
        TedPermission.with(getActivity())
                .setPermissionListener(permissionListener)
                .setDeniedMessage("Access denied")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }

    private void getAllVideosFromGallery()
    {
        mDateVideosList = new ArrayList<>();
        mVideoList = new ArrayList<>();
        Uri uri;
        Cursor cursor;

        uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.Video.Media.DATA //0
                , MediaStore.Video.Media.BUCKET_DISPLAY_NAME  //1
                , MediaStore.Video.Media._ID        //2
                , MediaStore.Video.Media.DATE_ADDED    //3
                , MediaStore.Video.Media.SIZE       //4
                , MediaStore.Video.Media.DURATION    //5
                , MediaStore.Video.Thumbnails.DATA};   //6
        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        cursor = getActivity().getApplicationContext().getContentResolver().query(uri, projection, null, null, orderBy + " DESC");

        while (cursor.moveToNext())
        {

            String id = cursor.getString(2);
            String path = cursor.getString(0);
            String thumb = cursor.getString(6);
            long dateAdded = cursor.getLong(3);
            long duration = cursor.getLong(5)/1000;
            duration = (long)(Math.round((double)duration*1000)/1000);
            long size = cursor.getLong(4)/1000000;
            size = (long)(Math.round((double)size*100000)/100000);
            long phut = duration/60;
            duration = duration%60;
            String dodai = phut + "p:" + duration + "s";

            long datet = 0;
            File file = new File(path);
            if(file.exists()) {
                datet = file.lastModified();
            }
            Date datett = new Date(datet);
            String datettt = new SimpleDateFormat("dd-MM-yyyy").format(datett);

            Video video = new Video(id, path, thumb, datett, dodai, size);

            mVideoList.add(video);
        }
        List<String> test = new ArrayList<String>();
        for(int i=0; i<mVideoList.size(); i++)
        {
            String date = new SimpleDateFormat("dd-MM-yyyy").format(mVideoList.get(i).getDateAdded());
            test.add(date);
        }
        List<String> test1 = new ArrayList<String>();
        for (int i=0; i<test.size(); i++)
        {
            if(!test1.contains(test.get(i)))
            {
                test1.add(test.get(i));
            }
        }
        for(String element : test1)
        {
            DateVideo dateVideo = new DateVideo(element,null);
            mDateVideosList.add(dateVideo);
        }
        for (int i=0; i<mDateVideosList.size();i++)
        {
            List<Video> videoListt  = new ArrayList<>();
            for(int s = 0; s < mVideoList.size(); s++)
            {
                String date = new SimpleDateFormat("dd-MM-yyyy").format(mVideoList.get(s).getDateAdded());
                if(mDateVideosList.get(i).getDate().equals(date))
                {
                    videoListt.add(mVideoList.get(s));
                }
            }
            mDateVideosList.get(i).setVideos(videoListt);
        }

        cursor.close();
        mDateVideoAdapter.setData(mDateVideosList);
        mRecyclerViewVideo.setAdapter(mDateVideoAdapter);
    }
}
