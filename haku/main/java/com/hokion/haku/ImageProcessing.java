package com.hokion.haku;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by NGo on 2016/04/06.
 * For scaling purposes
 */
public class ImageProcessing {

    public static Bitmap scaleImage(Context context, int resource, final int requiredSize) {
        Bitmap bm = BitmapFactory.decodeResource(context.getResources(), resource);
        return scaleImage(bm,requiredSize);
    }

    public static Bitmap scaleImage(Drawable drawable, final int requiredSize) {
        Bitmap bm;
/*
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }
*/
        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bm = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bm = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bm);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return scaleImage(bm,requiredSize);
    }

    public static Bitmap scaleImage(Context context, Uri uri, final int requiredSize) {
        try {
            InputStream imageStream = context.getContentResolver().openInputStream(uri);
            Bitmap bm = BitmapFactory.decodeStream(imageStream);
            return scaleImage(bm,requiredSize);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap scaleImage(Bitmap bm, final int requiredSize) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) requiredSize) / width;
        float scaleHeight = ((float) requiredSize) / height;
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }
}
