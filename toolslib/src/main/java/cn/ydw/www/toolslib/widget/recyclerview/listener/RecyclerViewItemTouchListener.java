package cn.ydw.www.toolslib.widget.recyclerview.listener;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;


/**
 * =====================================
 * 作    者: 杨德望
 * 版    本：${VERSION_CODE}.
 * 创建日期：2018/1/16.
 * 描    述： recyclerView的item touch 事件  单触碰实例,
 *      FIXME 注意, 该类暂不使用, 主要是作为资料参考用的
 * =====================================
 */
public class RecyclerViewItemTouchListener extends RecyclerView.SimpleOnItemTouchListener {
    private GestureDetectorCompat mGestureDetector;

    /**
     * ////////////////////////////////////////////////////////////////////////////////////////////
     * 1.用户轻触触摸屏，由1个MotionEvent ACTION_DOWN触发
     * {@link GestureDetector.OnGestureListener#onDown(MotionEvent arg0)} ;
     *
     * 2.用户轻触触摸屏，尚未松开或拖动，由一个1个MotionEvent ACTION_DOWN触发
     * 注意和onDown()的区别，强调的是没有松开或者拖动的状态
     * {@link GestureDetector.OnGestureListener#onShowPress(MotionEvent e)}
     *
     * 3.用户（轻触触摸屏后）松开，由一个1个MotionEvent ACTION_UP触发
     * {@link GestureDetector.OnGestureListener#onSingleTapUp(MotionEvent e)}
     *
     * 4.用户按下触摸屏、快速移动后松开，由1个MotionEvent ACTION_DOWN, 多个ACTION_MOVE, 1个ACTION_UP触发
     * {@link GestureDetector.OnGestureListener#onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)}
     *
     * 5.用户按下触摸屏，并拖动，由1个MotionEvent ACTION_DOWN, 多个ACTION_MOVE触发
     * {@link GestureDetector.OnGestureListener#onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)}
     *
     * 6.用户长按触摸屏，由多个MotionEvent ACTION_DOWN触发
     * {@link GestureDetector.OnGestureListener#onLongPress(MotionEvent e)}
     *
     * /////////////////////////////////////////////////////////////////////////////////////////////
     * 1.双击的第二下Touch down时触发
     * {@link GestureDetector.SimpleOnGestureListener#onDoubleTap(MotionEvent e) }
     *
     * 2.双击的第二下Touch down和up都会触发，可用e.getAction()区分
     * {@link GestureDetector.SimpleOnGestureListener#onDoubleTapEvent(MotionEvent e)}
     *
     * 3.Touch down时触发
     * {@link GestureDetector.SimpleOnGestureListener# onDown(MotionEvent e) }
     *
     * 4.Touch了滑动一点距离后，up时触发
     * {@link GestureDetector.SimpleOnGestureListener#onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)}
     *
     * 5.Touch了不移动一直Touch down时触发
     * {@link GestureDetector.SimpleOnGestureListener#onLongPress(MotionEvent e)}
     *
     * 6.Touch了滑动时触发
     * {@link GestureDetector.SimpleOnGestureListener#onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)}
     *
     * 7.Touch了还没有滑动时触发
     * (1)onDown只要Touch Down一定立刻触发
     * (2)Touch Down后过一会没有滑动先触发onShowPress再触发onLongPress
     * So: Touch Down后一直不滑动，onDown -> onShowPress -> onLongPress这个顺序触发。
     * {@link GestureDetector.SimpleOnGestureListener#onShowPress(MotionEvent e)}
     *
     * 8.两个函数都是在Touch Down后又没有滑动(onScroll)，又没有长按(onLongPress)，然后Touch Up时触发
     * 点击一下非常快的(不滑动)Touch Up: onDown->onSingleTapUp->onSingleTapConfirmed
     * 点击一下稍微慢点的(不滑动)Touch Up: onDown->onShowPress->onSingleTapUp->onSingleTapConfirmed
     * {@link GestureDetector.SimpleOnGestureListener#onSingleTapConfirmed(MotionEvent e)}
     *
     * @param recyclerView 复用list
     */

    public RecyclerViewItemTouchListener(final RecyclerView recyclerView, GestureDetector.SimpleOnGestureListener listener) {
        mGestureDetector = new GestureDetectorCompat(recyclerView.getContext(),listener);
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        return mGestureDetector.onTouchEvent(e);
    }
}
