package com.longtraidep.imagegallery.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.longtraidep.imagegallery.CreateAlbumDialog;
import com.longtraidep.imagegallery.Object.Album;
import com.longtraidep.imagegallery.Object.AlbumLab;
import com.longtraidep.imagegallery.R;

import java.util.List;

public class AlbumActivity extends AppCompatActivity implements CreateAlbumDialog.CreateAlbumDialogListener { //CreateAlbumDialog là dialog sẽ hiện ra lúc tạo album, cho phép người dùng nhập tên album và mật khẩu của album mới tạo đó
    private static final String ARG_ALBUM_ID = "album_id";
    private RecyclerView mRecyclerView;       //RecyclerView dùng để hiển thị danh sách album
    private AlbumAdapter mAdapter;            //Adapter các album
    private FloatingActionButton mAddAlbumButton;    //Nút dùng để thêm album
    private FloatingActionButton mBackBtn;           //Nút dùng để trở lại màn hình trước đó

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_album);     //Layout chính của AlbumActivity là fragment_album

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_album);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));     //Hiển thị RecyclerView theo GridView với số cột là 2

        mAddAlbumButton = (FloatingActionButton) findViewById(R.id.add_new_album);
        mAddAlbumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateAlbumDialog();
            }             //Khi nhấn vào nút thêm album, sẽ gọi hàm mở Dialog (dialog này tên là CreateAlbum, implement từ CreateAlbumDialog.java), cho phép người dùng nhập tên và password của album cần tạo
        });

        mBackBtn = (FloatingActionButton) findViewById(R.id.back_btn);   //Khi nhấn vào nút này thì sẽ back trở lại màn hình trước đó
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AlbumActivity.this, CameraActivity.class);
                startActivity(i);
            }
        });

        updateUI();       //Gọi hàm updateUI(), hàm này sẽ xử lý việc lấy danh sách album từ database, sau đó gán vào một list, rồi dùng Adater và RecyclerView để hiển thị
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    //Hàm này để mở Dialog CreateAlbum (tạo album, cho phép nhập name và password của album được tạo)
    public void openCreateAlbumDialog() {
        CreateAlbumDialog createAlbumDialog = new CreateAlbumDialog();
        createAlbumDialog.show(getSupportFragmentManager(), "create album");
    }

    private void updateUI() {
        AlbumLab albumLab = AlbumLab.get(this);
        List<Album> albums = albumLab.getAlbums();
        if (mAdapter == null) {
            mAdapter = new AlbumAdapter(albums);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setAlbums(albums);
            mAdapter.notifyDataSetChanged();
        }
    }

    //Hàm này ghi đè phương thức applyNamePassword từ interface của file CreateAlbumDialog.java (xem file để biết thêm chi tiết),
    // sẽ nhận thông tin khi người dùng nhập trên dialog (trong trường hợp này là name và password của album vừa tạo)
    @Override
    public void applyNamePassword(String name, String password) {
        Album album = new Album();       //tạo object album mới
        album.setName(name);             //set tên của album bằng tên mà người dùng nhập ở dialog
        album.setPassword(password);     //set password của album bằng password mà người dùng nhập ở dialog

        AlbumLab.get(this).addAlbum(album);   //gọi hàm add từ AlbumLab để thêm album mới tạo này vào database
        Toast.makeText(this, "Create album successful!", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(AlbumActivity.this, ImageAlbumActivity.class);   //sau khi tạo thành công, sẽ chuyển hướng cho người dùng sang trang xem những bức ảnh trong album
        i.putExtra("name", name);                                                      //khi chuyển sang trang xem ảnh, sẽ chuyển theo 2 dữ liệu đó là uuid (ID) và name (tên) của album đo
        i.putExtra("uuid", album.getId().toString());
        startActivity(i);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////   View holder
    private class AlbumHolder extends RecyclerView.ViewHolder {            //ViewHolder chứa từng item Album ảnh
        private TextView mNameAlbumTextView;           //Textview này dùng để hiển thị tên của album
        private Album mAlbum;

        public AlbumHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_album, parent, false));
            mNameAlbumTextView = (TextView) itemView.findViewById(R.id.name_album);
        }

        public void bind(Album album) {
            mAlbum = album;
            mNameAlbumTextView.setText(mAlbum.getName());
        }
    }

    /////////////////////////////////////////////////  Adapter
    private class AlbumAdapter extends RecyclerView.Adapter<AlbumHolder> {     //Adapter danh sách album
        private List<Album> mAlbums;

        public AlbumAdapter(List<Album> albums) {
            mAlbums = albums;
        }

        @NonNull
        @Override
        public AlbumHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
            return new AlbumHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull AlbumHolder holder, int position) {
            Album album = mAlbums.get(position);
            holder.bind(album);
            holder.itemView.setOnClickListener(new View.OnClickListener() {   //khi kích vào 1 album bất kỳ, nếu mật khẩu bằng rỗng thì sẽ vào thẳng album, nếu mật khẩu khác rỗng, thì sẽ bắt buộc người dùng phải nhập
                @Override
                public void onClick(View v) {
                    if (mAlbums.get(holder.getAdapterPosition()).getPassword().equals("")) {
                        Intent i = new Intent(AlbumActivity.this, ImageAlbumActivity.class);
                        i.putExtra("uuid", mAlbums.get(holder.getAdapterPosition()).getId().toString());
                        i.putExtra("name", mAlbums.get(holder.getAdapterPosition()).getName());
                        startActivity(i);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());      //quá trình tạo dialog để người dùng nhập mật khẩu khi muốn xem album
                        View dialogView = LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.enter_password_dialog, null);
                        EditText mPassword = (EditText) dialogView.findViewById(R.id.check_pasword);


                        builder.setView(dialogView).setTitle("Enter password").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String pass = mPassword.getText().toString();
                                if (pass == null || pass.isEmpty()) {            //nếu người dùng không nhập gì mà nhấn OK, sẽ thông báo "Please fill..."
                                    Toast.makeText(getApplicationContext(), "Please fill password field!", Toast.LENGTH_SHORT).show();
                                }
                                if (mAlbums.get(holder.getAdapterPosition()).getPassword().equals(pass)) {  //nếu người dùng nhập đúng, thì chuyển hướng, gói theo hai dữ liệu là uuid và name (như đã nói ở trên)
                                    Intent i = new Intent(AlbumActivity.this, ImageAlbumActivity.class);
                                    i.putExtra("uuid", mAlbums.get(holder.getAdapterPosition()).getId().toString());
                                    i.putExtra("name", mAlbums.get(holder.getAdapterPosition()).getName());
                                    startActivity(i);
                                } else {          //nếu người dùng nhập sai thì thông báo "Wrong password!"
                                    Toast.makeText(getApplicationContext(), "Wrong password!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builder.setCancelable(true);
                        builder.show();     //hàm này dùng để hiển thị dialog lên màn hình
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            if (mAlbums != null)
                return mAlbums.size();
            return 0;
        }

        public void setAlbums(List<Album> albums) {
            mAlbums = albums;
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////
}
