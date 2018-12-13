package cn.ydw.www.toolslib.widget.charinput;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import java.util.LinkedList;

import cn.ydw.www.toolslib.R;

/**
 * ========================================
 *
 * @author 杨德望
 * Create on 2018/12/10
 * 描述:字符输入模块
 * =========================================
 */
public class CharInputView extends FrameLayout {
    private final LinkedList<PwdTextView> mBlockList = new LinkedList<>();// 装载格子的容器

    private int mBlockCount = 4; // 格子数量
    // 输入框数量
    private int mEtNumber;
    // 输入框的宽度
    private int mEtWidth;
    //输入框分割线
    private Drawable mEtDividerDrawable;
    //输入框文字颜色
    private int mEtTextColor ;
    //输入框文字大小
    private float mEtTextSize ;
    //输入框获取焦点时背景
    private Drawable mEtBackgroundDrawableFocus;
    //输入框没有焦点时背景
    private Drawable mEtBackgroundDrawableNormal;
    //是否是密码模式
    private boolean mEtPwd = false;
    //密码模式时圆的半径
    private float mEtPwdRadius;



    public CharInputView(@NonNull Context context) {
        super(context);
        initUI(null, 0);
    }

    public CharInputView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initUI(attrs, 0);
    }

    public CharInputView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initUI(attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CharInputView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initUI(attrs, defStyleAttr);
    }
    // 初始化布局数据
    private void initUI(@Nullable AttributeSet attrs, int defStyleAttr) {
        if (attrs != null) {
//            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.VerificationCodeView, defStyleAttr, 0);
//            ta.recycle();
        }
    }

    /**
     * 输入的格子数量
     * @param count 数量
     */
    private void setInputSize(int count) {

    }
}
