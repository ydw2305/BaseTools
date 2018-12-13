/*******************************************************************************
 * <p>Copyright(c) 2017</p>
 * <p>
 * @autor 杨德望
 * @date  ${DATE}
 * @see ${PACKAGE_NAME}
 * </p>
 *
 ******************************************************************************/
package cn.ydw.www.toolslib.widget.wheelview.graphics;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import cn.ydw.www.toolslib.widget.wheelview.common.WheelConstants;
import cn.ydw.www.toolslib.widget.wheelview.widget.WheelView;

/**
 * 滚轮样式
 *
 * @author venshine
 */
@SuppressWarnings ({"unused","WeakerAccess"})
public class WheelDrawable extends Drawable {

    protected int mWidth;   // 控件宽

    protected int mHeight;  // 控件高

    protected WheelView.WheelViewStyle mStyle;

    private Paint mBgPaint;

    public WheelDrawable(int width, int height, WheelView.WheelViewStyle style) {
        mWidth = width;
        mHeight = height;
        mStyle = style;
        init();
    }

    private void init() {
        mBgPaint = new Paint();
        mBgPaint.setColor(mStyle.backgroundColor != -1 ? mStyle.backgroundColor : WheelConstants.WHEEL_BG);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.drawRect(0, 0, mWidth, mHeight, mBgPaint);
    }

    @Override
    public void setAlpha(int alpha) {
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
    }

    @Override
    public int getOpacity() {
        //noinspection WrongConstant
        return PixelFormat.UNKNOWN;
    }
}
