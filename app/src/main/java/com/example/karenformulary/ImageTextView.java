package com.example.karenformulary;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;

public class ImageTextView extends View {

    private Context mContext;
    private boolean showText = false;
    private boolean isImage = false;
    private String data;
    private Paint textPaint;
    private float textHeight = 100;

    public ImageTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ImageTextView,
                0, 0);

        try {
            showText = a.getBoolean(R.styleable.ImageTextView_showText, false);
            isImage = a.getBoolean(R.styleable.ImageTextView_isImage, false);
            data = a.getString(R.styleable.ImageTextView_data);
        } finally {
            a.recycle();
        }

        InitPaints();
    }

    // Do this once instead of every draw which is very slow
    private void InitPaints() {
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.argb(255, 255, 0, 0));
        if (textHeight == 0) {
            textHeight = textPaint.getTextSize();
        } else {
            textPaint.setTextSize(textHeight);
        }
    }


    private void drawText(Canvas canvas, String text) {
        String s = "Testing textဃ";
        canvas.drawText(s, 0, s.length(), 0, textHeight, textPaint);
    }

    private void drawImage(Canvas canvas, String path) {
        Bitmap bitmap =  BitmapFactory.decodeResource(mContext.getResources(), R.drawable.placeholder);
        canvas.drawBitmap(bitmap, null, new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()), null);
        Log.i("DEMO", "Drawing with canvas h=" + canvas.getHeight());
    }

    @Override
    protected void onDraw (Canvas canvas) {
        if (isImage) {
            drawImage(canvas, data);
        } else {
            drawText(canvas, data);
        }
    }


}
