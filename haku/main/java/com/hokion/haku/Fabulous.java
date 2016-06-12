package com.hokion.haku;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageButton;

/**
 * Created by NGo on 2016/04/05.
 * Based on http://www.survivingwithandroid.com/2014/09/android-floating-action-button.html
 */
public class Fabulous extends ImageButton {

    private Context context;
    private int bgColor;
    private int bgColorPressed;

    public Fabulous(Context context) {
        super(context);
        this.context = context;
        init(null);
    }

    public Fabulous(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs);
    }

    public Fabulous(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs);
    }

    private void init(AttributeSet attrSet) {
        Resources.Theme theme = context.getTheme();
        TypedArray arr = theme.obtainStyledAttributes(attrSet, R.styleable.FAB, 0, 0);
        try {
            setBgColor(arr.getColor(R.styleable.FAB_bg_color, Color.BLUE));
            setBgColorPressed(arr.getColor(R.styleable.FAB_bg_color_pressed, Color.GRAY));

            StateListDrawable sld = new StateListDrawable();
            sld.addState(new int[] {android.R.attr.state_pressed}, createButton(bgColorPressed));
            sld.addState(new int[] {}, createButton(bgColor));
            setBackground(sld);
        }
        catch(Throwable t) {
            Log.e("catch", "TypedArray catch");
        }
        finally {
            arr.recycle();
        }
    }

    public void setBgColor(int color) {
        this.bgColor = color;
    }

    public void setBgColorPressed(int color) {
        this.bgColorPressed = color;
    }

    private Drawable createButton(int color) {
        OvalShape oShape = new OvalShape();
        ShapeDrawable sd = new ShapeDrawable(oShape);
        setWillNotDraw(false);
        sd.getPaint().setColor(color);

        ShapeDrawable sd1 = new ShapeDrawable(oShape);
        sd1.setShaderFactory(new ShapeDrawable.ShaderFactory() {
            @Override
            public Shader resize(int width, int height) {
                return new LinearGradient(0,0,0, height,
                        new int[] {
                                Color.WHITE,
                                Color.GRAY,
                                Color.DKGRAY,
                                Color.BLACK
                        }, null, Shader.TileMode.REPEAT);
            }
        });

        LayerDrawable ld = new LayerDrawable(new Drawable[] { sd1, sd });
        ld.setLayerInset(0, 5, 5, 0, 0);
        ld.setLayerInset(1, 0, 0, 5, 5);

        return ld;
    }
}
