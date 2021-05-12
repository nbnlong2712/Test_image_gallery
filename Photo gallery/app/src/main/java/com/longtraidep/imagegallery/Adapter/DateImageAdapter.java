package com.longtraidep.imagegallery.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.longtraidep.imagegallery.Object.DateImage;
import com.longtraidep.imagegallery.Object.Image;
import com.longtraidep.imagegallery.R;

import java.util.List;

public class DateImageAdapter extends RecyclerView.Adapter<DateImageAdapter.DateImageHolder> {
    private Activity mActivity;
    private List<DateImage> mDateImages;

    public DateImageAdapter(Activity activity)
    {
        mActivity = activity;
    }
    public void setData(List<DateImage> list)
    {
        mDateImages = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DateImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_date_image, parent, false);
        return new DateImageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DateImageHolder holder, int position) {
        DateImage dateImage = mDateImages.get(position);
        holder.mDateImage.setText(dateImage.getDate());
        setImageItemRecycler(holder.mRecyclerViewDateImage, mDateImages.get(position).getImages());
    }

    @Override
    public int getItemCount() {
        if(mDateImages!= null)
            return mDateImages.size();
        return 0;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////
    public class DateImageHolder extends RecyclerView.ViewHolder{
        private TextView mDateImage;
        private RecyclerView mRecyclerViewDateImage;

        public DateImageHolder(@NonNull View itemView) {
            super(itemView);
            mDateImage = (TextView) itemView.findViewById(R.id.date_image);
            mRecyclerViewDateImage = (RecyclerView) itemView.findViewById(R.id.recyclerview_date_image);
        }
    }
    private void setImageItemRecycler(RecyclerView recyclerView, List<Image> imageList)
    {
        ImageAdapter imageAdapter = new ImageAdapter(mActivity);
        imageAdapter.setData(imageList);
        recyclerView.setLayoutManager(new GridLayoutManager(mActivity,4));
        recyclerView.setAdapter(imageAdapter);
    }
}
