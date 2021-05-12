package com.longtraidep.imagegallery.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbAlbumImage extends SQLiteOpenHelper {
    public static final String DbAlbumImage = "AlbumImage.db";

    public DbAlbumImage(@Nullable Context context) {
        super(context, DbAlbumImage, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists AlbumImage (_id integer primary key autoincrement, uuid, path)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Boolean insertData(String uuid, String path)
    {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("uuid", uuid);
        contentValues.put("path", path);

        long result = MyDB.insert("AlbumImage", null, contentValues);
        if(result == 1) return false;
        else return true;
    }
}
