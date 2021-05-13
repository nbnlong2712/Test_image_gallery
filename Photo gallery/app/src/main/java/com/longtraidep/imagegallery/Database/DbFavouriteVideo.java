package com.longtraidep.imagegallery.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbFavouriteVideo extends SQLiteOpenHelper {
    public static final String DbFavouriteVideo = "FavouriteVideo.db";

    public DbFavouriteVideo(@Nullable Context context) {
        super(context, DbFavouriteVideo, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists FavouriteVideo (thumbnail, path_video)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Boolean insertData(String thumbnail, String path_video)
    {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("thumbnail", thumbnail);
        contentValues.put("path_video", path_video);

        long result = MyDB.insert("FavouriteVideo", null, contentValues);
        if(result == 1) return false;
        else return true;
    }
}
