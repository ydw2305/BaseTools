
package cn.ydw.www.toolslib.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.MediaMetadataRetriever;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Locale;

/**
 * @author 杨德望
 * Create on 2018/3/7
 * 描述: 文件工具类
 */
public class FileUtils {
    /**
     * 获取总缓存
     *
     * @param context 上下文
     * @return 格式化后的数据长度
     * @throws Exception 异常
     */
    public static String getTotalCacheSize(Context context) throws Exception {
        long cacheSize = getFolderSize(context.getCacheDir());
        Logger.e("打印", "缓存getCacheDir数据" + cacheSize);
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cacheSize += getFolderSize(context.getExternalCacheDir());
            Logger.e("打印", "缓存getExternalCacheDir数据" + cacheSize);
        }
//        File file = new File(BitmapUtils.IMAGE_FILE_PATH);
//        if (file.isDirectory()){
//            cacheSize += getFolderSize(file);
//            Logger.e("打印", "缓存IMAGE_FILE_PATH数据" + cacheSize);
//        }
        return getFormatSize(cacheSize);
    }


    /**
     * 清楚所有缓存
     *
     * @param context 上下文
     */
    public static void clearAllCache(Context context) {
        boolean b = deleteDirOrFile(context.getCacheDir());
        Log.e("打印", "删除getCacheDir = " + b);
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            boolean b1 = deleteDirOrFile(context.getExternalCacheDir());
            Log.e("打印", "删除getExternalCacheDir = " + b1);
        }
