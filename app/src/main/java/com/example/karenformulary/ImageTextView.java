package com.example.karenformulary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ImageTextView extends LinearLayout {

    private Context myContext;
    private TextView textView;

    // The text/img path data
    private String data_EN;
    private String data_KA;

    // Return a list of bitmaps in the drug directory
    public static List<Bitmap> getImageBitmaps() {
        try {

            StringBuilder filePathBuilder = new StringBuilder("Drug_Images/");

            // Get the drug name
            String friendlyPath = ActivityDrugInfoPage.drugName;
            String badChars = "\\/:*?\"<>|";
            for (int i = 0; i < badChars.length(); i++) {
                friendlyPath = friendlyPath.replace(badChars.charAt(i), '_');
            }
            friendlyPath.replace("./", "_/");

            // Construct the drug image paths
            filePathBuilder.append(friendlyPath);
            String folderPath = filePathBuilder.toString();
            String[] a = ActivityMain.assetManager.list(folderPath);

            if (a == null || a.length == 0) {
                Log.w("IMGS", "Nothing in the folder " + folderPath);
                return null;
            }

            // For each file, try making it a bitmap, if that fails, don't make, if works, add
            // to bitmaps
            List<Bitmap> bitmaps = new ArrayList<Bitmap>();
            InputStream inStream = null;
            for (String s : a) {
                Log.i("IMGS", "Trying img " + folderPath + "/" + s);

                inStream = ActivityMain.assetManager.open(folderPath + "/" + s);
                if (inStream != null) {
                    Bitmap bitmap = BitmapFactory.decodeStream(inStream);
                    if (bitmap != null) {
                        Log.i("IMGS", "Adding img " + s);
                        bitmaps.add(bitmap);
                    }
                }
            }

            return bitmaps;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap getImageBitmap(String path) {
        InputStream inStream = null;
        Bitmap bitmap;
        try {


            StringBuilder filePathBuilder = new StringBuilder("Drug_Images/");
            // Add drug name
            // TODO Make the paths be friendly to all OS's
            String friendlyPath = ActivityDrugInfoPage.drugName;
            String badChars = "\\/:*?\"<>|";
            for (int i = 0; i < badChars.length(); i++) {
                friendlyPath = friendlyPath.replace(badChars.charAt(i), '_');
            }
            friendlyPath.replace("./", "_/");
            filePathBuilder.append(friendlyPath);
            filePathBuilder.append("/");


            // Add data
            filePathBuilder.append(path);

            // Make it be the picture
            filePathBuilder.append(".png");

            Log.i("TESTimg", "opening = '" + filePathBuilder.toString());

            inStream = ActivityMain.assetManager.open(filePathBuilder.toString());
            Log.i("TESTimg", "path = '" + filePathBuilder.toString() + "' ?= " + Boolean.toString(inStream != null));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        bitmap = BitmapFactory.decodeStream(inStream);
        return bitmap;
    }

  public ImageTextView(Context context) {
        this(context, null);
    }

    public ImageTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        myContext = context;
        createTextView();
    }

    private void createTextView() {
        textView = new TextView(myContext);
        textView.setLayoutParams(generateDefaultLayoutParams());
        this.addView(textView);
    }

    public void setText(String text) {
        if (ActivityMain.isKaren) {

        }
        //setData(text);
    }

    // Call on a language update
    public void onLanguageUpdate() {
        if (textView != null) {
            String data = getData();
            textView.setText(data);
        }
    }

    public void setData(String newData_EN, String newData_KA) {
        data_EN = newData_EN;
        data_KA = newData_KA;
        onLanguageUpdate();
    }

    public String getData() {
        return getData(ActivityMain.isKaren);
    }

    public String getData(boolean isKaren) {
        return (isKaren) ? data_KA : data_EN;
    }

    public void setTextSize(float size) {
        textView.setTextSize(size);
    }

    /*
     * =============================================================================================
     * =============================================================================================
     *
     * BE WARNED!
     * Beyond this lies Lovecraftian graphical complications! Do not change! Unless you know what
     * your doing!
     * Break this, and the program WILL CRASH.
     *
     * =============================================================================================
     * =============================================================================================
     */

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();

        int maxHeight = 0;
        int maxWidth = 0;

        // Find out how big everyone wants to be
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        // Find rightmost and bottom-most child
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                int childRight;
                int childBottom;

                LinearLayout.LayoutParams lp
                        = (LinearLayout.LayoutParams) child.getLayoutParams();

                childRight = child.getMeasuredWidth();
                childBottom = child.getMeasuredHeight();

                maxWidth = Math.max(maxWidth, childRight);
                maxHeight = Math.max(maxHeight, childBottom);
            }
        }

        // Check against minimum height and width
        maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());

        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, 0),
                resolveSizeAndState(maxHeight, heightMeasureSpec, 0));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        layoutVertical(l, t, r, b);
    }
    private int mGravity = Gravity.START | Gravity.TOP;

    void layoutVertical(int left, int top, int right, int bottom) {
        final int paddingLeft = 0;

        int childTop;
        int childLeft;

        // Where right end of child should go
        final int width = right - left;
        int childRight = width;

        // Space available for child
        int childSpace = width;

        final int count = getVirtualChildCount();

        final int majorGravity = mGravity & Gravity.VERTICAL_GRAVITY_MASK;
        final int minorGravity = mGravity & Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK;

        switch (majorGravity) {
            case Gravity.BOTTOM:
                // mTotalLength contains the padding already
                childTop = bottom - top;
                break;

            // mTotalLength contains the padding already
            case Gravity.CENTER_VERTICAL:
                childTop = (bottom - top) / 2;
                break;

            case Gravity.TOP:
            default:
                childTop = 0;
                break;
        }

        for (int i = 0; i < count; i++) {
            final View child = getVirtualChildAt(i);
            if (child == null) {
                childTop += measureNullChild(i);
            } else if (child.getVisibility() != GONE) {
                final int childWidth = child.getMeasuredWidth();
                final int childHeight = child.getMeasuredHeight();

                final LinearLayout.LayoutParams lp =
                        (LinearLayout.LayoutParams) child.getLayoutParams();

                int gravity = lp.gravity;
                if (gravity < 0) {
                    gravity = minorGravity;
                }
                final int layoutDirection = getLayoutDirection();
                final int absoluteGravity = Gravity.getAbsoluteGravity(gravity, layoutDirection);
                switch (absoluteGravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
                    case Gravity.CENTER_HORIZONTAL:
                        childLeft = paddingLeft + ((childSpace - childWidth) / 2)
                                + lp.leftMargin - lp.rightMargin;
                        break;

                    case Gravity.RIGHT:
                        childLeft = childRight - childWidth - lp.rightMargin;
                        break;

                    case Gravity.LEFT:
                    default:
                        childLeft = paddingLeft + lp.leftMargin;
                        break;
                }

                /*
                if (LinearLayout.hasDividerBeforeChildAt(i)) {
                    childTop += mDividerHeight;
                }
                */

                //*
                childTop += lp.topMargin;
                setChildFrame(child, childLeft, childTop + getLocationOffset(child),
                        childWidth, childHeight);
                childTop += childHeight + lp.bottomMargin + getNextLocationOffset(child);

                i += getChildrenSkipCount(child, i);
                //*/
            }
        }
    }

    /**
     * <p>Returns the view at the specified index. This method can be overridden
     * to take into account virtual children. Refer to
     * {@link android.widget.TableLayout} and {@link android.widget.TableRow}
     * for an example.</p>
     *
     * @param index the child's index
     * @return the child at the specified index, may be {@code null}
     */
    View getVirtualChildAt(int index) {
        return getChildAt(index);
    }

    /**
     * <p>Returns the virtual number of children. This number might be different
     * than the actual number of children if the layout can hold virtual
     * children. Refer to
     * {@link android.widget.TableLayout} and {@link android.widget.TableRow}
     * for an example.</p>
     *
     * @return the virtual number of children
     */
    int getVirtualChildCount() {
        return getChildCount();
    }

    int measureNullChild(int childIndex) {
        return 0;
    }

    private void setChildFrame(View child, int left, int top, int width, int height) {
        child.layout(left, top, left + width, top + height);
    }

    int getLocationOffset(View child) {
        return 0;
    }

    int getNextLocationOffset(View child) {
        return 0;
    }

    int getChildrenSkipCount(View child, int index) {
        return 0;
    }
}
