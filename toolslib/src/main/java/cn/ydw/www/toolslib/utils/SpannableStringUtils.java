package cn.ydw.www.toolslib.utils;

import android.support.annotation.ColorInt;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.LeadingMarginSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.widget.TextView;

/**
 * =====================================
 * 作    者: 杨德望
 * 版    本：${VERSION_CODE}.
 * 创建日期：2018/3/19.
 * 描    述：
 * =====================================
 */
public final class SpannableStringUtils {

    private CharSequence[] mBaseStr;

    private SpannableString mSpannableString;
    private StringBuilder mBuilder;

    /**
     * @param baseStr 要格式化是字符串
     *
     */
    private SpannableStringUtils(CharSequence... baseStr) {
        mBaseStr = baseStr;
        mBuilder = new StringBuilder();
        for (CharSequence s: baseStr) {
            mBuilder.append(s);
        }
        mSpannableString = new SpannableString(mBuilder);
    }

    /**
     * 设置基本字符
     * @param baseStr 全部字符
     * @throws  NullPointerException
     *          If the <tt>format</tt> is <tt>null</tt>
     * @return 字符编译器
     */
    public static SpannableStringUtils create(CharSequence... baseStr) {
        if (baseStr == null || baseStr.length <= 0) {
            throw new NullPointerException();
        }
        return new SpannableStringUtils(baseStr);
    }

    /**
     * 设置文字前置色
     * @param index 起始位置
     * @param color 前置色
     * @return 字符编译器
     */
    public SpannableStringUtils setForegroundColor(int index, @ColorInt int color){
        if (index >= 0 && index < mBuilder.length()) {
            CharSequence s = mBaseStr[index];
            if (!TextUtils.isEmpty(s)) {
                int start = mBuilder.indexOf(s.toString());
                int end = start + s.length();
                mSpannableString.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            }
        }
        return this;
    }

    /**
     * 设置文字前置色
     * @param index 起始位置
     * @param color 背景色
     * @return 字符编译器
     */
    public SpannableStringUtils setBackgroundColor(int index, @ColorInt int color) {
        if (index >= 0 && index < mBuilder.length()) {
            CharSequence s = mBaseStr[index];
            if (!TextUtils.isEmpty(s)) {
                int start = mBuilder.indexOf(s.toString());
                int end = start + s.length();
                mSpannableString.setSpan(new BackgroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return this;
    }

    /**
     * 设置文字类型
     * @param index 起始位置
     * @param style 风格
     * @return 字符编译器
     */
    public SpannableStringUtils setStyle(int index, int style){
        if (index >= 0 && index < mBuilder.length()) {
            CharSequence s = mBaseStr[index];
            if (!TextUtils.isEmpty(s)) {
                int start = mBuilder.indexOf(s.toString());
                int end = start + s.length();
                mSpannableString.setSpan(new StyleSpan(style), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return this;
    }

    /**
     * 设置文字大小
     * @param index 起始位置
     * @param size 大小
     * @return 字符编辑器
     */
    public SpannableStringUtils setSize(int index, int size) {
        if (index >= 0 && index < mBuilder.length()) {
            CharSequence s = mBaseStr[index];
            if (!TextUtils.isEmpty(s)) {
                int start = mBuilder.indexOf(s.toString());
                int end = start + s.length();
                mSpannableString.setSpan(new AbsoluteSizeSpan(size), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return this;
    }
    /**
     * 设置缩进
     * @param index 起始位置
     * @param first 首行缩进
     * @param rest 剩余行缩进
     * @return 字符编辑器
     */
    public SpannableStringUtils setLeadingMargin(int index, int first, int rest) {
        if (index >= 0 && index < mBuilder.length()) {
            CharSequence s = mBaseStr[index];
            if (!TextUtils.isEmpty(s)) {
                int start = mBuilder.indexOf(s.toString());
                int end = start + s.length();
                mSpannableString.setSpan(new LeadingMarginSpan.Standard(first, rest),
                        start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return this;
    }


    /**
     * 指定区域点击了会触发跳转 (包含起点不包含终点)
     * @param index 起始位置
     * @param tv 为主体设置事件模式
     * @return 字符编辑器
     */
    public SpannableStringUtils setCall(int index, TextView tv, URLSpan span){
        if (index >= 0 && index < mBuilder.length()) {
            CharSequence s = mBaseStr[index];
            if (!TextUtils.isEmpty(s) && tv != null) {
                int start = mBuilder.indexOf(s.toString());
                int end = start + s.length();

//        String urlHeader = "tel:";//打电话
//        String urlHeader = "smsto";//发短信
//        String urlHeader = "mailto:";//发邮件
//        String urlHeader = "http://";//跳网址
                mSpannableString.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                //设置TextView可点击
                tv.setMovementMethod(LinkMovementMethod.getInstance());
            }
        }
        return this;
    }
    /**
     * 指定区域可点击 (包含起点不包含终点)
     * @param index 起始位置
     * @param tv 为主体设置事件模式
     * @return 字符编辑器
     */
    public SpannableStringUtils setClick(int index, TextView tv, ClickableSpan span){
        if (index >= 0 && index < mBuilder.length()) {
            CharSequence s = mBaseStr[index];
            if (!TextUtils.isEmpty(s)) {
                int start = mBuilder.indexOf(s.toString());
                int end = start + s.length();

                mSpannableString.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                //设置TextView可点击
                tv.setMovementMethod(LinkMovementMethod.getInstance());
            }
        }
        return this;
    }
    /**
     *  把指定区域的文字，换成图片 (包含起点不包含终点)
     * @param index 起始位置
     * @param tv 为主体设置事件模式
     * @return 字符编辑器
     */
    public SpannableStringUtils setImage(int index, TextView tv, ImageSpan span){
        if (index >= 0 && index < mBuilder.length()) {
            CharSequence s = mBaseStr[index];
            if (!TextUtils.isEmpty(s)) {
                int start = mBuilder.indexOf(s.toString());
                int end = start + s.length();

                mSpannableString.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                //设置TextView可点击
                tv.setMovementMethod(LinkMovementMethod.getInstance());
            }
        }
        return this;
    }


    /**
     *  设置下划线
     * @param index 起始位置
     * @return 字符编辑器
     */
    public SpannableStringUtils setUnderline(int index) {
        if (index >= 0 && index < mBuilder.length()) {
            CharSequence s = mBaseStr[index];
            if (!TextUtils.isEmpty(s)) {
                int start = mBuilder.indexOf(s.toString());
                int end = start + s.length();
                mSpannableString.setSpan(new UnderlineSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return this;
    }

    /**
     *  设置删除线
     * @param index 起始位置
     * @return 字符编辑器
     */
    public SpannableStringUtils setDiscount(int index) {
        if (index >= 0 && index < mBuilder.length()) {
            CharSequence s = mBaseStr[index];
            if (!TextUtils.isEmpty(s)) {
                int start = mBuilder.indexOf(s.toString());
                int end = start + s.length();
                mSpannableString.setSpan(new StrikethroughSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return this;
    }

    public SpannableString builder(){
        return mSpannableString;
    }
}
