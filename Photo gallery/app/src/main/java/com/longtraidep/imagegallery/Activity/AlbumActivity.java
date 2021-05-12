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

public class AlbumActivity extends AppCompatActivity implements CreateAlbumDialog.CreateAlbumDialogListener {
    private static final String ARG_ALBUM_ID = "album_id";
    private RecyclerView mRecyclerView;
    private AlbumAdapter mAdapter;
    private FloatingActionButton mAddAlbumButton;
    private FloatingActionButton mBackBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_album);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_album);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        mAddAlbumButton = (FloatingActionButton) findViewById(R.id.add_new_album);
        mAddAlbumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateAlbumDialog();
            }
        });

        mBackBtn = (FloatingActionButton) findViewById(R.id.back_btn);
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AlbumActivity.this, CameraActivity.class);
                startActivity(i);
            }
        });

        updateUI();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

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

    @Override
    public void applyNamePassword(String name, String password) {
        Album album = new Album();
        album.setName(name);
        album.setPassword(password);
        AlbumLab.get(this).addAlbum(album);
        Toast.makeText(this, "Create album successful!", Toast.LENGTH_SHORT).show();
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////   View holder
    private class AlbumHolder extends RecyclerView.ViewHolder //implements View.OnClickListener
    {
        private Dialog mDialog;
        private TextView mNameAlbumTextView;
        private Album mAlbum;

        public AlbumHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_album, parent, false));
            //itemView.setOnClickListener(this);

            mNameAlbumTextView = (TextView) itemView.findViewById(R.id.name_album);
        }

       /*@Override
        public void onClick(View v) {
            openCheckPasswordDialog();
        }

        public void openCheckPasswordDialog()
        {
            mDialog = new Dialog(getApplicationContext());
            mDialog.setContentView(R.layout.enter_password_dialog);
            EditText mPassword = (EditText) mDialog.findViewById(R.id.check_pasword);
            Button mOKBtn = (Button) mDialog.findViewById(R.id.button_ok);
            Button mCancelBtn = (Button) mDialog.findViewById(R.id.button_cancel);

            mCancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                }
            });

            mOKBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String password = mPassword.getText().toString();
                    if(password == null || password.isEmpty())
                    {
                        Toast.makeText(getApplicationContext(), "Please fill password field!", Toast.LENGTH_SHORT).show();
                    }
                    if(mAlbum.getPassword().equals(password))
                    {
                        Toast.makeText(getApplicationContext(), "Password Correct!", Toast.LENGTH_SHORT).show();
                    }
                    else Toast.makeText(getApplicationContext(), "Wrong password!", Toast.LENGTH_SHORT).show();
                }
            });
            mDialog.show();
        }*/

        public void bind(Album album) {
            mAlbum = album;
            mNameAlbumTextView.setText(mAlbum.getName());
        }
    }

    /////////////////////////////////////////////////  Adapter
    private class AlbumAdapter extends RecyclerView.Adapter<AlbumHolder> {
        private List<Album> mAlbums;
        private Dialog mDialog;

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
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                    View dialogView = LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.enter_password_dialog, null);
                    EditText mPassword = (EditText) dialogView.findViewById(R.id.check_pasword);


                    builder.setView(dialogView).setTitle("Enter password").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String pass = mPassword.getText().toString();
                            if (pass == null || pass.isEmpty()) {
                                Toast.makeText(getApplicationContext(), "Please fill password field!", Toast.LENGTH_SHORT).show();
                            }
                            if (mAlbums.get(holder.getAdapterPosition()).getPassword().equals(pass)) {
                                Intent i = new Intent(AlbumActivity.this, CameraActivity.class);
                                startActivity(i);
                            } else {
                                Toast.makeText(getApplicationContext(), "Sai con me may roi!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.setCancelable(true);
                    builder.show();
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