//        File file = new File(BitmapUtils.IMAGE_FILE_PATH);
//        if (file.isDirectory()){
//            boolean b1 = deleteDirOrFile(file);
//            Log.e("打印", "删除IMAGE_FILE_PATH = " + b1);
//        }
    }

    private static boolean deleteDirOrFile(File dir) {
        if (dir != null) {
            if (dir.isDirectory()) {
                String[] children = dir.list();
                for (String aChildren : children) {
                    boolean success = deleteDirOrFile(new File(dir, aChildren));
                    if (!success) {
                        return false;
                    }
                }
            }
            return dir.delete();
        }
        return true;
    }

    /**
     * 删除文件
     *
     * @param url 文件路径
     * @return 是否删除成功, true是成功
     */
    public static boolean deleteFile(String url) {
        if (TextUtils.isEmpty(url)) {
            return true;
        }
        try {
            File file = new File(url);
            return deleteFile(file);
        } catch (Exception e) {
            return true;
        }
    }
    /**
     * 删除文件
     *
     * @param file 文件
     * @return 是否删除成功, true是成功
     */
    public static boolean deleteFile(File file) {
        if (!file.exists()) {
            return true;
        }
        try {
            return deleteDirOrFile(file);
        } catch (Exception e) {
            return true;
        }
    }

    // 获取文件
    //Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
    //Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
    private static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (File aFileList : fileList) {
                // 如果下面还有文件
                if (aFileList.isDirectory()) {
                    size = size + getFolderSize(aFileList);
                } else {
                    size = size + aFileList.length();
                }
            }
        } catch (Exception e) {
            Logger.e("获取文件异常", e);
        }
        return size;
    }

    /**
     * 格式化单位
     *
     * @param size 长度
     * @return 单位
     */
    private static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return "0Byte";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }


    public static void writeLogInLocal(String str) {
        File f = new File(Environment.getExternalStorageDirectory(), "/errorLog");
        if (!f.exists() && !f.mkdirs()) {
            return;
        }
        File file = new File(f, "test.txt");//文件夹路径
        FileOutputStream fos = null;
        // 创建FileOutputStream对象
        try {
            // 如果文件存在则删除, 然后在文件系统中根据路径创建一个新的空文件
            if (file.exists()) {
                if (file.delete() && !file.createNewFile()) {
                    return;
                }
            }
            // 获取FileOutputStream对象
            fos = new FileOutputStream(file);
            fos.write(str.getBytes());
        } catch (Exception e) {
            // 打印异常信息
            Logger.e("写入异常", e);
        } finally {
            // 关闭创建的流对象
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    Logger.e("流关闭异常", e);
                }
            }
        }
    }

    public static final int VIDEO_WIDTH = 0, VIDEO_HEIGHT = 1, VIDEO_ROTATION = 2;

    /**
     * 不加载视频, 通过检索本地资源获取视频宽高, 注意, 只能获取本地视频!
     *
     * @param videoPath 本地视频路径
     * @return 宽 {@link #VIDEO_WIDTH} , 高 {@link #VIDEO_HEIGHT}, 旋转角 {@link #VIDEO_ROTATION}
     */
    public static int[] getVideoWidthAndHeight(String videoPath) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(videoPath);
        String s = "0";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            s = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);
        }
        String videoW = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
        String videoH = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
        int[] wh = new int[3];
        if (s.equals("90") || s.equals("270")) {
            wh[VIDEO_HEIGHT] = Integer.valueOf(videoW);
            wh[VIDEO_WIDTH] = Integer.valueOf(videoH);
        } else if (s.equals("0") || s.equals("180") || s.equals("360")) {
            wh[VIDEO_WIDTH] = Integer.valueOf(videoW);
            wh[VIDEO_HEIGHT] = Integer.valueOf(videoH);
        }
        wh[VIDEO_ROTATION] = Integer.valueOf(s);
        return wh;
    }

    /**
     * 获取一个新的缓存文件, 即在缓存目录里面创建一个待写入的缓存文件
     * @param mContext 上下文
     * @param displayName 文件名 ( 包含后缀的)
     * @param type 缓存文件夹类型, 可为空, 参考{@link #getCacheDirectory(Context, String)}
     * @return 待写入的新文件
     */
    public static File getNewCacheFile(Context mContext, String displayName, @Nullable String type) {
        if (TextUtils.isEmpty(displayName)) {
            return null;
        }
        File outputDir = FileUtils.getCacheDirectory(mContext, type);
        File newFile = new File(outputDir, displayName);
        if (outputDir.exists() || outputDir.mkdirs()) {
            try {
                if (!newFile.exists()) {
                    if (!newFile.createNewFile()) {
                        Logger.e("创建文件错误");
                        return null;
                    }
                }
                return newFile;
            } catch (Exception e) {
                Logger.e("创建文件异常", e);
            }
        }
        return null;
    }


    /**
     * 获取应用专属缓存目录, 推荐使用这个用来存储文件
     * android 4.4及以上系统不需要申请SD卡读写权限
     * 因此也不用考虑6.0系统动态申请SD卡读写权限问题，切随应用被卸载后自动清空 不会污染用户存储空间
     *
     * @param context 上下文
     * @param type    文件夹类型 可以为空，为空则返回API得到的一级目录
     *                如果为空则返回 /storage/emulated/0/Android/data/app_package_name/cache
     *                否则返回对应类型的文件夹如Environment.DIRECTORY_PICTURES
     *                对应的文件夹为 .../data/app_package_name/files/Pictures
     *                {@link Environment#DIRECTORY_MUSIC},
     *                {@link Environment#DIRECTORY_PODCASTS},
     *                {@link Environment#DIRECTORY_RINGTONES},
     *                {@link Environment#DIRECTORY_ALARMS},
     *                {@link Environment#DIRECTORY_NOTIFICATIONS},
     *                {@link Environment#DIRECTORY_PICTURES}, or
     *                {@link Environment#DIRECTORY_MOVIES}.or 自定义文件夹名称
     * @return 缓存文件夹 如果没有SD卡或SD卡有问题则返回内存缓存目录，否则优先返回SD卡缓存目录
     */
    public static File getCacheDirectory(Context context, @Nullable String type) {
        File appCacheDir = getExternalCacheDirectory(context, type);
        if (appCacheDir == null) {
            appCacheDir = getInternalCacheDirectory(context, type);
        }

        if (appCacheDir == null) {
            Logger.e("getCacheDirectory fail ,the reason is mobile phone unknown exception !");
        } else {
            if (!appCacheDir.exists() && !appCacheDir.mkdirs()) {
                Logger.e("getCacheDirectory fail ,the reason is make directory fail !");
            }
        }
        return appCacheDir;
    }

    /**
     * 获取SD卡缓存目录
     *
     * @param context 上下文
     * @param type    文件夹类型 如果为空则返回 /storage/emulated/0/Android/data/app_package_name/cache
     *                否则返回对应类型的文件夹如Environment.DIRECTORY_PICTURES 对应的文件夹为 .../data/app_package_name/files/Pictures
     *                {@link Environment#DIRECTORY_MUSIC},
     *                {@link Environment#DIRECTORY_PODCASTS},
     *                {@link Environment#DIRECTORY_RINGTONES},
     *                {@link Environment#DIRECTORY_ALARMS},
     *                {@link Environment#DIRECTORY_NOTIFICATIONS},
     *                {@link Environment#DIRECTORY_PICTURES}, or
     *                {@link Environment#DIRECTORY_MOVIES}.or 自定义文件夹名称
     * @return 缓存目录文件夹 或 null（无SD卡或SD卡挂载失败）
     */
    public static File getExternalCacheDirectory(Context context, @Nullable String type) {
        File appCacheDir = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            if (TextUtils.isEmpty(type)) {
                appCacheDir = context.getExternalCacheDir();
            } else {
                appCacheDir = context.getExternalFilesDir(type);
            }

            if (appCacheDir == null) {// 有些手机需要通过自定义目录
                appCacheDir = new File(Environment.getExternalStorageDirectory(),
                        "Android/data/" + context.getPackageName() + "/cache/" + type);
            }

            if (!appCacheDir.exists()) {
                Logger.e("getExternalDirectory fail ,the reason is sdCard unknown exception !");
            } else {
                if (!appCacheDir.exists() && !appCacheDir.mkdirs()) {
                    Logger.e("getExternalDirectory fail ,the reason is make directory fail !");
                }
            }
        } else {
            Logger.e("getExternalDirectory fail ,the reason is sdCard nonexistence or sdCard mount fail !");
        }
        return appCacheDir;
    }

    /**
     * 获取内存缓存目录
     *
     * @param type 子目录，可以为空，为空直接返回一级目录
     * @return 缓存目录文件夹 或 null（创建目录文件失败）
     * 注：该方法获取的目录是能供当前应用自己使用，外部应用没有读写权限，如 系统相机应用
     */
    public static File getInternalCacheDirectory(Context context, @Nullable String type) {
        File appCacheDir;
        if (TextUtils.isEmpty(type)) {
            appCacheDir = context.getCacheDir();// /data/data/app_package_name/cache
        } else {
            appCacheDir = new File(context.getFilesDir(), type);// /data/data/app_package_name/files/type
        }

        if (!appCacheDir.exists() && !appCacheDir.mkdirs()) {
            Logger.e("getInternalDirectory fail ,the reason is make directory fail !");
        }
        return appCacheDir;
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

    /**
     * 获取文件类型
     * @param file 文件
     * @return 类型字符串
     */
    public static String getMimeType(File file){
        if (file == null || !file.exists() || file.isDirectory()) {
            return "file/*";
        }
        String fileName = file.getName();
        if (TextUtils.isEmpty(fileName) || fileName.endsWith(".")) {
            return "file/*";
        }
        int index = fileName.lastIndexOf(".");
        if (index != -1) {
            String suffix = fileName.substring(index + 1).toLowerCase(Locale.US);
            String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(suffix);
            if (!TextUtils.isEmpty(type)) {
                return type;
            }
        }
        return "file/*";
    }


    /**
     * 获取文本里面的内容
     * @param filePath 文本文件路径
     * @return 字符串
     */
    public static String readStrFromTxT(String filePath) {
        return readStrFromTxT(new File(filePath));
    }

    /**
     * 获取文本里面的内容
     * @param file 文本文件
     * @return 字符串
     */
    public static String readStrFromTxT(File file) {
        if (file != null && file.isFile() && file.exists()) {
            StringBuilder mBuilder = new StringBuilder();
            FileInputStream is = null;
            BufferedReader br = null;
            InputStreamReader isr = null;
            try {
                is = new FileInputStream(file);
                isr = new InputStreamReader(is);
                br = new BufferedReader(isr);
                String line;
                while ((line = br.readLine()) != null) {
                    mBuilder.append(line);
                }
            } catch (FileNotFoundException e) {
                Logger.e("文件载入异常", e);
            } catch (IOException e) {
                Logger.e("文件解析异常", e);
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    if (br != null) {
                        br.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    if (isr != null) {
                        isr.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return mBuilder.toString();
        }
        return null;
    }
    /**
     * 获取文本里面的内容
     * @param fileInputStream assets 里面的文件资源
     * @return 字符串
     */
    public static String readStrFromAssetTxT(InputStream fileInputStream) {
        if (fileInputStream != null) {
            StringBuilder mBuilder = new StringBuilder();
            BufferedReader br = null;
            InputStreamReader isr = null;
            try {
                isr = new InputStreamReader(fileInputStream);
                br = new BufferedReader(isr);
                String line;
                while ((line = br.readLine()) != null) {
                    mBuilder.append(line);
                }
            } catch (FileNotFoundException e) {
                Logger.e("文件载入异常", e);
            } catch (IOException e) {
                Logger.e("文件解析异常", e);
            } finally {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    if (br != null) {
                        br.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    if (isr != null) {
                        isr.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return mBuilder.toString();
        }
        return null;
    }


}
