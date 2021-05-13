package com.longtraidep.imagegallery.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.longtraidep.imagegallery.R;

public class FavoriteActivity extends AppCompatActivity {

    private Button mButtonVideo;   //nút để chuyển hướng người dùng sang màn hình các ảnh yêu thích
    private Button mButtonImage;   //nút để chuyển hướng người dùng sang màn hình các video yêu thích
    private ImageButton mBack;     //nút để trở lại activity trước

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        mButtonImage = (Button) findViewById(R.id.button_favor_image);
        mButtonVideo = (Button) findViewById(R.id.button_favor_video);
        mBack = (ImageButton) findViewById(R.id.btnback);

        mButtonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FavoriteActivity.this, FavouriteImageActivity.class);
                startActivity(i);
            }
        });

        mButtonVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FavoriteActivity.this, FavouriteVideoActivity.class);
                startActivity(i);
            }
        });

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FavoriteActivity.this, CameraActivity.class);
                startActivity(i);
            }
        });
    }
}
