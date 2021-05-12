package com.longtraidep.imagegallery.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.longtraidep.imagegallery.Adapter.SlideImageAdapter;
import com.longtraidep.imagegallery.Object.Image;
import com.longtraidep.imagegallery.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SlideShowActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private SlideImageAdapter mSlideImageAdapter;
    private List<Image> mImageList;
    private ImageButton mBackButton;
    private Timer mTimer;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_show_image);

        mImageList = getListImage();

        mViewPager = (ViewPager) findViewById(R.id.slide_show_viewpager);
        mSlideImageAdapter = new SlideImageAdapter(this, mImageList);
        mBackButton = (ImageButton) findViewById(R.id.button_back);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SlideShowActivity.this, CameraActivity.class);
                startActivity(i);
            }
        });

        mViewPager.setAdapter(mSlideImageAdapter);
        autoSlideImages();
    }

    private List<Image> getListImage() {
            mImageList = new ArrayList<>();
            Uri uri;
            Cursor cursor;
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            String[] projection = {MediaStore.Images.Media.DATA};

            final String orderBy = MediaStore.Video.Media.DATE_TAKEN;
            cursor = this.getApplicationContext().getContentResolver().query(uri, projection, null, null, orderBy + " DESC");

            while (cursor.moveToNext())
            {
                String path = cursor.getString(0);
                Image image = new Image("", path, new Date(), 0, 0, "", 0);
                mImageList.add(image);
            }

            cursor.close();
            return mImageList;
    }

    private void autoSlideImages()
    {
        if(mImageList == null || mImageList.isEmpty() || mViewPager == null)
            return;
        if(mTimer == null)
            mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        int currentItem = mViewPager.getCurrentItem();
                        int totalItem = mImageList.size() - 1;
                        if(currentItem < totalItem)
                        {
                            currentItem++;
                            mViewPager.setCurrentItem(currentItem);
                        }
                        else
                        {
                            mViewPager.setCurrentItem(0);
                        }
                    }
                });
            }
        }, 500, 2500);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mTimer!= null)
        {
            mTimer.cancel();
            mTimer = null;
        }
    }
}
