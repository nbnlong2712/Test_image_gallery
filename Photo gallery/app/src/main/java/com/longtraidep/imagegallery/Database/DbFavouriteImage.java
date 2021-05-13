package com.longtraidep.imagegallery.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbFavouriteImage extends SQLiteOpenHelper {
    public static final String DbFavouriteImage = "FavouriteImage.db";

    public DbFavouriteImage(@Nullable Context context) {
        super(context, DbFavouriteImage, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists FavouriteImage (path_image)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Boolean insertData(String path_image)
    {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("path_image", path_image);

        long result = MyDB.insert("FavouriteImage", null, contentValues);
        if(result == 1) return false;
        else return true;
    }
}
