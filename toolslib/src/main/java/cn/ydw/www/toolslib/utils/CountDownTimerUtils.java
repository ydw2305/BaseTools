
package cn.ydw.www.toolslib.utils;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

/**
 * @author 杨德望 create on 2017/4/26.
 * 描述: 倒计时类
 */
public class CountDownTimerUtils extends CountDownTimer {

    private TextView mTextView;
    private String mTickStr = "可重新发送", mFinishStr = "点击重新获取";
    private TimerCallBack mTimerCallBack;

    /**
     *
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    private CountDownTimerUtils(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    /**
     * 创建计时器
     * @param millisInFuture 要计时的时间, 单位是毫秒, 例如: 120 * 1000 (计时120秒)
     * @param countDownInterval 刷新的间隔, 但单位是毫秒, 例如: 1 * 1000 (每秒刷新一次)
     */
    public static CountDownTimerUtils create(long millisInFuture, long countDownInterval) {
        return new CountDownTimerUtils(millisInFuture, countDownInterval);
    }

    /**
     * 设置被刷新的控件是文本控件,
     * @param mTextView 文本控件
     */
    public CountDownTimerUtils setTextView(TextView mTextView) {
        this.mTextView = mTextView;
        return this;
    }

    /**
     * 设置文本信息, (如果有文本控件的时候, 该设置才有意义)
     * @param mTickStr 刷新时候的文本
     * @param mFinishStr 结束时候的文本
     */
    public CountDownTimerUtils setTextInfo(String mTickStr, String mFinishStr) {
        if (!TextUtils.isEmpty(mTickStr)) {
            this.mTickStr = mTickStr;
        }
        if (!TextUtils.isEmpty(mFinishStr)) {
            this.mFinishStr = mFinishStr;
        }
        return this;
    }

    /**
     * 设置计时器回调
     * @param mTimerCallBack 监听器
     */
    public CountDownTimerUtils setCallback(TimerCallBack mTimerCallBack){
        this.mTimerCallBack = mTimerCallBack;
        return this;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        if (mTimerCallBack != null) {
            mTimerCallBack.onTick(millisUntilFinished);
        }
        if (mTextView != null) {
            if (mTextView.isEnabled() || !mTextView.isSelected()) {
                mTextView.setEnabled(false); //设置无法触碰
                mTextView.setSelected(true);//设置选中状态,这时是不能点击的
            }

            long time = millisUntilFinished / 1000;
            SpannableString mStr = SpannableStringUtils.create(time + "秒后", mTickStr)
                    .setForegroundColor(0, Color.RED).builder();
            mTextView.setText(mStr);//设置倒计时时间
        }
    }

    @Override
    public void onFinish() {
        if (mTimerCallBack != null) {
            mTimerCallBack.onFinish();
        }
        if (mTextView != null) {
            if (!mTextView.isEnabled() || mTextView.isSelected()) {
                mTextView.setEnabled(true); //设置可触碰
                mTextView.setSelected(false);//设置非选中状态, 表示可以点击了
            }
            mTextView.setText(mFinishStr);
        }
    }

    public interface TimerCallBack{
        void onTick(long time);
        void onFinish();
    }
}
