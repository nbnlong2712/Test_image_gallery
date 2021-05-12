package com.longtraidep.imagegallery.Activity;

import android.app.WallpaperManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity;
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.longtraidep.imagegallery.GlideApp.GlideApp;
import com.longtraidep.imagegallery.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FullScreenImageActivity extends AppCompatActivity {
    private BottomNavigationView mBottomNavigationView;
    private ImageButton mBackButton, mDeleteButton;
    private TextView mDateTime;
    private TextView mLocation;
    private TextView mPath;
    private TextView mSize;
    private String path = "";
    private ImageView mImageView;
    private Bitmap mBitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.full_screen_navigation_image);
        mImageView = (ImageView) findViewById(R.id.full_screen_image);
        mDateTime = (TextView) findViewById(R.id.datetime_text);
        mLocation = (TextView) findViewById(R.id.location_text);
        mPath = (TextView) findViewById(R.id.path_text);
        mSize = (TextView) findViewById(R.id.size_text);
        mDeleteButton = (ImageButton) findViewById(R.id.delete_button);

        Intent callingActivity = getIntent();
        if (callingActivity != null) {
            path = callingActivity.getStringExtra("path");
            String datetime = callingActivity.getStringExtra("dateTime");
            String location = callingActivity.getStringExtra("location");
            String size = callingActivity.getStringExtra("size");
            GlideApp.with(this).load(path).into(mImageView);
            mLocation.setText(location);
            mDateTime.setText(datetime);
            mPath.setText("Path file: " + path);
            mSize.setText("Size: " + size + " KB");
        }

        try {
            mBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(new File(path)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        mBackButton = (ImageButton) findViewById(R.id.back_button);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intt = new Intent(FullScreenImageActivity.this, GalleryActivity.class);
                startActivity(intt);
            }
        });

        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteImage(path);
                Toast.makeText(getApplicationContext(), "Image deleted!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(FullScreenImageActivity.this, CameraActivity.class);
                startActivity(i);
            }
        });
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_edit:
                        Uri filePath = Uri.fromFile(new File(path));
                        Intent dsPhotoEditorIntent = new Intent(FullScreenImageActivity.this, DsPhotoEditorActivity.class);
                        dsPhotoEditorIntent.setData(filePath);
                        dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY, "Camera");
                        int[] toolsToHide = {DsPhotoEditorActivity.TOOL_ORIENTATION, DsPhotoEditorActivity.TOOL_CROP};
                        dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_TOOLS_TO_HIDE, toolsToHide);
                        startActivityForResult(dsPhotoEditorIntent, 200);
                        break;
                    case R.id.navigation_favourite:
                        break;
                    case R.id.navigation_share:
                        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                        StrictMode.setVmPolicy(builder.build());
                        File file = new File(path);
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("image/*");
                        i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(Intent.createChooser(i, "Share image via: "));
                        break;
                    case R.id.navigation_copy:
                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                        String imageFileName = "IMG_" + timeStamp;
                        MediaStore.Images.Media.insertImage(getContentResolver(), mBitmap, imageFileName, "khakhakha");
                        Toast.makeText(getApplicationContext(), "Copied image!", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.navigation_background:
                        WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
                        try {
                            wallpaperManager.setBitmap(mBitmap);
                            Toast.makeText(getApplicationContext(), "Set background success!", Toast.LENGTH_SHORT).show();
                            Intent in = new Intent(FullScreenImageActivity.this, CameraActivity.class);
                            startActivity(in);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                }
                return false;
            }
        });
    }

    public void deleteImage(String filePath) {
        ContentResolver contentResolver = getContentResolver();
        contentResolver.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                MediaStore.Images.ImageColumns.DATA + "=?", new String[]{filePath});
    }
}
