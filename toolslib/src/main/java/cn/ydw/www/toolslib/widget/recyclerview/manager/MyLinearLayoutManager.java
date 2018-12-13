package cn.ydw.www.toolslib.widget.recyclerview.manager;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * =====================================
 * 作    者: 杨德望
 * 版    本：${VERSION}.
 * 创建日期：2017/12/16.
 * 描    述：自定义的 LinearLayoutManager 管理器
 * =====================================
 */

public class MyLinearLayoutManager extends LinearLayoutManager {

    private boolean mScrollVerticalEnable = true; //是否可垂直滑动
    private boolean mScrollHorizontalEnable = true; //是否可水平滑动
    private double mSpeedRatio = 1d;//滑动速度

    /**
     * Creates a vertical LinearLayoutManager
     *
     * @param context Current context, will be used to access resources.
     */
    public MyLinearLayoutManager(Context context) {
        super(context);
    }

    /**
     * @param context       Current context, will be used to access resources.
     * @param orientation   Layout orientation. Should be {@link #HORIZONTAL} or {@link
     *                      #VERTICAL}.
     * @param reverseLayout When set to true, layouts from end to start.
     */
    public MyLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public MyLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    /**
     * 是否具备滑动能力
     * @param enable 是否可滑
     */
    public void setEnable(boolean enable) {
        this.mScrollHorizontalEnable = enable;
        this.mScrollVerticalEnable = enable;
    }

    /**
     * 是否具备垂直滑动的能力
     * @param enable 是否可滑
     */
    public void setScrollVerticalEnable(boolean enable) {
        this.mScrollVerticalEnable = enable;
    }
    /**
     * 是否具备水平滑动的能力
     * @param enable 是否可滑
     */
    public void setScrollHorizontalEnable(boolean enable) {
        this.mScrollHorizontalEnable = enable;
    }

    /**
     * 设置滑动速度, 注意: 水平和垂直的都一样被限制了, 若需要请特别处理
     * @param speedRatio 速度
     */
    public void setSpeedRatio(double speedRatio) {
        mSpeedRatio = (speedRatio <= 1 && speedRatio >= 0)? speedRatio: 1;
    }

    @Override
    public boolean canScrollVertically() {
        return  mScrollVerticalEnable && super.canScrollVertically();
    }

    @Override
    public boolean canScrollHorizontally() {
        return mScrollHorizontalEnable && super.canScrollHorizontally();
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        return super.scrollHorizontallyBy((int)(dx * mSpeedRatio), recycler, state);
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        return super.scrollVerticallyBy((int) (dy * mSpeedRatio), recycler, state);
    }
}
