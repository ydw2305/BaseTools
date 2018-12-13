package cn.ydw.www.toolslib.widget.recyclerview.manager;

import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import cn.ydw.www.toolslib.widget.recyclerview.utils.SwipeCardConfig;

/**
 * ========================================
 *
 * @author 杨德望
 * Create on 2018/9/1
 * 描述:
 * =========================================
 */
public class MySwipeCardLayoutManager extends RecyclerView.LayoutManager {

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    /**
     * 在这里面给子view布局,也就是item
     *
     * @param recycler 复用列表
     * @param state 滑动状态
     */
    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        //拿到总item数量
        int itemCount = getItemCount();
        if (itemCount < 1) {//没有item当然就没必要布局了
            return;
        }

        //在布局之前，将所有的子View先Detach掉，放入到Scrap缓存中
        detachAndScrapAttachedViews(recycler);

        int bottomPosition;  //用来记录最底层view的postion

        if (itemCount < SwipeCardConfig.MAX_SHOW_COUNT) {  //如果不足最大数量(4个)
            //那么最底层就是最后一条数据对应的position
            bottomPosition = itemCount - 1;
        } else {
            //否则最底层就是第MAX_SHOW_COUNT(4)条数据对应的position
            bottomPosition = SwipeCardConfig.MAX_SHOW_COUNT - 1;
        }

        /*
         * 这里开始布局且绘制子view
         * 注意:这里要先从最底层开始绘制,因为后绘制的才能覆盖先绘制的，
         *     滑动的时候是滑最上面一层的,也就是后绘制的
         * position也是层数
         */
        for (int position = bottomPosition; position >= 0; position--) {
            //根据position找recycler要itemview
            View view = recycler.getViewForPosition(position);
            //将子View添加至RecyclerView中
            addView(view);
            //测量子view并且把Margin也作为子控件的一部分
            measureChildWithMargins(view, 0, 0);
            //宽度空隙 getWidth()得到Recycler控件的宽度,getDecoratedMeasuredWidth(view)拿到子view的宽度
//            int widthSpace = getWidth() - getDecoratedMeasuredWidth(view);
//            //高度空隙
            int heightSpace = getHeight() - getDecoratedMeasuredHeight(view);
//            //给子view布局,这里居中了
//            layoutDecoratedWithMargins(view, widthSpace / 2, heightSpace / 2,
//                    widthSpace / 2 + getDecoratedMeasuredWidth(view),
//                    getDecoratedMeasuredHeight(view) + heightSpace / 2);
            layoutDecoratedWithMargins(view, 0, heightSpace / 2,
                    getWidth(), getDecoratedMeasuredHeight(view) + heightSpace / 2);

            //下面要调整每一层itemview的的大小及X轴, Y轴和Z轴的偏移和旋转角度
            if (position > 0) {  //大于0就是不是顶层

                //依次往下，每层都要水平方向缩小, 如果小于等于0 或者大于等于1 就没有缩放的意义了
                if (SwipeCardConfig.SCALE_X_GAP > 0 && SwipeCardConfig.SCALE_X_GAP < 1) {
                    view.setScaleX(1 - SwipeCardConfig.SCALE_X_GAP * position);
                }
                //依次往下，每层都要垂直方向缩小, 如果小于等于0 或者大于等于1 就没有缩放的意义了
                if (SwipeCardConfig.SCALE_Y_GAP > 0 && SwipeCardConfig.SCALE_Y_GAP < 1) {
                    view.setScaleY(1 - SwipeCardConfig.SCALE_Y_GAP * position);
                }
                //依次往下，每层都要旋转, 旋转角的差距会越来越小
                view.setRotation(SwipeCardConfig.ROTATE_GAP * SwipeCardConfig.ROTATE_OFFSET * position);
                //依次往下，每层产生位移, 水平方向移动
                view.setTranslationX(SwipeCardConfig.TRANS_X_GAP * position);
                //依次往下，每层产生位移, 垂直方向移动
                view.setTranslationY(SwipeCardConfig.TRANS_Y_GAP * position);
                //依次往下, 每层在Z轴方向的平移会根据层数会越来越小
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    view.setTranslationZ(SwipeCardConfig.TRANS_Z_GAP * (bottomPosition - position));
                }

            } else {
                //如果是第0层(最上面那层),只需调整Z轴高度
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    view.setTranslationZ(SwipeCardConfig.TRANS_Z_GAP * (bottomPosition));  //Z轴方向的平移
                }
            }
        }
    }
}
