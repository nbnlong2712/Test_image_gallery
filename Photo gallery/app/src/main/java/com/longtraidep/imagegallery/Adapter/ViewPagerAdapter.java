package com.longtraidep.imagegallery.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.longtraidep.imagegallery.Fragment.AnhFragment;
import com.longtraidep.imagegallery.Fragment.ImageFragment;
import com.longtraidep.imagegallery.Fragment.VideoFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return new ImageFragment();
            case 1:
                return new AnhFragment();
            case 2:
                return new VideoFragment();
            default: return new AnhFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
