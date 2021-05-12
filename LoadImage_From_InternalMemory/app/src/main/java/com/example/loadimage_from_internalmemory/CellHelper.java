package com.example.loadimage_from_internalmemory;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;



// methods is created from internet
//this class decrease image size in order to take hight performence.
public class CellHelper {

    public static int calculateImagesize(BitmapFactory.Options options, int reqWidth, int reqHeight){
        final int height = options.outHeight;
        final int weight = options.outWidth;
        int inSamplesize = 1;
        if(height > reqHeight || weight > reqHeight){
            final int halfHeight = height/2;
            final int halfeWeight = weight/2;
            while ((halfHeight / inSamplesize) >= reqHeight && (halfeWeight / inSamplesize)>= reqWidth){
                inSamplesize = 2;
            }

        }
        return inSamplesize;
    }

    //decode image form bitmap from path

    public static Bitmap decodeImgFrompath(String pathname, int reqWidth, int reqHeight){
        final  BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathname);
        options.inSampleSize = calculateImagesize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(pathname, options);
    }
}
