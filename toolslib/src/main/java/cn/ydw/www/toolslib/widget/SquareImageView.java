package cn.ydw.www.toolslib.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * =====================================
 * 作    者: 杨德望
 * 版    本：${VERSION}.
 * 创建日期：2017/12/11.
 * 描    述：自定义的图片控件, 长宽相等, 好像必须要设置宽度才能变正方形, 所以随意吧
 * =====================================
 */
public class SquareImageView extends ImageView {

    public SquareImageView(Context context) {
        super(context);
    }

    public SquareImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
        int childSize = getMeasuredWidth();
        // 高度和宽度一样
        if (childSize == 0) {
            childSize = getMeasuredHeight();
        }
        int length = MeasureSpec.makeMeasureSpec(childSize, MeasureSpec.EXACTLY);
        super.onMeasure(length, length);
    }
}
