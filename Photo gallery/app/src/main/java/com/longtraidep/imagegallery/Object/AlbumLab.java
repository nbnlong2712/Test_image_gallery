package com.longtraidep.imagegallery.Object;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.longtraidep.imagegallery.Database.AlbumCursorWrapper;
import com.longtraidep.imagegallery.Database.DbAlbum;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AlbumLab {
    private static AlbumLab sAlbumLab;     //
    private Context mContext;
    private SQLiteDatabase mDatabase;

    private AlbumLab(Context context)
    {
        mContext = context.getApplicationContext();
        mDatabase = new DbAlbum(mContext).getWritableDatabase();
    }

    public static AlbumLab get(Context context)
    {
        if(sAlbumLab == null)
            sAlbumLab = new AlbumLab(context);
        return sAlbumLab;
    }

    private AlbumCursorWrapper queryAlbums(String whereClause, String[] whereArgs)
    {
        Cursor cursor = mDatabase.query(
                "Album",
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new AlbumCursorWrapper(cursor);
    }

    public Album getAlbum(UUID mid)
    {
        AlbumCursorWrapper cursor = queryAlbums("uuid" + " = ?", new String[]{mid.toString()});
        try {
            if (cursor.getCount() == 0)
                return null;
            cursor.moveToFirst();
            return cursor.getAlbum();
        }
        finally {
            cursor.close();
        }
    }

    private static ContentValues getContentValues(Album album) {
        ContentValues values = new ContentValues();
        values.put("uuid", album.getId().toString());
        values.put("name", album.getName());
        values.put("password", album.getPassword());
        return values;
    }

    public void addAlbum(Album album)
    {
        ContentValues values = getContentValues(album);
        mDatabase.insert("Album", null, values);
    }

    public List<Album> getAlbums()
    {
        List<Album> albums = new ArrayList<>();
        AlbumCursorWrapper cursor = queryAlbums(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                albums.add(cursor.getAlbum());
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }
        return albums;
    }
}
