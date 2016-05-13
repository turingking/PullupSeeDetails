package com.turingking.demo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

/**
 * Created by king on 16-4-22.
 */
public class MyScrollView extends ScrollView implements SlidingDetailsLayout.TopBottomListener {

    private boolean top,bottom;

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        top = false;
        bottom = false;
    }


    @Override
    public boolean isScrollTop() {
        return top;
    }

    @Override
    public boolean isScrollBottom() {
        return bottom;
    }

    @Override
    public void scrollToTop() {
        fullScroll(View.FOCUS_UP);
    }

    @Override
    public void scrollToBottom() {
        fullScroll(View.FOCUS_DOWN);
    }



    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed,l,t,r,b);


        if(getChildAt(0).getHeight()<getHeight()){
            top = true;
            bottom = true;
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        int scrollY = getScrollY();

        top = scrollY == 0;

        View view = getChildAt(getChildCount()-1);

        // Calculate the scrolldiff
        int diff = (view.getBottom()-(getHeight()+getScrollY()));

        // if diff is zero, then the bottom has been reached
        bottom = diff == 0;


        //Log.e(" tag ","top = "+top +" bottom = "+bottom);

    }
}
