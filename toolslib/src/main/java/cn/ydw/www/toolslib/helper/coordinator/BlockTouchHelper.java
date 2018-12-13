package cn.ydw.www.toolslib.helper.coordinator;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;

/**
 * ========================================
 *
 * @author 杨德望
 * Create on 2018/10/17
 * 描述: 仿造Instagram的相册联动辅助类
 * =========================================
 */
@SuppressWarnings("WeakerAccess")
public final class BlockTouchHelper implements View.OnTouchListener{

    public final static int BlockState_Normal  = 1;      // 滑块静置状态
    public final static int BlockState_Move    = 1 << 1; // 滑块正在滑动
    public final static int BlockState_Check   = 1 << 2; // 滑块正在被校验

    public final static int BlockPoint_Normal  = 1;      // 滑块在默认位置
    public final static int BlockPoint_Top     = 1 << 1; // 滑块在顶部

    private final static long BlockAnimatorTime = 500L; // 滑块自动移动缓冲时间
    private final static float offsetNum = 0.2f; // 波动的百分比, 即往上滑或者往下滑超过这个比例, 那么滑动事件成立, 否则不成立


    private float minHeight = -1;// 滑块最小可到达的高度
    private float maxHeight = -1;// 滑块最大可到达的高度
    private float defaultHeight = -1;// 滑块默认的位置高度

    private float lastOffsetPoint;//旧动画偏移量
    private float errorOffset = 0;// 误差量, 即主布局如果往下挪了, 那么挪了多少就要多偏移多少


    // 滑块动作监听
    private Callback mCallback;
    // 滑块状态
    private int mBlockState = BlockState_Normal;
    // 滑块位置
    private int mBlockPoint = BlockPoint_Normal;
    // 主控件, 即当前被触碰的东东
    private View rootView;
    private ValueAnimator mBlockAnimator;

    public BlockTouchHelper() {
    }

    public BlockTouchHelper(Callback mCallback) {
        this.mCallback = mCallback;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // 初始化基本参数
        initBaseParam(v);
        float lastPointY = v.getY();
        if (event.getAction() == MotionEvent.ACTION_MOVE){
            // 触碰的坐标
            float pointY = event.getRawY() - v.getHeight() - errorOffset;
            // 控件挪动
            float mNowPointY = getNowPointY(pointY);
            if (mNowPointY != -1) {
                v.setY(mNowPointY);
            }
//            Logger.e("挪动: event.getRawY() = " + event.getRawY()
//                    + "; v.getHeight() = " + v.getHeight()
//                    + "; errorOffset = " + errorOffset + "; mNowPointY = " + mNowPointY
//                    + "; minHeight = " + minHeight + "; maxHeight = " + maxHeight);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            checkBlockPoint();
        }
        if (mCallback != null) {
            mCallback.onBlockStateChange(mBlockState, mBlockPoint, v.getY() - lastPointY);
        }
        return true;
    }

    /**
     * 初始化基本参数
     * @param view 使用这个触碰监听的基本控件
     */
    private void initBaseParam(View view){
        if (rootView == null) {
            rootView = view;
        }
        if (minHeight == -1) {
            minHeight = 0;
        }
        if (maxHeight == -1) {
            maxHeight = view.getY();
        }
        if (defaultHeight == -1) {
            defaultHeight = view.getY();
        }
    }

    /**
     * 设置最小高度, 即滑块最大可以往上到达的高度, (与顶点的距离)
     * @param minHeight 最小高度
     */
    public void setMinHeight(float minHeight) {
        this.minHeight = minHeight;
    }

    /**
     * 设置最大高度, 即滑块最大可以往下滑到的高度, (与顶点的距离)
     * @param maxHeight 最大高度
     */
    public void setMaxHeight(float maxHeight) {
        this.maxHeight = maxHeight;
    }

    /**
     * 设置误差偏移量, 即主布局如果往下挪了, 那么挪了多少就要多偏移多少. 往下偏移的量为正, 反之为负
     * @param errorOffset 误差偏移量,
     */
    public void setErrorOffset(float errorOffset) {
        this.errorOffset = errorOffset;
    }

    /**
     * 获取滑块位置
     * @return 滑块是在上面还是在默认位置
     *          {@link #BlockPoint_Top 滑块在顶部}
     *          {@link #BlockPoint_Normal 滑块在默认位置}
     */
    public int getBlockPoint() {
        return mBlockPoint;
    }

