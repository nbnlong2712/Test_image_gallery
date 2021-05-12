package com.longtraidep.imagegallery.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbAlbum extends SQLiteOpenHelper {
    public static final String DbAlbum = "Album.db";

    public DbAlbum(@Nullable Context context) {
        super(context, DbAlbum, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists Album (_id integer primary key autoincrement, uuid, name, password)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    public Boolean insertData(String uuid, String name, String password)
    {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("uuid", uuid);
        contentValues.put("name", name);
        contentValues.put("password", password);

        long result = MyDB.insert("Album", null, contentValues);
        if(result == 1) return false;
        else return true;
    }

    public Boolean checkPassword(String uuid, String password)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("Select * from Album where uuid = ? and password = ?", new String[]{uuid, password});
        if(cursor.getCount() > 0)
            return true;
        else return false;
    }
}
