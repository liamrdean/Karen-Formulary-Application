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

import java.io.IOException;
import java.io.InputStream;


public class ImageTextView extends View {
    /* TODO
     * Dynamic heights
     * Actually center please thanks :)
     * Figure out more about OnDraw such as determining the canvas size and such
     */

    private Context mContext;
    private boolean isImage = false;
    private Dim measureDimension = new Dim(-1, -1);
    private Dim drawDimension = new Dim(-1, -1);
    private Dim maxDimensions = new Dim(-1, -1);
    private Dim minDimensions = new Dim(-1, -1);


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
        return data != null;// && data.charAt(0) == '$';
    }

    // If this should be an image, init it, else set bitmap to null
    private void maybeInitImage() {
        if (!isImage) {
            bitmap = null;
            return;
        }
        Log.w("TESTimg", "Creating bitmap with data = \"" + this.data + "\"");


        InputStream inStream = null;


//        bitmap = BitmapFactory.decodeStream(inStream);

        // TEMP
        // VERY TEMPORARY IMAGE LOADING I DON'T WANT TO BUILD A FULL IMAGE LOADER
        /*
        Log.i("kljlkj", this.data);
        int res = R.drawable.placeholder;
        switch(data.charAt(0)) {
            case '.':
                res = R.drawable.psmall;
                break;
            case ',':
                res = R.drawable.pmedium;
                break;
            case '1':
                // Load the CSV headers

                break;
            default:
                res = R.drawable.placeholder;
        }


        if (data.charAt(0) != '1') {
            bitmap = BitmapFactory.decodeResource(mContext.getResources(), res);
        } else if (inStream != null) {
        }
        */

        if (data.charAt(0) == '.') {
            bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.placeholder);
            return;
        }

        try {
            Log.i("TESTimg", "Trying " + data);

            StringBuilder filePathBuilder = new StringBuilder("Drug_Images/");
            // Add drug name
            filePathBuilder.append("Dihydrogen Monoxide/");

            // Add data
            filePathBuilder.append(data);

            // Grab the picture
            filePathBuilder.append(".png");

            Log.i("TESTimg", "opening = '" + filePathBuilder.toString());

            inStream = MainActivity.assetManager.open(filePathBuilder.toString());
            Log.i("TESTimg", "path = '" + filePathBuilder.toString() + "' ?= " + Boolean.toString(inStream != null));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        bitmap = BitmapFactory.decodeStream(inStream);
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
        drawDimension.set(-1, -1);
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

    public void setIsImage(boolean b) {
        this.isImage = b;
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
        if (text == null || text.isEmpty()) {
            text = "ERROR: NULL TEXT IN ImageTextView.java.drawText";
        }
        String s = text;
        Log.i("DEMOdimDrT", "@ " + -textBounds.left + " " + -textBounds.top);
        Log.i("DEMOdimDrT", "Canvas height " + canvas.getHeight());
        canvas.drawText(
                s,
                -textBounds.left,
                -textBounds.top,
                textPaint);
    }

    private void drawImage(Canvas canvas, String text) {
        if (bitmap == null) {
            Log.w("DEMO", "Called draw image while bitmap is null");
            return;
        }
        /*
        int bmWidth = bitmap.getWidth();
        int bmHeight = bitmap.getHeight();

        Rect origdst = new Rect(0, 0, bmWidth, bmHeight);


        if (measureDimension.getWidthInt() < bmWidth) {
            origdst = new Rect(0, 0, measureDimension.getWidthInt(), measureDimension.getHeightInt());
        }

        // If the image is smaller, center it
        if (measureDimension.getWidthInt() > bmWidth) {
            Log.w("DEMODK", measureDimension.getWidthInt() + " " + measureDimension.getHeightInt() + " " + bmWidth + " " + bmHeight);
            int xOff = (int) Math.ceil((measureDimension.getWidthInt() - bmWidth) / 2.0);
            int yOff = (int) Math.ceil((measureDimension.getHeightInt() - bmHeight) / 2.0);
            origdst.left = xOff;
            origdst.right += xOff;

        }
        */

        Dim imageOffset = measureDimension.getOtherInCenter(drawDimension);
        Rect dst = Dim.toRect(imageOffset, drawDimension);


        Log.i("DEMODKrect", "got " + dst.toString()  + " from offset " + imageOffset.toString() + " size " + drawDimension);
        // Since src is null, draws entire bitmap
       // canvas.drawBitmap(bitmap, null, origdst, null);
        canvas.drawBitmap(bitmap, null, dst, null);
    }

    @Override
    protected void onDraw (Canvas canvas) {
        super.onDraw(canvas); // Apparently draws the background

        if(!calculatedMeasure) {
            calculateMeasureAndRedraw();
        }

        /*
        if (tempIsImageTest()) {
            isImage = true;
        }
        */

        if (isImage) {
            drawImage(canvas, data);
        } else {
            drawText(canvas, data);
        }
    }


    private void calculateMeasureAndRedraw() {
        calculateMeasure(-1, -1);
        callRedraw();
    }


    // Ignores width and height if they are negative
    private void calculateMeasure(int targetWidth, int targetHeight) {
        if (isImage && bitmap == null) {
            return;
        }
        if (!isImage && textPaint == null) {
            InitPaints();
        }

        if (!isImage) {
            textPaint.getTextBounds(data, 0, data.length(), textBounds);
        }
        Log.i("DEMOdimMe", textBounds.toString());

        Log.i("DEMOdimMe", "drawDimWInt = " + this.drawDimension.getWInt() + " drawDimHInt = " + this.drawDimension.getHInt());

        if (this.drawDimension.getWidthInt() < 0) {
            if (isImage) {
                drawDimension.setWidth(bitmap.getWidth());
            } else {
                //                Log.i("DEMOme", textBounds.toString());
                drawDimension.setWidth(textBounds.right  - textBounds.left);
            }
        }

        if (this.drawDimension.getHeight() < 0) {
            if (isImage) {
                drawDimension.setHeight(bitmap.getHeight());
            } else {
                // Since we want at least text height, ceiling the value
                // Bottom - top since decreases going down
                Log.i("DEMOdimA", "Setting width to " + textBounds.bottom + "-" + textBounds.top + "=" + (textBounds.bottom - textBounds.top));
                drawDimension.setHeight(textBounds.bottom - textBounds.top);
            }
        }

        if (isImage) {
            Dim imageDim = new Dim(bitmap.getWidth(), bitmap.getHeight());
            imageDim.shrinkWidthTo(measureDimension);
            drawDimension.set(imageDim);
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


        int newWidth = MeasureSpec.getSize(widthMeasureSpec);
        int newHeight = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        measureDimension.set(newWidth, 999999);

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

        calculateMeasure(newWidth, -1);
        Log.i("DEMOmei", "calc " + String.format("%d wide by %d tall", drawDimension.getWidthInt(), drawDimension.getHeightInt()));
        // Scale height to maintain ratio with width, which will change to a known amount
        /*
        if (isImage) {
           // viewHeight = (int) Math.ceil((double) (viewHeight * newWidth) / (double) viewWidth);
        }
        viewWidth = newWidth;
        */

        Log.i("DEMOmei", "Ended up with " + String.format("%d wide by %d tall", drawDimension.getWidthInt(), drawDimension.getHeightInt()));

        // Must do this or get an error
        Log.i("DEMOdimMe", drawDimension.getWidthInt() + " " + drawDimension.getHeightInt());

        measureDimension.set(newWidth, drawDimension.getHeight());
        setMeasuredDimension(measureDimension.getWidthInt(), measureDimension.getHeightInt());
        callRedraw();
    }

    // Dimension class. Would use Rect but I don't like the implementation.
    private static class Dim {
        private double w;
        private double h;

        // Use -1 to specify that we don't care
        public Dim() {
            this.w = -1;
            this.h = -1;
        }

        public Dim(int width, int height) {
            this.w = (double) width;
            this.h = (double) height;
        }

        public Dim(double width, double height) {
            this.w = width;
            this.h = height;
        }

        public double getWidth() {return w;}
        public double getW() {return w;}
        public double getHeight() {return h;}
        public double getH() {return h;}
        // Get but return ints
        public int getWidthInt() {return (int) Math.ceil(this.w);}
        public int getWInt() {return (int) Math.ceil(this.w);}
        public int getHeightInt() {return (int) Math.ceil(this.h);}
        public int getHInt() {return (int) Math.ceil(this.h);}

        public void set(double width, double height) {this.w = width; this.h = height;}
        public void set(Dim other) {this.w = other.w; this.h = other.h;}
        public void setWidth(double width) {this.w = width;}
        public void setW(double w) {this.w = w;}
        public void setHeight(double height) {this.h = height;}
        public void setH(double h) {this.h = h;}

        @Override
        public String toString() {
            return String.format("(%f, %f)", w, h);
        }

        // Shrink selfs width to fit in the other dimension
        public void shrinkWidthTo(Dim other) {
            this.shrinkWidthTo(other.getWidth(), other.getHeight());
        }

        public void shrinkWidthTo(double otherWidth, double otherHeight) {
            if (this.w < otherWidth) {
                return;
            }

            String pString = "Start this=" + this.toString() + " other=" + (new Dim(otherWidth, otherHeight));

            //double ratioW = this.w / otherWidth;

            if (otherWidth != 0) {
                h *= otherWidth / w;
            }
            w = otherWidth;

            //h *= ratioW;
            //w *= ratioW;
            pString += " now=" + this.toString();
            Log.i("DEMODKdim", pString);
        }

        // Shrink selfs width to fit in the other dimension
        public void shrinkHeightTo(Dim other) {
            this.shrinkHeightTo(other.getWidth(), other.getHeight());
        }

        public void shrinkHeightTo(double otherWidth, double otherHeight) {
            if (otherHeight != 0) {
                w = otherWidth * h / otherHeight;
            }
            h = otherHeight;
        }

        public void shrinkTo(Dim other) {
            this.shrinkTo(other.getWidth(), other.getHeight());
        }

        public void shrinkTo(double otherWidth, double otherHeight) {
            double ratioW = this.w / otherWidth;
            double ratioH = this.h / otherHeight;

            w *= ratioW;
            h *= ratioH;

            /*
            if (otherWidth > 0) {
                w *= this.w / otherWidth;
            }
            if (w < 0) {
                w = otherWidth;
            }

            if (otherHeight > 0) {
                h *= this.h / otherHeight;
            }
            if (h < 0) {
                h = otherHeight;
            }
            */
        }

        // Compares width, on tie compares height, if that ties returns a. Else returns dim with larger dim
        public static Dim max(Dim a, Dim b) {
            if (a.w == b.w) {
                if (a.h >= b.h) {
                    return a;
                } else if (a.h < b.h) {
                    return b;
                }
            } else if (a.w > b.w) {
                return a;
            }
            // else a.w is less than b.w
            return b;
        }

        // Just does width comparisons for now
        public static Dim min(Dim a, Dim b) {
            if (a.w == b.w) {
                if (a.h <= b.h) {
                    return a;
                } else if (a.h < b.h) {
                    return b;
                }
            } else if (a.w < b.w) {
                return a;
            }
            // else a.w is less than b.w
            return b;
        }

        // Line up the centers, so that the center of other is the same as the center of this.
        // Returns the offset so that other is centered.
        public Dim getOtherInCenter(Dim other) {
            double width = (w - other.w) / 2.0;
            double height = (h - other.h) / 2.0;

            if (w < 0 || other.w < 0) {
                width = -1;
            }
            if (h < 0 || other.h < 0) {
                height = -1;
            }

            return new Dim(width, height);
        }

        public static Rect toRect(Dim offset, Dim size) {
            return new Rect(offset.getWidthInt(), offset.getHeightInt(),
                    offset.getWidthInt() + size.getWidthInt(), offset.getHeightInt() +size.getHeightInt());
        }
    }

    public void setMaxDimensions(double width, double height) {
        maxDimensions.set(width, height);
    }

    public void setMinDimensions(double width, double height) {
        minDimensions.set(width, height);
    }

    public void setDrawDimensions(double width, double height) {
        drawDimension.set(width, height);
    }

    public void setMeasureDimension(double width, double height) {
        measureDimension.set(width, height);
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

