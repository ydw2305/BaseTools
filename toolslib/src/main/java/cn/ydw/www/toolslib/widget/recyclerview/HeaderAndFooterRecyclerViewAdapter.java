package cn.ydw.www.toolslib.widget.recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;

/**
 * @author 杨德望 on 2017/5/9.
 * <p/>
 * RecyclerView.Adapter with Header and Footer
 */
public class HeaderAndFooterRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER_VIEW = Integer.MIN_VALUE;
    private static final int TYPE_FOOTER_VIEW = Integer.MIN_VALUE/2 + 1;

    /**
     * RecyclerView使用的，真正的Adapter
     */
    private RecyclerView.Adapter mInnerAdapter;

    private LinkedList<View> mHeaderViews = new LinkedList<>();
    private LinkedList<View> mFooterViews = new LinkedList<>();


    public HeaderAndFooterRecyclerViewAdapter(RecyclerView.Adapter innerAdapter) {
        this.mInnerAdapter = innerAdapter;
        mInnerAdapter.registerAdapterDataObserver(new MyAdapterDataObserver());
    }


    /**
     * 获取内部适配器
     * @return RecyclerView 的适配器
     */
    public RecyclerView.Adapter getInnerAdapter() {
        return mInnerAdapter;
    }

    /**
     * 添加头部视图
     * @param header 头部视图
     */
    public void addHeaderView(View header) {

        if (header == null) {
            throw new RuntimeException("header is null");
        }

        mHeaderViews.add(header);
        this.notifyDataSetChanged();
    }

    /**
     * 添加尾部视图
     * @param footer 尾部视图
     */
    public void addFooterView(View footer) {

        if (footer == null) {
            throw new RuntimeException("footer is null");
        }

        mFooterViews.add(footer);
        this.notifyDataSetChanged();
    }

    /**
     * @return
     * 返回所有 FootView
     */
    public LinkedList<View> getFooterView() {
        return  getFooterViewsCount()>0 ? mFooterViews: null;
    }

    /**
     * @return
     * 返回所有 HeaderView
     */
    public LinkedList<View> getHeaderView() {
        return  getHeaderViewsCount()>0 ? mHeaderViews: null;
    }

    public void removeHeaderView(View view) {
        if (view == null) return;
        if (mHeaderViews.remove(view)) {
            this.notifyDataSetChanged();
        }
    }

    public void removeFooterView(View view) {
        if (view == null) return;
        if (mFooterViews.remove(view)){
            this.notifyDataSetChanged();
        }
    }
    // 获取头视图的数量
    public int getHeaderViewsCount() {
        return mHeaderViews.size();
    }
    // 获取尾视图的数量
    public int getFooterViewsCount() {
        return mFooterViews.size();
    }
    // 获取内部适配器视图的数量
    public int getInnerViewsCount() {
        return mInnerAdapter.getItemCount();
    }

    public boolean isHeader(int position) {
        return getHeaderViewsCount() > 0 && position <= 0;
    }

    public boolean isFooter(int position) {
        int lastPosition = getInnerViewsCount() + getHeaderViewsCount();
        return getFooterViewsCount() > 0 && position >= lastPosition;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int headerViewsCountCount = getHeaderViewsCount();
        if (viewType < TYPE_HEADER_VIEW + headerViewsCountCount) {
            return new ViewHolder(mHeaderViews.get(viewType - TYPE_HEADER_VIEW));
        } else if (viewType >= TYPE_FOOTER_VIEW && viewType < Integer.MAX_VALUE / 2) {
            return new ViewHolder(mFooterViews.get(viewType - TYPE_FOOTER_VIEW));
        } else {
            return mInnerAdapter.onCreateViewHolder(parent, viewType - Integer.MAX_VALUE / 2);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int headerViewsCountCount = getHeaderViewsCount();
        if (position >= headerViewsCountCount && position < headerViewsCountCount + getInnerViewsCount()) {
            mInnerAdapter.onBindViewHolder(holder, position - headerViewsCountCount);

        } else {
            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
            if(layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
                ((StaggeredGridLayoutManager.LayoutParams) layoutParams).setFullSpan(true);
            }
        }
    }

    @Override
    public int getItemCount() {
        return getHeaderViewsCount() + getFooterViewsCount() + getInnerViewsCount();
    }

    @Override
    public int getItemViewType(int position) {
        int innerCount = getInnerViewsCount();
        int headerViewsCountCount = getHeaderViewsCount();
        if (position < headerViewsCountCount) {
            return TYPE_HEADER_VIEW + position;
        } else if (headerViewsCountCount <= position && position < headerViewsCountCount + innerCount) {

            int innerItemViewType = mInnerAdapter.getItemViewType(position - headerViewsCountCount);
            if(innerItemViewType >= Integer.MAX_VALUE / 2) {
                throw new IllegalArgumentException("your adapter's return value of getViewTypeCount() must < Integer.MAX_VALUE / 2");
            }
            return innerItemViewType + Integer.MAX_VALUE / 2;
        } else {
            return TYPE_FOOTER_VIEW + position - headerViewsCountCount - innerCount;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    // ------------------------------ 自定义的数据刷新器 -----------------------------
    private class MyAdapterDataObserver extends RecyclerView.AdapterDataObserver {

        @Override
        public void onChanged() {
            super.onChanged();
            notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
            notifyItemRangeChanged(positionStart + getHeaderViewsCount(), itemCount);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            notifyItemRangeInserted(positionStart + getHeaderViewsCount(), itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            notifyItemRangeRemoved(positionStart + getHeaderViewsCount(), itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            int headerViewsCountCount = getHeaderViewsCount();
            notifyItemRangeChanged(fromPosition + headerViewsCountCount,
                    toPosition + headerViewsCountCount + itemCount);
        }
    }
}
