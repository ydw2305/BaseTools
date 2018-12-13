package cn.ydw.www.toolslib.widget.recyclerview.utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.LinkedList;

import cn.ydw.www.toolslib.widget.recyclerview.HeaderAndFooterRecyclerViewAdapter;


/**
 * @author 杨德望 on 2017/5/9.
 * <p/>
 * RecyclerView设置Header/Footer所用到的工具类
 */
public class RecyclerViewUtils {

    /**
     * 设置HeaderView
     *
     * @param recyclerView 复用列表
     * @param view 头布局
     */
    public static void addHeaderView(RecyclerView recyclerView, View view) {
        if (view == null) return;
        RecyclerView.Adapter outerAdapter = recyclerView.getAdapter();

        if (outerAdapter == null || !(outerAdapter instanceof HeaderAndFooterRecyclerViewAdapter)) {
            return;
        }

        HeaderAndFooterRecyclerViewAdapter headerAndFooterAdapter = (HeaderAndFooterRecyclerViewAdapter) outerAdapter;
        headerAndFooterAdapter.addHeaderView(view);
    }

    /**
     * 设置FooterView
     *
     * @param recyclerView 复用列表
     * @param view 尾布局
     */
    public static void addFooterView(RecyclerView recyclerView, View view) {
        if (view == null) return;
        RecyclerView.Adapter outerAdapter = recyclerView.getAdapter();

        if (outerAdapter == null || !(outerAdapter instanceof HeaderAndFooterRecyclerViewAdapter)) {
            return;
        }

        HeaderAndFooterRecyclerViewAdapter headerAndFooterAdapter = (HeaderAndFooterRecyclerViewAdapter) outerAdapter;
        headerAndFooterAdapter.addFooterView(view);
    }

    /**
     * 移除FooterView
     *
     * @param recyclerView 复用列表
     */
    public static void removeAllFooterView(RecyclerView recyclerView) {

        RecyclerView.Adapter outerAdapter = recyclerView.getAdapter();

        if (outerAdapter != null && outerAdapter instanceof HeaderAndFooterRecyclerViewAdapter) {

            int footerViewCounter = ((HeaderAndFooterRecyclerViewAdapter) outerAdapter).getFooterViewsCount();
            if (footerViewCounter > 0) {
                LinkedList<View> mFooterView = ((HeaderAndFooterRecyclerViewAdapter) outerAdapter).getFooterView();
                if (mFooterView != null) {
                    while (mFooterView.size() > 0) {
                        View footerView = mFooterView.get(0);
                        ((HeaderAndFooterRecyclerViewAdapter) outerAdapter).removeFooterView(footerView);
                    }
                }
            }
        }
    }

    /**
     * 移除HeaderView
     *
     * @param recyclerView 复用布局
     */
    public static void removeAllHeaderView(RecyclerView recyclerView) {

        RecyclerView.Adapter outerAdapter = recyclerView.getAdapter();

        if (outerAdapter != null && outerAdapter instanceof HeaderAndFooterRecyclerViewAdapter) {

            int headerViewCounter = ((HeaderAndFooterRecyclerViewAdapter) outerAdapter).getHeaderViewsCount();
            if (headerViewCounter > 0) {
                LinkedList<View> mHeaderView = ((HeaderAndFooterRecyclerViewAdapter) outerAdapter).getHeaderView();
                if (mHeaderView != null) {
                    while (mHeaderView.size() > 0) {
                        View headerView = mHeaderView.get(0);
                        ((HeaderAndFooterRecyclerViewAdapter) outerAdapter).removeFooterView(headerView);
                    }
                }
            }
        }
    }

    /**
     * 请使用本方法替代RecyclerView.ViewHolder的getLayoutPosition()方法
     *
     * @param recyclerView 复用布局
     * @param holder 填充辅助类
     * @return 位置
     */
    public static int getLayoutPosition(RecyclerView recyclerView, RecyclerView.ViewHolder holder) {
        RecyclerView.Adapter outerAdapter = recyclerView.getAdapter();
        if (outerAdapter != null && outerAdapter instanceof HeaderAndFooterRecyclerViewAdapter) {

            int headerViewCounter = ((HeaderAndFooterRecyclerViewAdapter) outerAdapter).getHeaderViewsCount();
            if (headerViewCounter > 0) {
                return holder.getLayoutPosition() - headerViewCounter;
            }
        }

        return holder.getLayoutPosition();
    }

    /**
     * 请使用本方法替代RecyclerView.ViewHolder的getAdapterPosition()方法
     *
     * @param recyclerView 复用布局
     * @param holder 填充辅助类
     * @return 位置
     */
    public static int getAdapterPosition(RecyclerView recyclerView, RecyclerView.ViewHolder holder) {
        RecyclerView.Adapter outerAdapter = recyclerView.getAdapter();
        if (outerAdapter != null && outerAdapter instanceof HeaderAndFooterRecyclerViewAdapter) {

            int headerViewCounter = ((HeaderAndFooterRecyclerViewAdapter) outerAdapter).getHeaderViewsCount();
            if (headerViewCounter > 0) {
                return holder.getAdapterPosition() - headerViewCounter;
            }
        }

        return holder.getAdapterPosition();
    }

    /**
     * 获取列表头布局数量
     * @param recyclerView 复用列表
     * @return 数量
     */
    public static int getHeaderViewCount(RecyclerView recyclerView) {
        RecyclerView.Adapter outerAdapter = recyclerView.getAdapter();
        if (outerAdapter != null && outerAdapter instanceof HeaderAndFooterRecyclerViewAdapter) {
            return  ((HeaderAndFooterRecyclerViewAdapter) outerAdapter).getHeaderViewsCount();
        }
        return 0;
    }

    /**
     * 获取列表尾布局数量
     * @param recyclerView 复用列表
     * @return 数量
     */
    public static int getFooterViewCount(RecyclerView recyclerView) {
        RecyclerView.Adapter outerAdapter = recyclerView.getAdapter();
        if (outerAdapter != null && outerAdapter instanceof HeaderAndFooterRecyclerViewAdapter) {
            return  ((HeaderAndFooterRecyclerViewAdapter) outerAdapter).getFooterViewsCount();
        }
        return 0;
    }
}