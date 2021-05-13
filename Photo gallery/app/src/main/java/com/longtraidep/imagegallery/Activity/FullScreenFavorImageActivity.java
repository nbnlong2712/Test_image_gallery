package com.longtraidep.imagegallery.Activity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dsphotoeditor.sdk.ui.photoview.PhotoView;
import com.longtraidep.imagegallery.GlideApp.GlideApp;
import com.longtraidep.imagegallery.R;

public class FullScreenFavorImageActivity extends AppCompatActivity {
    private PhotoView mPhotoView;
    private ImageButton mBack;
    private ImageButton mDelete;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_favor_image);

        Intent intent = getIntent();
        String pathimage = intent.getStringExtra("path");

        mPhotoView = (PhotoView) findViewById(R.id.full_screen_favor_image);
        mBack = (ImageButton) findViewById(R.id.back_to_favor_image);
        mDelete = (ImageButton) findViewById(R.id.delete_from_favor);

        GlideApp.with(this).load(pathimage).into(mPhotoView);

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FullScreenFavorImageActivity.this, FavouriteImageActivity.class);
                startActivity(i);
            }
        });

        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = openOrCreateDatabase("FavouriteImage.db", Context.MODE_PRIVATE, null);
                db.delete("FavouriteImage", "path_image = ?", new String[]{pathimage});
                Intent i = new Intent(FullScreenFavorImageActivity.this, FavouriteImageActivity.class);
                startActivity(i);
            }
        });

    }
}
