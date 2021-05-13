package com.longtraidep.imagegallery.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.longtraidep.imagegallery.Adapter.ViewPagerAdapter;
import com.longtraidep.imagegallery.PreferenceUtils;
import com.longtraidep.imagegallery.R;
import com.sa90.materialarcmenu.ArcMenu;

public class GalleryActivity extends AppCompatActivity {
    private BottomNavigationView mBottomNavigationView;
    private ViewPager mViewPager;
    private RelativeLayout mRelativeLayout;
    private ArcMenu mArcMenu;
    private FloatingActionButton mOpenCameraButton, mChangeLanguageButton, mChangeThemeButton, mSlideShowButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        mRelativeLayout = (RelativeLayout) findViewById(R.id.gallery_layout);
        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mArcMenu = (ArcMenu) findViewById(R.id.arc_menu_button);
        mOpenCameraButton = (FloatingActionButton) findViewById(R.id.open_camera_button);
        mChangeLanguageButton = (FloatingActionButton) findViewById(R.id.change_language_button);
        mChangeThemeButton = (FloatingActionButton) findViewById(R.id.change_theme_button);
        mSlideShowButton = (FloatingActionButton) findViewById(R.id.slide_show_button);

        mOpenCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GalleryActivity.this, CameraActivity.class);
                startActivity(i);
            }
        });
        mSlideShowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GalleryActivity.this, SlideShowActivity.class);
                startActivity(i);
            }
        });

        if(PreferenceUtils.getTheme(getApplicationContext()).equals("light")) {
            PreferenceUtils.saveTheme("light", getApplicationContext());
            mRelativeLayout.setBackgroundColor(Color.WHITE);
            mBottomNavigationView.setBackgroundColor(Color.WHITE);
        }else {
            PreferenceUtils.saveTheme("dark", getApplicationContext());
            mRelativeLayout.setBackgroundColor(Color.BLACK);
            mBottomNavigationView.setBackgroundColor(Color.BLACK);
        }
        mChangeThemeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PreferenceUtils.getTheme(getApplicationContext()).equals("dark"))
                {
                    PreferenceUtils.saveTheme("light", getApplicationContext());
                    mRelativeLayout.setBackgroundColor(Color.WHITE);
                    mBottomNavigationView.setBackgroundColor(Color.WHITE);
                }
                else
                {
                    PreferenceUtils.saveTheme("dark", getApplicationContext());
                    mRelativeLayout.setBackgroundColor(Color.BLACK);
                    mBottomNavigationView.setBackgroundColor(Color.BLACK);
                }
            }
        });


        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mViewPager.setAdapter(adapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position)
                {
                    case 0:
                        mBottomNavigationView.getMenu().findItem(R.id.navigation_image).setChecked(true);
                        break;
                    case 1:
                        mBottomNavigationView.getMenu().findItem(R.id.navigation_anh).setChecked(true);
                        break;
                    case 2:
                        mBottomNavigationView.getMenu().findItem(R.id.navigation_video).setChecked(true);
                        break;
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.navigation_image:
                        mViewPager.setCurrentItem(0);
                        break;
                    case R.id.navigation_anh:
                        mViewPager.setCurrentItem(1);
                        break;
                    case R.id.navigation_video:
                        mViewPager.setCurrentItem(2);
                        break;
                    case R.id.navigation_album:
                        Intent i = new Intent(GalleryActivity.this, AlbumActivity.class);
                        startActivity(i);
                        break;
                    case R.id.navigation_nguoi:
                        Intent in = new Intent(GalleryActivity.this, FavoriteActivity.class);
                        startActivity(in);
                        break;
                }
                return true;
            }
        });
    }
}
