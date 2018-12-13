package cn.ydw.www.toolslib.widget.recyclerview.listener;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * @author 杨德望 create on 2017/5/9.
 * <p/>
 * 继承自RecyclerView.OnScrollListener，可以监听到是否滑动到页面最低部
 */
public  class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener  {

    /**
     * 当前RecyclerView类型
     */
    private LayoutManagerType layoutManagerType;

    /**
     * 最后一个的位置
     */
    private int[] lastPositions;

    /**
     * 最后一个可见的item的位置
     */
    private int lastVisibleItemPosition;
    /**hide*/
    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);



        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

        if (layoutManagerType == null) {
            if (layoutManager instanceof GridLayoutManager) {
                layoutManagerType = LayoutManagerType.GridLayout;
            } else if (layoutManager instanceof LinearLayoutManager) {
                layoutManagerType = LayoutManagerType.LinearLayout;
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                layoutManagerType = LayoutManagerType.StaggeredGridLayout;
            } else {
                throw new RuntimeException(
                        "Unsupported LayoutManager used. Valid ones are " +
                                "LinearLayoutManager, GridLayoutManager and StaggeredGridLayoutManager");
            }
        }

        switch (layoutManagerType) {
            case LinearLayout:
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                break;
            case GridLayout:
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                break;
            case StaggeredGridLayout:
                StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                if (lastPositions == null) {
                    lastPositions = new int[staggeredGridLayoutManager.getSpanCount()];
                }
                staggeredGridLayoutManager.findLastVisibleItemPositions(lastPositions);
                lastVisibleItemPosition = findMax(lastPositions);
                break;
        }
    }
    /**hide*/
    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        //当前滑动的状态
        onCurrentScrollState(recyclerView, newState);

        if (isScrollToHeader(recyclerView)) {
            onRefreshPage(recyclerView);

        } else  {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            if ((visibleItemCount > 0 && newState == RecyclerView.SCROLL_STATE_IDLE
                    && (lastVisibleItemPosition) >= totalItemCount - 1)) {
                onLoadNextPage(recyclerView);
            }
        }

    }

    /**
     * 取数组中最大值
     *
     * @param lastPositions 最后一个位置
     */
    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }


    /**
     * Check if this view can be scrolled vertically in a certain direction.
     *
     * @param mRecyclerView a recycler view.
     * @return true if this view can be scrolled in the specified direction, false otherwise.
     */
    public boolean isScrollToHeader(RecyclerView mRecyclerView) {
        final int offset = mRecyclerView.computeVerticalScrollOffset();
        final int range = mRecyclerView.computeVerticalScrollRange() - mRecyclerView.computeVerticalScrollExtent();
        return range == 0 || offset <= 0;
    }
    /**
     * Check if this view can be scrolled vertically in a certain direction.
     *
     * @param mRecyclerView a recycler view
     * @return true if this view can be scrolled in the specified direction, false otherwise.
     */
    public boolean isScrollToFooter(RecyclerView mRecyclerView) {
        final int offset = mRecyclerView.computeVerticalScrollOffset();
        final int range = mRecyclerView.computeVerticalScrollRange() - mRecyclerView.computeVerticalScrollExtent();
        return range == 0 || offset >= range - 1;
    }

    // 加载更多
    public void onLoadNextPage(final RecyclerView recyclerView){
    }
    // 下拉刷新
    public void onRefreshPage(RecyclerView recyclerView) {

    }

    // 当前的滑动状态
    public void onCurrentScrollState(RecyclerView recyclerView, int currentScrollState) {
    }

    public enum LayoutManagerType {
        LinearLayout,
        StaggeredGridLayout,
        GridLayout
    }
}
