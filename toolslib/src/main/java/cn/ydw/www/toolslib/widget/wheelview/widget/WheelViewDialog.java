/*******************************************************************************
 * <p>Copyright(c) 2017</p>
 * <p>
 * @autor 杨德望
 * @date  ${DATE}
 * @see ${PACKAGE_NAME}
 * </p>
 *
 ******************************************************************************/
package cn.ydw.www.toolslib.widget.wheelview.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import cn.ydw.www.toolslib.widget.wheelview.adapter.ArrayWheelAdapter;
import cn.ydw.www.toolslib.widget.wheelview.common.WheelConstants;
import cn.ydw.www.toolslib.widget.wheelview.util.WheelUtils;

/**
 * 滚轮对话框
 *
 * @author venshine
 */
@SuppressWarnings ({"unused","WeakerAccess"})
public class WheelViewDialog<T> implements View.OnClickListener {

    private TextView mTitle;

    private View mLine1, mLine2;

    private WheelView<T> mWheelView;

    private WheelView.WheelViewStyle mStyle;

    private TextView mButton;

    private AlertDialog mDialog;

    private Context mContext;

    private OnDialogItemClickListener mOnDialogItemClickListener;

    private int mSelectedPos;

    private T mSelectedText;

    public WheelViewDialog(Context context) {
        mContext = context;
        init();
    }

    private void init() {
        LinearLayout layout = new LinearLayout(mContext);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(WheelUtils.dip2px(mContext, 20), 0, WheelUtils.dip2px(mContext, 20), 0);

        mTitle = new TextView(mContext);
        mTitle.setTextColor(WheelConstants.DIALOG_WHEEL_COLOR);
        mTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        mTitle.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                WheelUtils.dip2px(mContext, 50));
        layout.addView(mTitle, titleParams);
        //上面的线
        mLine1 = new View(mContext);
        mLine1.setBackgroundColor(WheelConstants.DIALOG_WHEEL_COLOR);
        LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                WheelUtils.dip2px(mContext, 2));
        layout.addView(mLine1, lineParams);
        //中间的区域, 内容+ 两条线
        mWheelView = new WheelView<>(mContext);
        mWheelView.setSkin(WheelView.Skin.Holo);
        mWheelView.setWheelAdapter(new ArrayWheelAdapter(mContext));
        mStyle = new WheelView.WheelViewStyle();
        mStyle.textColor = Color.GRAY;
        mStyle.selectedTextZoom = 1.2f;
        mStyle.backgroundColor = Color.TRANSPARENT;
        mWheelView.setStyle(mStyle);

        mWheelView.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener<T>() {
            @Override
            public void onItemSelected(int position, T text) {
                mSelectedPos = position;
                mSelectedText = text;
            }
        });
        LinearLayout.LayoutParams wheelParams = new LinearLayout.LayoutParams(
                WheelUtils.dip2px(mContext, 200), ViewGroup.LayoutParams.WRAP_CONTENT); //中间位置的布局格式
        wheelParams.gravity = Gravity.CENTER_HORIZONTAL;
        layout.addView(mWheelView, wheelParams);
        //下面的线
        mLine2 = new View(mContext);
        mLine2.setBackgroundColor(WheelConstants.DIALOG_WHEEL_COLOR);
        LinearLayout.LayoutParams line2Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                WheelUtils.dip2px(mContext, 1f));
        layout.addView(mLine2, line2Params);
        //按钮
        mButton = new TextView(mContext);
        mButton.setTextColor(WheelConstants.DIALOG_WHEEL_COLOR);
        mButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        mButton.setGravity(Gravity.CENTER);
        mButton.setClickable(true);
        mButton.setOnClickListener(this);
        mButton.setText("OK");
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                WheelUtils.dip2px(mContext, 45));
        layout.addView(mButton, buttonParams);

        mDialog = new AlertDialog.Builder(mContext).create();
        mDialog.setView(layout);
        mDialog.setCanceledOnTouchOutside(false);
    }

    /**
     * 点击事件
     *
     * @param onDialogItemClickListener
     * @return
     */
    public WheelViewDialog setOnDialogItemClickListener(OnDialogItemClickListener onDialogItemClickListener) {
        mOnDialogItemClickListener = onDialogItemClickListener;
        return this;
    }

    /**
     * 设置dialog外观颜色
     *
     * @param color
     * @return
     */
    public WheelViewDialog setDialogStyle(int color) {
        mTitle.setTextColor(color);
        mLine1.setBackgroundColor(color);
        mLine2.setBackgroundColor(color);
        mButton.setTextColor(color);
        mStyle.selectedTextColor = color;
        mStyle.holoBorderColor = color;
        return this;
    }

    /**
     * 设置标题
     *
     * @param title
     * @return
     */
    public WheelViewDialog<T> setTitle(String title) {
        mTitle.setText(title);
        return this;
    }

    /**
     * 设置标题颜色
     *
     * @param color
     * @return
     */
    public WheelViewDialog setTextColor(int color) {
        mTitle.setTextColor(color);
        return this;
    }

    /**
     * 设置标题大小
     *
     * @param size
     * @return
     */
    public WheelViewDialog setTextSize(int size) {
        mTitle.setTextSize(size);
        return this;
    }

    /**
     * 设置按钮文本
     *
     * @param text
     * @return
     */
    public WheelViewDialog setButtonText(String text) {
        mButton.setText(text);
        return this;
    }

    /**
     * 设置按钮文本颜色
     *
     * @param color
     * @return
     */
    public WheelViewDialog setButtonColor(int color) {
        mButton.setTextColor(color);
        return this;
    }

    /**
     * 设置按钮文本尺寸
     *
     * @param size
     * @return
     */
    public WheelViewDialog setButtonSize(int size) {
        mButton.setTextSize(size);
        return this;
    }

    /**
     * 设置数据项显示个数
     *
     * @param count
     */
    public WheelViewDialog setCount(int count) {
        mWheelView.setWheelSize(count);
        return this;
    }

    /**
     * 数据项是否循环显示
     *
     * @param loop
     */
    public WheelViewDialog setLoop(boolean loop) {
        mWheelView.setLoop(loop);
        return this;
    }

    /**
     * 设置数据项显示位置
     *
     * @param selection
     */
    public WheelViewDialog setSelection(int selection) {
        mWheelView.setSelection(selection);
        return this;
    }

    /**
     * 设置数据项
     *
     * @param arrays
     */
    public WheelViewDialog setItems(T[] arrays) {
        return setItems(Arrays.asList(arrays));
    }

    /**
     * 设置数据项
     *
     * @param list
     */
    public WheelViewDialog<T> setItems(List<T> list) {
        mWheelView.setWheelData(list);
        return this;
    }

    /**
     * 显示
     */
    public WheelViewDialog show() {
        if (!mDialog.isShowing()) {
            mDialog.show();
        }
        return this;
    }

    /**
     * 隐藏
     */
    public WheelViewDialog dismiss() {
        if (mDialog.isShowing()) {
            mDialog.dismiss();
        }
        return this;
    }

    @Override
    public void onClick(View v) {
        dismiss();
        if (null != mOnDialogItemClickListener) {
            mOnDialogItemClickListener.onItemClick(mSelectedPos, mSelectedText);
        }
    }

    public interface OnDialogItemClickListener<T> {
        void onItemClick(int position, T s);
    }
}