    /**
     * 获取当前控件位置坐标y
     * @param pointY 触碰点坐标y
     * @return 坐标y
     */
    private float getNowPointY(float pointY) {
        // : 2018/10/17 校验是否符合规则
        if (pointY < minHeight || pointY > maxHeight) {
            return -1;
        }
        setBlockState(BlockState_Move);
        return pointY;
    }

    /**
     * 手势抬起, 校验滑块位置
     */
    private void checkBlockPoint() {
        if (rootView == null) { // 如果滑块不存在, 那么取消检验
            setBlockState(BlockState_Normal);
            return;
        }
        // 手势抬起时, 滑块当前位置
        float nowPointY = rootView.getY();
        if (mBlockPoint == BlockPoint_Normal) {
            setBlockState(BlockState_Check);
            // 滑块之前在默认位置,
            if (nowPointY < defaultHeight * (1 - offsetNum)) {
                // 若滑块往上移动了, 且移动事件成立
                moveToTop();
            } else {
                // 只是略微触碰, 则滑动事件不成立
                moveToNormal();
            }
        } else if (mBlockPoint == BlockPoint_Top) {
            setBlockState(BlockState_Check);
            // 滑块之前在顶部位置,
            if (nowPointY > defaultHeight * (offsetNum / 2)) {
                // 若滑块往下移动了, 且移动事件成立
                moveToNormal();
            } else {
                // 只是略微触碰, 则滑动事件不成立
                moveToTop();
            }
        }
    }

    /**
     * 将滑块移动至默认位置
     */
    public void moveToNormal() {
        if (rootView == null) {
            setBlockState(BlockState_Normal);
            return;
        }
        if (mBlockAnimator != null) {
            if (mBlockAnimator.isRunning() || mBlockAnimator.isStarted()) {
                return;
            }
        }
        if (mBlockPoint != BlockPoint_Normal) {
            mBlockPoint = BlockPoint_Normal;
            setBlockState(BlockState_Move);
            float nowPointY = rootView.getY();
            startAnimator(defaultHeight - nowPointY);
        }
    }

    /**
     * 将滑块移动至顶部
     */
    public void moveToTop() {
        if (rootView == null) {
            setBlockState(BlockState_Normal);
            return;
        }
        if (mBlockAnimator != null) {
            if (mBlockAnimator.isRunning() || mBlockAnimator.isStarted()) {
                return;
            }
        }
        if (mBlockPoint != BlockPoint_Top) {
            mBlockPoint = BlockPoint_Top;
            setBlockState(BlockState_Move);
            float nowPointY = rootView.getY();
            startAnimator(minHeight - nowPointY);
        }
    }

    /**
     * 启动滑动动画,
     * @param totalPointY 需要动画偏移的总量, 如果总量为正, 表示要往下滑动的量, 如果为负, 表示要往上滑动的量
     */
    private void startAnimator(float totalPointY){ //缓冲滑动, 防止用户看到闪一下的情况
        if (rootView == null) {
            setBlockState(BlockState_Normal);
            return;
        }
        if (mBlockAnimator == null) {
            mBlockAnimator = new ValueAnimator();
            mBlockAnimator.setDuration(BlockAnimatorTime);
            mBlockAnimator.addUpdateListener(new BlockAnimatorUpdateListener());
        }
        mBlockAnimator.setFloatValues(0, totalPointY);
        lastOffsetPoint = 0;
        mBlockAnimator.start();
    }
    // 滑块动画变化监听
    private class BlockAnimatorUpdateListener implements ValueAnimator.AnimatorUpdateListener {

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            if (rootView == null) {
                setBlockState(BlockState_Normal);
                return;
            }
            Object mAnimatedValue = animation.getAnimatedValue();
            if (mAnimatedValue != null) {
                float offset = Float.valueOf(mAnimatedValue.toString());
                float dy = (offset - lastOffsetPoint);
                rootView.setY(rootView.getY() + dy);
                if (mCallback != null) {
                    mCallback.onBlockStateChange(mBlockState, mBlockPoint, dy);
                }
                lastOffsetPoint = offset;
            }
        }
    }

    /**
     * 设置滑块状态
     * @param blockState 滑块状态
     */
    private void setBlockState(int blockState) {
        if (mBlockState != blockState) {
            mBlockState = blockState;
            if (mCallback != null) {
                mCallback.onBlockScroll(mBlockState, mBlockPoint);
            }
        }
    }

    public void onDestroy() {
        if (mBlockAnimator != null) {
            mBlockAnimator.removeAllUpdateListeners();
        }
    }

    public static abstract class Callback {
        public void onBlockScroll(int blockState, int blockPoint) {
        }
        public void onBlockStateChange (int blockState, int blockPoint, float dy) {
        }
    }
}
