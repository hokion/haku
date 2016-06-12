package com.hokion.haku;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.view.Gravity;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.Random;

import static android.view.LayoutInflater.*;

public class DrawerService extends Service {

    protected static WindowManager windowManager;
    protected static WindowManager.LayoutParams params;
    private static Fabulous chatHead;
    private static Context context;
    private boolean headsStatus;
    private Fabulous subhead[];
    private int subheadCount;

    private int statusBarHeight;
    private int navigationBarHeight;

    private int xAdd;
    private int yAdd;
    private Point point;

    final Handler handler = new Handler();
    final int long_delay = 15000;
    final int short_delay = 750;
    private int count;
    final int ITERATION = 30;
    final int PIXEL_SKIP = 10;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("InflateParams")
    @Override
    public void onCreate() {
        super.onCreate();

        context = this;
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        point = new Point();
        windowManager.getDefaultDisplay().getRealSize(point);

        chatHead = (Fabulous) from(getApplicationContext()).inflate(R.layout.fabby, null);
        //TODO check if there is a saved icon
        chatHead.setImageBitmap(ImageProcessing.scaleImage(this, R.drawable.ic_launcher, 144));
        chatHead.setOnTouchListener(new Touchy(this, true));
        chatHead.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

        // status bar height
        statusBarHeight = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }

        // navigation bar height
        navigationBarHeight = 0;
        resourceId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            navigationBarHeight = getResources().getDimensionPixelSize(resourceId);
        }

        processOrientation();

        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.START | Gravity.TOP;
        params.x = point.x/2 - chatHead.getMeasuredWidth()/2;
        params.y = point.y/2 - chatHead.getMeasuredHeight()/2;
        windowManager.addView(chatHead, params);

        headsStatus = false;
        count = ITERATION;
        handler.postDelayed(thread, long_delay);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        processOrientation();
        //chatHead.setOnTouchListener(new Touchy());
        params.x = point.x / 2 - chatHead.getMeasuredWidth() / 2;
        params.y = point.y / 2 - chatHead.getMeasuredHeight() / 2;
        windowManager.updateViewLayout(chatHead, params);
        count = ITERATION;
    }

    public int[] processOrientation() {
        int maxDim[] = new int[2]; //min,max
        int rotation = windowManager.getDefaultDisplay().getRotation();
        windowManager.getDefaultDisplay().getRealSize(point);

        if (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) {
            maxDim[0] = point.x - navigationBarHeight;
            maxDim[1] = point.y;
        } else {
            maxDim[0] = point.x;
            maxDim[1] = point.y-navigationBarHeight;
        }
        return maxDim;
    }

    final Runnable thread = new Runnable() {
        public void run() {
            if (getHeadsStatus()){
                closeHeads();
            } else {
                if (count == ITERATION) {
                    count = 0;
                    int angle = new Random().nextInt(360);
                    xAdd = (int) (PIXEL_SKIP * Math.cos(Math.toRadians(angle)));
                    yAdd = -(int) (PIXEL_SKIP * Math.sin(Math.toRadians(angle)));
                }
                params.x += xAdd;
                params.y += yAdd;

                int maxDim[] = processOrientation();
                if (params.x + chatHead.getWidth() >= maxDim[0] || params.x < 0) {
                    params.x -= (2 * xAdd);
                    xAdd = -xAdd;
                }
                if (params.y + chatHead.getHeight() >= maxDim[1] || params.y < statusBarHeight) {
                    params.y -= (2 * yAdd);
                    yAdd = -yAdd;
                }
                count++;

                windowManager.updateViewLayout(chatHead, params);
            }
            if (count == ITERATION) {
                handler.postDelayed(this, long_delay);
            } else {
                handler.postDelayed(this, short_delay);
            }
        }
    };

    public void hideHead() {
        windowManager.removeView(chatHead);
    }

    public void stopThread() {
        handler.removeCallbacks(thread);
    }

    public void startThread() {
        count = ITERATION;
        handler.postDelayed(thread, long_delay);
    }

    public boolean getHeadsStatus() {
        return headsStatus;
    }

    public void showHeads() {
        final int fixedSpace = 10;
        headsStatus = true;
        subheadCount = countHeads();
        // TODO show heads implementation.
        // get shared preference; if head is assigned, do task

        if (subheadCount == 0) {
            //TODO make main head do something
            return;
        }

        subhead = new Fabulous[subheadCount];
        WindowManager.LayoutParams subhead_manager[] = new WindowManager.LayoutParams[subheadCount];
        for (int cnt = 0; cnt < subheadCount; cnt++ ) {
            subhead[cnt] = (Fabulous) from(getApplicationContext()).inflate(R.layout.fabby, null);
            subhead[cnt].setImageBitmap(ImageProcessing.scaleImage(this, R.drawable.ic_launcher, 144));
            subhead[cnt].setOnTouchListener(new Touchy(this, false));
            subhead[cnt].measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            subhead[cnt].setBgColor(R.color.colorRed);

            subhead_manager[cnt] = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                            WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
                    PixelFormat.TRANSLUCENT);
            subhead_manager[cnt].gravity = Gravity.START | Gravity.TOP;
            subhead_manager[cnt].x = (int)(params.x - Math.sin(Math.toRadians(360 * cnt / subheadCount)) *
                    (chatHead.getMeasuredHeight() + subhead[cnt].getMeasuredHeight() + fixedSpace));
            subhead_manager[cnt].y = (int)(params.y - Math.cos(Math.toRadians(360 * cnt / subheadCount)) *
                    (chatHead.getMeasuredWidth() + subhead[cnt].getMeasuredWidth() + fixedSpace));

            windowManager.addView(subhead[cnt], subhead_manager[cnt]);
        }

    }

    public void closeHeads() {
        headsStatus = false;
        for (int cnt = 0; cnt < subheadCount; cnt++ ) {
            windowManager.removeView(subhead[cnt]);
        }
    }

    public static void changeIcon(Uri image) {
        chatHead.setImageBitmap(ImageProcessing.scaleImage(context, image, 144));
    }

    private int countHeads(){
        //TODO get sharedPref, then count how many are checked
        return 0;
    }

    public static void subHeadAction() {
        //Get ListHandler to do stuff
        context.startActivity(context.getPackageManager().getLaunchIntentForPackage(packages.get(selInput).processName));
    }
}