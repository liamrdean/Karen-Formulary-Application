package com.example.karenformulary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class ImageTextView extends View {
    /* TODO
     * Dynamic heights
     * Actually center please thanks :)
     * Figure out more about OnDraw such as determining the canvas size and such
     */

    private Context mContext;
    private boolean isImage = false;
    private int viewHeight = -1;
    private int viewWidth = -1;
    // This makes a draw call call calculatedMeasure if it has not been called already
    // thus preventing bad drawings (out of bounds)
    private boolean calculatedMeasure = false;

    // Text related variables
    private String data = "a";
    private Paint textPaint;
    private float textHeight = 100;
    private Rect textBounds = new Rect(0,0,0,0);

    // Things for image
    private Bitmap bitmap;
    // Total height and width of this view (is in pixels).
    // NOTE: if < 0: is treated like null



    private static int count = 0; // DEBUG
    private boolean tempIsImageTest() {
        return tempIsImageTest(this.data);
    }
    private static boolean tempIsImageTest(String data) {
        return data != null && data.charAt(0) == '$';
    }

    // If this should be an image, init it
    private void maybeInitImage() {
        if (!(isImage = tempIsImageTest(data))){
            Log.i("DEMO", "Not image " + data);
            return;
        }

        if (isImage) {
            Log.w("DEMO", "Creating bitmap");

            // TEMP
            // VERY TEMPORARY IMAGE LOADING I DON'T WANT TO BUILD A FULL IMAGE LOADER
            int res = R.drawable.placeholder;
            if (data.length() > 1) {
                switch(data.charAt(1)) {
                    case '.':
                        res = R.drawable.psmall;
                        break;
                    case ',':
                        res = R.drawable.pmedium;
                }
            }
            bitmap =  BitmapFactory.decodeResource(mContext.getResources(), res);

        }
    }

    public ImageTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        count++;
        Log.i("DEMOC", "Creating a new ImageTxtview number " + count);

        mContext = context;
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ImageTextView,
                0, 0);

        try {
            isImage = a.getBoolean(R.styleable.ImageTextView_isImage, false);
            String s = a.getString(R.styleable.ImageTextView_data);
            data = s;


            maybeInitImage();
//            Log.i("DEMO", "pre data");
//            this.setData(s);
//            Log.i("DEMO", "Post data");
        } finally {
            a.recycle();
        }


        InitPaints();
    }

    // Basically redraws. Basically.
    /* Must be called whenever data changes */
    private void callRedraw() {
        invalidate();
        requestLayout();
    }

    private void InitData() {
        viewHeight = -1;
        viewWidth = -1;
        calculatedMeasure = false;
        maybeInitImage();
        calculateMeasureAndRedraw();
    }

    // Do this once instead of every draw which is very slow
    private void InitPaints() {
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.argb(255, 255, 0, 0));
        if (textHeight <  1) {
            textHeight = textPaint.getTextSize();
        }
        textPaint.setTextSize(textHeight);
    }

    public void setData(String newData){
        this.data = newData;
        this.InitData();
    }

    public void setTextHeight(float height) {
        textHeight = height;
        calculateMeasureAndRedraw();
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
        Log.i("DEMODRAW", "Drawing" + s + canvas.getClipBounds());
        canvas.drawText(
                s,
                -textBounds.left,
                -textBounds.top,
                textPaint);
    }

    private void drawImage(Canvas canvas, String text) {
//        Log.i("DEMO", "Drawing with" + text + " in as " + isImage );

        Log.i("DEMODRAW", "Drawing" + canvas.getClipBounds());
        if (bitmap == null) {
            Log.w("DEMO", "Called draw image while bitmap is null");
            return;
        }

        int bmWidth = bitmap.getWidth();
        int bmHeight = bitmap.getHeight();

        Rect dst = new Rect(0, 0, bmWidth, bmHeight);

        if (viewWidth < bmWidth) {
            dst = new Rect(0, 0, viewWidth, viewHeight);
        }

        // If the image is smaller, center it
        if (viewWidth > bmWidth) {
            Log.w("DEMODK", viewWidth + " " + viewHeight + " " + bmWidth + " " + bmHeight);
            int xOff = (int) Math.ceil((viewWidth - bmWidth) / 2.0);
            int yOff = (int) Math.ceil((viewHeight - bmHeight) / 2.0);
            dst.left = xOff;
            dst.right += xOff;

        }

        Log.i("DEMODK", dst.toString());
        // Since src is null, draws entire bitmap :)
        canvas.drawBitmap(bitmap, null, dst, null);
    }

    @Override
    protected void onDraw (Canvas canvas) {
        super.onDraw(canvas); // Aparently draws the background

        if(!calculatedMeasure) {
            calculateMeasureAndRedraw();
        }


        Log.i("DEMO_DRAW", "drawing" + canvas.getClipBounds().toString());

        if (tempIsImageTest()) {
            isImage = true;
        }

        if (isImage) {
            drawImage(canvas, data);
        } else {
            drawText(canvas, data);
        }
    }


    private void calculateMeasureAndRedraw() {
        calculateMeasure();
        callRedraw();
    }


    private void calculateMeasure() {
        if (isImage && bitmap == null) {
            return;
        }
        if (!isImage && textPaint == null) {
            InitPaints();
        }

        if (!isImage) {
            /*
            if (data == null) {
                data = "Testing|";
            }*/
            textPaint.getTextBounds(data, 0, data.length(), textBounds);
        }

        if (this.viewWidth < 0) {
            if (isImage) {
                viewWidth = bitmap.getWidth();
            } else {
                //                Log.i("DEMOme", textBounds.toString());
                viewWidth = textBounds.right  - textBounds.left;
            }
        }

        if (this.viewHeight < 0) {
            if (isImage) {
                viewHeight = bitmap.getHeight();
            } else {
                // Since we want at least text height, ceiling the value
                // Bottom - top since decreases going down
                Log.i("DEMO", textBounds.toString());
                viewHeight = textBounds.bottom - textBounds.top;
            }
        }

        /*
        // Do the scaling calculation
        if (isImage) {
            // Simply set the height to the height of the bitmap
            float ratio = bitmap.getWidth() / this.viewWidth;
            this.viewHeight = (int) Math.ceil(bitmap.getHeight() * ratio);
        } else {
        }
        //*/

        calculatedMeasure = true;
    }

    // This is a helper debug function. Does what it says on the tin
    private String getNameOfMeasureSpecMode(int mode) {
        switch(mode) {
            case MeasureSpec.EXACTLY:
                return "EXACTLY";
            case MeasureSpec.AT_MOST:
                return "AT_MOST";
            case MeasureSpec.UNSPECIFIED:
                return "UNSPECIFIED";
            default:
                return "ERROR: UNKNOWN mode = " + mode;
        }
    }


    // This measures the contents so that it can be read properly
    @Override
    protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec) {
        // MeasureSpec is View.MeasureSpec
        Log.i("DEMOme", MeasureSpec.toString(widthMeasureSpec) + " " + MeasureSpec.toString(heightMeasureSpec));
        if(isImage){Log.i("DEMOmeme", MeasureSpec.toString(widthMeasureSpec) + " " + MeasureSpec.toString(heightMeasureSpec));}

        calculateMeasure();
        Log.i("DEMOmei", "calc " + String.format("%d wide by %d tall", viewWidth, viewHeight));

        int newWidth = MeasureSpec.getSize(widthMeasureSpec);
        int newHeight = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        // Rest of code assumes (widthMode = EXACTLY && heightMode == UNSPECIFIED) is true
        // so check that it is
        if (widthMode != MeasureSpec.EXACTLY) {
            Log.w("ImgTxtView", "ImageTextView.onMeasure: expected width mode EXACTLY got "
                    + getNameOfMeasureSpecMode(widthMode));
            return;
        }
        if (heightMode != MeasureSpec.UNSPECIFIED) {
            Log.w("ImgTxtView", "ImageTextView.onMeasure: expected height mode Unspecified got "
                    + getNameOfMeasureSpecMode(widthMode));
            return;
        }

        // Scale height to maintain ratio with width, which will change to a known amount
        if (isImage) {
           // viewHeight = (int) Math.ceil((double) (viewHeight * newWidth) / (double) viewWidth);
        }
        viewWidth = newWidth;


        // Must do or get an error
        Log.i("DEMOmei", "Ended up with " + String.format("%d wide by %d tall", viewWidth, viewHeight));

        setMeasuredDimension(viewWidth, viewHeight);
        callRedraw();
    }

}


