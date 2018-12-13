
package cn.ydw.www.toolslib.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.lang.reflect.Method;

/**
 * 这是跟获取控件屏幕尺寸有关系的工具类,
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public final class ScreenUtils {

    /**
     * 获取屏幕显示指标对象
     *
     * @param context 上下文
     * @return 显示指标 对象
     */
    public static DisplayMetrics getScreenDisplayMetrics(Context context) {
        if (context != null) {
            // 获取屏幕宽管理器
            WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            if (manager != null) {
                Display mDefaultDisplay = manager.getDefaultDisplay();
                if (mDefaultDisplay != null) {
                    DisplayMetrics metrics = new DisplayMetrics();
                    mDefaultDisplay.getMetrics(metrics);
                    return metrics;
                }
            }
        }
        return null;
    }

    /**
     * <p>
     * // 获取屏幕密度（方法1）
     * //     DisplayMetrics dm = new DisplayMetrics();
     * //     dm = getResources().getDisplayMetrics();
     * //     float density = dm.density; // 屏幕密度（像素比例：0.75/1.0/1.5/2.0）
     * //     int densityDPI = dm.densityDpi; // 屏幕密度（每寸像素：120/160/240/320）
     *
     * @param context 上下文
     * @return 屏幕密度
     */
    public static float getScreenDensity(Context context) {
        DisplayMetrics mDM = getScreenDisplayMetrics(context);
        if (mDM != null) {
            return mDM.density;
        }
        return 0;
    }

    /**
     * 获取屏幕密度（方法2）
     *
     * @param context 上下文
     * @return 屏幕密度
     */
    public static float getScreenDensityDpi(Context context) {
        DisplayMetrics mDM = getScreenDisplayMetrics(context);
        if (mDM != null) {
            return mDM.densityDpi;
        }
        return 0;
    }


    /**
     * 获取屏幕高度，不包括虚拟功能高度 (方法 1 )
     *
     * @param context 上下文
     * @return 高度
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics mDM = getScreenDisplayMetrics(context);
        if (mDM != null) {
            return mDM.heightPixels;
        }
        return 0;
    }

    /**
     * 获取屏幕宽度 (方法 1)
     *
     * @param context 上下文
     * @return 宽度
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics mDM = getScreenDisplayMetrics(context);
        if (mDM != null) {
            return mDM.widthPixels;
        }
        return 0;
    }

    /**
     * 获取屏幕高度，不包括虚拟功能高度 (方法 2)(系统已经不建议使用了)
     *
     * @param activity 活动
     * @return 高度
     */
    public static int getScreenHeight(Activity activity) {
        if (activity != null) {
            WindowManager mWindowManager = activity.getWindowManager();
            if (mWindowManager != null) {
                Display mDefaultDisplay = mWindowManager.getDefaultDisplay();
                if (mDefaultDisplay != null) {
                    // 获取屏幕宽高（方法2）
                    return mDefaultDisplay.getHeight();// 屏幕高（像素，如：800p）
                }
            }
        }
        return 0;
    }

    /**
     * 获取屏幕宽尺寸 (系统已经不建议使用了)
     *
     * @param activity 活动
     * @return 宽度
     */
    public static int getScreenWidth(Activity activity) {
        if (activity != null) {
            WindowManager mWindowManager = activity.getWindowManager();
            if (mWindowManager != null) {
                Display mDefaultDisplay = mWindowManager.getDefaultDisplay();
                if (mDefaultDisplay != null) {
                    // 获取屏幕宽（方法2）
                    return mDefaultDisplay.getWidth();// 屏幕宽（像素，如：480px）
                }
            }
        }
        return 0;
    }

    /**
     * 获取屏幕原始尺寸高度，包括虚拟功能键高度
     *
     * @param act 活动
     * @return 高度
     */
    public static int getScreenHeightWithVirtualKey(Activity act) {
        int dpi = 0;
        Display display = act.getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        @SuppressWarnings("rawtypes")
        Class c;
        try {
            c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            dpi = dm.heightPixels;
        } catch (Exception e) {
            Logger.e("获取屏幕高度包括虚拟键异常", e);
        }
        return dpi;
    }

    /**
     * 通过反射获取状态按钮高度异常
     *
     * @param context 上下文
     * @return 高度
     */
    public static int getStatusBarHeight(Context context) {
        if (context == null) return 0;
        int result = 0;
        try {
            Resources mResources = context.getResources();
            int resourceId = mResources.getIdentifier(
                    "status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = mResources.getDimensionPixelSize(resourceId);
            }
        } catch (Exception e) {
            Logger.e("获取状态按钮高度异常", e);
        }
        return result;
    }

    /**
     * 通过反射获取虚拟按键的高度
     *
     * @param context 上下文
     * @return 高度
     */
    public static int getNavigationBarHeight(Context context) {
        int result = 0;
        try {
            if (hasNavBar(context)) {
                Resources res = context.getResources();
                int resourceId = res.getIdentifier("navigation_bar_height",
                        "dimen", "android");
                if (resourceId > 0) {
                    result = res.getDimensionPixelSize(resourceId);
                }
            }
        } catch (Exception e) {
            Logger.e("反射获取虚拟按键高度异常", e);
        }
        return result;
    }

    /**
     * 检查是否存在虚拟按键栏
     *
     * @param context 上下文
     * @return 是否存在虚拟键
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static boolean hasNavBar(Context context) {
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("config_showNavigationBar",
                "bool", "android");
        if (resourceId != 0) {
            boolean hasNav = res.getBoolean(resourceId);
            // check override flag
            String sNavBarOverride = getNavBarOverride();
            if ("1".equals(sNavBarOverride)) {
                hasNav = false;
            } else if ("0".equals(sNavBarOverride)) {
                hasNav = true;
            }
            return hasNav;
        } else { // fallback
            return !ViewConfiguration.get(context).hasPermanentMenuKey();
        }
    }

    /**
     * 判断虚拟按键栏是否重写
     *
     * @return 虚拟栏是否被重写
     */
    private static String getNavBarOverride() {
        String sNavBarOverride = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                @SuppressLint("PrivateApi")
                Class c = Class.forName("android.os.SystemProperties");
                //noinspection unchecked
                Method m = c.getDeclaredMethod("get", String.class);
                m.setAccessible(true);
                sNavBarOverride = (String) m.invoke(null, "qemu.hw.mainkeys");
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return sNavBarOverride;
    }

    /**
     * 隐藏虚拟按键，并且全屏
     */
    @SuppressLint("ObsoleteSdkInt")
    public static void hideNavBar(Activity act) {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = act.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = act.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                    //                    | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                    | View.SYSTEM_UI_FLAG_IMMERSIVE;
            decorView.setSystemUiVisibility(uiOptions);
            act.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * 启调键盘   (两种启调方式, 在不同机型存在启调差异)
     */
    public static void showKeyboard(final Context mContext) {
        if (mContext == null) return;
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(InputMethodManager.RESULT_UNCHANGED_SHOWN, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    /**
     * 启动键盘
     *
     * @param act  活动页
     * @param view 被指定视图
     */
    public static void showKeyboard(Activity act, EditText view) {
        if (act == null || act.isFinishing() || view == null) return;
        InputMethodManager imm = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            view.findFocus();
            view.requestFocus();
            imm.showSoftInput(view, InputMethodManager.RESULT_UNCHANGED_SHOWN);
        }
    }

    /**
     * 隐藏键盘
     *
     * @param activity 活动页
     */
    public static void hintSoftInput(Activity activity) {
        if (activity == null || activity.isFinishing()) return;
        View parent = ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
        hideKeyboard(activity, parent);
    }

    /**
     * 隐藏键盘
     *
     * @param act 活动页
     */
    public static void hideKeyboard(Activity act) {
        if (act == null || act.isFinishing()) return;
        final View v = act.getWindow().peekDecorView();
        hideKeyboard(act, v);
    }

    /**
     * 隐藏键盘
     *
     * @param context 活动页
     * @param view    视图
     */
    public static void hideKeyboard(Context context, View view) {
        if (context == null || view == null) return;
        IBinder mWindowToken = view.getWindowToken();
        if (mWindowToken != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(mWindowToken, InputMethodManager.RESULT_UNCHANGED_SHOWN);
//                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);//解决三星不能隐藏键盘的问题, 等遇到了再处理,
//                  (注: 现在如果使用, 则存在未被开启键盘的时候, 调用该方法, 会启动键盘)
            }
        }
    }

    public static int getScreenBrightness(Activity act) {
        try {
            return Settings.System.getInt(act.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            Logger.e("获取屏幕亮度异常", e);
        }
        return 0;
    }

    /**
     * 设置当前屏幕亮度值 0--255，并使之生效
     * @param act 活动页
     * @param value 亮度值   0--255 之间
     */
    public static void setScreenBrightness(Activity act, float value) {
        value = value>255.0f? 255.0f: value< 0.0f? 0.0f: value;
        WindowManager.LayoutParams lp = act.getWindow().getAttributes();
        lp.screenBrightness = lp.screenBrightness + (value) / 255.0f;
        if (lp.screenBrightness > 1) {
            lp.screenBrightness = 1;
            //              vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            //              long[] pattern = {10, 200}; // OFF/ON/OFF/ON...
            //              vibrator.vibrate(pattern, -1);
        } else if (lp.screenBrightness < 0.2) {
            lp.screenBrightness = (float) 0.2;
            //              vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            //              long[] pattern = {10, 200}; // OFF/ON/OFF/ON...
            //              vibrator.vibrate(pattern, -1);
        }
        act.getWindow().setAttributes(lp);

        // 保存设置的屏幕亮度值
         Settings.System.putInt(act.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, (int) value);
    }


    /**
     * 获取屏幕图片, 类似截图
     *
     * @param act 活动页
     * @return 位图
     */
    public static Bitmap getWindowBitmap(Activity act) {
        if (act != null && !act.isFinishing()) {
            Window mWindow = act.getWindow();
            if (mWindow != null) {
                View parent = mWindow.getDecorView();
                if (parent != null) {
                    parent.setDrawingCacheEnabled(true);
                    parent.buildDrawingCache(true);
                    Bitmap mBitmap = parent.getDrawingCache();
                    parent.setDrawingCacheEnabled(false);
                    parent.buildDrawingCache(false);
                    return mBitmap;
                }
            }
        }
        return null;
    }

    ////////////////////////////////////////// covert ////////////////////////////////////////////////

    /**
     * 根据屏幕密度, 将dp(device independent pixels 无关像素点) 转 px( 像素)
     */
    @Deprecated
    public static int dp2px(Context context, float dp) {
        if (context != null) {
            Resources mResources = context.getResources();
            if (mResources != null) {
                DisplayMetrics metrics = mResources.getDisplayMetrics();
                if (metrics != null) {
                    return (int) (dp * (metrics.densityDpi / 160f));
                }
            }
        }
        return 0;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        if (context != null ) {
            Resources mResources = context.getResources();
            if (mResources != null) {
                DisplayMetrics mMetrics = mResources.getDisplayMetrics();
                if (mMetrics != null) {
                    final float scale = mMetrics.density;
                    return (int) (dpValue * scale + 0.5f);
                }
            }
        }
        return 0;
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        if (context != null ) {
            Resources mResources = context.getResources();
            if (mResources != null) {
                DisplayMetrics mMetrics = mResources.getDisplayMetrics();
                if (mMetrics != null) {
                    final float scale = mMetrics.density;
                    return (int) (pxValue / scale + 0.5f);
                }
            }
        }
        return 0;
    }
    /**
     * 根据手机的分辨率从 sp 的单位 转成为 px(像素)
     */
    public static int sp2px(Context context, float sp) {
        if (context != null ) {
            Resources mResources = context.getResources();
            if (mResources != null) {
                DisplayMetrics mMetrics = mResources.getDisplayMetrics();
                if (mMetrics != null) {
                    final float scale = mMetrics.scaledDensity;
                    return (int) (sp  * sp + 0.5f);
                }
            }
        }
        return 0;
    }
    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 sp
     */
    public static int px2sp(Context context, float px) {
        if (context != null ) {
            Resources mResources = context.getResources();
            if (mResources != null) {
                DisplayMetrics mMetrics = mResources.getDisplayMetrics();
                if (mMetrics != null) {
                    final float scale = mMetrics.scaledDensity;
                    return (int) (px / scale + 0.5f);
                }
            }
        }
        return 0;
    }

}
