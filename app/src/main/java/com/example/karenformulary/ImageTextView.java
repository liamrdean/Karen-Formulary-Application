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
    /* TODO
     * Dynamic heights
     * Fix the sharing issue
     * Actually center please thanks :)
     * Figure out more about OnDraw such as determining the canvas size and such
     */

    private Context mContext;
    private boolean isImage = false;
    private String data = "a";
    private Paint textPaint;
    private float textHeight = 100;
    private Bitmap bitmap;
    // Total height and width of this view (is in pixels).
    // NOTE: if < 0: is treated like null
    private int viewHeight = -1;
    private int viewWidth = -1;


    private static int count = 0; // DEBUG
    public ImageTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        count++;
        Log.i("DEMOCount", "Creating a new ImageTxtview number " + count);

        mContext = context;
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ImageTextView,
                0, 0);

        try {
            isImage = a.getBoolean(R.styleable.ImageTextView_isImage, false);
            String s = a.getString(R.styleable.ImageTextView_data);
            data = s;
//            Log.i("DEMO", "pre data");
//            this.setData(s);
//            Log.i("DEMO", "Post data");
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

    public void setData(String newData){
        this.data = newData;
        calcMeasure();
    }

    public void setIsImage(boolean b){
        this.isImage = b;
    }

    public void setTextHeight(float height) {
        textHeight = height;
        textPaint.setTextSize(textHeight);
    }

    // Returns if x is in [min, max]
    private boolean inRange(int min, int x, int max) {
        return x >= min && x <= max;
    }

    // Pass colors in as 0-255 returns true if successful
    public boolean setTextColor(int a, int r, int g, int b) {
        // Ensure that it is in the range
        if (!(inRange(0, a, 255) && inRange(0, r, 255) && inRange(0, g, 255) && inRange(0, b, 255))) {
            return false;
        }
        textPaint.setColor(Color.argb(a, r, g, b));
        return true;
    }


    private void drawText(Canvas canvas, String text) {
//        Log.i("DEMO", "Drawing with" + text + " in as " + isImage );
        if (text == null || text.isEmpty()) {
            text = "[[[[[[[[[";
        }
        String s = text;
//        Log.i("DEMO", "Drawing" + s + canvas.getClipBounds().left);
        canvas.drawText(
                s,
                0,
                s.length(),
                0,
                textHeight,
                textPaint);
    }

    private void drawImage(Canvas canvas, String text) {
//        Log.i("DEMO", "Drawing with" + text + " in as " + isImage );

        bitmap =  BitmapFactory.decodeResource(mContext.getResources(), R.drawable.placeholder);
        canvas.drawBitmap(bitmap, null, new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()), null);
    }

    @Override
    protected void onDraw (Canvas canvas) {
        super.onDraw(canvas);

        Log.i("DEMOCount", "drawing");

        if (data != null && data.charAt(0) == '$') {
            isImage = true;
        }

        if (isImage) {
            drawImage(canvas, data);
        } else {
            drawText(canvas, data);
        }
    }


    private void calcMeasure() {
        if (isImage && bitmap == null) {
            return;
        }


        if (this.viewWidth < 0) {
            if (isImage) {
                viewWidth = bitmap.getWidth();
            } else {
                Rect textBounds = new Rect(0,0,0,0);
                textPaint.getTextBounds(data, 0, data.length(), textBounds);


                Log.i("DEMOme", textBounds.toString());
                viewWidth = textBounds.right  - textBounds.left;
            }
        }

        if (this.viewHeight < 0) {
            if (isImage) {
                viewHeight = bitmap.getHeight();
            } else {
                // Since we want at least text height, ceiling the value
                viewHeight = (int) Math.ceil(textHeight);
            }
        }




        // Do the scaling calculation

        if (isImage) {
            // Simply set the height to the height of the bitmap
            float ratio = bitmap.getWidth() / this.viewWidth;
            this.viewHeight = bitmap.getHeight();
        } else {

        }
    }

/*
    // This measures the contents so that it can be read properly
    @Override
    protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec) {
        // MeasureSpec is View.MeasureSpec
        Log.i("DEMOme", MeasureSpec.toString(widthMeasureSpec) + " " + MeasureSpec.toString(heightMeasureSpec));

        calcMeasure();

        int newWidth = MeasureSpec.getSize(widthMeasureSpec);
        int newHeight = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        // If unspecified leave as is. AKA: don't care.
        switch (widthMode) {
            case MeasureSpec.AT_MOST:
                // Run the exact calcs given but only if
                viewWidth = Math.min(newWidth, viewWidth);
                break;
            case MeasureSpec.EXACTLY:

                //*
                int oldWidth = viewWidth;
                viewWidth = newWidth;
                // if nothing we can do to maintain ratio.
                switch (heightMode) {
                    case MeasureSpec.EXACTLY:
                        break;
                }

                viewHeight = (int) Math.ceil(oldWidth * newHeight / (double)(newWidth));
                viewWidth = newWidth;
                break;
            case MeasureSpec.UNSPECIFIED:
                break; // Just leave it, the appropriate value has been calculated
        }

        switch (heightMode) {
            case MeasureSpec.AT_MOST:
                // Run the exact calcs given but only if
                viewHeight = Math.min(newHeight, viewHeight);
                break;
            case MeasureSpec.EXACTLY:
                viewHeight = newHeight;
                break;
            case MeasureSpec.UNSPECIFIED:
                break; // Just leave it, the appropriate value has been calculated
        }


        //*
        // Handle when parent wants an exact width &| height
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY) {
            viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        }
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) {
            viewHeight = MeasureSpec.getSize(heightMeasureSpec);
        }
        /

        Log.i("DEMOme", "Setting to " + viewWidth + " x " + viewHeight );

        // Must do or get an error
        setMeasuredDimension(viewWidth, viewHeight);
    }
    */

}
