package cn.ydw.www.toolslib.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author 杨德望 create on 2017/1/4.
 * 描述: 工具存储类
 */
public class SPUtils {

    /**
     * 写入字符串类型数据
     * @param context 上下文
     * @param key 关键字
     * @param value 参数
     */
    @SuppressWarnings ("unused")
    public static void writeStringInfo(Context context, String key,String value) {
        SharedPreferences sp = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key,value);
        edit.apply();
    }

    /**
     * 读取字符串类型数据
     * @param context 上下文
     * @param key 关键字
     * @return 返回类型
     */
    @SuppressWarnings ("unused")
    public static String readStringInfo(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(key,Context.MODE_PRIVATE);
        return sp.getString(key,"");
    }

    /**
     * 写入布尔类型数据
     * @param context 上下文
     * @param key 关键字
     * @param value 参数
     */
    @SuppressWarnings ("unused")
    public static void writeBooleanInfo(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean(key, value);
        edit.apply();
    }

    /**
     * 读取布尔类型数据
     * @param context 上下文
     * @param key 关键字
     * @return 返回类型, 默认FALSE
     */
    @SuppressWarnings("unused")
    public static boolean readBooleanInfo(Context context, String key) {
        return readBooleanInfo(context, key, false);
    }
    /**
     * 读取布尔类型数据
     * @param context 上下文
     * @param key 关键字
     * @return 返回类型, 默认FALSE
     */
    @SuppressWarnings("WeakerAccess")
    public static boolean readBooleanInfo(Context context, String key, boolean defaultState) {
        SharedPreferences sp = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defaultState);
    }

    /**
     * 写入整型数据
     * @param context 上下文
     * @param key 关键字
     * @param value 参数
     */
    @SuppressWarnings("unused")
    public static void writeIntInfo(Context context, String key, int value) {
        SharedPreferences sp = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putInt(key, value);
        edit.apply();
    }

    /**
     * 读取整型数据
     * @param context 上下文
     * @param key 关键字
     * @return 返回类型
     */
    @SuppressWarnings ("unused")
    public static int readIntInfo(Context context, String key) {
        return readIntInfo(context, key, -1);
    }
    /**
     * 读取整型数据
     * @param context 上下文
     * @param key 关键字
     * @return 返回类型
     */
    @SuppressWarnings("WeakerAccess")
    public static int readIntInfo(Context context, String key, int defaultValue) {
        SharedPreferences sp = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        return sp.getInt(key, defaultValue);
    }

}
