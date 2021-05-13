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

import com.longtraidep.imagegallery.Database.DbFavouriteImage;
import com.longtraidep.imagegallery.GlideApp.GlideApp;
import com.longtraidep.imagegallery.R;

import java.util.ArrayList;
import java.util.List;

public class FavouriteImageActivity extends AppCompatActivity {
    private ImageButton mBack;      //nút để trở lại activity trước
    private RecyclerView mRecyclerView;                  //recyclerview để hiển thị các ảnh
    private List<String> mPathFavorImages;               //list để lưu path của các ảnh
    private FavouriteImageAdapter mAdapter;              //Adapter list các ảnh yêu thích

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_image);

        mBack = (ImageButton) findViewById(R.id.favor_image_back);
        mRecyclerView = (RecyclerView) findViewById(R.id.rcv_favor_image);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));

        mBack.setOnClickListener(new View.OnClickListener() {  //nút để trở lại activity trước
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FavouriteImageActivity.this, FavoriteActivity.class);
                startActivity(i);
            }
        });
        getAllFavorImage();    //hàm này sẽ lấy tất cả path ảnh yêu thích từ db, sau đó gán vào adapter và hiển thị lên recyclerview
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllFavorImage();
    }

    private void getAllFavorImage()
    {
        mPathFavorImages = new ArrayList<>();
        SQLiteDatabase db = openOrCreateDatabase("FavouriteImage.db", Context.MODE_PRIVATE, null); //mở db và lấy tất cả ảnh
        Cursor cursor = db.rawQuery("select distinct * from FavouriteImage", null);
        while (cursor.moveToNext())
        {
            mPathFavorImages.add(cursor.getString(0));
        }
        if(mAdapter == null) {
            mAdapter = new FavouriteImageAdapter(getApplicationContext());
            mAdapter.setData(mPathFavorImages);
            mRecyclerView.setAdapter(mAdapter);
        }
        else {
            mAdapter.setData(mPathFavorImages);
            mRecyclerView.setAdapter(mAdapter);
        }

    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    private class FavouriteImageHolder extends RecyclerView.ViewHolder implements View.OnClickListener   //chứa item ảnh yêu thích
    {
        private String mPathhFile;
        private ImageView mImageView;

        public FavouriteImageHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mImageView = (ImageView) itemView.findViewById(R.id.image_favor_item);
        }

        public void bind(String pathFile)
        {
            mPathhFile = pathFile;
        }

        @Override
        public void onClick(View v) {    //lúc nhấn vào 1 ảnh nào đó thì chuyển sang màn hình full screen của ảnh đó, chuyển theo path ảnh đó
            Intent i = new Intent(FavouriteImageActivity.this, FullScreenFavorImageActivity.class);
            i.putExtra("path", mPathhFile);
            startActivity(i);
        }
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    private class FavouriteImageAdapter extends RecyclerView.Adapter<FavouriteImageHolder>
    {
        private List<String> mFavorImagePaths;
        private Context mContext;

        public FavouriteImageAdapter(Context context)
        {
            mContext = context;
        }

        public void setData(List<String> favorImagePaths)
        {
            mFavorImagePaths = favorImagePaths;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public FavouriteImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_favor_image, parent, false);
            return new FavouriteImageHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull FavouriteImageHolder holder, int position) {
            String pathfile = mFavorImagePaths.get(position);
            holder.bind(pathfile);
            GlideApp.with(getApplicationContext()).load(pathfile).into(holder.mImageView);
        }

        @Override
        public int getItemCount() {
            if(mFavorImagePaths != null)
                return mFavorImagePaths.size();
            return 0;
        }
    }
}
