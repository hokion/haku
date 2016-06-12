package com.hokion.haku;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.Calendar;

/**
 * Created by NGo on 2016/04/05.
 * Touch implementation.
 */
public class Touchy implements View.OnTouchListener {
    DrawerService context;
    private int initialX;
    private int initialY;
    private float initialTouchX;
    private float initialTouchY;
    private ImageView close;
    private WindowManager.LayoutParams param_close;
    private long startClickTime;
    private boolean isMain;

    public Touchy (Context base, boolean type) {
        context = (DrawerService) base;
        isMain = type;
        if (isMain) {
            close = new ImageView(base);
            close.setImageResource(R.drawable.ic_launcher);
        } else {
            close = null;
        }
        param_close = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        param_close.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final int MAX_CLICK_DURATION = 200;
        if (isMain) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    initialX = DrawerService.params.x;
                    initialY = DrawerService.params.y;
                    initialTouchX = event.getRawX();
                    initialTouchY = event.getRawY();
                    startClickTime = Calendar.getInstance().getTimeInMillis();
                    context.stopThread();
                    return true;
                case MotionEvent.ACTION_UP:
                    context.startThread();
                    long clickDuration = Calendar.getInstance().getTimeInMillis() - startClickTime;
                    if (clickDuration < MAX_CLICK_DURATION) {
                        if (context.getHeadsStatus()) {
                            context.closeHeads();
                        } else {
                            context.showHeads();
                        }
                    }
                    if (close.isShown()) {
                        if (isViewOverlapping(v)) {
                            context.hideHead();
                            context.stopThread();
                            context.stopSelf();
                        }
                        DrawerService.windowManager.removeView(close);
                    }
                    return true;
                case MotionEvent.ACTION_MOVE:
                    DrawerService.params.x = initialX + (int) (event.getRawX() - initialTouchX);
                    DrawerService.params.y = initialY + (int) (event.getRawY() - initialTouchY);
                    int maxDim[] = context.processOrientation();

                    DrawerService.params.x = DrawerService.params.x + v.getHeight() >= maxDim[0] ? maxDim[0] - v.getHeight() : DrawerService.params.x;
                    DrawerService.params.y = DrawerService.params.y + v.getHeight() >= maxDim[1] ? maxDim[1] - v.getHeight() : DrawerService.params.y;
                    DrawerService.windowManager.updateViewLayout(v, DrawerService.params);
                    if (!close.isShown()) {
                        DrawerService.windowManager.addView(close, param_close);
                    }
                    return true;
            }
        } else {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                // TODO imple subhead tap
                DrawerService.subHeadAction();
                return true;
            }
        }
        return false;
    }

    private boolean isViewOverlapping(View v) {

        int headRect[] = new int[2];
        int closeRect[] = new int[2];
        v.getLocationOnScreen(headRect);
        close.getLocationOnScreen(closeRect);

        headRect[0] += v.getWidth()/2;
        headRect[1] += v.getHeight()/2;

        return (headRect[0] <= closeRect[0] + close.getWidth() && headRect[0] >= closeRect[0])
                && (headRect[1] <= closeRect[1] + close.getHeight() && headRect[1] >= closeRect[1]);
    }
}