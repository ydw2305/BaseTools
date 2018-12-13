package cn.ydw.www.toolslib.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


/**
 * @author 杨德望
 * Create on 2018/3/26
 * 描述: 对话框类
 */

public class MyDialogUtils {



    public interface OnBtnClickListener{
        void onSure();
        void onMaybe();
        void onCancel();
    }

    public static abstract class OnSureClickListener implements OnBtnClickListener{
        @Override
        public void onMaybe(){}

        @Override
        public void onCancel(){}
    }

    public static abstract class OnSureAndCancelClickListener implements OnBtnClickListener{
        @Override
        public void onMaybe() {}
    }
    public static abstract class OnSureAndMaybeClickListener implements OnBtnClickListener{
        @Override
        public void onCancel() {}
    }

    /**
     * 选择弹窗
     * @param context 上下文
     * @param title 标题
     * @param msg 内容
     * @param osc 有个保存退出按钮, 直接退出按钮, 返回按钮的回调
     */
    public static void selDialog(Context context, String title, String msg, final OnBtnClickListener osc){
        selDialog(context, title, msg, "确定", "取消", null, osc);
    }
    /**
     * 选择弹窗
     * @param context 上下文
     * @param title 标题
     * @param msg 内容
     * @param osc 有个保存退出按钮, 直接退出按钮, 返回按钮的回调
     */
    public static void selDialog(Context context, String title, String msg, String sureStr,
                                 final OnBtnClickListener osc){
        selDialog(context, title, msg, sureStr, null, null, osc);
    }
    /**
     * 选择弹窗
     * @param context 上下文
     * @param title 标题
     * @param msg 内容
     * @param osc 有个保存退出按钮, 直接退出按钮, 返回按钮的回调
     */
    public static void selDialog(Context context, String title, String msg, String sureStr,
                                 String cancelStr, final OnBtnClickListener osc){
        selDialog(context, title, msg, sureStr, cancelStr, null, osc);
    }
    /**
     * 选择弹窗
     * @param context 上下文
     * @param title 标题
     * @param msg 内容
     * @param osc 有个保存退出按钮, 直接退出按钮, 返回按钮的回调
     */
    public static void selDialog(Context context, String title, String msg, String sureStr,
                                 String cancelStr, String maybeStr, final OnBtnClickListener osc){
        selDialog(context, title, msg, sureStr, cancelStr, maybeStr, true, osc);
    }
    /**
     * 选择弹窗
     * @param context 上下文
     * @param title 标题
     * @param msg 内容
     * @param osc 有个保存退出按钮, 直接退出按钮, 返回按钮的回调
     */
    public static void selDialog(Context context, String title, String msg, String sureStr,
                                 String cancelStr, String maybeStr, boolean cancelAble,
                                 final OnBtnClickListener osc){
        if (context == null) return;
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context).setTitle(title)//设置对话框标题
                .setMessage(msg);//设置显示的内容
        if (sureStr != null) {
            mBuilder.setPositiveButton(sureStr, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    if (osc != null)
                        osc.onSure();
                }
            });
        }
        if (maybeStr != null) {
            mBuilder.setNegativeButton(maybeStr, new DialogInterface.OnClickListener() {//添加返回按钮
                @Override
                public void onClick(DialogInterface dialog, int which) {//响应事件
                    dialog.dismiss();
                    if (osc != null)
                        osc.onMaybe();
                }
            });
        }
        if (cancelStr != null) {
            mBuilder.setNeutralButton(cancelStr, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    if (osc != null)
                        osc.onCancel();
                }
            });
        }
        mBuilder.setCancelable(cancelAble);
        mBuilder.show();//在按键响应事件中显示此对话框
    }

    // 提示用户去应用设置界面手动开启权限
    @SuppressWarnings("unused")
    public static void showDialogTipUserGoToAppSetting(final Context context, String msg,
                                                       OnBtnClickListener osc) {
        selDialog(context, "警告", msg + "\n请在\"设置->权限管理\"中，允许这些权限",
                "确定", "取消", null, false, osc);
    }

    // 跳转到当前应用的设置界面
    public static void goToAppSetting(Context context) {
        if (context == null) return;
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        context.startActivity(intent);
    }



    /**
     * 显示点赞动画  ( FIXME 暂未测试, 所以不知道有没有问题, 而且还没优化, 待定)
     *
     * @param locationView 需要定位的view，动画将显示在其左下方
     * @param xOffset      x轴的偏移量
     * @param yOffset      y轴的偏移量
     */
    public static void showLike(View locationView, int xOffset, int yOffset, @DrawableRes int showImg) {
        if (locationView == null) {
            return;
        }
        Context context = locationView.getContext();
        if (!(context instanceof Activity)) {
            return;
        }
        //1.获取Activity最外层的DecorView
        Activity activity = (Activity) context;
        View decorView = activity.getWindow().getDecorView();
        FrameLayout frameLayout = null;
        if (decorView != null && decorView instanceof FrameLayout) {
            frameLayout = (FrameLayout) decorView;
        }
        if (frameLayout == null) {
            return;
        }
        //2.通过getLocationInWindow 获取需要显示的位置
        final ImageView likeView = new ImageView(context);
        //注意不能使用warp_content 在实际的代码运行中。高度会比GIF的高度高。因此这里直接使用GIF的大小作为ImageView的大小
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(200, 200);
        int[] outLocation = new int[2];
        locationView.getLocationInWindow(outLocation);
        // 80 和 100 是一点点测试出来的。不同的需求可以自己测出需要的偏移量
        layoutParams.leftMargin = outLocation[0] + xOffset - 80;
        layoutParams.topMargin = outLocation[1] + yOffset - 100;
        layoutParams.gravity = Gravity.START | Gravity.TOP;
        likeView.setLayoutParams(layoutParams);
        frameLayout.addView(likeView);

        //3.创建消失动画
        final Animation dismissAnimation = new AlphaAnimation(1.0f, 0.0f);
        dismissAnimation.setDuration(500);
        dismissAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ViewGroup parent = (ViewGroup) likeView.getParent();
                if (parent != null) {
                    parent.removeView(likeView);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        //4.监听GIF消失事件
        loadOneTimeGif(context, showImg/*R.drawable.ic_like_flower*/, likeView, new GifListener() {
            @Override
            public void gifPlayComplete() {
                likeView.post(new Runnable() {
                    @Override
                    public void run() {
                        int[] location = new int[2];
                        likeView.getLocationOnScreen(location);

                        likeView.startAnimation(dismissAnimation);
                    }
                } );
            }
        });


    }


    private static void loadOneTimeGif(Context context, Object model, final ImageView imageView, final GifListener gifListener) {
        RequestOptions option = new RequestOptions()
        //关闭缓存，在连续播放多个相同的Gif时，会出现第二个Gif是从最后一帧开始播放的。
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .skipMemoryCache(true);
        Glide.with(context).asGif().load(model).apply(option).listener(new RequestListener<GifDrawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                try {
                    Field gifStateField = GifDrawable.class.getDeclaredField("state");
                    gifStateField.setAccessible(true);
                    Class gifStateClass = Class.forName("com.bumptech.glide.load.resource.gif.GifDrawable$GifState");
                    Field gifFrameLoaderField = gifStateClass.getDeclaredField("frameLoader");
                    gifFrameLoaderField.setAccessible(true);
                    Class gifFrameLoaderClass = Class.forName("com.bumptech.glide.load.resource.gif.GifFrameLoader");
                    Field gifDecoderField = gifFrameLoaderClass.getDeclaredField("gifDecoder");
                    gifDecoderField.setAccessible(true);
                    Class gifDecoderClass = Class.forName("com.bumptech.glide.gifdecoder.GifDecoder");
                    Object gifDecoder = gifDecoderField.get(gifFrameLoaderField.get(gifStateField.get(resource)));
                    //noinspection unchecked
                    Method getDelayMethod = gifDecoderClass.getDeclaredMethod("getDelay", int.class);
                    getDelayMethod.setAccessible(true);
                    //设置只播放一次
                    resource.setLoopCount(1);
                    //获得总帧数
                    int count = resource.getFrameCount();
                    int delay = 0;
                    for (int i = 0; i < count; i++) {
                        //计算每一帧所需要的时间进行累加
                        delay += (int) getDelayMethod.invoke(gifDecoder, i);
                    }
                    imageView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (gifListener != null) {
                                gifListener.gifPlayComplete();
                            }
                        }
                    }, delay);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        }).into(imageView);
    }

    /**
     * Gif播放完毕回调
     */
    public interface GifListener {
        void gifPlayComplete();
    }

}
