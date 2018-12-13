package cn.ydw.www.toolslib.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;

import java.io.IOException;

/**
 * =====================================
 * 作    者: 杨德望
 * 版    本：${VERSION_CODE}.
 * 创建日期：2018/2/9.
 * 描    述：
 * =====================================
 */
public final class SystemUtils {
    /**
     * 播放震动
     * @param num 震动次数, 在1到100之间
     * @return 震动控件, 用完请取消, 或者销毁的时候请取消, 不然会一直放到线程结束
     */
    public static Vibrator playvVbrator(Activity act, @Nullable Vibrator vibrator,
                                        @IntRange(from=-1, to=100) int num,
                                        @IntRange(from = 1) long... pattern){
         /*
         * 想设置震动大小可以通过改变pattern来设定，如果开启时间太短，震动效果可能感觉不到
         * */
        if (vibrator == null) {
            vibrator = (Vibrator) act.getSystemService(Context.VIBRATOR_SERVICE);
        }
        if (vibrator != null) {
            if (pattern == null || pattern.length == 0 || pattern.length % 2 != 0) {
                pattern = new long[]{100,400,100,400};   // 停止 开启 停止 开启
            }
            vibrator.vibrate(pattern,num);           //重复两次上面的pattern 如果只想震动一次，index设为-1
        }
        return vibrator;
    }

    /**
     * 播放系统提示音
     */
    public static void palySystemSound(Activity act){
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone rt = RingtoneManager.getRingtone(act, uri);
        if (rt != null)
            rt.play();
    }

    /**
     * 播放声音 不能同时播放多种音频
     * 消耗资源较大
     * @param rawId 资源id
     */
    public static void playSoundByMedia(Activity act, int rawId) {
        try {
            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);
            try {
                AssetFileDescriptor file = act.getResources().openRawResourceFd(
                        rawId);
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(0.50f, 0.50f);
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
                mediaPlayer.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static final MediaPlayer.OnCompletionListener beepListener = new MediaPlayer.OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };


    /**
     * 适合播放声音短，文件小
     * 可以同时播放多种音频
     * 消耗资源较小
     */
    public static SoundPool playSound(Activity act, int rawId) {
        SoundPool soundPool;
        if (Build.VERSION.SDK_INT >= 21) {
            SoundPool.Builder builder = new SoundPool.Builder();
            //传入音频的数量
            builder.setMaxStreams(1);
            //AudioAttributes是一个封装音频各种属性的类
            AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
            //设置音频流的合适属性
            attrBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC);
            builder.setAudioAttributes(attrBuilder.build());
            soundPool = builder.build();
        } else {
            //第一个参数是可以支持的声音数量，第二个是声音类型，第三个是声音品质
            soundPool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 5);
        }
        //第一个参数Context,第二个参数资源Id，第三个参数优先级
        soundPool.load(act, rawId, 1);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                //第一个参数id，即传入池中的顺序，
                // 第二个和第三个参数为左右声道，
                // 第四个参数为优先级，
                // 第五个是否循环播放，0不循环，-1循环
                //最后一个参数播放比率，范围0.5到2，通常为1表示正常播放
                soundPool.play(1, 1, 1, 0, 0, 1);
            }
        });
        //不用了请回收Pool中的资源
        //soundPool.release();
        return soundPool;
    }

    /**
     * 获取版本名
     *
     * @param act 活动
     * @return 返回版本名字符串
     */
    public static String getVersionName(Activity act) {
        //获取系统服务
//        ConnectivityManager manager = (ConnectivityManager) act.getSystemService(Context.CONNECTIVITY_SERVICE);
        //获取当前版本名
        PackageManager pm = act.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(act.getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "1.0";
        }
    }

    /**
     * 获取版本号
     *
     * @param act 活动
     * @return 返回版本号整型
     */
    public static int getVersionCode(Activity act) {
        //获取系统服务
//        ConnectivityManager manager = (ConnectivityManager) act.getSystemService(Context.CONNECTIVITY_SERVICE);
        //获取当前版本名
        PackageManager pm = act.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(act.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return 1;
        }
    }
}
