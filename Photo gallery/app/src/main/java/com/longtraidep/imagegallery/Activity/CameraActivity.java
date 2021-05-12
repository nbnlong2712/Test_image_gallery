package com.longtraidep.imagegallery.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;

import com.longtraidep.imagegallery.PreferenceUtils;
import com.longtraidep.imagegallery.R;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraActivity extends AppCompatActivity {
    private ImageButton mImageButton;
    private ImageButton mVideoButton;
    private ImageButton mGalleryButton;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_VIDEO_RECORD = 2;
    String currentPhotoPath;
    private ConstraintLayout mConstraintLayout;

    private OutputStream outputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_image);

        mConstraintLayout = (ConstraintLayout) findViewById(R.id.camera_layout);

        if(PreferenceUtils.getTheme(getApplicationContext()).equals("dark"))
        {
            mConstraintLayout.setBackgroundColor(Color.BLACK);
        }
        else {
            mConstraintLayout.setBackgroundColor(Color.WHITE);
        }
        mImageButton = (ImageButton) findViewById(R.id.imageButton);
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        });


        mGalleryButton = (ImageButton) findViewById(R.id.galleryButton);
        mGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CameraActivity.this, GalleryActivity.class);
                startActivity(i);
            }
        });

        mVideoButton = (ImageButton) findViewById(R.id.videoButton);
        mVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent recordVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                startActivityForResult(recordVideoIntent, REQUEST_VIDEO_RECORD);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                imageBitmap = Bitmap.createScaledBitmap(imageBitmap, 750, 1000, true);
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "IMG_" + timeStamp;

                MediaStore.Images.Media.insertImage(getContentResolver(), imageBitmap, imageFileName, "khakhakha");
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
        else if(requestCode == REQUEST_VIDEO_RECORD)
        {
            if (resultCode == Activity.RESULT_OK && data != null)
            {
                Uri videoUri = data.getData();
            }
        }
    }
}