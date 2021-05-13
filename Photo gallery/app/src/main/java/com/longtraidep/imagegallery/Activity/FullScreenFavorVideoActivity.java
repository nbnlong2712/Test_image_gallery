package com.longtraidep.imagegallery.Activity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.longtraidep.imagegallery.R;

import hb.xvideoplayer.MxVideoPlayer;
import hb.xvideoplayer.MxVideoPlayerWidget;

public class FullScreenFavorVideoActivity extends AppCompatActivity {
    private ImageButton mBack;
    private ImageButton mDelete;
    private MxVideoPlayerWidget mVideo;
    String mPath = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_favor_video);

        mBack = (ImageButton) findViewById(R.id.back_btnn);
        mDelete = (ImageButton) findViewById(R.id.delete_video_favor);
        mVideo = (MxVideoPlayerWidget) findViewById(R.id.full_screen_favor_video);

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FullScreenFavorVideoActivity.this, FavouriteVideoActivity.class);
                startActivity(i);
            }
        });

        Intent intent = getIntent();
        mPath = intent.getStringExtra("path");
        mVideo.startPlay(mPath, MxVideoPlayer.SCREEN_LAYOUT_NORMAL, "");

        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = openOrCreateDatabase("FavouriteVideo.db", Context.MODE_PRIVATE, null);
                db.delete("FavouriteVideo", "path_video = ?", new String[]{mPath});
                Intent i = new Intent(FullScreenFavorVideoActivity.this, FavouriteVideoActivity.class);
                startActivity(i);
            }
        });

    }
}
