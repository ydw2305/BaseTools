package cn.ydw.www.toolslib.widget;

import android.os.Handler;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * =====================================
 * 作    者: 杨德望
 * 版    本：${VERSION}.
 * 创建日期：2017/12/21.
 * 描    述： 触碰事件, 不是很完善, 建议参考系统的 {@link GestureDetector},
 * 示例如下 {@link OnMyTouchListener #mGestureDetector}
 * 或者使用 {@link RecyclerViewItemTouchListener}
 *
 * // FIXME 待修改, 后期考虑修改成自定义控件, 然后通用化, 降低一下耦合度
 * =====================================
 */
public class OnMyTouchListener<T> {
    private final int MAX_LONG_PRESS_TIME = 500;// 长按/双击最长等待时间
    private final int MAX_SINGLE_CLICK_TIME = 200;// 单击最长等待时间
    private final int MAX_MOVE_FOR_CLICK = 5;// 最长改变距离,超过则算移动

    private final int Type_LongPress = 0, Type_SinglePress = 1, Type_DoublePress = 2;

    private int mClickCount;// 点击次数
    private int mFirstX;
    private int mFirstY;
    private long mLastDownTime;
    private long mLastUpTime;
    private boolean isLongPress = false;
    private MyRun mSingleClickTask, mDoubleClickTask, mLongPressTask;


    private OnPressListener<T> opl;

    public OnMyTouchListener() {
        mSingleClickTask = new MyRun(Type_SinglePress);
        mDoubleClickTask = new MyRun(Type_DoublePress);
        mLongPressTask = new MyRun(Type_LongPress);
    }

    public void setOpl(OnPressListener<T> opl) {
        this.opl = opl;
    }

    private Handler mBaseHandler = new Handler();

    public void attachTouch(MotionEvent event, final int position, final T info) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastDownTime = System.currentTimeMillis();
                mFirstX = (int) event.getX();
                mFirstY = (int) event.getY();
                mClickCount++;
//                        Logger.e("打印", "DOWN-->mClickCount=" + mClickCount);
                if (mSingleClickTask != null) {
//                            Logger.e("打印", "移除单击");
                    mBaseHandler.removeCallbacks(mSingleClickTask);
                }
                if (mDoubleClickTask != null) {
//                            Logger.e("打印", "移除双击");
                    mBaseHandler.removeCallbacks(mDoubleClickTask);
                }
                if (mLongPressTask != null) {
//                            Logger.e("打印", "移除长按");
                    mBaseHandler.removeCallbacks(mLongPressTask);
                    isLongPress = false;
                }
                if (mClickCount == 1 && mLongPressTask != null) {
                    mLongPressTask.setInfo(info);
                    mLongPressTask.setPosition(position);
//                            Logger.e("打印", "呼叫了长按" + System.currentTimeMillis());
                    mBaseHandler.postDelayed(mLongPressTask, MAX_LONG_PRESS_TIME);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int mMoveX = (int) event.getX();
                int mMoveY = (int) event.getY();
                int absMx = Math.abs(mMoveX - mFirstX);
                int absMy = Math.abs(mMoveY - mFirstY);
//                        if (isLog)
//                            Logger.e("打印", "MOVE-->absMx=" + absMx + "; absMy=" + absMy);
                if (absMx > MAX_MOVE_FOR_CLICK || absMy > MAX_MOVE_FOR_CLICK) {
                    mBaseHandler.removeCallbacks(mSingleClickTask);
                    mBaseHandler.removeCallbacks(mDoubleClickTask);
                    mClickCount = 0;//移动了
                    if (isLongPress) {
                        if (opl != null) {
                            opl.onLongPressMove(position, info, event);
                        }
                    } else {
                        mBaseHandler.removeCallbacks(mLongPressTask);
                    }
                }

                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mLastUpTime = System.currentTimeMillis();
                mBaseHandler.removeCallbacks(mLongPressTask);
                if (isLongPress && opl != null) {
//                    Logger.e("打印", "UP--> 长按松开" + position);
                    opl.onLongPressUp(position, info);
                    isLongPress = false;
                } else {
                    if ((mLastUpTime - mLastDownTime) < MAX_LONG_PRESS_TIME) {
                        if (mClickCount == 1) {
                            mSingleClickTask.setInfo(info);
                            mSingleClickTask.setPosition(position);
//                        Logger.e("打印", "UP--> 单击松开" + position);
                            mBaseHandler.postDelayed(mSingleClickTask, MAX_SINGLE_CLICK_TIME);
                        } else if (mClickCount == 2) {
                            //处理双击
//                                    Logger.e("打印", "double double double....");
                            mDoubleClickTask.setPosition(position);
                            mDoubleClickTask.setInfo(info);
                            mBaseHandler.removeCallbacks(mSingleClickTask);
                            mBaseHandler.post(mDoubleClickTask);
//                        Logger.e("打印", "UP--> 双击松开" + position);
                        }
                    } else {
                        //超出了双击间隔时间
                        mClickCount = 0;
                    }
                }

                break;
        }
    }

    public void cancelLongPress() {
        if (mBaseHandler != null && mLongPressTask != null) {
            mBaseHandler.removeCallbacks(mLongPressTask);
        }
    }

    private class MyRun implements Runnable {
        private int typeNum;
        private T info;
        private int position = -1;

        MyRun(int typeNum) {
            this.typeNum = typeNum;
        }

        public void setInfo(T info) {
            this.info = info;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        @Override
        public void run() {
            mClickCount = 0;
            if (opl != null) {
                if (typeNum == Type_LongPress) {
//                    Logger.e("打印", "长按");
                    isLongPress = true;
                    //处理长按
                    opl.onLongPress(position, info);
                } else if (typeNum == Type_SinglePress) {
//                    Logger.e("打印", "单击");
                    //处理单击
                    opl.onSinglePress(position, info);
                } else if (typeNum == Type_DoublePress) {
//                    Logger.e("打印", "双击");
                    //处理双击
                    opl.onDoublePress(position, info);
                }
            }
        }
    }

    public interface OnPressListener<T> {
        void onSinglePress(int position, T info);

        void onDoublePress(int position, T info);

        void onLongPress(int position, T info);

        void onLongPressMove(int position, T info, MotionEvent event);

        void onLongPressUp(int position, T info);
    }

}
