package com.example.karenformulary;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ImageTextView extends LinearLayout {

    private Context myContext;
    private TextView textView;

    // The text/img path data
    private String data;

    public ImageTextView(Context context) {
        this(context, null);
    }

    public ImageTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        myContext = context;
        createTextView();
    }

    public void setText(String text) {
        setData(text);
    }
    int j = 0;

    public void setData(String newData) {
        data = newData;
        if (textView != null) {
            textView.setText(data);
        }
    }

    public String getData() {
        return data;
    }

    public void setTextSize(float size) {
        textView.setTextSize(size);
    }

    private void createTextView() {
        textView = new TextView(myContext);
        textView.setLayoutParams(generateDefaultLayoutParams());
        this.addView(textView);
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



    public static final String bee = "abc defghijklm nopqrstuvxyz 0123 456789ABCDEFG HIJKMNLOPQRSTUVWXYZ!@#$%^&*()\n\tline 1\n\tline2\n\tline3";



}
