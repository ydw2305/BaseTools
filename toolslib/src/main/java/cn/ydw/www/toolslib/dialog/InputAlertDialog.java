package cn.ydw.www.toolslib.dialog;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import cn.ydw.www.toolslib.R;
import cn.ydw.www.toolslib.base.BaseDialogFragment;

/**
 * ========================================
 *
 * @author 杨德望
 * Create on 2018/8/17
 * 描述: 输入弹窗
 * =========================================
 */
public class InputAlertDialog extends BaseDialogFragment implements View.OnClickListener {

    private TextView mTvTitle;
    private TextView mTvMsg;
    private EditText mEdInput;
    private TextView mTvCancel;
    private TextView mTvSure;

    private AlertCallback.OnBtnListener mBtnListener;
    private CharSequence mTitle, mMsg, defaultStr, hintStr, cancelTxt, sureTxt;
    private int inputType = -1;
    private InputFilter[] filters;

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_window_input_dialog_view, container, false);
    }

    @Override
    public void initData() {
        mTvTitle = getRootView().findViewById(R.id.tv_winInput_title);
        mTvMsg = getRootView().findViewById(R.id.tv_winInput_msg);
        mEdInput = getRootView().findViewById(R.id.ed_winInput_msg);
        mTvCancel = getRootView().findViewById(R.id.tv_winInput_cancel);
        mTvSure = getRootView().findViewById(R.id.tv_winInput_sure);
        mTvCancel.setOnClickListener(this);
        mTvSure.setOnClickListener(this);

        // 初始化
        setTitle(mTitle);
        setMsg(mMsg);
        setInputDefault(defaultStr);
        setInputHint(hintStr);
        setInputType(inputType);
        setInputFilter(filters);
        setCancelTxt(cancelTxt);
        setSureTxt(sureTxt);
    }

    @Override
    public void onClick(View v) {
        int mId = v.getId();
        if (mId == R.id.tv_winInput_cancel) {
            if (mBtnListener != null) {
                mBtnListener.onBtnClick(this, AlertCallback.cancelPosition, mEdInput);
            }
        } else if (mId == R.id.tv_winInput_sure) {
            if (mBtnListener != null && mEdInput != null) {
                mBtnListener.onBtnClick(this, AlertCallback.surePosition, mEdInput);
            }
        }
    }

    /**
     * 设置标题
     *
     * @param mTitle 标题
     * @return this
     */
    public InputAlertDialog setTitle(CharSequence mTitle) {
        if (mTvTitle != null) {
            if (TextUtils.isEmpty(mTitle)) {
                if (mTvTitle.getVisibility() != View.GONE) {
                    mTvTitle.setVisibility(View.GONE);
                }
            } else {
                if (mTvTitle.getVisibility() != View.VISIBLE) {
                    mTvTitle.setVisibility(View.VISIBLE);
                }
                mTvTitle.setText(mTitle);
            }
        } else {
            this.mTitle = mTitle;
        }
        return this;
    }

    /**
     * 设置内容
     *
     * @param mMsg 内容
     * @return this
     */
    public InputAlertDialog setMsg(CharSequence mMsg) {
        if (mTvMsg != null) {
            if (TextUtils.isEmpty(mMsg)) {
                if (mTvMsg.getVisibility() != View.GONE) {
                    mTvMsg.setVisibility(View.GONE);
                }
            } else {
                if (mTvMsg.getVisibility() != View.VISIBLE) {
                    mTvMsg.setVisibility(View.VISIBLE);
                }
                mTvMsg.setText(mMsg);
            }
        } else {
            this.mMsg = mMsg;
        }
        return this;
    }

    /**
     * 设置输入框默认显示的字符
     *
     * @param defaultStr 默认文字
     * @return this
     */
    public InputAlertDialog setInputDefault(CharSequence defaultStr) {
        if (mEdInput != null) {
            mEdInput.setText(defaultStr == null ? "" : defaultStr);
        } else {
            this.defaultStr = defaultStr;
        }
        return this;
    }

    /**
     * 设置输入框默认提示文字
     *
     * @param hintStr 提示文字
     * @return this
     */
    public InputAlertDialog setInputHint(CharSequence hintStr) {
        if (mEdInput != null) {
            mEdInput.setHint(hintStr == null ? "" : hintStr);
        } else {
            this.hintStr = hintStr;
        }
        return this;
    }

    /**
     * 设置输入类型
     *
     * @param inputType 参考 {@link InputType}
     * @return this
     */
    public InputAlertDialog setInputType(int inputType) {
        if (mEdInput != null) {
            if (inputType >= 0x00000000) {
                mEdInput.setInputType(inputType);
            }
        } else {
            this.inputType = inputType;
        }
        return this;
    }

    /**
     * 设置输入框过滤器
     *
     * @param filters 过滤器
     * @return this
     */
    public InputAlertDialog setInputFilter(InputFilter[] filters) {
        if (mEdInput != null) {
            if (filters != null && filters.length > 0) {
                mEdInput.setFilters(filters);
            }
        } else {
            this.filters = filters;
        }
        return this;
    }

    /**
     * 设置取消按钮文本
     *
     * @param cancelTxt 取消按钮文本
     * @return this
     */
    public InputAlertDialog setCancelTxt(CharSequence cancelTxt) {
        if (mTvCancel != null) {
            if (!TextUtils.isEmpty(cancelTxt)) {
                mTvCancel.setText(cancelTxt);
            }
        } else {
            this.cancelTxt = cancelTxt;
        }
        return this;
    }

    /**
     * 设置确认按钮文本
     *
     * @param sureTxt 确认按钮文本
     * @return this
     */
    public InputAlertDialog setSureTxt(CharSequence sureTxt) {
        if (mTvSure != null) {
            if (!TextUtils.isEmpty(sureTxt)) {
                mTvSure.setText(sureTxt);
            }
        } else {
            this.sureTxt = sureTxt;
        }
        return this;
    }

    /**
     * 设置按钮点击监听
     *
     * @param mBtnListener 监听
     * @return this
     */
    public InputAlertDialog setOnBtnClickListener(AlertCallback.OnBtnListener mBtnListener) {
        this.mBtnListener = mBtnListener;
        return this;
    }

}
