package cn.ydw.www.toolslib.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Scroller;

import cn.ydw.www.toolslib.R;


/**
 * @author XiaoMu
 * Created on 2017/5/12.
 * 侧滑关闭功能, 使用的话直接  new SlidingLayout(activity).bindActivity(); 就可以了
 * 扩展功能: 全屏侧滑和速度侧滑, 暂时没时间研究了, 就先不做了, 注意, 如果
 * <p>
 * <p>
 * 点击的ViewGroup 的打印: activity ( framelayout ( framelayout))
 * Activity         ---> {@link Activity#dispatchTouchEvent}     {@link MotionEvent#ACTION_DOWN}
 * ParentViewGroup  ---> {@link ViewGroup#dispatchTouchEvent}    {@link MotionEvent#ACTION_DOWN}
 * ParentViewGroup  ---> {@link ViewGroup#onInterceptTouchEvent} {@link MotionEvent#ACTION_DOWN}
 * ChildViewGroup   ---> {@link ViewGroup#dispatchTouchEvent}    {@link MotionEvent#ACTION_DOWN}
 * ChildViewGroup   ---> {@link ViewGroup#onInterceptTouchEvent} {@link MotionEvent#ACTION_DOWN}
 * ChildViewGroup   ---> {@link ViewGroup#onTouchEvent}          {@link MotionEvent#ACTION_DOWN}
 * ParentViewGroup  ---> {@link ViewGroup#onTouchEvent}          {@link MotionEvent#ACTION_DOWN}
 * Activity         ---> {@link Activity#onTouchEvent}           {@link MotionEvent#ACTION_DOWN}
 * Activity         ---> {@link Activity#dispatchTouchEvent}     {@link MotionEvent#ACTION_UP}
 * Activity         ---> {@link Activity#onTouchEvent}           {@link MotionEvent#ACTION_UP}
 * <p><br/>
 * 点击的View 的打印: activity ( framelayout ( button))
 * Activity         ---> {@link Activity#dispatchTouchEvent}     {@link MotionEvent#ACTION_DOWN}
 * ParentViewGroup  ---> {@link ViewGroup#dispatchTouchEvent}    {@link MotionEvent#ACTION_DOWN}
 * ParentViewGroup  ---> {@link ViewGroup#onInterceptTouchEvent} {@link MotionEvent#ACTION_DOWN}
 * ChildView        ---> {@link View#dispatchTouchEvent}         {@link MotionEvent#ACTION_DOWN}
 * ChildView        ---> {@link View#onTouchEvent}               {@link MotionEvent#ACTION_DOWN}
 * Activity         ---> {@link Activity#dispatchTouchEvent}     {@link MotionEvent#ACTION_UP}
 * ParentViewGroup  ---> {@link ViewGroup#dispatchTouchEvent}    {@link MotionEvent#ACTION_UP}
 * ParentViewGroup  ---> {@link ViewGroup#onInterceptTouchEvent} {@link MotionEvent#ACTION_UP}
 * ChildView        ---> {@link View#dispatchTouchEvent}         {@link MotionEvent#ACTION_UP}
 * ChildView        ---> {@link View#onTouchEvent}               {@link MotionEvent#ACTION_UP}
 * <p><br/>
 */
@SuppressLint("ViewConstructor")
@SuppressWarnings("unused")
public class SlidingLayout extends FrameLayout {
    private Activity mActivity;
    private Scroller mScroller;
    private OnScrollListener osl;
    private boolean velocityTrackerEnable = false;

    /**
     * 上次ACTION_MOVE时的X坐标
     */
    private int mLastMotionX;
    /**
     * 屏幕宽度
     */
    private int mWidth = -1;
    /**
     * 可滑动的最小X坐标，小于该坐标的滑动不处理
     */
    private int mMinX;
    /**
     * 页面边缘的阴影图
     */
    private Drawable mLeftShadow;
    /**
     * 页面边缘阴影的宽度默认值
     */
    private static final int SHADOW_WIDTH = 16;
    /**
     * 页面边缘阴影的宽度
     */
    private int mShadowWidth;
    private Activity activity;

    public SlidingLayout(Activity activity) {
        this(activity, null);
    }

    public SlidingLayout(Activity activity, AttributeSet attrs) {
        this(activity, attrs, 0);
    }

    public SlidingLayout(Activity activity, AttributeSet attrs, int defStyleAttr) {
        super(activity, attrs, defStyleAttr);
        initView(activity);
        this.activity = activity;
    }

    private void initView(Activity activity) {
        mActivity = activity;
        mScroller = new Scroller(mActivity);
        //noinspection deprecation
        mLeftShadow = getResources().getDrawable(R.drawable.left_shadow);
        int density = (int) activity.getResources().getDisplayMetrics().density;
        mShadowWidth = SHADOW_WIDTH * density;
    }

