package com.example.layoutphotogallery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Cho_ban extends AppCompatActivity {

    Button button_anh;
    Button button_cho_ban;
    Button button_album;
    Button button_nguoi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cho_ban);

        button_anh = (Button) findViewById(R.id.buttonAnh);
        button_cho_ban = (Button) findViewById(R.id.buttonChoBan);
        button_album = (Button) findViewById(R.id.buttonAlbum);
        button_nguoi = (Button) findViewById(R.id.buttonNguoi);

        button_anh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Cho_ban.this, Anh.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        button_nguoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Cho_ban.this, Nguoi.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        button_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Cho_ban.this, Album.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }
}