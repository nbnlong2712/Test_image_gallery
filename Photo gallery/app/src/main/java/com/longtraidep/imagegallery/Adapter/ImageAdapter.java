package com.longtraidep.imagegallery.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.longtraidep.imagegallery.Activity.FullScreenImageActivity;
import com.longtraidep.imagegallery.GlideApp.GlideApp;
import com.longtraidep.imagegallery.Object.Image;
import com.longtraidep.imagegallery.R;

import java.text.DateFormat;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageHolder> {
    private Activity mActivity;
    private List<Image> mImages;

    public ImageAdapter(Activity activity) {
        mActivity = activity;
    }

    public void setData(List<Image> list)
    {
        mImages = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ImageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageHolder holder, int position) {
        Image image = mImages.get(position);
        holder.bind(image);
        GlideApp.with(mActivity).load(image.getPath()).into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        if(mImages != null)
            return mImages.size();
        return 0;
    }
///////////////////////////////////////////////////
    public class ImageHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mImageView;
        private Image mImage;

        public ImageHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mImageView = (ImageView) itemView.findViewById(R.id.image_item);
        }
        public void bind(Image image)
        {
            mImage = image;
        }
        @Override
        public void onClick(View v) {
            Intent i = new Intent(mActivity, FullScreenImageActivity.class);

            i.putExtra("path", mImage.getPath());
            String strDate = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(mImage.getDate().getTime());
            i.putExtra("size", mImage.getSize()+"");
            i.putExtra("dateTime", strDate);
            i.putExtra("location", mImage.getLocation());
            mActivity.startActivity(i);
        }
    }
}
