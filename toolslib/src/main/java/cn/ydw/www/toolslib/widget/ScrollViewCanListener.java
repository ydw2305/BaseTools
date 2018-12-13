package cn.ydw.www.toolslib.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

/**
 * @author XiaoMu
 * 可以监听滚动的scrollview
 */

public class ScrollViewCanListener extends ScrollView {
    private OnMyScrollListener ml;

    public ScrollViewCanListener(Context context) {
        super(context);
    }

    public ScrollViewCanListener(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollViewCanListener(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @SuppressLint ("ObsoleteSdkInt")
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (ml != null)
            ml.onScrollChanged(l, t, oldl, oldt);

        /*******************************************/
        if (android.os.Build.VERSION.SDK_INT < 9) {  // API 9及之后走onOverScrolled方法监听
            if (getScrollY() == 0) {    // 小心踩坑1: 这里不能是getScrollY() <= 0
                isScrolledToTop = true;
                isScrolledToBottom = false;
            } else if (getScrollY() > 0 && getScrollY() + getHeight() - getPaddingTop()-getPaddingBottom() == getChildAt(0).getHeight()) {
                // 小心踩坑2: 这里不能是 >=
                // 小心踩坑3（可能忽视的细节2）：这里最容易忽视的就是ScrollView上下的padding　
                isScrolledToBottom = true;
                isScrolledToTop = false;
            } else {
                isScrolledToTop = false;
                isScrolledToBottom = false;
            }
            notifyScrollChangedListeners();
        }
        //为了兼容一些边界奇葩情况，上面的代码就会写成<=,>=的情况，结果就出bug了
        // 我写的时候写成这样：getScrollY() + getHeight() >= getChildAt(0).getHeight()
        // 结果发现快滑动到底部但是还没到时，会发现上面的条件成立了，导致判断错误
        // 原因：getScrollY()值不是绝对靠谱的，它会超过边界值，但是它自己会恢复正确，导致上面的计算条件不成立
        // 仔细想想也感觉想得通，系统的ScrollView在处理滚动的时候动态计算那个scrollY的时候也会出现超过边界再修正的情况
    }

    public interface OnMyScrollListener{
        /**
         * l, t代表left, top，也就是触摸点相对左上角的偏移量。而oldl, oldt就是滑动前的偏移量。
         */
        void onScrollChanged(int left, int top, int oldLeft, int oldTop);
    }
    public void setOnMyScrollListener(OnMyScrollListener ml){
        this.ml = ml;
    }


    private boolean isScrolledToTop = true;// 初始化的时候设置一下值
    private boolean isScrolledToBottom = false;

    private ISmartScrollChangedListener mSmartScrollChangedListener;

    /** 定义监听接口 */
    public interface ISmartScrollChangedListener {
        void onScrolledToBottom();
        void onScrolledToTop();
    }

    public void setCanScrollChangedListener(ISmartScrollChangedListener smartScrollChangedListener) {
        mSmartScrollChangedListener = smartScrollChangedListener;
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        if (scrollY == 0) {
            isScrolledToTop = clampedY;
            isScrolledToBottom = false;
        } else {
            isScrolledToTop = false;
            isScrolledToBottom = clampedY;
        }
        notifyScrollChangedListeners();
    }
    public boolean canScroll() {
        View child = getChildAt(0);
        if (child != null) {
            int childHeight = child.getHeight();
            return getHeight() < childHeight;
        }
        return false;
    }

    private void notifyScrollChangedListeners() {
        if (isScrolledToTop) {
            if (mSmartScrollChangedListener != null) {
                mSmartScrollChangedListener.onScrolledToTop();
            }
        } else if (isScrolledToBottom || !canScroll()) {
            if (mSmartScrollChangedListener != null) {
                mSmartScrollChangedListener.onScrolledToBottom();
            }
        }
    }

    public boolean isScrolledToTop() {
        return isScrolledToTop;
    }

    public boolean isScrolledToBottom() {
        return isScrolledToBottom || !canScroll();
    }

}
