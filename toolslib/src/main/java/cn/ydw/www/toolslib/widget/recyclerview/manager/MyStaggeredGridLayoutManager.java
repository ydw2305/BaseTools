/*******************************************************************************
 * <p>Copyright(c) 2017</p>
 * <p>
 * @autor 杨德望
 * @date  ${DATE}
 * @see ${PACKAGE_NAME}
 * </p>
 *
 ******************************************************************************/

package cn.ydw.www.toolslib.widget.recyclerview.manager;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import cn.ydw.www.toolslib.utils.Logger;

/**
 * @author 杨德望 on 2017/5/9.
 * <p/>
 * 拓展的StaggeredGridLayoutManager，
 */
public class MyStaggeredGridLayoutManager extends StaggeredGridLayoutManager {

    private GridLayoutManager.SpanSizeLookup mSpanSizeLookup;
    private boolean mScrollVerticalEnable = true; //是否可垂直滑动
    private boolean mScrollHorizontalEnable = true; //是否可水平滑动
    private double mSpeedRatioX = 1d, mSpeedRatioY = 1d;//滑动速度

    public MyStaggeredGridLayoutManager(int spanCount, int orientation) {
        super(spanCount, orientation);
    }

    /**
     * Returns the current used by the GridLayoutManager.
     *
     * @return The current used by the GridLayoutManager.
     */
    public GridLayoutManager.SpanSizeLookup getSpanSizeLookup() {
        return mSpanSizeLookup;
    }

    /**
     * 设置某个位置的item的跨列程度，这里和GridLayoutManager有点不一样，
     * 如果你设置某个位置的item的span>1了，那么这个item会占据所有列
     *
     * @param spanSizeLookup instance to be used to query number of spans
     *                       occupied by each item
     */
    public void setSpanSizeLookup(GridLayoutManager.SpanSizeLookup spanSizeLookup) {
        mSpanSizeLookup = spanSizeLookup;
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

    @Override
    public boolean canScrollVertically() {
        return  mScrollVerticalEnable && super.canScrollVertically();
    }

    @Override
    public boolean canScrollHorizontally() {
        return mScrollHorizontalEnable && super.canScrollHorizontally();
    }

    /**
     * 设置滑动速度, 注意: 水平和垂直的都一样被限制了, 若需要请特别处理
     * @param speedRatio 速度
     */
    public void setSpeedRatio(double speedRatio) {
        setSpeedRatio(speedRatio, speedRatio);
    }

    /**
     * 设置滑动速度,
     * @param speedRatioX 设置水平滑动速度, 即 Horizontally
     * @param speedRatioY 设置垂直滑动速度, 即 Vertically
     */
    public void setSpeedRatio(double speedRatioX, double speedRatioY) {
        mSpeedRatioX = (speedRatioX <= 1 && speedRatioX >= 0)? speedRatioX: 1;
        mSpeedRatioY = (speedRatioY <= 1 && speedRatioY >= 0)? speedRatioY: 1;
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        return super.scrollHorizontallyBy((int)(dx * mSpeedRatioX), recycler, state);
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        return super.scrollVerticallyBy((int) (dy * mSpeedRatioY), recycler, state);
    }

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
        //Log.d(TAG, "item count = " + getItemCount());
        for (int i = 0; i < getItemCount(); i++) {
            if (mSpanSizeLookup.getSpanSize(i) > 1) {
                //Log.d(TAG, "lookup > 1 = " + i);
                try {
                    //fix 动态添加时报IndexOutOfBoundsException
                    View view = recycler.getViewForPosition(i);
                    if (view != null) {
                        //占用所有的列
                        StaggeredGridLayoutManager.LayoutParams lp = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
                        lp.setFullSpan(true);
                    }
                    // recycler.recycleView(view);
                } catch (Exception e) {
                    Logger.e("manager", "重设占用格数异常", e);
                }
            }
        }
        super.onMeasure(recycler, state, widthSpec, heightSpec);
    }
}