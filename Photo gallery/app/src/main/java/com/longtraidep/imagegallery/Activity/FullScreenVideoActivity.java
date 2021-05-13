package com.longtraidep.imagegallery.Activity;

import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.longtraidep.imagegallery.Database.DbFavouriteVideo;
import com.longtraidep.imagegallery.R;

import java.io.File;

import hb.xvideoplayer.MxVideoPlayer;
import hb.xvideoplayer.MxVideoPlayerWidget;

public class FullScreenVideoActivity extends AppCompatActivity {
    private BottomNavigationView mBottomNavigationView;
    private ImageButton mBackButton;
    private MxVideoPlayerWidget mVideoView;
    private TextView mPathText, mSizeText, mDateText, mDurationText;
    private String path = "", thumb = "";
    private DbFavouriteVideo mDbFavouriteVideo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_video);

        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.full_screen_navigation_video);
        mBackButton = (ImageButton) findViewById(R.id.back_button);
        mVideoView = (MxVideoPlayerWidget) findViewById(R.id.full_screen_video);
        mDateText = (TextView) findViewById(R.id.datetime_text);
        mPathText = (TextView) findViewById(R.id.path_text);
        mSizeText = (TextView) findViewById(R.id.size_text);
        mDurationText = (TextView) findViewById(R.id.duration_text);

        mDbFavouriteVideo = new DbFavouriteVideo(FullScreenVideoActivity.this);
        mDbFavouriteVideo.getWritableDatabase();

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FullScreenVideoActivity.this, GalleryActivity.class);
                startActivity(i);
            }
        });

        Intent callingActivity = getIntent();
        if(callingActivity != null)
        {
            path = callingActivity.getStringExtra("path");
            thumb = callingActivity.getStringExtra("thumb");
            String size = callingActivity.getStringExtra("size");
            String date = callingActivity.getStringExtra("dateTime");
            String duration = callingActivity.getStringExtra("duration");
            mVideoView.startPlay(path, MxVideoPlayer.SCREEN_LAYOUT_NORMAL, "");
            mDateText.setText(date);
            mPathText.setText("Path file: " + path);
            mSizeText.setText("Size: "+ size + " MB");
            mDurationText.setText("Duration: " + duration);
        }

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.navigation_copy:

                        break;
                    case R.id.navigation_favourite:
                        mDbFavouriteVideo.insertData(thumb, path);
                        Toast.makeText(getApplicationContext(), "Saved in Favourite Video!", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.navigation_delete:
                        deleteImage(path);
                        Toast.makeText(getApplicationContext(), "Video deleted!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(FullScreenVideoActivity.this, GalleryActivity.class);
                        startActivity(i);
                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        MxVideoPlayer.releaseAllVideos();
    }

    public void deleteImage(String file_dj_path) {
        File fdelete = new File(file_dj_path);
        if (fdelete.exists()) {
            if (fdelete.delete()) {
                callBroadCast();
            } else {
            }
        }
    }

    public void callBroadCast() {
        if (Build.VERSION.SDK_INT >= 14) {
            MediaScannerConnection.scanFile(this, new String[]{Environment.getExternalStorageDirectory().toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {

                public void onScanCompleted(String path, Uri uri) {}
            });
        } else
        {
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
                    Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        }
    }
}
