package cn.ydw.www.toolslib.widget.recyclerview.listener;

import android.graphics.Canvas;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import cn.ydw.www.toolslib.widget.recyclerview.utils.SwipeCardConfig;

/**
 * ========================================
 *
 * @author 杨德望
 * Create on 2018/9/1
 * 描述: 卡片式滑动切换item
 * <p>
 * //1.创建ItemTuchCallBack
 * SwipeCardItemTouchCallback mCallback = new SwipeCardItemTouchCallback().setSwipeListener(mItemAdapter);
 * //2.创建ItemTouchHelper并把callBack传进去
 * ItemTouchHelper itemTouchHelper = new ItemTouchHelper(mCallback);
 * //3.与RecyclerView关联起来
 * itemTouchHelper.attachToRecyclerView(mRecyclerView);
 * </p>
 * =========================================
 */
public class SwipeCardItemTouchCallback extends ItemTouchHelper.Callback {

    private SwipeListener mSwipeListener;

    /**
     * 设置滑动监听
     *
     * @param swipeListener 监听
     */
    public SwipeCardItemTouchCallback setSwipeListener(SwipeListener swipeListener) {
        mSwipeListener = swipeListener;
        return this;
    }

    /**
     * @return 是否开启长按拖拽
     * true，开启
     * false,不开启长按退拽
     */
    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    /**
     * @return 是否开启滑动
     * true，开启
     * false,不开启长按退拽
     */
    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    /**
     * ItemTouchHelper支持设置事件方向，并且必须重写当前getMovementFlags来指定支持的方向
     * dragFlags  表示拖拽的方向，有六个类型的值：LEFT、RIGHT、START、END、UP、DOWN
     * swipeFlags 表示滑动的方向，有六个类型的值：LEFT、RIGHT、START、END、UP、DOWN
     * 最后要通过makeMovementFlags（dragFlag，swipe）创建方向的Flag
     */
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        //由于我们不需要长按拖拽，所以直接传入0即可，传入0代表不监听
        int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        return makeMovementFlags(0, swipeFlags);
    }

    /**
     * 长按item就可以拖动，然后拖动到其他item的时候触发onMove
     * 这里我们不需要
     *
     * @param recyclerView 复用列表
     * @param viewHolder   拖动的viewholder
     * @param target       目标位置的viewholder
     * @return 是否拖拽移动
     */
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    /**
     * 把item滑出屏幕的时候调用
     *
     * @param viewHolder 滑动的viewholder
     * @param direction  滑动的方向 {@link ItemTouchHelper#LEFT ... 之类的}
     *                   有六个类型的值：LEFT、RIGHT、START、END、UP、DOWN
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (mSwipeListener != null) {
            // 注: direction 只能是 ItemTouchHelper.LEFT 之类的
            mSwipeListener.onSwiped(viewHolder.getLayoutPosition(), direction);
        }
//        //移除这条数据
//        Object remove = mDatas.remove(viewHolder.getLayoutPosition());
//
//        /* 这个位置可以用来加载数据,当滑到还剩4个或者多少个时可以在后面加载数据，添加到mDatas中*/
//        //这里就为了方便，直接循环了，把移除的元素再添加到末尾
//        mDatas.add(mDatas.size(), remove);
//
//        //刷新
//        mAdapter.notifyDataSetChanged();
        //复位
        viewHolder.itemView.setRotation(0);
    }

    /**
     * 注: 这只是滑动时的动画而已
     * <p>
     * 只要拖动、滑动了item,就会触发这个方法，而且是动的过程中会一直触发
     * 所以动画效果就是在这个方法中来实现的
     */
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        //拿到子view的数量
        int childCount = recyclerView.getChildCount();
        if (childCount < 1) { // 如果没有布局, 就不需要动画了
            return;
        }
        float swipeValue = (float) Math.sqrt(dX * dX + dY * dY);   //滑动离中心的距离
        float fraction = swipeValue / (recyclerView.getWidth() * 0.5f);
        //边界修正 最大为1
        if (fraction > 1) {
            fraction = 1;
        }

        //调整每个子view的缩放、位移之类的
        for (int index = 0; index < childCount; index++) {
            // 拿到子view
            View mChildView = recyclerView.getChildAt(index);
            //转换一下,level代表层数,最上面是第0层
            int level = childCount - index - 1;
            if (level > 0) {
                //依次往下，每层水平方向缩小量逐步减少
                if (SwipeCardConfig.SCALE_X_GAP > 0 && SwipeCardConfig.SCALE_X_GAP < 1) {
                    mChildView.setScaleX(1 - SwipeCardConfig.SCALE_X_GAP * (level - fraction));
                }
                //依次往下，每层垂直方向缩小逐步减少
                if (SwipeCardConfig.SCALE_Y_GAP > 0 && SwipeCardConfig.SCALE_Y_GAP < 1) {
                    mChildView.setScaleY(1 - SwipeCardConfig.SCALE_Y_GAP * (level - fraction));
                }
                //依次往下，每层都要旋转, 旋转角的差距会越来越小
                mChildView.setRotation(SwipeCardConfig.ROTATE_GAP * SwipeCardConfig.ROTATE_OFFSET * (level - fraction));
                //依次往下，每层产生位移, 水平方向移动
                mChildView.setTranslationX(SwipeCardConfig.TRANS_X_GAP * (level - fraction));
                //依次往下，每层产生位移, 垂直方向移动
                mChildView.setTranslationY(SwipeCardConfig.TRANS_Y_GAP * (level - fraction));
                //依次往下, 每层在Z轴方向的平移会根据层数会越来越小
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mChildView.setTranslationZ(SwipeCardConfig.TRANS_Z_GAP * (childCount - (level - fraction)));
                }

            } else { //第0层
                //拿到水平方向的偏移比率
                float xFraction = dX / (recyclerView.getWidth() * 0.5f);
                //边界修正,有正有负，因为旋转有两个方向
                if (xFraction > 1) {
                    xFraction = 1;
                } else if (xFraction < -1) {
                    xFraction = -1;
                }
                //第一层左右滑动的时候稍微有点旋转
                mChildView.setRotation(xFraction * 15);  //这里最多旋转15度
            }
            if (mSwipeListener != null) {
                int direction = 0;
                direction |= (dX > 0 ? ItemTouchHelper.RIGHT : dX < 0 ? ItemTouchHelper.LEFT : 0);
                direction |= (dY > 0 ? ItemTouchHelper.DOWN : dY < 0 ? ItemTouchHelper.UP : 0);
                // 注: direction 只能是 ItemTouchHelper.LEFT 之类的
                mSwipeListener.onTopSwiping(level, direction);
            }
        }

    }

    @Override
    public float getSwipeThreshold(RecyclerView.ViewHolder viewHolder) {
//        Log.i(TAG, "getSwipeThreshold: ");
//        if (isUpOrDown(viewHolder.itemView)) { //如果是向上或者向下滑动
//            return Float.MAX_VALUE; //就返回阈值为很大
//        }
        return super.getSwipeThreshold(viewHolder);
    }

    /**
     * 获得逃脱(swipe)速度
     *
     * @param defaultValue 初始速度
     * @return 速度阈值
     */
    @Override
    public float getSwipeEscapeVelocity(float defaultValue) {
//        Log.d(TAG, "getSwipeEscapeVelocity: " + defaultValue);
//        View topView = mRecyclerView.getChildAt(mRecyclerView.getChildCount() - 1);
//        if (isUpOrDown(topView)) { //如果是向上或者向下滑动
//            return Float.MAX_VALUE; //就返回阈值为很大
//        }
        return super.getSwipeEscapeVelocity(defaultValue);
    }


    /**
     * 获得swipe的速度阈值
     *
     * @param defaultValue 默认速度阈值
     * @return 速度
     */
    @Override
    public float getSwipeVelocityThreshold(float defaultValue) {
//        Log.d(TAG, "getSwipeVelocityThreshold: " + defaultValue);
//        View topView = mRecyclerView.getChildAt(mRecyclerView.getChildCount() - 1);
//        if (isUpOrDown(topView)) { //如果是向上或者向下滑动
//            return Float.MAX_VALUE; //就返回阈值为很大
//        }
        return super.getSwipeVelocityThreshold(defaultValue);
    }

    /**
     * 判断是否是向上滑或者向下滑
     */
    private boolean isUpOrDown(View topView) {
        float x = topView.getX();
        float y = topView.getY();
        int left = topView.getLeft();
        int top = topView.getTop();
        // 水平方向的移动小于垂直方向的话, 就是上下滑
        return (Math.pow(x - left, 2) < Math.pow(y - top, 2));
    }
}
