package com.longtraidep.imagegallery.Database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.longtraidep.imagegallery.Object.Album;

import java.util.UUID;

public class AlbumCursorWrapper extends CursorWrapper {

    public AlbumCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Album getAlbum()
    {
        String uuid = getString(getColumnIndex("uuid"));
        String name = getString(getColumnIndex("name"));
        String password = getString(getColumnIndex("password"));

        Album album = new Album(UUID.fromString(uuid));
        album.setName(name);
        album.setPassword(password);

        return album;
    }
}
