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
import android.graphics.Paint;
import android.support.annotation.NonNull;

import cn.ydw.www.toolslib.widget.wheelview.common.WheelConstants;
import cn.ydw.www.toolslib.widget.wheelview.widget.WheelView;

/**
 * holo滚轮样式
 *
 * @author venshine
 */
@SuppressWarnings ("WeakerAccess")
public class HoloDrawable extends WheelDrawable {

    private Paint mHoloBgPaint, mHoloPaint;

    private int mWheelSize, mItemH;

    public HoloDrawable(int width, int height, WheelView.WheelViewStyle style, int wheelSize, int itemH) {
        super(width, height, style);
        mWheelSize = wheelSize;
        mItemH = itemH;
        init();
    }

    private void init() {
        mHoloBgPaint = new Paint();
        mHoloBgPaint.setColor(mStyle.backgroundColor != -1 ? mStyle.backgroundColor : WheelConstants
                .WHEEL_SKIN_HOLO_BG);

        mHoloPaint = new Paint();
        mHoloPaint.setStrokeWidth(mStyle.holoBorderWidth != -1 ? mStyle.holoBorderWidth : 3);
        mHoloPaint.setColor(mStyle.holoBorderColor != -1 ? mStyle.holoBorderColor : WheelConstants
                .WHEEL_SKIN_HOLO_BORDER_COLOR);
    }


    @Override
    public void draw(@NonNull Canvas canvas) {
        // draw background
        canvas.drawRect(0, 0, mWidth, mHeight, mHoloBgPaint);

        // draw select border
        if (mItemH != 0) {
            canvas.drawLine(0, mItemH * (mWheelSize / 2), mWidth, mItemH
                    * (mWheelSize / 2), mHoloPaint);
            canvas.drawLine(0, mItemH * (mWheelSize / 2 + 1), mWidth, mItemH
                    * (mWheelSize / 2 + 1), mHoloPaint);
        }
    }
}
