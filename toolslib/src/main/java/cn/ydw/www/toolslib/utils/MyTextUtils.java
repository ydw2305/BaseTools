
package cn.ydw.www.toolslib.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.ColorStateList;
import android.support.annotation.ColorInt;
import android.widget.TextView;

import java.math.BigDecimal;

/**
 * @author 杨德望 create on 2018/7/19
 * 描述: 文本的处理工具类
 */
@SuppressWarnings("WeakerAccess")
public class MyTextUtils {

    /**
     * ----------截取图片网址字符串-------------
     **/
    public static String appandImgUrl(String imgUrl, int width, int height) {
        if (width <= 0 || height <= 0) {
            return imgUrl;
        }
        int i = imgUrl.lastIndexOf('.');
        int indexOf = imgUrl.indexOf("_x_");
        if (i == -1 || indexOf != -1)
            return imgUrl;
        String sec = imgUrl.substring(i, imgUrl.length());//获取后缀
        String fri = imgUrl.substring(0, imgUrl.length() - sec.length());//获取前面的网址
        if (sec.length() == imgUrl.length() || fri.length() == imgUrl.length())
            return imgUrl;
        return fri + "_x_" + String.valueOf(width) + "_x_" + String.valueOf(height) + sec;
    }


    /**
     * -------------设置文本选中字体颜色--------------------
     **/
    public static void setSelectorColor(TextView tv, @ColorInt int normal, @ColorInt int select) {
        int[] colors = new int[]{normal, select};
        int[][] states = new int[2][];
        states[0] = new int[]{-android.R.attr.state_selected};//未选中
        states[1] = new int[]{android.R.attr.state_selected};//选中

        ColorStateList colorStateList = new ColorStateList(states, colors);
        tv.setTextColor(colorStateList);
    }

    /**
     * 设置剪切板文字
     *
     * @param mContext 上下文
     * @param s        选中内容
     *                 注: 如需自由复制TextView等控件的文字，只需在该控件上
     *                 加上 android:textIsSelectable="true"，或者java代码加 setTextIsSelectable（true）;
     *                 参考: {@link #setClipData(Context, ClipData)}
     */
    public static void setClipData(Context mContext, CharSequence s) {
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        if (cm != null) {
            // 创建普通字符型ClipData
            ClipData mClipData = ClipData.newPlainText("已选择", s);
            // 将ClipData内容放到系统剪贴板里。
            cm.setPrimaryClip(mClipData);
        }
    }

    /**
     * 设置剪切板文字
     *
     * @param mContext  上下文
     * @param mClipData 选中内容
     *                  注: 如需自由复制TextView等控件的文字，只需在该控件上
     *                  加上 android:textIsSelectable="true"，或者java代码加 setTextIsSelectable（true）;
     *                  // 创建普通字符型ClipData
     *                  ClipData mClipData = ClipData.newPlainText("已选择", s);
     *                  // 创建URL型ClipData：
     *                  ClipData mClipData = ClipData.newRawUri("Label",Uri.parse("http://www.baidu.com"));
     *                  // 创建Intent型ClipData：
     *                  ClipData mClipData = ClipData.newIntent("Label", intent);
     */
    public static void setClipData(Context mContext, ClipData mClipData) {
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        if (cm != null) {
            // 将ClipData内容放到系统剪贴板里。
            cm.setPrimaryClip(mClipData);
        }
    }

    /**
     * 获取剪切板文字
     * @param mContext 上下文
     * @return 文字字符
     */
    public static CharSequence getClipDataTxt(Context mContext) {
        //获取剪贴板管理器：
        ClipData.Item mDataItem = getClipDataItem(mContext);
        if (mDataItem != null) {
            return mDataItem.getText();
        }
        return null;
    }
    /**
     * 获取剪切板文字
     * @param mContext 上下文
     * @return 剪切板
     */
    public static ClipData.Item  getClipDataItem(Context mContext) {
        //获取剪贴板管理器：
        ClipData mClipData = getClipData(mContext);
        if (mClipData != null && mClipData.getItemCount() > 0) {
            return mClipData.getItemAt(0);
        }
        return null;
    }
    /**
     * 获取剪切板文字
     * @param mContext 上下文
     * @return 剪切板
     */
    public static ClipData getClipData(Context mContext) {
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        if (cm != null) {
            return cm.getPrimaryClip();
        }
        return null;
    }

    /**
     * 四舍五入
     * @param decimal 要被四舍五入的数
     * @param scaleNum 小数点后面留几位,
     * @return 处理后的数
     */
    public static float roundDecimal(double decimal, int scaleNum) {
        BigDecimal bigDecimal = new BigDecimal(decimal)
                .setScale(scaleNum, BigDecimal.ROUND_HALF_UP);//两位小数, 四舍五入
        return bigDecimal.floatValue();
    }
}
