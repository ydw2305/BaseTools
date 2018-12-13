package cn.ydw.www.toolslib.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.NonNull;
import android.support.annotation.RawRes;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Base64;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import cn.ydw.www.toolslib.utils.gifencoder.AnimatedGifEncoder;

/**
 * @author 杨德望
 * 描述: 跟图片处理有关的工具类
 */
@SuppressWarnings("all")
public class BitmapUtils {

    public static final int Bitmap_Width = 0, Bitmap_Height = 1;

    public static enum ScaleType {
        NORMAL, HORIZONTAL, VERTICAL
    }

    ////////////////////////////////////// read ///////////////////////////////////////////////


    /**
     * 获取缩放后的本地图片
     * (效率较高)
     * 文件方式读入
     *
     * @param filePath 文件路径
     * @return 图片
     */
    public static Bitmap readBitmapFromFileDescriptor(String filePath) {
        return readBitmapFromFileDescriptor(filePath, 1080, 1920);
    }

    /**
     * 获取缩放后的本地图片
     * (效率较高)
     * 文件方式读入
     *
     * @param filePath 文件路径
     * @param width    宽
     * @param height   高
     * @return 图片
     */
    public static Bitmap readBitmapFromFileDescriptor(String filePath, int width, int height) {
        if (TextUtils.isEmpty(filePath)) return null;
        try {
            FileInputStream fis = new FileInputStream(filePath);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFileDescriptor(fis.getFD(), null, options);
            float srcWidth = options.outWidth;
            float srcHeight = options.outHeight;
            int inSampleSize = 1;
            if (srcHeight > height || srcWidth > width) {
                if (srcWidth > srcHeight) {
                    inSampleSize = Math.round(srcHeight / height);
                } else {
                    inSampleSize = Math.round(srcWidth / width);
                }
            }
            options.inJustDecodeBounds = false;
            options.inPreferredConfig = Config.ARGB_4444;
            options.inSampleSize = inSampleSize;
            options.inScaled = true;
            return BitmapFactory.decodeFileDescriptor(fis.getFD(), null, options);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 读取图片 从string里面获取
     *
     * @param filePath 路径
     * @return 图片
     */
    public static Bitmap readBitmapFromPath(String filePath) {
        return readBitmapFromPath(filePath, 1080, 1920);
    }

    /**
     * 从文件路径读取图片
     *
     * @param filePath 文件路径
     * @param width    宽
     * @param height   高
     * @return 位图
     */
    public static Bitmap readBitmapFromPath(String filePath, int width, int height) {
        if (TextUtils.isEmpty(filePath))
            return null;
        FileInputStream fis = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;// 只读边,不读内容
            fis = new FileInputStream(filePath);
            BitmapFactory.decodeFileDescriptor(fis.getFD(), null, options);
            float srcWidth = options.outWidth;
            float srcHeight = options.outHeight;
            int inSampleSize = 1;
            if (srcHeight > height || srcWidth > width) {
                if (srcWidth > srcHeight) {
                    inSampleSize = Math.round(srcHeight / height);
                } else {
                    inSampleSize = Math.round(srcWidth / width);
                }
            }
            options.inJustDecodeBounds = false;
            options.inSampleSize = inSampleSize;// 设置压缩比例
            options.inPreferredConfig = Config.ARGB_4444;// 该模式是默认的,可不设
            options.inPurgeable = true;// 同时设置才会有效
            options.inInputShareable = true;// 当系统内存不够时候图片自动被回收
            return BitmapFactory.decodeFileDescriptor(fis.getFD(), null, options);
        } catch (Exception e) {
            Logger.e("读取图片失败", e);
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                Logger.e("关流失败", e);
            }
        }
        return null;
    }


