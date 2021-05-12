package com.longtraidep.imagegallery.Fragment;

import android.Manifest;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.media.AsyncPlayer;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.os.BuildCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.longtraidep.imagegallery.Object.DateImage;
import com.longtraidep.imagegallery.Adapter.DateImageAdapter;
import com.longtraidep.imagegallery.Object.Image;
import com.longtraidep.imagegallery.R;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AnhFragment extends Fragment {
    private RecyclerView mRecyclerViewImage;
    private List<Image> mImageList;
    private List<DateImage> mDateImagesList;
    private DateImageAdapter mDateImageAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_anh, container, false);

        mRecyclerViewImage = (RecyclerView) v.findViewById(R.id.recyclerview_image);
        mDateImageAdapter = new DateImageAdapter(getActivity());

        mRecyclerViewImage.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        mDateImagesList = new ArrayList<>();

        checkPermission();

        return v;
    }

    @Override
    public void onResume() {
        mRecyclerViewImage.setAdapter(mDateImageAdapter);
        super.onResume();
    }

    private void checkPermission() {
        PermissionListener permissionListener = new PermissionListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onPermissionGranted() {
                try {
                    getAllImagesFromGallery();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
            }
        };
        TedPermission.with(getActivity())
                .setPermissionListener(permissionListener)
                .setDeniedMessage("If you reject permission,you can not use this service\\n\\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_MEDIA_LOCATION)
                .check();
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void getAllImagesFromGallery() throws IOException {
        mDateImagesList = new ArrayList<>();
        mImageList = new ArrayList<>();
        Uri uri;
        Cursor cursor = null;

        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA,
                MediaStore.MediaColumns.TITLE,
                MediaStore.Images.Media.SIZE
        };

        final String orderBy = MediaStore.Video.Media.DATE_TAKEN;
        cursor = getActivity().getApplicationContext().getContentResolver()
                .query(uri, projection, null, null, orderBy + " DESC");

        int idColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID);
        int latColumn = BuildCompat.isAtLeastQ() ? -1 : cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.LATITUDE);
        int longColumn = BuildCompat.isAtLeastQ() ? -1 : cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.LONGITUDE);

        //Get list of image from gallery
        while (cursor.moveToNext()) {
            Uri photoUri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cursor.getString(idColumn));
            uri = MediaStore.setRequireOriginal(photoUri);

            float[] latLong = new float[2];
            if (BuildCompat.isAtLeastQ()) {
                photoUri = MediaStore.setRequireOriginal(photoUri);
                InputStream stream = getActivity().getApplicationContext().getContentResolver().openInputStream(photoUri);
                if (stream == null) {
                    continue;
                }
                ExifInterface exifInterface = new ExifInterface(stream);
                boolean returnedLatLong = exifInterface.getLatLong(latLong);
                stream.close();
            } else {
                latLong = new float[]{
                        cursor.getFloat(latColumn),
                        cursor.getFloat(longColumn)
                };
            }

            String id = cursor.getString(0);
            String path = cursor.getString(1);
            long size = cursor.getLong(3) / 1000;
            size = (long) (Math.round((double) size * 1000) / 1000);

            long datet = 0;
            File file = new File(path);
            if (file.exists()) {
                datet = file.lastModified();
            }
            Date datett = new Date(datet);
            String datettt = new SimpleDateFormat("dd-MM-yyyy").format(datett);


            Image image = new Image(id, path, datett, latLong[0], latLong[1], getCompleteAddressString(latLong[0], latLong[1]), size);
            mImageList.add(image);
        }
        List<String> test = new ArrayList<>();

        for (int i = 0; i < mImageList.size(); i++) {
            String date = new SimpleDateFormat("dd-MM-yyyy").format(mImageList.get(i).getDate());
            test.add(date);
        }
        List<String> test1 = new ArrayList<String>();
        for (int i = 0; i < test.size(); i++) {
            if (!test1.contains(test.get(i))) {
                test1.add(test.get(i));
            }
        }
        for (String element : test1) {
            DateImage dateImage = new DateImage(element, null);
            mDateImagesList.add(dateImage);
        }
        for (int i = 0; i < mDateImagesList.size(); i++) {
            List<Image> imageListt = new ArrayList<>();
            for (int s = 0; s < mImageList.size(); s++) {
                String date = new SimpleDateFormat("dd-MM-yyyy").format(mImageList.get(s).getDate());
                if (mDateImagesList.get(i).getDate().equals(date)) {
                    imageListt.add(mImageList.get(s));
                }
            }
            mDateImagesList.get(i).setImages(imageListt);
        }
        cursor.close();
        mDateImageAdapter.setData(mDateImagesList);
        mRecyclerViewImage.setAdapter(mDateImageAdapter);
    }

    private String getCompleteAddressString(float LATITUDE, float LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
            } else {
                Log.w("LOCATION", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("LOCATION", "Canont get Address!");
        }
        return strAdd;
    }
}
