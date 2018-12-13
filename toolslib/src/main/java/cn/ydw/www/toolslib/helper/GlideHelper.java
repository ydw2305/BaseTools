package cn.ydw.www.toolslib.helper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.io.File;

import cn.ydw.www.toolslib.R;
import cn.ydw.www.toolslib.listener.NetMoreCallBack;

/**
 * =====================================
 * 作    者: 杨德望
 * 版    本：${VERSION_CODE}.
 * 创建日期：2018/3/10.
 * 描    述：
 * =====================================
 */
public final class GlideHelper {

    private static RequestOptions requestOptions;
    private static RequestOptions requestOptionsOverride;
    private static RequestOptions circleRequestOptions;
    private static DrawableTransitionOptions transitionOptions;

    static {
        requestOptions = new RequestOptions().skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        requestOptionsOverride = new RequestOptions().skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        circleRequestOptions = RequestOptions.circleCropTransform()
                .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        transitionOptions = new DrawableTransitionOptions().crossFade(1000);
    }

    //////////////////////////////////////// 加载图片 ////////////////////////////////////////////////

    /**
     * 加载图片
     * @param path 图片路径或网址
     * @param iv 图片控件
     */
    public static void displayImage(Object path, ImageView iv){
        if (iv == null) return;
        displayImage(iv.getContext(), path, iv, requestOptions.placeholder(R.drawable.bg_gray)
                .error(R.drawable.bg_black), transitionOptions);
    }
    /**
     * 加载图片
     * @param mContext 上下文
     * @param path 图片路径或网址
     * @param iv 图片控件
     */
    public static void displayImage(Context mContext, Object path, ImageView iv){
        displayImage(mContext, path, iv, requestOptions.placeholder(R.drawable.bg_gray)
                .error(R.drawable.bg_black), transitionOptions);
    }
    /**
     * 加载图片, 重置宽高度
     * @param mContext 上下文
     * @param path 图片路径或网址
     * @param iv 图片控件
     */
    public static void displayImage(Context mContext, Object path, ImageView iv, int widthAndHeigh){
        displayImage(mContext, path, iv, requestOptionsOverride.override(widthAndHeigh)
                .placeholder(R.drawable.bg_black).error(R.drawable.bg_black), transitionOptions);
    }

    /**
    /**
     * 加载图片, 转变为圆形
     * @param mContext 上下文
     * @param path 图片路径或网址
     * @param iv 图片控件
     */
    public static void displayImageCircle(Context mContext, Object path, ImageView iv) {
        displayImage(mContext, path, iv, circleRequestOptions
                .error(R.mipmap.ic_empty_failed), transitionOptions);
    }

    ///////////////////////////////////////// 重启或停止 //////////////////////////////////////////////

    /**
     * 适用于 RecyclerView 滑动时, 启动或暂停 glide 加载
     */
    public static void callStartOrStop(Context mContext, boolean isStop){
        if (mContext == null) {
            return;
        }
        if (isStop) {
            Glide.with(mContext).pauseRequests();
        } else Glide.with(mContext).resumeRequests();
    }
    /**
     * 适用于 RecyclerView 滑动时, 启动或暂停 glide 加载
     */
    public static void callStartOrStop(Activity act, boolean isStop){
        if (act == null || act.isFinishing()) {
            return;
        }
        if (isStop) {
            Glide.with(act).pauseRequests();
        } else Glide.with(act).resumeRequests();
    }
    /**
     * 适用于 RecyclerView 滑动时, 启动或暂停 glide 加载
     */
    public static void callStartOrStop(Fragment fragment, boolean isStop){
        if (fragment == null || fragment.isHidden() || fragment.isRemoving()|| fragment.isDetached()) {
            return;
        }
        if (isStop) {
            Glide.with(fragment).pauseRequests();
        } else Glide.with(fragment).resumeRequests();
    }

    //////////////////////////////////////// glide 基本处理 ///////////////////////////////////////////

    /**
     * 使用glide加载图片, 兼容GIF, 使用加载动画
     *
     * @param mContext 上下文
     * @param imgUrl   图片网址
     * @param iv       图片控件
     */
    private static void displayImage(Context mContext, Object imgUrl, ImageView iv,
                                     RequestOptions mOptions,
                                     DrawableTransitionOptions mTransitionOptions) {
        if (mContext == null || iv == null || imgUrl == null) return;
        if ((String.valueOf(imgUrl).toLowerCase()).contains(".gif")) {
            Glide.with(mContext).asGif().load(imgUrl).apply( mOptions)
                    .transition(mTransitionOptions).into(iv);
        } else if (imgUrl instanceof String || imgUrl instanceof Uri || imgUrl instanceof File
                || imgUrl instanceof Bitmap || imgUrl instanceof Drawable){
            Glide.with(mContext).load(imgUrl).apply(mOptions).transition(mTransitionOptions).into(iv);
        }
    }
    /**
     * 使用glide加载图片, 兼容GIF, 使用加载动画
     *
     * @param mContext 上下文
     * @param imgUrl   图片网址
     * @param iv       图片控件
     */
    private static void displayImage(Context mContext, Object imgUrl, ImageView iv,
                                     RequestOptions mOptions,
                                     DrawableTransitionOptions mTransitionOptions, final NetMoreCallBack callBack) {
        if (mContext == null || iv == null || imgUrl == null) return;
        if ((String.valueOf(imgUrl).toLowerCase()).contains(".gif")) {
            Glide.with(mContext).asGif().load(imgUrl).apply( mOptions)
                    .transition(mTransitionOptions).listener(new RequestListener<GifDrawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                            Target<GifDrawable> target, boolean isFirstResource) {
                    if (callBack != null) {
                        callBack.onError("加载GIF异常");
                    }
                    return false;
                }

                @Override
                public boolean onResourceReady(GifDrawable resource, Object model,
                                               Target<GifDrawable> target, DataSource dataSource,
                                               boolean isFirstResource) {
                    if (callBack != null) {
                        callBack.onSuccess(resource, dataSource, isFirstResource);
                        return true;
                    }
                    return false;
                }
            }).into(iv);
        } else if (imgUrl instanceof String || imgUrl instanceof Uri || imgUrl instanceof File
                || imgUrl instanceof Bitmap || imgUrl instanceof Drawable){
            Glide.with(mContext).load(imgUrl).apply(mOptions).transition(mTransitionOptions)
                    .listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                            Target<Drawable> target, boolean isFirstResource) {
                    if (callBack != null) {
                        callBack.onError("加载图片异常");
                    }
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model,
                                               Target<Drawable> target, DataSource dataSource,
                                               boolean isFirstResource) {
                    if (callBack != null) {
                        callBack.onSuccess(resource, dataSource, isFirstResource);
                    }
                    return false;
                }
            }).into(iv);
        }
    }
}
