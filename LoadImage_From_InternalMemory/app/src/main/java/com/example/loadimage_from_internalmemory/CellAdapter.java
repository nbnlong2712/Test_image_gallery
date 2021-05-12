package com.example.loadimage_from_internalmemory;


import android.content.Context;
import android.graphics.Bitmap;
import android.telephony.AccessNetworkConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class CellAdapter extends RecyclerView.Adapter<CellAdapter.ViewHolder> {
    private ArrayList<Cell> galerryList;
    private Context mcontext;

    public CellAdapter(Context mcontext,ArrayList<Cell> galerryList) {
        this.galerryList = galerryList;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public CellAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell, parent, false);
        return new CellAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        setImageFromPath(galerryList.get(position).getPath(), holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mcontext, galerryList.get(position).getTitle(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return galerryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView =  (ImageView)itemView.findViewById(R.id.image);
        }
    }

    private void setImageFromPath(String path, ImageView img){
        File imgfile = new File(path);
        if(imgfile.exists()){
            Bitmap myBitmap = CellHelper.decodeImgFrompath(imgfile.getAbsolutePath(), 200, 200);
            img.setImageBitmap(myBitmap);
        }
    }
}
