package com.ideahunters.customwidgets.crystalpreloaders;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.ideahunters.R;


/**
 * Created by owais.ali on 7/18/2016.
 */
public class CrystalPreloader extends View {

    ////////////////////////////////////////
    // PUBLIC CLASS CONSTANTS
    ////////////////////////////////////////

    public static final class Size {
        public static final int VERY_SMALL = 0;
        public static final int SMALL = 1;
        public static final int MEDIUM = 2;
        public static final int LARGE = 3;
        public static final int EXTRA_LARGE = 4;
    }
    ////////////////////////////////////////
    // PRIVATE VAR
    ////////////////////////////////////////

    private BasePreloader loader;
    private Paint fgPaint;
    private Paint bgPaint;
    private int fgColor;
    private int bgColor;
    private int size;
    private int style;

    ////////////////////////////////////////
    // CONSTRUCTOR
    ////////////////////////////////////////

    public CrystalPreloader(Context context) {
        this(context, null);
    }

    public CrystalPreloader(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CrystalPreloader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // prevent render is in edit mode
        //if(isInEditMode()) return;

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CrystalPreloader);
        try {
            fgColor = getFgColor(array);
            bgColor = getBgColor(array);
            size = getSize(array);
        } finally {
            array.recycle();
        }

        // initialize
        init();
    }

    ////////////////////////////////////////
    // PRIVATE METHODS
    ////////////////////////////////////////

    private void init() {
        fgPaint = new Paint();
        fgPaint.setAntiAlias(true);
        fgPaint.setColor(getFgColor());
        fgPaint.setStyle(Paint.Style.FILL);
        fgPaint.setDither(true);

        bgPaint = new Paint();
        bgPaint.setAntiAlias(true);
        bgPaint.setColor(getBgColor());
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setDither(true);
        loader = new BallScale(this, getSize());
    }

    ////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////

    public final int getFgColor() {
        return this.fgColor;
    }

    public final int getBgColor() {
        return this.bgColor;
    }

    public final int getSize() {
        return this.size;
    }

    public final int getStyle() {
        return this.style;
    }

    ////////////////////////////////////////
    // PROTECTED METHODS
    ////////////////////////////////////////

    protected int getFgColor(final TypedArray typedArray) {
        return typedArray.getColor(R.styleable.CrystalPreloader_crs_pl_fg_color, Color.RED);
    }

    protected int getBgColor(final TypedArray typedArray) {
        return typedArray.getColor(R.styleable.CrystalPreloader_crs_pl_bg_color, Color.BLACK);
    }

    protected int getSize(final TypedArray typedArray) {
        return typedArray.getInt(R.styleable.CrystalPreloader_crs_pl_size, Size.SMALL);
    }

    ////////////////////////////////////////
    // OVERRIDE METHODS
    ////////////////////////////////////////


    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(loader.getWidth(), loader.getHeight());
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        loader.onDraw(canvas, fgPaint, bgPaint, loader.getWidth(), loader.getHeight(), loader.getWidth() / 2, loader.getHeight() / 2);
    }
}
