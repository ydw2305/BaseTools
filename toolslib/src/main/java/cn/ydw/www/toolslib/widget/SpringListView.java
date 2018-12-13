/*******************************************************************************
 * <p>Copyright(c) 2017</p>
 * <p>
 * @autor 杨德望
 * @date  ${DATE}
 * @see ${PACKAGE_NAME}
 * </p>
 *
 ******************************************************************************/

package cn.ydw.www.toolslib.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * @author 杨德望
 * @date 2017/9/22.
 *
 * 弹簧效果的listview
 */
public class SpringListView extends ListView {
    private int mMaxYOverscrollDistance;

    public SpringListView(Context context) {
        super(context);
    }
    public SpringListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public SpringListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @TargetApi (Build.VERSION_CODES.LOLLIPOP)
    public SpringListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private float lastY = 0;
    private boolean isTouch = false, isBackScroll = false, isTop = true;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastY = ev.getY();
                isTouch = true;
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isBackScroll && !isTop){
                    isTouch = true;
                    mMaxYOverscrollDistance = Math.abs ((int)((ev.getY() - lastY)/2));
                }
                break;
            case MotionEvent.ACTION_UP:
                isTouch = false;
                isBackScroll = false;
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        Log.e("位置", "情况" + l +" + "+ t +" + "+oldl +" + "+oldt);
        isTop = t<0;
        if (isTouch){
            if (Math.abs(t) - Math.abs(oldt) < 0){
                mMaxYOverscrollDistance = 0;
                isBackScroll = true;
            }
        }
    }
    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY,
                                   int scrollRangeX, int scrollRangeY, int maxOverScrollX,
                                   int maxOverScrollY, boolean isTouchEvent) {
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX,
                scrollRangeY, maxOverScrollX, mMaxYOverscrollDistance, isTouchEvent);
    }
}
