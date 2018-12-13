package cn.ydw.www.toolslib.widget.recyclerview.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.LinkedList;

import cn.ydw.www.toolslib.widget.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import cn.ydw.www.toolslib.widget.recyclerview.weight.LoadingFooter;

/**
 * Created by 杨德望 on 2017/5/9.
 *
 * 分页展示数据时，RecyclerView的FooterView State 操作工具类
 *
 * RecyclerView一共有几种State：Normal/Loading/Error/TheEnd
 */
public class RecyclerViewStateUtils {

    /**
     * 设置headerAndFooterAdapter的FooterView State
     *
     * @param instance      context
     * @param recyclerView  recyclerView
     * @param state         FooterView State
     */
    public static void setFooterViewState(Context instance,
                                          RecyclerView recyclerView,
                                          LoadingFooter.State state) {
        setFooterViewState(instance, recyclerView, 0, state, null);

    }
    /**
     * 设置headerAndFooterAdapter的FooterView State
     *
     * @param instance      context
     * @param recyclerView  recyclerView
     * @param state         FooterView State
     * @param errorListener FooterView处于Error状态时的点击事件
     */
    public static void setFooterViewState(Context instance,
                                          RecyclerView recyclerView,
                                          LoadingFooter.State state,
                                          View.OnClickListener errorListener) {
        // 即使只有一页, 也要加屁股
        setFooterViewState(instance, recyclerView, 0, state, errorListener);
    }
    /**
     * 设置headerAndFooterAdapter的FooterView State
     *
     * @param instance      context
     * @param recyclerView  recyclerView
     * @param pageSize      分页展示时，recyclerView每一页的数量
     * @param state         FooterView State
     */
    public static void setFooterViewState(Context instance,
                                          RecyclerView recyclerView,
                                          int pageSize,
                                          LoadingFooter.State state) {
        // 即使只有一页, 也要加屁股
        setFooterViewState(instance, recyclerView, pageSize, state, null);
    }
    /**
     * 设置headerAndFooterAdapter的FooterView State
     *
     * @param instance      context
     * @param recyclerView  recyclerView
     * @param pageSize      分页展示时，recyclerView每一页的数量
     * @param state         FooterView State
     * @param errorListener FooterView处于Error状态时的点击事件
     */
    public static void setFooterViewState(Context instance,
                                          RecyclerView recyclerView, int pageSize,
                                          LoadingFooter.State state,
                                          View.OnClickListener errorListener) {

        if(instance == null) {
            return;
        }

        RecyclerView.Adapter outerAdapter = recyclerView.getAdapter();
        if (outerAdapter == null || !(outerAdapter instanceof HeaderAndFooterRecyclerViewAdapter)) {
            return;
        }

        HeaderAndFooterRecyclerViewAdapter headerAndFooterAdapter = (HeaderAndFooterRecyclerViewAdapter) outerAdapter;

        //只有一页的时候，就不加加载状态了
        if (headerAndFooterAdapter.getInnerAdapter().getItemCount() < pageSize) {
            return;
        }
        // 是否有加载状态的控件
        LoadingFooter mLoadingFooter = null;

        //已经有footerView了
        if (headerAndFooterAdapter.getFooterViewsCount() > 0) {
            LinkedList<View> mFooterView = headerAndFooterAdapter.getFooterView();
            int index = 0;
            while (index < headerAndFooterAdapter.getFooterViewsCount()) {
                View mFootView = mFooterView.get(index);
                if (mFootView instanceof LoadingFooter) {
                    if (mLoadingFooter == null) {
                        mLoadingFooter = (LoadingFooter) mFootView;
                        index++;
                    } else {
                        headerAndFooterAdapter.removeFooterView(mFootView);
                    }
                }
            }
        }
        if (mLoadingFooter == null) {
            mLoadingFooter = new LoadingFooter(instance);
            headerAndFooterAdapter.addFooterView(mLoadingFooter);
        }
        mLoadingFooter.setState(state);
        if (state == LoadingFooter.State.NetWorkError) {
            mLoadingFooter.setOnClickListener(errorListener);
        }
    }

    /**
     * 获取当前RecyclerView.FooterView的状态
     *
     * @param recyclerView 复用列表
     */
    public static LoadingFooter.State getFooterViewState(RecyclerView recyclerView) {

        RecyclerView.Adapter outerAdapter = recyclerView.getAdapter();
        if (outerAdapter != null && outerAdapter instanceof HeaderAndFooterRecyclerViewAdapter) {
            HeaderAndFooterRecyclerViewAdapter mAdapter = (HeaderAndFooterRecyclerViewAdapter) outerAdapter;
            if (mAdapter.getFooterViewsCount() > 0) {
                LinkedList<View> mFooterView = mAdapter.getFooterView();
                for (View mFootView: mFooterView) {
                    if (mFootView instanceof LoadingFooter) {
                        // 是否有加载状态的控件
                        LoadingFooter mLoadingFooter = (LoadingFooter) mFootView;
                        return mLoadingFooter.getState();
                    }
                }
            }
        }
        return LoadingFooter.State.Normal;
    }

}
