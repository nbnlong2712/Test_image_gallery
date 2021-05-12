package com.longtraidep.imagegallery.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.longtraidep.imagegallery.GlideApp.GlideApp;
import com.longtraidep.imagegallery.Object.Image;
import com.longtraidep.imagegallery.R;

import java.util.List;

public class SlideImageAdapter extends PagerAdapter {
    private Context mContext;
    private List<Image> mImageList;

    public SlideImageAdapter(Context context, List<Image> imageList) {
        mContext = context;
        mImageList = imageList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_slide_image, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.image_slide);

        Image image = mImageList.get(position);
        if(image != null)
            GlideApp.with(mContext).load(image.getPath()).into(imageView);

        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        if(mImageList != null)
            return mImageList.size();
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
