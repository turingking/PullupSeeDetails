package com.turingking.demo;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * Created by king on 16-4-22.
 */
public class SlidingDetailsLayout extends ViewGroup {
    private final static String TAG = SlidingDetailsLayout.class.getSimpleName();
    /**
     * 用于完成滚动操作的实例
     */
    private Scroller mScroller;



    /**
     * 手机按下时的屏幕坐标
     */
    private float mYDown;

    /**
     * 手机当时所处的屏幕坐标
     */
    private float mYMove;

    /**
     * 上次触发ACTION_MOVE事件时的屏幕坐标
     */
    private float mYLastMove;



    //顶部的view 底部的view
    private View topView,bottomView;
    private View promptView;

    //当前所在的索引
    private int position = 0;

    private TopBottomListener topListener,bottomListener;

    private PositionChangListener positionChangListener;

    /*
    * 监听view是否到了顶部或者底部
    * */
    public interface TopBottomListener{
        public boolean isScrollTop();//已经到了顶部
        public boolean isScrollBottom();//已经到了底部

        public void scrollToTop();//滚动到顶部
        public void scrollToBottom();//滚动到底部
    }

    public interface PositionChangListener{
        public void position(int positon);
        public void onBottom();
        public void backBottom();

        public void onTop();
        public void backTop();
    }


    public SlidingDetailsLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 第一步，创建Scroller的实例
        mScroller = new Scroller(context);
    }

    public void setPositionChangListener(PositionChangListener positionChangListener) {
        this.positionChangListener = positionChangListener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(topView == null){
            //获取 三个view 并计算 promptView 的宽高
            topView = getChildAt(0);
            promptView = getChildAt(1);
            bottomView = getChildAt(2);

            measureChild(topView, widthMeasureSpec, heightMeasureSpec);
            measureChild(promptView, widthMeasureSpec, heightMeasureSpec);
            measureChild(bottomView, widthMeasureSpec, heightMeasureSpec);

            //topView 和 bottomView 必须继承 TopBottomListener 接口
            if(topView instanceof TopBottomListener){
                topListener = (TopBottomListener) topView;
            }
            if(bottomView instanceof TopBottomListener){
                bottomListener = (TopBottomListener) bottomView;
            }


        }


    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            //布局 topView和bottomView宽高都是充满父view
            topView.layout(0,0,getMeasuredWidth(),getMeasuredHeight());
            promptView.layout(0,getMeasuredHeight(),promptView.getMeasuredWidth(),getMeasuredHeight()+promptView.getMeasuredHeight());
            bottomView.layout(0,getMeasuredHeight()+promptView.getMeasuredHeight(),getMeasuredWidth(),2*getMeasuredHeight()+promptView.getMeasuredHeight());

        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mYDown = ev.getRawY();
                mYLastMove = mYDown;
                break;
            case MotionEvent.ACTION_MOVE:

                float mYMove = (mYLastMove - ev.getRawY());
                //根据当前position 判断是在顶部view还是底部view


                if(position == 0){//顶部view

                    if(topListener == null){//如果并不是可以内容滚动的view 则直接滑动
                        return true;
                    }

                    if(topListener.isScrollBottom() && mYMove > 0){//topView 已经移动到了底部
                        //Log.e(TAG,"top view is bottom");
                        return true;
                    }
                }else{//底部view

                    if(bottomListener == null){
                        return true;
                    }

                    if(bottomListener.isScrollTop() && mYMove < 0){//bottomView 已经移动到了顶部
                        //Log.e(TAG,"bottom view is top");
                        return true;
                    }
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    private boolean top,bottom;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //如果子view 没有可点击 也能触发
                return true;

            case MotionEvent.ACTION_MOVE:
                mYMove = event.getRawY();
                int scrolledY= (int) (mYLastMove - mYMove);

                //中间view 状态改变
                if(position == 0){
                    if(getScrollY() + getHeight() > getHeight() + promptView.getHeight()){
                        if(positionChangListener!=null){
                            if(!bottom){
                                positionChangListener.onBottom();
                                bottom = true;
                            }

                        }
                    }else{
                        if(positionChangListener!=null){
                            if(bottom){
                                positionChangListener.backBottom();
                                bottom = false;
                            }

                        }
                    }
                }else{

                    if(getScrollY() < getHeight()){
                        if(positionChangListener!=null){
                            if(!top){
                                positionChangListener.onTop();
                                top = true;
                            }

                        }

                    }else{

                        if(positionChangListener!=null){
                            if(top){
                                positionChangListener.backTop();
                                top = false;
                            }
                        }

                    }

                }

                if (getScrollY() + scrolledY < topView.getTop()) {
                    scrollTo(0, topView.getTop());
                    return true;
                } else if (getScrollY() + getHeight() + scrolledY > bottomView.getBottom()) {
                    scrollTo(0,bottomView.getBottom() - getHeight() );
                    return true;
                }
                scrollBy(0,scrolledY);
                mYLastMove = mYMove;
                break;
            case MotionEvent.ACTION_UP:
                // 当手指抬起时，根据当前的滚动值来判定应该滚动到哪个子控件的界面
                int targetIndex;

                if(position == 0){
                    targetIndex = getScrollY() + getHeight() > getHeight() + promptView.getHeight() ? 1 : 0;

                }else{
                    targetIndex = getScrollY() < getHeight() ? 0 : 1;

                }





                position = targetIndex;

                int dy;
                if(targetIndex == 0){
                    dy = targetIndex * getHeight() - getScrollY();
                }else{
                    dy = targetIndex * getHeight() + promptView.getHeight() - getScrollY();
                }


                // 第二步，调用startScroll()方法来初始化滚动数据并刷新界面
                mScroller.startScroll(0, getScrollY(), 0, dy);
                invalidate();

                if(positionChangListener!=null){
                    positionChangListener.position(position);
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        // 第三步，重写computeScroll()方法，并在其内部完成平滑滚动的逻辑
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }





    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

}
