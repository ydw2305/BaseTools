/*
 * Copyright 2017 Yang DeWang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.ydw.www.toolslib.widget.draglist;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewConfiguration;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import cn.ydw.www.toolslib.widget.draglist.listener.OnItemMoveListener;
import cn.ydw.www.toolslib.widget.draglist.listener.OnItemMovementListener;
import cn.ydw.www.toolslib.widget.draglist.listener.OnItemStateChangedListener;
import cn.ydw.www.toolslib.widget.draglist.touch.DefaultItemTouchHelperCallback;


/**
 * =====================================
 * 作    者: 杨德望
 * 版    本：${VERSION_CODE}.
 * 创建日期：2018/1/9.
 * 描    述：拖拽排序, 删除 item 的 RecyclerView
 * =====================================
 */
public class SwipeOrDragRecyclerView extends RecyclerView {

    /**
     * Left menu.
     */
    public static final int LEFT_DIRECTION = 1;
    /**
     * Right menu.
     */
    public static final int RIGHT_DIRECTION = -1;
    @IntDef({LEFT_DIRECTION, RIGHT_DIRECTION})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DirectionMode {

    }
    protected int mScaleTouchSlop;

    private ItemTouchHelper mDefaultItemTouchHelper;

    private DefaultItemTouchHelperCallback mDefaultItemTouchHelperCallback;

    private SwipeAdapterWrapper mAdapterWrapper;

    public SwipeOrDragRecyclerView(Context context) {
        this(context, null);
    }

    public SwipeOrDragRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeOrDragRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mScaleTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    private void initializeItemTouchHelper() {
        if (mDefaultItemTouchHelper == null) {
            mDefaultItemTouchHelperCallback = new DefaultItemTouchHelperCallback();
            mDefaultItemTouchHelper = new ItemTouchHelper(mDefaultItemTouchHelperCallback);
            mDefaultItemTouchHelper.attachToRecyclerView(this);
        }
    }

    /**
     * Set OnItemMoveListener.
     *
     * @param onItemMoveListener {@link OnItemMoveListener}.
     */
    public void setOnItemMoveListener(OnItemMoveListener onItemMoveListener) {
        initializeItemTouchHelper();
        this.mDefaultItemTouchHelperCallback.setOnItemMoveListener(onItemMoveListener);
    }

    /**
     * Set OnItemMovementListener.
     *
     * @param onItemMovementListener {@link OnItemMovementListener}.
     */
    public void setOnItemMovementListener(OnItemMovementListener onItemMovementListener) {
        initializeItemTouchHelper();
        this.mDefaultItemTouchHelperCallback.setOnItemMovementListener(onItemMovementListener);
    }

    /**
     * Set OnItemStateChangedListener.
     *
     * @param onItemStateChangedListener {@link OnItemStateChangedListener}.
     */
    public void setOnItemStateChangedListener(OnItemStateChangedListener onItemStateChangedListener) {
        initializeItemTouchHelper();
        this.mDefaultItemTouchHelperCallback.setOnItemStateChangedListener(onItemStateChangedListener);
    }

    /**
     * Set can long press drag.
     *
     * @param canDrag drag true, otherwise is can't.
     */
    public void setLongPressDragEnabled(boolean canDrag) {
        initializeItemTouchHelper();
        this.mDefaultItemTouchHelperCallback.setLongPressDragEnabled(canDrag);
    }

    /**
     * Get can long press drag.
     *
     * @return drag true, otherwise is can't.
     */
    public boolean isLongPressDragEnabled() {
        initializeItemTouchHelper();
        return this.mDefaultItemTouchHelperCallback.isLongPressDragEnabled();
    }


    /**
     * Set can swipe delete.
     *
     * @param canSwipe swipe true, otherwise is can't.
     */
    public void setItemViewSwipeEnabled(boolean canSwipe) {
        initializeItemTouchHelper();
        this.mDefaultItemTouchHelperCallback.setItemViewSwipeEnabled(canSwipe);
    }

    /**
     * Get can long press swipe.
     *
     * @return swipe true, otherwise is can't.
     */
    public boolean isItemViewSwipeEnabled() {
        initializeItemTouchHelper();
        return this.mDefaultItemTouchHelperCallback.isItemViewSwipeEnabled();
    }

