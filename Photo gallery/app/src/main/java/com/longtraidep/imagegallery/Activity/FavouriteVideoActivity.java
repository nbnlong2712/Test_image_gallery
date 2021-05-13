package com.longtraidep.imagegallery.Activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.longtraidep.imagegallery.GlideApp.GlideApp;
import com.longtraidep.imagegallery.Object.FavorVideo;
import com.longtraidep.imagegallery.R;

import java.util.ArrayList;
import java.util.List;

public class FavouriteVideoActivity extends AppCompatActivity {
    private ImageButton mBack;
    private RecyclerView mRecyclerView;                     //recyclerview hiển thị list video
    private List<FavorVideo> mFavorVideoList;             //list các favorvideo
    private FavouriteVideoAdapter mAdapter;               //adapter các favorvideo

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_video);

        mBack = (ImageButton) findViewById(R.id.favor_video_back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FavouriteVideoActivity.this, FavoriteActivity.class);
                startActivity(i);
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.rcv_favor_video);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3)); //hiển thị recyclerview theo gridview với số cột là 3
        getAllFavorVideo();       //lấy all video yêu thích từ database
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllFavorVideo();
    }

    private void getAllFavorVideo()
    {
        mFavorVideoList = new ArrayList<>();
        SQLiteDatabase db = openOrCreateDatabase("FavouriteVideo.db", Context.MODE_PRIVATE, null);   //mở db và lấy favorvideo
        Cursor cursor = db.rawQuery("select distinct * from FavouriteVideo", null);
        while (cursor.moveToNext())
        {
            String thumb = cursor.getString(0);     //cột 0 chứa các thumbnail (ảnh nền dưới dạng path)
            String path_vid = cursor.getString(1);  //cột 1 chứa các path của video
            FavorVideo faV = new FavorVideo(thumb, path_vid);
            mFavorVideoList.add(faV);
        }
        if(mAdapter == null) {
            mAdapter = new FavouriteVideoAdapter(getApplicationContext());
            mAdapter.setData(mFavorVideoList);
            mRecyclerView.setAdapter(mAdapter);
        }
        else {
            mAdapter.setData(mFavorVideoList);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////
    private class FavouriteVideoHolder extends RecyclerView.ViewHolder implements View.OnClickListener   //chứa item video yêu thích
    {
        private FavorVideo mFavorVideo;
        private ImageView mImageView;

        public FavouriteVideoHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mImageView = (ImageView) itemView.findViewById(R.id.thumb_favor_video);
        }

        public void bind(FavorVideo favorVideo)
        {
            mFavorVideo = favorVideo;
        }

        @Override
        public void onClick(View v) {    //lúc nhấn vào 1 video bất kỳ thì chuyển sang màn hình play video đó, chuyển theo path video
            Intent i = new Intent(FavouriteVideoActivity.this, FullScreenFavorVideoActivity.class);
            i.putExtra("path", mFavorVideo.getPathVideo());
            startActivity(i);
        }
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////
    private class FavouriteVideoAdapter extends RecyclerView.Adapter<FavouriteVideoHolder>
    {
        private List<FavorVideo> mVideoList;
        private Context mContext;

        public FavouriteVideoAdapter(Context context)
        {
            mContext = context;
        }

        public void setData(List<FavorVideo> videoList)
        {
            mVideoList = videoList;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public FavouriteVideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_favor_video, parent, false);
            return new FavouriteVideoHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull FavouriteVideoHolder holder, int position) {
            FavorVideo favorVideo = mVideoList.get(position);
            holder.bind(favorVideo);
            GlideApp.with(getApplicationContext()).load(favorVideo.getThumbnail()).into(holder.mImageView);// hiển thị nền của video
        }

        @Override
        public int getItemCount() {
            if(mVideoList != null)
                return mVideoList.size();
            return 0;
        }
    }
}
