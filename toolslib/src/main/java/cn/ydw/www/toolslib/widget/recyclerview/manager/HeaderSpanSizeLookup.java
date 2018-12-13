package cn.ydw.www.toolslib.widget.recyclerview.manager;

import android.support.v7.widget.GridLayoutManager;

import cn.ydw.www.toolslib.utils.Logger;
import cn.ydw.www.toolslib.widget.recyclerview.HeaderAndFooterRecyclerViewAdapter;

/**
 * Created by XiaoMu on 2017/5/9.
 * <p/>
 * RecyclerView为GridLayoutManager时，设置了HeaderView，就会用到这个SpanSizeLookup
 * 使用如:
 * manager.setSpanSizeLookup(new HeaderSpanSizeLookup((HeaderAndFooterRecyclerViewAdapter) mRecyclerView.getAdapter(), manager.getSpanCount()));
 */
public class HeaderSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {

    private HeaderAndFooterRecyclerViewAdapter adapter;
    private int mSpanSize;

    public HeaderSpanSizeLookup(HeaderAndFooterRecyclerViewAdapter adapter, int spanSize) {
        this.adapter = adapter;
        this.mSpanSize = spanSize;
    }

    @Override
    public int getSpanSize(int position) {
//        Logger.e(position + " 格子状态 + head = " +adapter.isHeader(position) + " foot = " + adapter.isFooter(position));
        boolean isHeaderOrFooter = adapter.isHeader(position) || adapter.isFooter(position);
        return isHeaderOrFooter ? mSpanSize : 1;
    }


    /*@Override
public void onViewAttachedToWindow(RecyclerView.ViewHolder holder)
{
    mInnerAdapter.onViewAttachedToWindow(holder);
    int position = holder.getLayoutPosition();
    if (isHeaderViewPos(position) || isFooterViewPos(position))
    {
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();

        if (lp != null
                && lp instanceof StaggeredGridLayoutManager.LayoutParams)
        {

            StaggeredGridLayoutManager.LayoutParams p =
                    (StaggeredGridLayoutManager.LayoutParams) lp;

            p.setFullSpan(true);
        }
    }
}*/
}