    /**
     * Start drag a item.
     *
     * @param viewHolder the ViewHolder to start dragging. It must be a direct child of RecyclerView.
     */
    public void startDrag(ViewHolder viewHolder) {
        initializeItemTouchHelper();
        this.mDefaultItemTouchHelper.startDrag(viewHolder);
    }

    /**
     * Star swipe a item.
     *
     * @param viewHolder the ViewHolder to start swiping. It must be a direct child of RecyclerView.
     */
    public void startSwipe(ViewHolder viewHolder) {
        initializeItemTouchHelper();
        this.mDefaultItemTouchHelper.startSwipe(viewHolder);
    }

    /**
     * Check the Adapter and throw an exception if it already exists.
     */
    private void checkAdapterExist(String message) {
        if (mAdapterWrapper != null)
            throw new IllegalStateException(message);
    }


    @Override
    public void setLayoutManager(LayoutManager layoutManager) {
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            final GridLayoutManager.SpanSizeLookup spanSizeLookupHolder = gridLayoutManager.getSpanSizeLookup();

            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (mAdapterWrapper.isHeaderView(position) || mAdapterWrapper.isFooterView(position)) {
                        return gridLayoutManager.getSpanCount();
                    }
                    if (spanSizeLookupHolder != null)
                        return spanSizeLookupHolder.getSpanSize(position - getHeaderItemCount());
                    return 1;
                }
            });
        }
        super.setLayoutManager(layoutManager);
    }

    /**
     * Get the original adapter.
     */
    public Adapter getOriginAdapter() {
        if (mAdapterWrapper == null) return null;
        return mAdapterWrapper.getOriginAdapter();
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (mAdapterWrapper != null) {
            mAdapterWrapper.getOriginAdapter().unregisterAdapterDataObserver(mAdapterDataObserver);
        }

        if (adapter == null) {
            mAdapterWrapper = null;
        } else {
            adapter.registerAdapterDataObserver(mAdapterDataObserver);

            mAdapterWrapper = new SwipeAdapterWrapper(adapter);

            if (mHeaderViewList.size() > 0) {
                for (View view : mHeaderViewList) {
                    mAdapterWrapper.addHeaderView(view);
                }
            }
            if (mFooterViewList.size() > 0) {
                for (View view : mFooterViewList) {
                    mAdapterWrapper.addFooterView(view);
                }
            }
        }
        super.setAdapter(mAdapterWrapper);
    }

    private AdapterDataObserver mAdapterDataObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            mAdapterWrapper.notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            positionStart += getHeaderItemCount();
            mAdapterWrapper.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            positionStart += getHeaderItemCount();
            mAdapterWrapper.notifyItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            positionStart += getHeaderItemCount();
            mAdapterWrapper.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            positionStart += getHeaderItemCount();
            mAdapterWrapper.notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            fromPosition += getHeaderItemCount();
            toPosition += getHeaderItemCount();
            mAdapterWrapper.notifyItemMoved(fromPosition, toPosition);
        }
    };

    private List<View> mHeaderViewList = new ArrayList<>();
    private List<View> mFooterViewList = new ArrayList<>();

    /**
     * Add view at the headers.
     */
    public void addHeaderView(View view) {
        mHeaderViewList.add(view);
        if (mAdapterWrapper != null) {
            mAdapterWrapper.addHeaderViewAndNotify(view);
        }
    }

    /**
     * Remove view from header.
     */
    public void removeHeaderView(View view) {
        mHeaderViewList.remove(view);
        if (mAdapterWrapper != null) {
            mAdapterWrapper.removeHeaderViewAndNotify(view);
        }
    }

    /**
     * Add view at the footer.
     */
    public void addFooterView(View view) {
        mFooterViewList.add(view);
        if (mAdapterWrapper != null) {
            mAdapterWrapper.addFooterViewAndNotify(view);
        }
    }

    public void removeFooterView(View view) {
        mFooterViewList.remove(view);
        if (mAdapterWrapper != null) {
            mAdapterWrapper.removeFooterViewAndNotify(view);
        }
    }

    /**
     * Get size of headers.
     */
    public int getHeaderItemCount() {
        if (mAdapterWrapper == null) return 0;
        return mAdapterWrapper.getHeaderItemCount();
    }

    /**
     * Get size of footer.
     */
    public int getFooterItemCount() {
        if (mAdapterWrapper == null) return 0;
        return mAdapterWrapper.getFooterItemCount();
    }

    /**
     * Get ViewType of item.
     */
    public int getItemViewType(int position) {
        if (mAdapterWrapper == null) return 0;
        return mAdapterWrapper.getItemViewType(position);
    }


    private int mScrollState = -1;

    private boolean isLoadMore = false;
    private boolean isAutoLoadMore = true;
    private boolean isLoadError = false;

    private boolean mDataEmpty = true;
    private boolean mHasMore = false;

    private LoadMoreView mLoadMoreView;
    private LoadMoreListener mLoadMoreListener;

    @Override
    public void onScrollStateChanged(int state) {
        this.mScrollState = state;
    }

    @Override
    public void onScrolled(int dx, int dy) {
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager != null && layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;

            int itemCount = layoutManager.getItemCount();
            if (itemCount <= 0) return;

            int lastVisiblePosition = linearLayoutManager.findLastVisibleItemPosition();

            if (itemCount == lastVisiblePosition + 1 &&
                    (mScrollState == SCROLL_STATE_DRAGGING || mScrollState == SCROLL_STATE_SETTLING)) {
                dispatchLoadMore();
            }
        } else if (layoutManager != null && layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;

            int itemCount = layoutManager.getItemCount();
            if (itemCount <= 0) return;

            int[] lastVisiblePositionArray = staggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(null);
            int lastVisiblePosition = lastVisiblePositionArray[lastVisiblePositionArray.length - 1];

            if (itemCount == lastVisiblePosition + 1 &&
                    (mScrollState == SCROLL_STATE_DRAGGING || mScrollState == SCROLL_STATE_SETTLING)) {
                dispatchLoadMore();
            }
        }
    }

    private void dispatchLoadMore() {
        if (isLoadError) return;

        if (!isAutoLoadMore) {
            if (mLoadMoreView != null)
                mLoadMoreView.onWaitToLoadMore(mLoadMoreListener);
        } else {
            if (isLoadMore || mDataEmpty || !mHasMore) return;

            isLoadMore = true;

            if (mLoadMoreView != null)
                mLoadMoreView.onLoading();

            if (mLoadMoreListener != null)
                mLoadMoreListener.onLoadMore();
        }
    }

    /**
     * Load more view.
     */
    public void setLoadMoreView(LoadMoreView loadMoreView) {
        mLoadMoreView = loadMoreView;
    }

    /**
     * Load more listener.
     */
    public void setLoadMoreListener(LoadMoreListener loadMoreListener) {
        mLoadMoreListener = loadMoreListener;
    }

    /**
     * Automatically load more automatically.
     * <p>
     * Non-auto-loading mode, you can to click on the item to load.
     * </p>
     *
     * @param autoLoadMore you can use false.
     * @see LoadMoreView#onWaitToLoadMore(LoadMoreListener)
     */
    public void setAutoLoadMore(boolean autoLoadMore) {
        isAutoLoadMore = autoLoadMore;
    }

    /**
     * Load more done.
     *
     * @param dataEmpty data is empty ?
     * @param hasMore   has more data ?
     */
    public final void loadMoreFinish(boolean dataEmpty, boolean hasMore) {
        isLoadMore = false;
        isLoadError = false;

        mDataEmpty = dataEmpty;
        mHasMore = hasMore;

        if (mLoadMoreView != null) {
            mLoadMoreView.onLoadFinish(dataEmpty, hasMore);
        }
    }

    /**
     * Called when data is loaded incorrectly.
     *
     * @param errorCode    Error code, will be passed to the LoadView, you can according to it to customize the prompt information.
     * @param errorMessage Error message.
     */
    public void loadMoreError(int errorCode, String errorMessage) {
        isLoadMore = false;
        isLoadError = true;

        if (mLoadMoreView != null) {
            mLoadMoreView.onLoadError(errorCode, errorMessage);
        }
    }

    public interface LoadMoreView {

        /**
         * Show progress.
         */
        void onLoading();

        /**
         * Load finish, handle result.
         */
        void onLoadFinish(boolean dataEmpty, boolean hasMore);

        /**
         * Non-auto-loading mode, you can to click on the item to load.
         */
        void onWaitToLoadMore(LoadMoreListener loadMoreListener);

        /**
         * Load error.
         */
        void onLoadError(int errorCode, String errorMessage);
    }

    public interface LoadMoreListener {

        /**
         * More data should be requested.
         */
        void onLoadMore();
    }

}
