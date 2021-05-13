package com.longtraidep.imagegallery.Activity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dsphotoeditor.sdk.ui.photoview.PhotoView;
import com.longtraidep.imagegallery.GlideApp.GlideApp;
import com.longtraidep.imagegallery.R;

public class FullScreenAlbumImageActivity extends AppCompatActivity {
    private PhotoView mPhotoView;      //PhotoView này để hiển thị ảnh (này giống ImageView, nhưng cho phép zoom to nhỏ tùy ý)
    private ImageButton mBack;         //nút để trở lại acitivyt trước
    private ImageButton mDelete;       // nút dùng để xóa bức ảnh đang xem khỏi album

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_album_image);

        Intent intent = getIntent();             //nhận intent chuyển từ activity trước (ImageAlbumActivity.java)
        String uuidString = intent.getStringExtra("uuid");     //uuid của album
        String name_ab = intent.getStringExtra("name");        //name của album
        String pathpath = intent.getStringExtra("path");       //path của ảnh

        mPhotoView = (PhotoView) findViewById(R.id.full_screen_album_image);
        mBack = (ImageButton) findViewById(R.id.back_to_album_image);
        mDelete = (ImageButton) findViewById(R.id.delete_from_album);

        GlideApp.with(this).load(pathpath).into(mPhotoView);       //load ảnh vào PhotoView bằng thư viện Glide
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FullScreenAlbumImageActivity.this, ImageAlbumActivity.class);//trở lại activity trước, chuyển theo uuid và name của album để nó hiển thị
                i.putExtra("uuid", uuidString);
                i.putExtra("name", name_ab);
                startActivity(i);
            }
        });

        mDelete.setOnClickListener(new View.OnClickListener() {   //nút dùng để xóa bức ảnh đang xem khỏi album
            @Override
            public void onClick(View v) {    //mở database AlbumImage.db và xóa, câu truy vấn dễ đọc, sau đó về lại activity trước (activity chứa danh sách ảnh)
                SQLiteDatabase db = openOrCreateDatabase("AlbumImage.db", Context.MODE_PRIVATE, null);
                db.delete("AlbumImage", "path = ?", new String[]{pathpath});
                Intent i = new Intent(FullScreenAlbumImageActivity.this, ImageAlbumActivity.class);
                i.putExtra("uuid", uuidString);
                i.putExtra("name", name_ab);
                startActivity(i);
            }
        });
    }
}
