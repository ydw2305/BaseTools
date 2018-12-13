/*
 *
 * *****************************************************************************
 *  * @author 小沐
 *  * @file_name ${file_name}
 *  * @package_name：${package_name}
 *  * @project_name：${project_name}
 *  * @department：IT部
 *  * @date: ${date} ${time}
 *  * @version
 *  * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 *  * ****************************************************************************
 *  * ${tags}
 *
 */

package cn.ydw.www.toolslib.utils;

public class Logger {

    private static boolean gIsLog = true;
    private static final String TAG = "打印";

    public static void setLog(boolean isLog) {
        Logger.gIsLog = isLog;
    }

    public static void v(String msg) {
        if (gIsLog) {
            android.util.Log.v(TAG, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (gIsLog) {
            android.util.Log.v(tag, msg);
        }
    }

    /**
     * Send a {@link android.util.Log#VERBOSE} log message and log the exception.
     *
     * @param tag
     *            Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg
     *            The message you would like logged.
     * @param tr
     *            An exception to log
     */
    public static void v(String tag, String msg, Throwable tr) {
        if (gIsLog) {
            android.util.Log.v(tag, msg, tr);
        }
    }
    public static void d(String msg) {
        if (gIsLog) {
            android.util.Log.d(TAG, msg);
        }

    }

    public static void d(String tag, String msg) {
        if (gIsLog) {
            android.util.Log.d(tag, msg);
        }
    }

    /**
     * Send a {@link android.util.Log#DEBUG} log message and log the exception.
     *
     * @param tag
     *            Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg
     *            The message you would like logged.
     * @param tr
     *            An exception to log
     */
    public static void d(String tag, String msg, Throwable tr) {
        if (gIsLog) {
            android.util.Log.d(tag, msg, tr);
        }
    }

    public static void i(String msg){
        if (gIsLog) {
            android.util.Log.i(TAG, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (gIsLog) {
            android.util.Log.i(tag, msg);
        }
    }

    /**
     * Send a {@link android.util.Log#INFO} log message and log the exception.
     *
     * @param tag
     *            Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg
     *            The message you would like logged.
     * @param tr
     *            An exception to log
     */
    public static void i(String tag, String msg, Throwable tr) {
        if (gIsLog) {
            android.util.Log.i(tag, msg, tr);
        }

    }
    public static void w(String msg){
        if (gIsLog) {
            android.util.Log.w(TAG, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (gIsLog) {
            android.util.Log.w(tag, msg);
        }
    }

    /**
     * Send a {@link android.util.Log#WARN} log message and log the exception.
     *
     * @param tag
     *            Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg
     *            The message you would like logged.
     * @param tr
     *            An exception to log
     */
    public static void w(String tag, String msg, Throwable tr) {
        if (gIsLog) {
            android.util.Log.w(tag, msg, tr);
        }

    }

    public static void e(String msg) {
        if (gIsLog) {
            android.util.Log.e(TAG, msg);
        }
    }

    /**
     * Send an {@link android.util.Log#ERROR} log message.
     *
     * @param tag
     *            Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg
     *            The message you would like logged.
     */
    public static void e(String tag, String msg) {
        if (gIsLog) {
            android.util.Log.e(tag, msg);
        }
    }

    /**
     * Send a {@link android.util.Log#ERROR} log message and log the exception.
     *
     * @param tag
     *            Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg
     *            The message you would like logged.
     * @param tr
     *            An exception to log
     */
    public static void e(String tag, String msg, Throwable tr) {
        if (gIsLog) {
            android.util.Log.e(tag, msg, tr);
        }
    }

    public static void e(String msg, Throwable tr) {
        if (gIsLog) {
            android.util.Log.e(TAG, msg, tr);
        }
    }

}
