package cn.ydw.www.toolslib.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import cn.ydw.www.toolslib.R;


/**
 * @author 杨德望
 * @date 2017/7/6
 * 描述: 这是viewpager底部的指示器, 这里只是简单是实现了点的滑动等效果, 需要特殊操作, 请修改
 */
public class PagerIndicator extends View {
    private int circleNum;
    private int mRadius;
    private int position;
    private Paint mPaint;
    private float offset;
    private int excircleColor;//外圆
    private int circleColor;//内圆
    private boolean excircle2Fill;

    public PagerIndicator(Context context) {
        super(context);
        initPaint();
    }

    public PagerIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PagerIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PagerIndicator);
        excircleColor = typedArray.getColor(R.styleable.PagerIndicator_excircleColor, Color.GRAY);
        circleColor = typedArray.getColor(R.styleable.PagerIndicator_circleColor, Color.BLUE);
        circleNum = typedArray.getInteger(R.styleable.PagerIndicator_circleNum, 5);
        mRadius = typedArray.getInteger(R.styleable.PagerIndicator_radius, 10);
        excircle2Fill = typedArray.getBoolean(R.styleable.PagerIndicator_excircle2Fill, false);
        typedArray.recycle();
        initPaint();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PagerIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PagerIndicator);
        excircleColor = typedArray.getColor(R.styleable.PagerIndicator_excircleColor, Color.GRAY);
        circleColor = typedArray.getColor(R.styleable.PagerIndicator_circleColor, Color.BLUE);
        circleNum = typedArray.getInteger(R.styleable.PagerIndicator_circleNum, 5);
        mRadius = typedArray.getInteger(R.styleable.PagerIndicator_radius, 10);
        excircle2Fill = typedArray.getBoolean(R.styleable.PagerIndicator_excircle2Fill, false);
        typedArray.recycle();
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = canvas.getWidth();
        int mHeight = canvas.getHeight();
        canvas.translate(width / 2, mHeight/2);

        //画外圆
        if (excircle2Fill) {
            mPaint.setStyle(Paint.Style.FILL);
        } else {
            mPaint.setStyle(Paint.Style.STROKE);
        }
        mPaint.setColor(excircleColor);
        mPaint.setStrokeWidth(2);
        for (int i = 0; i < circleNum; i++) {
            canvas.drawCircle(-((circleNum - 1 - 2 * i) * 3f * mRadius/2), 0, mRadius, mPaint);
        }

        //画圆心
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(2);
        mPaint.setColor(circleColor);
        if (mRadius > 9 && !excircle2Fill) {
            canvas.drawCircle(-((circleNum - 1 - 2 * (position + offset)) * 3f * mRadius/2), 0, mRadius - 3f, mPaint);
        } else {
            canvas.drawCircle(-((circleNum - 1 - 2 * (position + offset)) * 3f * mRadius/2), 0, mRadius + 1, mPaint);
        }
    }

    public void setMove(int position, float offset) {
        this.position = position;
        this.offset = offset;

        //刷新画布
        invalidate();
    }

    public int getSelPosition(){
        return position;
    }

    public void setIndicatorCount(int circleNum) {
        this.circleNum = circleNum;
        //刷新画布
        invalidate();
    }
    public int getCircleNum(){
        return circleNum;
    }

}