/* This is old code from OnMeasure, I put enough thought into it I don't want to throw it away */
//        // If unspecified leave as is. AKA: don't care.
//        switch (widthMode) {
//            case MeasureSpec.AT_MOST:
//                // Run the exact calcs given but only if
//                viewWidth = Math.min(newWidth, viewWidth);
//                break;
//            case MeasureSpec.EXACTLY:
//
//                //*
//                int oldWidth = viewWidth;
//                viewWidth = newWidth;
//                // if nothing we can do to maintain ratio.
//                switch (heightMode) {
//                    case MeasureSpec.EXACTLY:
//                        break;
//                }
//
//                viewHeight = (int) Math.ceil(oldWidth * newHeight / (double)(newWidth));
//                viewWidth = newWidth;
//                break;
//            case MeasureSpec.UNSPECIFIED:
//                break; // Just leave it, the appropriate value has been calculated
//        }
//
//        switch (heightMode) {
//            case MeasureSpec.AT_MOST:
//                // Run the exact calcs given but only if
//                viewHeight = Math.min(newHeight, viewHeight);
//                break;
//            case MeasureSpec.EXACTLY:
//                viewHeight = newHeight;
//                break;
//            case MeasureSpec.UNSPECIFIED:
//                break; // Just leave it, the appropriate value has been calculated
//        }
//
//
//        //*
//        // Handle when parent wants an exact width &| height
//        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY) {
//            viewWidth = MeasureSpec.getSize(widthMeasureSpec);
//        }
//        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) {
//            viewHeight = MeasureSpec.getSize(heightMeasureSpec);
//        }
//
//        Log.i("DEMOme", "Setting to " + viewWidth + " x " + viewHeight );

