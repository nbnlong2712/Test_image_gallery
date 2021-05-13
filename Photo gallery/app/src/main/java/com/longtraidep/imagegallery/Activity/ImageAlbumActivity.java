package com.longtraidep.imagegallery.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.longtraidep.imagegallery.Database.DbAlbum;
import com.longtraidep.imagegallery.Database.DbAlbumImage;
import com.longtraidep.imagegallery.GlideApp.GlideApp;
import com.longtraidep.imagegallery.R;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ImageAlbumActivity extends AppCompatActivity {
    private FloatingActionButton mBackBtn;                    //nút dùng để trở lại danh sách album khi không muốn xem ảnh nữa (trở lại acitivty trước đó)
    private FloatingActionButton mAddImageBtn;        // nút thêm một ảnh mới vào album
    private FloatingActionButton mDeleteAlbum;        // nút xóa album (lưu ý là xóa album chứ không phải xóa 1 ảnh trong album)
    private TextView mAlbumName;                      //textview hiển thị tên album
    private RecyclerView mRecyclerView;               //recyclerview dùng hiển thị danh sách ảnh
    private List<String> mPathImages;                 //list dùng để lưu path của các ảnh sau khi được lấy lên từ database
    String uuidToString = "";                         // biến dùng để lưu uuid mà activity trước dùng Intent gửi sang (ở đây là AlbumActivity.java)
    String name_ab = "";                             //biến dùng để lưu tên của album mà activity trước dùng Intent gửi sang (ở đây là AlbumActivity.java)
    private int SELECT_FILE_CODE = 10;               //request code mà Intent sử dụng nhằm làm thao tác chọn ảnh từ Gallery
    private DbAlbumImage mDbAlbumImage;             // object của DbALbumImage, trong acitivty này thì dùng để insert một ảnh mới vào database khi nhấn nút Add ảnh
    private ImageAlbumAdapter mAdapter;           //Adapter của list ảnh
    private DbAlbum mDbAlbum;                       //onject của DbAlbum, trong activity này thì dùng để thực hiện thao tác xóa album

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_in_album);

        mDbAlbumImage = new DbAlbumImage(ImageAlbumActivity.this);   //khởi tạo mDbAlbumImage
        mDbAlbumImage.getWritableDatabase();

        mDbAlbum = new DbAlbum(ImageAlbumActivity.this);        //khởi tạo mDbAlbum
        mDbAlbum.getWritableDatabase();

        Intent intent = getIntent();                    //Intent dùng để nhận dữ liệu activity khác gửi qua (ở đây là AlbumActivity.java và FullScreenAlbumImage.java)
        uuidToString = intent.getStringExtra("uuid");   //lấy uuid gửi qua
        name_ab = intent.getStringExtra("name");        //lấy name gửi qua

        mAlbumName = (TextView) findViewById(R.id.name_of_album);
        mBackBtn = (FloatingActionButton) findViewById(R.id.back_button_album);
        mAddImageBtn = (FloatingActionButton) findViewById(R.id.add_image_album);

        mRecyclerView = (RecyclerView) findViewById(R.id.rcv_image_album);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));   //set cách hiển thị của recyclerview là hiển thị theo gridview với số cột là 4

        mBackBtn.setOnClickListener(new View.OnClickListener() {   //nút này để trở lại activity trước
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ImageAlbumActivity.this, AlbumActivity.class);
                startActivity(i);
            }
        });

        mAlbumName.setText(name_ab);     //set tên của album lên textview

        mAddImageBtn.setOnClickListener(new View.OnClickListener() {       // nút add ảnh vào album
            @Override
            public void onClick(View v) {
                Intent i = new Intent( Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);  //2 dòng này dùng cho thao tác chọn ảnh từ Gallery
                startActivityForResult(i, SELECT_FILE_CODE);   //SELECT_FILE_CODE là request code được khai báo ở trên (kéo lên xem)
            }
        });

        mDeleteAlbum = (FloatingActionButton) findViewById(R.id.delete_album);
        mDeleteAlbum.setOnClickListener(new View.OnClickListener() {   //nút xóa album (xóa album hiện tại chứ ko phải xóa ảnh trong album)
            @Override
            public void onClick(View v) {
                deleteAlbum(uuidToString);        //gọi hàm deleteALbum, truyền vào uuid nhận được từ những activity khác chuyển sang (uuid của album muốn xóa)
            }
        });

        getAllImageFromDB();     //hàm dùng để load tất cả ảnh từ database lên màn hình
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllImageFromDB();
    }

    private void getAllImageFromDB()
    {
        mPathImages = new ArrayList<>();  //list này đã đc khai báo ở trên (kéo lên xem)
        SQLiteDatabase db = openOrCreateDatabase("AlbumImage.db", Context.MODE_PRIVATE, null);  //mở database AlbumImage.db
        Cursor cursor = db.rawQuery("select distinct * from AlbumImage where uuid = ?", new String[]{uuidToString});   //truy vấn trong bảng AlbumImage, đọc câu truy vấn này dễ
        while (cursor.moveToNext())     //cursor dùng để duyệt các phần từ được lấy ra sau khi truy vấn
        {
            mPathImages.add(cursor.getString(1));     //các path ảnh được lấy lên sẽ được add  vào mPathImages
        }
        if(mAdapter == null) {    //nếu Adapter chưa được khởi tạo thì bắt đầu khởi tạo rồi gán vào RecyclerView
            mAdapter = new ImageAlbumAdapter(getApplicationContext());
            mAdapter.setData(mPathImages);
            mRecyclerView.setAdapter(mAdapter);
        }
        else {
            mAdapter.setData(mPathImages);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    public void deleteAlbum(String uuid)  //hàm xóa Album khỏi db
    {
        SQLiteDatabase db = openOrCreateDatabase("Album.db", Context.MODE_PRIVATE, null);  //mở db Album.db
        db.delete("Album", "uuid = ?", new String[]{uuid});                     //truy vấn và xóa

        SQLiteDatabase db1 = openOrCreateDatabase("AlbumImage.db", Context.MODE_PRIVATE, null);   //mở db AlbumImage.db
        db1.delete("AlbumImage", "uuid = ?", new String[]{uuid});                //truy vấn và xóa những dòng có ID của album đó

        Intent i = new Intent(ImageAlbumActivity.this, AlbumActivity.class);    //trở về lại activity trước
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {    //hàm này nhận dữ liệu hình ảnh sau khi chọn ảnh từ Gallery
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK && data!= null)
        {
            if(requestCode == SELECT_FILE_CODE)
            {
                String path = getRealPathFromURI(this, data.getData());    //dữ liệu nhận được là một URI, chuyển URI sang dạng path file
                mPathImages.add(path);                              //thêm path ảnh vừa thêm vào list
                mAdapter.setData(mPathImages);                     //set Adapter lại
                mRecyclerView.setAdapter(mAdapter);                //set recyclerview lại
                mDbAlbumImage.insertData(uuidToString, path);      //thêm ảnh vừa thêm vào database
            }
        }
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {     //hàm này dùng để chuyển URI sang path file (String)
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    private class ImageAlbumHolder extends RecyclerView.ViewHolder implements View.OnClickListener          //chứa item ảnh
    {
        private String mPathFile;
        private ImageView mImageView;

        public ImageAlbumHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mImageView = (ImageView) itemView.findViewById(R.id.album_image_item);
        }

        public void bind(String pathFile)
        {
            mPathFile = pathFile;
        }

        @Override
        public void onClick(View v) {       //lúc nhấn vào 1 ảnh, sẽ chuyển từ màn hình danh sách các ảnh sang màn hình chi tiết của ảnh vừa nhấn, chuyển theo 3 thông số là uuid, name (2 cái này là của album), và path (cái này là của ảnh vừa nhấn)
            Intent i = new Intent(ImageAlbumActivity.this, FullScreenAlbumImageActivity.class);
            i.putExtra("uuid", uuidToString);
            i.putExtra("name", name_ab);
            i.putExtra("path", mPathFile);
            startActivity(i);
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////
    private class ImageAlbumAdapter extends RecyclerView.Adapter<ImageAlbumHolder> //Adapter để chứa list ảnh
    {
        private List<String> mImagePaths;
        private Context mContext;

        public ImageAlbumAdapter(Context context)
        {
            mContext = context;
        }

        public void setData(List<String> imagePaths)
        {
            mImagePaths = imagePaths;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ImageAlbumHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_album_image, parent, false);
            return new ImageAlbumHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ImageAlbumHolder holder, int position) {
            String pathfile = mImagePaths.get(position);
            holder.bind(pathfile);
            GlideApp.with(getApplicationContext()).load(pathfile).into(holder.mImageView);
        }

        @Override
        public int getItemCount() {
            if(mImagePaths != null)
                return mImagePaths.size();
            return 0;
        }
    }
}