    /**
     * 读取图片, 仅仅获取宽高, 不载入到内存,
     *
     * @param filePath 图片文件路径
     * @return 宽高, 0是宽, 1是高
     */
    public static int[] readBitmapJustGetWidthAndHeight(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return new int[]{0, 0};
        }
        return readBitmapJustGetWidthAndHeight(new File(filePath));
    }

    /**
     * 读取图片, 仅仅获取宽高, 不载入到内存,
     *
     * @param filePath 图片文件路径
     * @return 宽高, 0是宽, 1是高
     */
    public static int[] readBitmapJustGetWidthAndHeight(File file) {
        int[] wh = new int[]{0, 0};
        if (file.exists()) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;// 只读边,不读内容
            BitmapFactory.decodeFile(file.getAbsolutePath(), options);
            wh[Bitmap_Width] = options.outWidth;
            wh[Bitmap_Height] = options.outHeight;
        }
        return wh;
    }

    ////////////////////////////////////// cover Or covert ///////////////////////////////////////////////

    /**
     * drawable 资源转为 Bitmap
     *
     * @param d drawable资源
     * @return 图片
     */
    public static Bitmap convertDrawable2Bitmap(Drawable d) {
        if (d instanceof BitmapDrawable) {
            BitmapDrawable bd = (BitmapDrawable) d;
            return bd.getBitmap();
        }
        return null;
    }


    /**
     * byte[] 转为 Bitmap
     */
    public static Bitmap convertBytes2Bimap(byte[] b) {
        if (b == null || b.length == 0) {
            return null;
        }
        return BitmapFactory.decodeByteArray(b, 0, b.length);
    }

    /**
     * Bitmap 转为 byte[]
     */
    public static byte[] convertBitmap2Bytes(Bitmap bm) {
        if (bm == null) return new byte[0];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        return baos.toByteArray();
    }

    /**
     * Bitmap转换成byte[]并且进行压缩,压缩到不大于maxkb
     *
     * @param bitmap 位图
     * @param maxkb  不超过多少 b , 如微信不超过 32 kb, 则输入 32 * 1024
     * @return 字节数组
     */
    public static byte[] convertBmpToByteArray(Bitmap bitmap, int maxkb) {
        ByteArrayOutputStream output = null;
        try {
            output = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
            int options = 100;
            while (output.toByteArray().length > maxkb && options != 10) {
                output.reset(); //清空output
                bitmap.compress(Bitmap.CompressFormat.JPEG, options, output);//这里压缩options%，把压缩后的数据存放到output中
                options -= 10;
            }
            return output.toByteArray();
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
            } catch (Exception e) {
                Logger.e("关流异常", e);
            }
        }
    }


    /**
     * 相机data字节流默认格式 YCbCr_420_SP 转 rgb 字节流格式
     *
     * @param yuv420sp 默认格式
     * @param width    相机宽
     * @param height   相机高
     * @return rgb 字节流
     */
    public static int[] converYUV420sp2RGB(byte[] yuv420sp, int width, int height) {
        final int frameSize = width * height;
        int[] rgb = new int[frameSize];
        for (int j = 0, yp = 0; j < height; j++) {
            int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;
            for (int i = 0; i < width; i++, yp++) {
                int y = (0xff & ((int) yuv420sp[yp])) - 16;
                if (y < 0) y = 0;
                if ((i & 1) == 0) {
                    v = (0xff & yuv420sp[uvp++]) - 128;
                    u = (0xff & yuv420sp[uvp++]) - 128;
                }

                int y1192 = 1192 * y;
                int r = (y1192 + 1634 * v);
                int g = (y1192 - 833 * v - 400 * u);
                int b = (y1192 + 2066 * u);

                if (r < 0) r = 0;
                else if (r > 262143) r = 262143;
                if (g < 0) g = 0;
                else if (g > 262143) g = 262143;
                if (b < 0) b = 0;
                else if (b > 262143) b = 262143;

                rgb[yp] = 0xff000000 | ((r << 6) & 0xff0000) | ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);
            }
        }
        return rgb;
    }


    /**
     * @param data
     * @param width
     * @param height
     * @return
     */
    public static Bitmap converByteArray2RGBABitmap(byte[] data, int width, int height) {
        int frameSize = width * height;
        int[] rgba = new int[frameSize];
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                int y = (0xff & ((int) data[i * width + j]));
                int u = (0xff & ((int) data[frameSize + (i >> 1) * width + (j & ~1) + 0]));
                int v = (0xff & ((int) data[frameSize + (i >> 1) * width + (j & ~1) + 1]));
                y = y < 16 ? 16 : y;
                int r = Math.round(1.164f * (y - 16) + 1.596f * (v - 128));
                int g = Math.round(1.164f * (y - 16) - 0.813f * (v - 128) - 0.391f * (u - 128));
                int b = Math.round(1.164f * (y - 16) + 2.018f * (u - 128));
                r = r < 0 ? 0 : (r > 255 ? 255 : r);
                g = g < 0 ? 0 : (g > 255 ? 255 : g);
                b = b < 0 ? 0 : (b > 255 ? 255 : b);
                rgba[i * width + j] = 0xff000000 + (b << 16) + (g << 8) + r;
            }
        Bitmap bmp = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        bmp.setPixels(rgba, 0, width, 0, 0, width, height);
        return bmp;
    }

    /**
     * byte[] 转为 base64 字符串
     *
     * @param imgBytes 字节数组
     * @return 返回base64字符串
     */
    public static String convertBytes2Base64(byte[] imgBytes) {
        if (imgBytes == null || imgBytes.length == 0) return "";
        return Base64.encodeToString(imgBytes, Base64.DEFAULT);
    }

    /**
     * base64 字符串 转 byte[]
     *
     * @param base64Data base64字符串
     * @return 字节数组
     */
    public static byte[] convertBase642Bitmap(String base64Data) {
        return TextUtils.isEmpty(base64Data) ? new byte[0] : Base64.decode(base64Data, Base64.DEFAULT);
    }

    /**
     * 转换file 变成 uri ( 在安卓 7.0 以后, 请求文件 URI 需要进行安全转换 )
     *
     * @param mContext  上下文
     * @param mFile     文件
     * @param authority FileProvider参数, 来自清单文件注册参数 authorities
     *                  <p>
     *                  <provider
     *                  android:name="android.support.v4.content.FileProvider"
     *                  android:authorities="cn.takethat.www.appproject"
     *                  android:exported="false"
     *                  android:grantUriPermissions="true">
     *                  <meta-data
     *                  android:name="android.support.FILE_PROVIDER_PATHS"
     *                  android:resource="@xml/filepaths" />
     *                  </provider>
     *                  </p>
     * @return uri
     */
    public static Uri coverFile2Uri(Context mContext, File mFile, String authority) {
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(mContext, authority, mFile);
        } else {
            uri = Uri.fromFile(mFile);
        }
        return uri;
    }
    /**
     * 转换file 变成 uri ( 在安卓 7.0 以后, 请求文件 URI 需要进行安全转换 ) , 这里使用取巧方式
     *
     * @param mFile     文件 todo 等待测试
     * @return uri
     */
    @Deprecated
    public static Uri coverFile2Uri(File mFile) {
        if (mFile != null && mFile.exists()) {
            return Uri.parse(("file://" + mFile.getAbsolutePath()));
        }
        return null;
    }

    /**
     * Try to return the absolute file path from the given Uri
     *
     * @param context 上下文
     * @param uri     路径
     * @return the file path or null
     */
    public static String coverUri2FilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null || ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{
                    MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    // =========
    // =通过URI获取本地图片的path
    // =兼容android 5.0
    // ==========
    public static String coverUri2Path(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= 19;

        // DocumentProvider
        if (isKitKat && isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if ("com.android.externalstorage.documents".equals(uri.getAuthority())) {
                // Whether the Uri authority is ExternalStorageProvider.
                final String docId = getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

            }
            // DownloadsProvider
            else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                // Whether the Uri authority is DownloadsProvider.
                final String id = getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                // Whether the Uri authority is MediaProvider.
                final String docId = getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if ("com.google.android.apps.photos.content".equals(uri.getAuthority()))
                // Whether the Uri authority is Google Photos.
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    private static final String PATH_DOCUMENT = "document";

    /**
     * Test if the given URI represents a {@link Document} backed by a
     * {@link DocumentsProvider}.
     */
    private static boolean isDocumentUri(Context context, Uri uri) {
        final List<String> paths = uri.getPathSegments();
        if (paths.size() < 2) {
            return false;
        }
        if (!PATH_DOCUMENT.equals(paths.get(0))) {
            return false;
        }

        return true;
    }

    private static String getDocumentId(Uri documentUri) {
        final List<String> paths = documentUri.getPathSegments();
        if (paths.size() < 2) {
            throw new IllegalArgumentException("Not a document: " + documentUri);
        }
        if (!PATH_DOCUMENT.equals(paths.get(0))) {
            throw new IllegalArgumentException("Not a document: " + documentUri);
        }
        return paths.get(1);
    }


    ////////////////////////////////////// set ///////////////////////////////////////////////

    /**
     * Bitmap 转为 drawable 资源
     *
     * @param bmp 图片
     * @return drawable资源
     */
    public static Drawable setBitmap2Drawable(Bitmap bmp) {
        return new BitmapDrawable(bmp);
    }


    /**
     * 质量压缩图片方法
     *
     * @param image 图片
     * @return 图片
     */
    public static Bitmap setBmpCompress(Bitmap image, int compressNum) {
        if (image == null) return null;
        if (compressNum < 1) compressNum = 1;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > compressNum && options > 20) {  //循环判断如果压缩后图片是否大于compressNum KB,大于继续压缩
            baos.reset();//重置baos即清空baos
            //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
            options -= 10;//每次都减少10
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        return BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
    }


    /**
     * 裁切图片, 并且压缩,
     *
     * @param bitPath
     * @param startX
     * @param startY
     * @param cropWidth
     * @param cropHeight
     * @return
     */
    public static Bitmap setBmpClip(String bitPath, float startX, float startY,
                                    float cropWidth, float cropHeight) {
        if (TextUtils.isEmpty(bitPath) || startX < 0 || startY < 0
                || cropWidth <= 0 || cropHeight <= 0) return null;
        // 获取在旋转之前的裁剪位置
        Rect srcRect = new Rect((int) startX, (int) startY,
                (int) (startX + cropWidth - 1), (int) (startY + cropHeight));

        final BitmapFactory.Options ops = new BitmapFactory.Options();

        // 如果裁剪之后的图片宽高仍然太大,则进行缩小
        int inSampleSize = 1;
        int limitHeight = 480;
        int limitWidth = 480;
        // 压缩图片
        if (cropWidth > cropHeight && cropWidth > limitWidth) {
            inSampleSize = Math.round(cropWidth / limitWidth);
        } else if (cropWidth < cropHeight && cropHeight > limitHeight) {
            inSampleSize = Math.round(cropHeight / limitHeight);
        }
        if (inSampleSize <= 0) {
            inSampleSize = 1;
        }
        Logger.e("打印", "缩放比例" + inSampleSize);
        ops.inSampleSize = inSampleSize;
        ops.inJustDecodeBounds = false;
        ops.inDither = true;
        ops.inPreferredConfig = Config.ARGB_4444;

        // 裁剪
        BitmapRegionDecoder decoder = null;
        try {
            decoder = BitmapRegionDecoder.newInstance(bitPath, false);
            return decoder.decodeRegion(srcRect, ops);
        } catch (Exception e) {
            Logger.e("图片处理异常", e);
            return null;
        }
    }


    /**
     * 裁切图片,
     *
     * @param context 上下文
     * @param imgPath 图片地址
     * @param left    要裁切的左
     * @param top     要裁切的上
     * @param right   要裁切的右
     * @param bottom  要裁切的下
     * @param width   原图的宽
     * @param height  原图的高
     * @return 新的位图
     */
    public static Bitmap setImageClip(Context context, String imgPath, int left, int top,
                                      int right, int bottom, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;

        int rawArea = (right - left) * (bottom - top);
        int targetArea = width * height * 4;

        int resultArea = rawArea;

        while (resultArea > targetArea) {
            options.inSampleSize *= 2;
            resultArea = rawArea / (options.inSampleSize * options.inSampleSize);
        }

        if (options.inSampleSize > 1) {
            options.inSampleSize /= 2;
        }

        BitmapRegionDecoder decoder = null;
        try {
            left /= options.inSampleSize;
            top /= options.inSampleSize;
            right /= options.inSampleSize;
            bottom /= options.inSampleSize;

            int croppedWidth = right - left;
            int croppedHeight = bottom - top;

//            Logger.e("角标 left = " + left + " top = " + top + " right = " + right + " bottom = " + bottom);

            /////////////////////////////////////////////////////////////
            decoder = BitmapRegionDecoder.newInstance(imgPath, false);

            Rect mRect = new Rect(left, top, right, bottom);
            Bitmap croppedBitmap = decoder.decodeRegion(mRect, options);

            ///////////////////////////////////////////////////////////////////

            return croppedBitmap;
        } catch (Throwable t) {
            Logger.e("切图异常", t);
            return null;
        } finally {
            if (decoder != null && !decoder.isRecycled()) {
                decoder.recycle();
            }
        }
    }

    /**
     * 设置图片旋转度, 以及镜像情况,( 将图片按照某个角度进行旋转, 然后再镜像)
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap setBmpRotate(Bitmap bm, int degree, ScaleType bst) {
        if (bm == null) return null;
        if (degree == 0 && bst == ScaleType.NORMAL) return bm;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        if ((degree % 360) != 0) {
            matrix.postRotate(degree % 360);
        }
        if (bst == ScaleType.VERTICAL) {//垂直方向翻转
            matrix.postScale(1, -1);
        } else if (bst == ScaleType.HORIZONTAL) { //水平方向翻转
            matrix.postScale(-1, 1);
        }
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            Bitmap returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
            bm.recycle();
            return returnBm;
        } catch (OutOfMemoryError e) {
            Logger.e("创建图片异常", e);
            gc();
            return bm;
        }
    }

    /**
     * 设置图片的新尺寸
     *
     * @param bitmap
     * @param newWidth
     * @param newHeight
     * @return
     */
    public static Bitmap setBitmapSize(Bitmap bitmap, int newWidth, int newHeight) {
        Bitmap resizedBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);

        float scaleX = newWidth / (float) bitmap.getWidth();
        float scaleY = newHeight / (float) bitmap.getHeight();
        float pivotX = 0;
        float pivotY = 0;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(scaleX, scaleY, pivotX, pivotY);

        Canvas canvas = new Canvas(resizedBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap, 0, 0, new Paint(Paint.FILTER_BITMAP_FLAG));
        canvas.restore();
        scaleMatrix.reset();

        return resizedBitmap;
    }


    /**
     * 设置图片模糊度
     *
     * @param mBitmap 图片
     * @param radius  模糊半径
     * @return 模糊过的图片
     */
    public static Bitmap setBmpBlur(Bitmap mBitmap, float radius) {
        float scaleFactor = 10;//图片缩放比例；
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();

        Bitmap overlay = Bitmap.createBitmap((int) (width / scaleFactor), (int) (height / scaleFactor), Config.ARGB_4444);
        Canvas canvas = new Canvas(overlay);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(mBitmap, 0, 0, paint);

        overlay = FastBlur.doBlur(overlay, (int) radius, true);//模糊程度
        return overlay;
    }


    // 高斯模糊处理
    private Bitmap setBmpBlur(Context mContext, Bitmap bitmap, float radius) {
        Bitmap output = ThumbnailUtils.extractThumbnail(bitmap, bitmap.getWidth()/2, bitmap.getHeight()/2);// 创建输出缩略图片
//        Bitmap output = Bitmap.createBitmap(bitmap); // 创建输出图片
        RenderScript rs = RenderScript.create(mContext); // 构建一个RenderScript对象
        ScriptIntrinsicBlur gaussianBlue = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs)); //
        // 创建高斯模糊脚本
        Allocation allIn = Allocation.createFromBitmap(rs, bitmap); // 开辟输入内存
        Allocation allOut = Allocation.createFromBitmap(rs, output); // 开辟输出内存
        gaussianBlue.setRadius(radius); // 设置模糊半径，范围0f<radius<=25f
        gaussianBlue.setInput(allIn); // 设置输入内存
        gaussianBlue.forEach(allOut); // 模糊编码，并将内存填入输出内存
        allOut.copyTo(output); // 将输出内存编码为Bitmap，图片大小必须注意
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 关闭RenderScript对象，API>=23则使用rs.releaseAllContexts()
            rs.releaseAllContexts();
        } else {
            rs.destroy();
        }
        return output;
    }

    /**
     * 设置图片效果，根据色调，饱和度，亮度来调节图片效果, 会创建新图
     *
     * @param bm:要处理的图像
     * @param hue:色调
     * @param saturation:饱和度
     * @param lum:亮度
     */
    public static Bitmap setBmpEffect(Bitmap bm, float hue, float saturation, float lum) {
        hue = hue < -180.0f ? -180.0f : hue > 180.0f ? 180.0f : hue;
        saturation = saturation < 0.0f ? 0.0f : saturation > 2.0f ? 2.0f : saturation;
        lum = lum < 0.0f ? 0.0f : lum > 2.0f ? 2.0f : lum;

        Bitmap bmp = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        ColorMatrix hueMatrix = new ColorMatrix();
        hueMatrix.setRotate(0, hue);    //0代表R，红色
        hueMatrix.setRotate(1, hue);    //1代表G，绿色
        hueMatrix.setRotate(2, hue);    //2代表B，蓝色

        ColorMatrix saturationMatrix = new ColorMatrix();
        saturationMatrix.setSaturation(saturation);

        ColorMatrix lumMatrix = new ColorMatrix();
        lumMatrix.setScale(lum, lum, lum, 1);


        ColorMatrix imageMatrix = new ColorMatrix();
        imageMatrix.postConcat(hueMatrix);
        imageMatrix.postConcat(saturationMatrix);
        imageMatrix.postConcat(lumMatrix);

        paint.setColorFilter(new ColorMatrixColorFilter(imageMatrix));
        canvas.drawBitmap(bm, 0, 0, paint);

        return bmp;
    }

    /**
     * 设置图片的合并, 将两张 bitmap 合并成一张
     *
     * @param firBmp 第一张图片
     * @param secBmp 第二张图片
     * @param dx     第二张图片的 sLeft 距离第一张图片的 fLeft 有多少距离, 若 sLeft 在 fLeft 的右边,则dx 为正,反之为负
     * @param dy     第二张图片的 sTop 距离第一张图片的 fTop 有多少距离, 若 sTop 在 fTop 的下边,则dy 为正,反之为负
     * @return Bitmap
     */
    public static Bitmap setBmpCombine(@NonNull Bitmap firBmp, Bitmap secBmp, int dx, int dy) {
        if (secBmp == null) {
            return firBmp;
        } else if (firBmp == null) {
            return null;
        }

        int firWidth = firBmp.getWidth();
        int firHeight = firBmp.getHeight();
        int secWidth = secBmp.getWidth();
        int secHeight = secBmp.getHeight();

        int bgWidth = Math.max(firWidth + dx, secWidth); // 获取底图的宽
        int bgHeight = Math.max(firHeight + dy, secHeight); // 获取底图的高
        // 创建底图
        Bitmap newmap = Bitmap.createBitmap(bgWidth, bgHeight, Config.ARGB_4444);
        Canvas canvas = new Canvas(newmap);

        float fLeft = 0, fTop = 0, sLeft = 0, sTop = 0;
        if (dx >= 0) {
            sLeft += dx;
        } else {
            fLeft += dx;
        }
        if (dy >= 0) {
            sTop += dy;
        } else {
            fTop += dy;
        }
        canvas.drawBitmap(firBmp, fLeft, fTop, null);
        canvas.drawBitmap(secBmp, sLeft, sTop, null);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return newmap;
    } //end of combineBitmap


    ////////////////////////////////////// get ///////////////////////////////////////////////


    /**
     * 从资源文件中获取Bitmap
     */
    public static Bitmap getBitmapFromResources(Activity act, @RawRes int resId) {
        Resources res = act.getResources();
        return BitmapFactory.decodeResource(res, resId);
    }

    /**
     * 以最省内存的方式读取本地资源的图片
     *
     * @param context 上下文
     * @param resId   图片ID
     * @return 图片
     */
    public static Bitmap getBitmapFromResources(Context context, @RawRes int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Config.ARGB_4444;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        //获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    /**
     * 获取包文件路径
     *
     * @param mContext 上下文
     * @return
     */
    public static final String getPackgePath(Context mContext) {
        return mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     *                      [url=home.php?mod=space&uid=7300]@return[/url] The value of
     *                      the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    ////////////////////////////////////// save ///////////////////////////////////////////////


    /**
     * 保存 图片二进制流到本地成为文件
     *
     * @param bytes 图片二进制数组
     * @return 文件
     */
    public static File saveByte2FileInLocal(Context mContext, byte[] bytes, String mOutDisplayName) {
        if (bytes == null || bytes.length < 0) return null;
        File outputDir = FileUtils.getCacheDirectory(mContext, Environment.DIRECTORY_PICTURES);
        File newFile = new File(outputDir, mOutDisplayName);
        if (outputDir.exists() || outputDir.mkdirs()) {
            // 创建FileOutputStream对象
            FileOutputStream outputStream = null;
            // 创建BufferedOutputStream对象
            BufferedOutputStream bufferedOutputStream = null;
            try {
                if (!newFile.exists()) {
                    if (!newFile.createNewFile()) {
                        Logger.e("无法创建文件错误");
                        return null;
                    }
                }
                // 获取FileOutputStream对象
                outputStream = new FileOutputStream(newFile);
                // 获取BufferedOutputStream对象
                bufferedOutputStream = new BufferedOutputStream(outputStream);
                // 往文件所在的缓冲输出流中写byte数据
                bufferedOutputStream.write(bytes);
                // 刷出缓冲输出流，该步很关键，要是不执行flush()方法，那么文件的内容是空的。
                bufferedOutputStream.flush();
                return newFile;
            } catch (Exception e) {
                // 打印异常信息
                Logger.e("存储异常", e);
            } finally {
                // 关闭创建的流对象
                if (outputStream != null) {
                    try {
                        outputStream.flush();
                        outputStream.close();
                    } catch (IOException e) {
                        Logger.e("关流异常", e);
                    }
                }
                if (bufferedOutputStream != null) {
                    try {
                        bufferedOutputStream.flush();
                        bufferedOutputStream.close();
                    } catch (Exception e) {
                        Logger.e("关流异常", e);
                    }
                }
            }
        }
        return null;


    }

    /**
     * 保存图片到本地 uri, 这个是同步 的, 耗时比较久
     *
     * @param mContext        上下文
     * @param mOutDisplayName 输出文件的名字( 带后缀的)
     * @param outputQuality   输出质量, 0 到 100
     * @param bitmap          位图
     */
    public static Uri saveBmp2LocalUri(Context mContext, String mOutDisplayName,
                                       int mOutputQuality, Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        if (mOutputQuality <= 0 || mOutputQuality > 100) {
            mOutputQuality = 80;
        }
        File outputDir = FileUtils.getCacheDirectory(mContext, Environment.DIRECTORY_PICTURES);
        File newFile = new File(outputDir, mOutDisplayName);
        if (outputDir.exists() || outputDir.mkdirs()) {
            try {
                if (!newFile.exists()) {
                    if (!newFile.createNewFile()) {
                        Logger.e("无法创建文件错误");
                        return null;
                    }
                }
                Uri mOutputUri = Uri.parse(("file:" + newFile.getAbsolutePath()));
                OutputStream os = null;
                try {
                    os = mContext.getContentResolver().openOutputStream(mOutputUri);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, mOutputQuality, os);
                    return mOutputUri;
                } catch (Exception e) {
                    Logger.e("转型异常", e);
                } finally {
                    try {
                        if (os != null) {
                            os.flush();
                            os.close();
                        }
                    } catch (IOException e) {
                        Logger.e("关流异常", e);
                    }
                    if (!bitmap.isRecycled()) {
                        bitmap.recycle();
                    }
                }
            } catch (IOException e) {
                Logger.e("文件创建异常", e);
            }
        }
        return null;
    }


    /**
     * 保存图片到本地 uri, 这个是异步 的
     *
     * @param mContext        上下文
     * @param mOutDisplayName 输出文件的名字( 带后缀的)
     * @param outputQuality   输出质量, 0 到 100
     * @param bitmap          位图
     * @param mHandler        辅助
     */
    public static void saveBmp2LocalUri(Context mContext, String mOutDisplayName,
                                        int outputQuality, Bitmap bitmap, Handler mHandler) {
        if (outputQuality <= 0 || outputQuality > 100) {
            outputQuality = 80;
        }
        File outputDir = FileUtils.getCacheDirectory(mContext, Environment.DIRECTORY_PICTURES);
        File newFile = new File(outputDir, mOutDisplayName);
        if (outputDir.exists() || outputDir.mkdirs()) {
            try {
                if (!newFile.exists()) {
                    if (!newFile.createNewFile()) {
                        Logger.e("无法创建文件错误");
                        mHandler.sendEmptyMessage(-1);
                        return;
                    }
                }
                new ConvertBmp2Uri(mContext.getContentResolver(),
                        Uri.fromFile(newFile), outputQuality, mHandler).execute(bitmap);
            } catch (IOException e) {
                Logger.e("文件创建异常", e);
            }
        } else {
            mHandler.sendEmptyMessage(-1);
        }
    }

    // 将bmp 转换成uri, 存储到本地
    private static class ConvertBmp2Uri extends AsyncTask<Bitmap, Void, Boolean> {
        private ContentResolver mResolver;
        private Uri mOutputUri;
        private int mOutputQuality;
        private Handler mHandler;

        ConvertBmp2Uri(ContentResolver mResolver, Uri dst, int outputQuality, Handler mHandler) {
            this.mResolver = mResolver;
            mOutputUri = dst;
            mOutputQuality = outputQuality;
            this.mHandler = mHandler;
        }

        @Override
        protected Boolean doInBackground(Bitmap... params) {
            if (params != null && params.length > 0) {
                OutputStream os = null;
                try {
                    os = mResolver.openOutputStream(mOutputUri);
                    params[0].compress(Bitmap.CompressFormat.JPEG, mOutputQuality, os);
                    return true;
                } catch (Exception e) {
                    Logger.e("转型异常", e);
                } finally {
                    try {
                        if (os != null) {
                            os.flush();
                            os.close();
                        }
                    } catch (IOException e) {
                        Logger.e("关流异常", e);
                    }
                    if (!params[0].isRecycled()) {
                        params[0].recycle();
                    }
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Message mMessage = mHandler.obtainMessage(0);
                mMessage.obj = mOutputUri;
                mHandler.sendMessage(mMessage);
            } else {
                Logger.e("传统意义错误");
                mHandler.sendEmptyMessage(-1);
            }
        }
    }


    ////////////////////////////////////// other ///////////////////////////////////////////////


    /**
     * 回收
     */
    public static void gc() {
        System.gc();
        // 表示java虚拟机会做一些努力运行已被丢弃对象（即没有被任何对象引用的对象）的 finalize
        // 方法，前提是这些被丢弃对象的finalize方法还没有被调用过
        System.runFinalization();
    }

    /**
     * 返回一个不可变的源位图的位图的子集,改变了可选的矩阵。新的位图可能与源相同的对象,或可能是一个副本。它初始化与原始位图的密度。如果源位图是不可变的
     * ,请求的子集是一样的源位图本身,然后返回源位图,没有新的位图创建。<br>
     *
     * @param source 产生子位图的源位图
     * @param x      子位图第一个像素在源位图的X坐标
     * @param y      子位图第一个像素在源位图的y坐标
     * @param width  子位图每一行的像素个数
     * @param height 子位图的行数
     * @param m      对像素值进行变换的可选矩阵
     * @param filter 如果为true，源图要被过滤。该参数仅在matrix包含了超过一个翻转才有效
     * @return 一个描述了源图指定子集的位图。 Bitmap
     */
    private static Bitmap createBitmap(Bitmap source, int x, int y, int width, int height, Matrix m, boolean filter) {
        Bitmap bitmap;
        try {
            bitmap = Bitmap.createBitmap(source, x, y, width, height, m, filter);
        } catch (OutOfMemoryError localOutOfMemoryError) {
            Logger.e("创建图片异常 ", localOutOfMemoryError);
            gc();
            bitmap = source;
        }
        return bitmap;
    }

    /**
     * 生成GIF 文件
     * @param mContext 上下文
     * @param fileName 生成的文件名, 不带后缀的
     * @param paths 要合成GIF的图片地址组
     * @param fps 刷新率, 大概隔多久刷新一下, 1000 表示 1秒
     * @param width 生成的文件宽度
     * @param height 生成的文件高度
     * @return 生成的GIF 文件
     */
    public static File createGif(Context mContext, String fileName, List<String> paths, int fps, int width, int height) {
        if (mContext == null || paths == null || paths.size() <= 0 || fps == 0 || width <= 0 || height <= 0) {
            return null;
        }
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            AnimatedGifEncoder localAnimatedGifEncoder = new AnimatedGifEncoder();
            localAnimatedGifEncoder.start(baos);//start
            localAnimatedGifEncoder.setRepeat(0);//设置生成gif的开始播放时间。0为立即开始播放
            localAnimatedGifEncoder.setDelay(Math.abs(fps));
            for (int i = 0; i < paths.size(); i++) {
                Bitmap mBitmap = readBitmapFromFileDescriptor(paths.get(i), width, height);
//            Bitmap mBitmap = BitmapFactory.decodeFile(paths.get(i));
                Bitmap resizeBm = setBitmapSize(mBitmap, width, height);
                localAnimatedGifEncoder.addFrame(resizeBm);
            }
            localAnimatedGifEncoder.finish();//finish
        } catch (Exception e) {
            Logger.e("生成GIF异常", e);
        }
        FileOutputStream fos = null;
        try {
            if (baos != null) {
                File mNewCacheFile = FileUtils.getNewCacheFile(mContext, fileName + ".gif", null);
                fos = new FileOutputStream(mNewCacheFile);
                baos.writeTo(fos);

                baos.flush();
                fos.flush();
                return mNewCacheFile;
            }
        } catch (Exception e) {
            Logger.e("存储GIF文件异常", e);
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
                Logger.e("关流异常", e);
            }
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                Logger.e("关流异常", e);
            }
        }
        return null;
    }

}
