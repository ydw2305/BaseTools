/*******************************************************************************
 * <p>Copyright(c) 2017</p>
 * <p>
 * @autor 杨德望
 * @date  ${DATE}
 * @see ${PACKAGE_NAME}
 * </p>
 *
 ******************************************************************************/

package cn.ydw.www.toolslib.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author XiaoMu
 * Created by Administrator on 2017/4/24.
 */

public class MyTimeUtils {
    public static final String yMdHm = "yyy-MM-dd HH:mm";
    public static final String yMdHms = "yyy-MM-dd HH:mm:ss";
    /**
     * // 将字符串转为时间戳
     * @param user_time 需要转变的时间字符串
     * @param format 时间格式
     * @return 时间戳
     */
    public static Long getTimeStr2Long(String user_time, String format) {
        long re_time;
        @SuppressLint ("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date d;
        try {
            d = sdf.parse(user_time);
            re_time = d.getTime();
        }catch (Exception e) {
            e.printStackTrace();
            return System.currentTimeMillis();
        }
        return re_time;
    }
    /**
     * // 将字符串转为时间
     * @param user_time 需要转变的时间字符串
     * @param format 时间格式
     * @return 时间戳
     */
    public static Date getTimeStr2Date(String user_time, String format) {
        if (TextUtils.isEmpty(user_time))
            return new Date(System.currentTimeMillis() + 60 * 1000L);
        @SuppressLint ("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date d;
        try {
            d = sdf.parse(user_time);
        }catch (ParseException e) {
            return new Date(System.currentTimeMillis() + 60 * 1000L);
        }
        return d;
    }
    /**
     * // 将时间转为字符串
     * @param date 需要转变的时间对象
     * @param format 时间格式
     * @return 时间戳
     */
    public static String getTimeDate2Str(Date date, String format) {
        @SuppressLint ("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }


    /**
     * //将时间戳转为距离现在多少天多少小时多少分多少秒后
     * @param time 时间戳
     * @return 时间字符串
     */
    public static String getTimeDistanceNowLong2StrAfter(Long time){
//        time = time - System.currentTimeMillis();
        if (time < 0){
            return "已过期";
        }
        StringBuffer sb = new StringBuffer();
        boolean b = timeFormat(time, sb);
        if (!b){
            sb.append("即将结束");
        }
        if (!sb.toString().equals("即将结束")) {
            sb.append("后截止");
        }
        return sb.toString();
    }
    /**
     * //将时间转为距离现在多少天多少小时多少分多少秒前
     * @param createTime 时间
     * @param format 时间格式
     * @return 时间字符串
     */
    public static String getTimeDistanceNowLong2StrBefore(String createTime, String format){
        if (TextUtils.isEmpty(createTime))
            return "未知";
        Long time = getTimeStr2Long(createTime, format);
        return getTimeDistanceNowLong2StrBefore(time);
    }
    /**
     * //将时间戳转为距离现在多少天多少小时多少分多少秒前
     * @param time 时间戳
     * @return 时间字符串
     */
    public static String getTimeDistanceNowLong2StrBefore(Long time){
        time = System.currentTimeMillis() - time;
        if (time < 30*1000){
            return "刚刚";
        }
        StringBuffer sb = new StringBuffer();
        boolean b = timeFormat(time, sb);
        if (b){
            sb.append("前");
        }
        return sb.toString();
    }

    /**
     * //将时间戳转为距离现在多少小时多少分多少秒前, 大于1个天则返回日期
     * @param lastTime 时间戳
     * @return 时间字符串
     */
    public static String getTime4NowLong2StrBefore(Long lastTime, String format){
        long time = System.currentTimeMillis() - lastTime;
        if (time < 30*1000){
            return "刚刚";
        }
        long mouth = (long) Math.ceil(time/24/60/60/301000.0f);// 月前
        if (mouth >= 1) {
            return getTimeDate2Str(new Date(lastTime), format);
        }
        StringBuffer sb = new StringBuffer();
        boolean b = timeFormat(time, sb);
        if (b){
            sb.append("前");
        }
        return sb.toString();
    }

    private static boolean timeFormat(Long time, StringBuffer sb) {
        //        long time = System.currentTimeMillis() - (lTime * 1000);
        long mill = (long) Math.ceil(time /1000);//秒前
        long minute = (long) Math.ceil(time/60/1000.0f);// 分钟前
        long hour = (long) Math.ceil(time/60/60/1000.0f);// 小时
        long day = (long) Math.ceil(time/24/60/60/1000.0f);// 天前
        long mouth = (long) Math.ceil(time/24/60/60/301000.0f);// 月前
        if (mouth - 1 > 0) {
            sb.append(mouth).append("个月");
        } else if (day - 1 > 0) {
            if (day >= 30) {
                sb.append("1个月");
            } else
                sb.append(day).append("天");
        } else if (hour - 1 > 0) {
            if (hour >= 24) {
                sb.append("1天");
            } else {
                sb.append(hour).append("小时");
            }
        } else if (minute - 1 > 0) {
            if (minute == 60) {
                sb.append("1小时");
            } else {
                sb.append(minute).append("分钟");
            }
        } else if (mill - 1 > 0) {
            if (mill == 60) {
                sb.append("1分钟");
            } else {
                sb.append(mill).append("秒");
            }
        } else {
            return false;
        }
        return true;
    }

    /**
     * //四舍五入, 保留n位小数点
     * @param num 需要保留几个位数
     * @return 格式化后的数据
     */
    @SuppressWarnings("unused")
    public static double formatDouble(double d, int num){
        BigDecimal bigDecimal = new BigDecimal(d);
        return bigDecimal.setScale(num, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
