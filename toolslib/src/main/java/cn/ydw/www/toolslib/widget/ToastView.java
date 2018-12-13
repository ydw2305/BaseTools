package cn.ydw.www.toolslib.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.ydw.www.toolslib.R;


/**
 * =====================================
 * 作    者: 杨德望
 * 版    本：${VERSION_CODE}.
 * 创建日期：2018/2/10.
 * 描    述：自定义的吐司弹窗
 * =====================================
 */
public class ToastView extends Toast {

    private final TextView mTvGold;
    private final TextView mTvEx;
    private final ImageView mIvPic;

    /**
     * Construct an empty Toast object.  You must call {@link #setView} before you
     * can call {@link #show}.
     *
     * @param context The context to use.  Usually your {@link Application}
     *                or {@link Activity} object.
     */
    public ToastView(Context context) {
        super(context);
        @SuppressLint("InflateParams")
        View mView = LayoutInflater.from(context).inflate(R.layout.layout_window_view4toast, null);
        mTvGold = mView.findViewById(R.id.tv_view4toast_gold);
        mTvEx = mView.findViewById(R.id.tv_view4toast_ex);
        mIvPic = mView.findViewById(R.id.iv_view4toast_pic);
        setView(mView);
        setDuration(Toast.LENGTH_SHORT);
    }

    /**
     * 展示吐司, 附带金币和经验
     * @param s 文本
     * @param gold 金币数
     * @param ex 经验数
     */
    public void showToast(CharSequence s, int gold, int ex, boolean... isError) {
        // set txt
        setText(TextUtils.isEmpty(s)? "": s);
        // set gold
        if (gold > 0) {
            if (mTvGold.getVisibility() != View.VISIBLE) {
                mTvGold.setVisibility(View.VISIBLE);
            }
            mTvGold.setText(("金币: " + gold));
        } else if (mTvGold.getVisibility() != View.GONE) {
            mTvGold.setVisibility(View.GONE);
        }
        // set experience
        if (ex > 0) {
            if (mTvEx.getVisibility() != View.VISIBLE) {
                mTvEx.setVisibility(View.VISIBLE);
            }
            mTvEx.setText(("经验: " + ex));
        } else if (mTvEx.getVisibility() != View.GONE){
            mTvEx.setVisibility(View.GONE);
        }
        // set whether the image needs to be shown
        if (isError != null && isError.length > 0) {
            if (mIvPic.getVisibility() != View.VISIBLE) {
                mIvPic.setVisibility(View.VISIBLE);
            }
            mIvPic.setSelected(isError[0]);
        } else if (mIvPic.getVisibility() != View.GONE) {
            mIvPic.setVisibility(View.GONE);
        }
        show();
    }

    /**
     * 展示吐司, 是否附带表情提示
     * @param s 文本
     * @param isError 是否是错误提示的表情
     */
    public void showToast(CharSequence s, boolean... isError){
        setText(TextUtils.isEmpty(s)? "": s);
        if (mTvGold.getVisibility() != View.GONE) {
            mTvGold.setVisibility(View.GONE);
        }
        if (mTvEx.getVisibility() != View.GONE) {
            mTvEx.setVisibility(View.GONE);
        }
        if (isError != null && isError.length > 0) {
            if (mIvPic.getVisibility() != View.VISIBLE) {
                mIvPic.setVisibility(View.VISIBLE);
            }
            mIvPic.setSelected(isError[0]);
        } else if (mIvPic.getVisibility() != View.GONE) {
            mIvPic.setVisibility(View.GONE);
        }
        show();
    }

    /**
     * 是否显示长吐司
     * @param isShowLong 是否显示长时间
     * @return this
     */
    public ToastView isShowLong(boolean isShowLong) {
        if (isShowLong) {
            if (getDuration() != Toast.LENGTH_LONG) {
                setDuration(Toast.LENGTH_LONG);
            }
        } else if (getDuration() != Toast.LENGTH_SHORT) {
            setDuration(Toast.LENGTH_SHORT);
        }
        return this;
    }
}