    /**
     * 绑定Activity
     */
    public SlidingLayout bindActivity() {
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        View child = decorView.getChildAt(0);
        decorView.removeView(child);
        addView(child);
        decorView.addView(this);
        return this;
    }

    /**
     * 滑动返回
     */
    private void scrollBack() {
        int startX = getScrollX();
        int dx = -getScrollX();
        mScroller.startScroll(startX, 0, dx, 0, 300);
        invalidate();
    }

    /**
     * 滑动关闭
     */
    private void scrollClose() {
        int startX = getScrollX();
        int dx = -getScrollX() - mWidth;
        mScroller.startScroll(startX, 0, dx, 0, 300);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), 0);
            postInvalidate();
        } else if (-getScrollX() == mWidth) {
            mActivity.setVisible(false);//关闭的时候不会有闪的效果
            mActivity.finish();
        }
        super.computeScroll();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        drawShadow(canvas);
    }

    /**
     * 绘制边缘的阴影
     */
    private void drawShadow(Canvas canvas) {
        // 保存画布当前的状态
        canvas.save();
        // 设置drawable的大小范围
        mLeftShadow.setBounds(0, 0, mShadowWidth, getHeight());
        // 让画布平移一定距离
        canvas.translate(-mShadowWidth, 0);
        // 绘制Drawable
        mLeftShadow.draw(canvas);
        // 恢复画布的状态
        canvas.restore();
    }

    public void setOnScrollListener(OnScrollListener osl) {
        this.osl = osl;
    }

    // 是否可以执行速度滑动 (速滑关闭)  FIXME 速滑感觉没效果, 所以没必要使用了
    public void setVelocityTrackerEnable(boolean enable) {
        this.velocityTrackerEnable = enable;
    }

    // 是否具备速滑能力, 默认是关闭的
    public boolean isVelocityTrackerEnable() {
        return velocityTrackerEnable;
    }

    private boolean isMove = false, willMove = false;

    /**
     * 建议在{@link Activity#dispatchTouchEvent(MotionEvent)} 里面调用该方法, 因为, 当activity里面
     * 的子view没用充满屏幕时, 就会造成非view部分的位置无法侧滑删除, 主要是不含子view的ViewGroup并没
     * 有 {@link MotionEvent#ACTION_MOVE} 状态( 仅activity有), 所以, 如果无法保证子View充满屏幕, 还
     * 是重写一下activity的触碰方法吧
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
//        if (velocityTrackerEnable ) {
//            createVelocityTracker(event);
//        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isMove = false;
                if (mWidth == -1) {
                    mWidth = getWidth();
                    mMinX = mWidth / 10;
                }
                mLastMotionX = (int) event.getX();
                willMove = mLastMotionX < mMinX;//表明点击的是屏幕十分之一可滑动区
                break;
            case MotionEvent.ACTION_MOVE:
                int rightOffsetX = mLastMotionX - (int) event.getX(); // 往右走的偏移量
//                    int xSpeed = getScrollVelocity();
                if (willMove && Math.abs(rightOffsetX) > 0) {
                    isMove = true;
                }
                if ((int) event.getX() > mMinX && willMove) {// 手指处于屏幕边缘时不处理滑动
                    scrollBy(rightOffsetX / 2, 0);
                }
                mLastMotionX = (int) event.getX();
                break;
            case MotionEvent.ACTION_UP:
//                recycleVelocityTracker();
                if (willMove && Math.abs(getScrollX()) > 2 * mMinX) {
                    scrollClose();
                } else {
                    scrollBack();
                }
                break;
        }
        if (osl != null)
            osl.onScroll(isMove);
        return isMove || super.dispatchTouchEvent(event); // 如果是移动状态, 直接将事件消耗在自己身上
    }

    public boolean isMove() {
        return isMove;
    }

    //用于计算手指滑动的速度。
    private VelocityTracker mVelocityTracker;

    /**
     * 创建VelocityTracker对象，并将触摸界面的滑动事件加入到VelocityTracker当中。
     *
     * @param event 事件
     */
    private void createVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    /**
     * 回收VelocityTracker对象。
     */
    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    /**
     * @return 滑动速度，以每秒钟移动了多少像素值为单位。
     */
    private int getScrollVelocity() {
        if (mVelocityTracker == null) {
            return 0;
        }
        mVelocityTracker.computeCurrentVelocity(1000);
        int velocity = (int) mVelocityTracker.getXVelocity();//x滑动的速度
        return Math.abs(velocity);
    }

    public interface OnScrollListener {
        void onScroll(boolean isScroll);
    }
}
