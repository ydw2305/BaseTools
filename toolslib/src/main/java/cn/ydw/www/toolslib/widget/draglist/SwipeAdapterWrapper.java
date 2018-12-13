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

import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * @author 杨德望
 * @date 2018/1/9
 * 描述: 拖拽适配器辅助
 */
public class SwipeAdapterWrapper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int BASE_ITEM_TYPE_HEADER = 100000;
    private static final int BASE_ITEM_TYPE_FOOTER = 200000;

    private SparseArrayCompat<View> mHeaderViews = new SparseArrayCompat<>();
    private SparseArrayCompat<View> mFootViews = new SparseArrayCompat<>();

    private RecyclerView.Adapter mAdapter;

    SwipeAdapterWrapper(RecyclerView.Adapter adapter) {
        this.mAdapter = adapter;
    }

    public RecyclerView.Adapter getOriginAdapter() {
        return mAdapter;
    }

    @Override
    public int getItemCount() {
        return getHeaderItemCount() + getContentItemCount() + getFooterItemCount();
    }

    private int getContentItemCount() {
        return mAdapter.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderView(position)) {
            return mHeaderViews.keyAt(position);
        } else if (isFooterView(position)) {
            return mFootViews.keyAt(position - getHeaderItemCount() - getContentItemCount());
        }
        return mAdapter.getItemViewType(position - getHeaderItemCount());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return mAdapter.onCreateViewHolder(parent, viewType);
    }

    private Class<?> getSupperClass(Class<?> aClass) {
        Class<?> supperClass = aClass.getSuperclass();
        if (supperClass != null && !supperClass.equals(Object.class)) {
            return getSupperClass(supperClass);
        }
        return aClass;
    }

    @Override
    public final void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    }

    @Override
    public final void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        if (isHeaderView(position) || isFooterView(position)) {
            return;
        }

        mAdapter.onBindViewHolder(holder, position - getHeaderItemCount(), payloads);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        mAdapter.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        int position = holder.getAdapterPosition();

        if (isHeaderView(position) || isFooterView(position)) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        } else {
            mAdapter.onViewAttachedToWindow(holder);
        }
    }

    public boolean isHeaderView(int position) {
        return position >= 0 && position < getHeaderItemCount();
    }

    public boolean isFooterView(int position) {
        return position >= getHeaderItemCount() + getContentItemCount();
    }

    public void addHeaderView(View view) {
        mHeaderViews.put(getHeaderItemCount() + BASE_ITEM_TYPE_HEADER, view);
    }

    public void addHeaderViewAndNotify(View view) {
        mHeaderViews.put(getHeaderItemCount() + BASE_ITEM_TYPE_HEADER, view);
        notifyItemInserted(getHeaderItemCount() - 1);
    }

    public void removeHeaderViewAndNotify(View view) {
        int headerIndex = mHeaderViews.indexOfValue(view);
        mHeaderViews.removeAt(headerIndex);
        notifyItemRemoved(headerIndex);
    }

    public void addFooterView(View view) {
        mFootViews.put(getFooterItemCount() + BASE_ITEM_TYPE_FOOTER, view);
    }

    public void addFooterViewAndNotify(View view) {
        mFootViews.put(getFooterItemCount() + BASE_ITEM_TYPE_FOOTER, view);
        notifyItemInserted(getHeaderItemCount() + getContentItemCount() + getFooterItemCount() - 1);
    }

    public void removeFooterViewAndNotify(View view) {
        int footerIndex = mFootViews.indexOfValue(view);
        mFootViews.removeAt(footerIndex);
        notifyItemRemoved(getHeaderItemCount() + getContentItemCount() + footerIndex);
    }

    public int getHeaderItemCount() {
        return mHeaderViews.size();
    }

    public int getFooterItemCount() {
        return mFootViews.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        mAdapter.setHasStableIds(hasStableIds);
    }

    @Override
    public long getItemId(int position) {
        if (!isHeaderView(position) && !isFooterView(position)) {
            return mAdapter.getItemId(position);
        }
        return super.getItemId(position);
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        int position = holder.getAdapterPosition();

        if (!isHeaderView(position) && !isFooterView(position))
            mAdapter.onViewRecycled(holder);
    }

    @Override
    public boolean onFailedToRecycleView(RecyclerView.ViewHolder holder) {
        int position = holder.getAdapterPosition();
        return !isHeaderView(position) && !isFooterView(position) && mAdapter.onFailedToRecycleView(holder);
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        int position = holder.getAdapterPosition();

        if (!isHeaderView(position) && !isFooterView(position))
            mAdapter.onViewDetachedFromWindow(holder);
    }

    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(observer);
    }

    @Override
    public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        super.unregisterAdapterDataObserver(observer);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        mAdapter.onDetachedFromRecyclerView(recyclerView);
    }
}