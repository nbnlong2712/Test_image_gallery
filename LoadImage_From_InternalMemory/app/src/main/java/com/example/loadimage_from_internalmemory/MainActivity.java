package com.example.loadimage_from_internalmemory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<Cell> Allfilepaths;
    OutputStream outputStream;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //stograge permision
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M  && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        }
        else {

            ShowImage();
        }
    }


    public  void ShowImage(){

        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pictures";
        Allfilepaths = new ArrayList<>();
        Allfilepaths = listAllfiles(path);

        if(Allfilepaths.size() == 0){
            Toast.makeText(this, path, Toast.LENGTH_LONG).show();
        }
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.grallery);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
        recyclerView.setLayoutManager(layoutManager);
        ArrayList<Cell> cells = prepareData();
        CellAdapter cellAdapter = new CellAdapter(getApplicationContext(), cells);
        recyclerView.setAdapter(cellAdapter);
    }

    public void onRequestPermissionReusult(int requestCode, String[] permission, int[]grantResults){
        if (requestCode == 1000){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                ShowImage();
            }
            else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }


    private ArrayList<Cell> prepareData(){
        ArrayList<Cell> AllImages = new ArrayList<>();
        for (Cell c : Allfilepaths){
            Cell cell = new Cell();
            cell.setTitle(c.getTitle());
            cell.setPath(c.getPath());
            AllImages.add(cell);
        }
        return AllImages;
    }

    // load all file
    private List<Cell> listAllfiles(String pathname) {
        List<Cell> allfiles = new ArrayList<>();
        File file = new File(pathname);
        File[] files = file.listFiles();
        if (files != null) {
            for (File f : files) {
                Cell cell = new Cell();
                cell.setTitle(f.getName());
                cell.setPath(f.getAbsolutePath());
                allfiles.add(cell);
            }
        }
        return allfiles;
    }

}