package cn.ydw.www.toolslib.widget.charinput;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 *
 * 作者：Tuo on 2018/4/13 16:35
 * 邮箱：839539179@qq.com
 * =======================================================================
 * @author 杨德望
 * Changed on 2018/12/10
 * 描述: 绘制密码圆圈图样
 *
 */
public class PwdTextView extends AppCompatTextView {

    private float radius;

    private boolean hasPwd;
    private Paint mPaint;

    public PwdTextView(Context context) {
        super(context);
        init();
    }

    public PwdTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PwdTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (mPaint == null) {
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setColor(Color.BLACK);
            mPaint.setStyle(Paint.Style.FILL);
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (hasPwd) {
            // 画一个黑色的圆
            if (mPaint != null) {
                canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, mPaint);
            }
        }
    }

    /**
     * 清理绘制的密码圆圈
     */
    public void clearPwd() {
        this.hasPwd = false;
        invalidate();
    }

    /**
     * 绘制密码上面的黑色圆圈
     * @param radius 圆圈半径
     */
    public void drawPwd(float radius) {
        this.hasPwd = true;
        if (radius == 0) {
            this.radius = getWidth() / 4;
        } else {
            this.radius = radius;
        }
        invalidate();
    }

}
