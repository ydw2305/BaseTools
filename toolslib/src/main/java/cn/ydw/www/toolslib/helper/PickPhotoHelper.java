package cn.ydw.www.toolslib.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import java.io.File;

import cn.ydw.www.toolslib.ToolConstants;
import cn.ydw.www.toolslib.dialog.AlertCallback;
import cn.ydw.www.toolslib.dialog.SheetAlertDialog;
import cn.ydw.www.toolslib.utils.BitmapUtils;
import cn.ydw.www.toolslib.utils.FileUtils;
import cn.ydw.www.toolslib.utils.MyDialogUtils;
import cn.ydw.www.toolslib.utils.permission.PermissionsManager;
import cn.ydw.www.toolslib.utils.permission.PermissionsResultAction;

/**
 * ========================================
 *
 * @author 杨德望
 * Create on 2018/8/9
 * 描述: 相机和相册的辅助类, (暂行办法, 待优化相册选择类)
 * =========================================
 */
public final class PickPhotoHelper implements AlertCallback.OnSheetItemListener {

    private final int REQUESTCODE_CAMERA = 3023; // 请求相机
    private final int REQUESTCODE_ALBUM = 3033; // 请求相册


    private PickPhotoHelper() {
    }

    public static PickPhotoHelper create() {
        // 创建图片相机相册选择器
        return new PickPhotoHelper();
    }

    private SheetAlertDialog mSheetAlertDialog;
    private SelectorCallback mSelectorCallback;
    private PickCallback mPickCallback;

    private String mCameraPhotoPath;

    private Context mContext;

    /**
     * 设置选图片启动回调
     *
     * @param selectorCallback 选择是开相机还是开相册
     * @return this
     */
    public PickPhotoHelper setSelectorCallback(SelectorCallback selectorCallback) {
        mSelectorCallback = selectorCallback;
        return this;
    }

    /**
     * 设置图片被选中回调
     *
     * @param pickCallback 图片选择后回调
     * @return this
     */
    public PickPhotoHelper setPickCallback(PickCallback pickCallback) {
        mPickCallback = pickCallback;
        return this;
    }

    /**
     * 显示相机相册选择器
     *
     * @param act 活动页
     */
    public void showPhotoSelector(final AppCompatActivity act) {
        PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(
                act, ToolConstants.imgPermission, new PermissionsResultAction() {
                    @Override
                    public void onGranted() {
                        showPhotoSelectorWithoutPermission(act.getSupportFragmentManager());
                    }

                    @Override
                    public void onDenied(String permission) {
                        MyDialogUtils.showDialogTipUserGoToAppSetting(act,
                                "您拒绝了相机权限",
                                new MyDialogUtils.OnSureClickListener() {
                                    @Override
                                    public void onSure() {
                                        MyDialogUtils.goToAppSetting(act);
                                    }
                                });
                    }
                });
    }

    /**
     * 显示相机相册选择器
     *
     * @param fragment 片段
     */
    public void showPhotoSelector(final Fragment fragment) {
        PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(
                fragment, ToolConstants.imgPermission, new PermissionsResultAction() {
                    @Override
                    public void onGranted() {
                        showPhotoSelectorWithoutPermission(fragment.getChildFragmentManager());
                    }

                    @Override
                    public void onDenied(String permission) {
                        MyDialogUtils.showDialogTipUserGoToAppSetting(fragment.getActivity(),
                                "您拒绝了相机权限",
                                new MyDialogUtils.OnSureClickListener() {
                                    @Override
                                    public void onSure() {
                                        MyDialogUtils.goToAppSetting(fragment.getActivity());
                                    }
                                });
                    }
                });
    }

    /**
     * 显示相机相册选择器, 不包含权限校验
     *
     * @param manager 片段管理器
     */
    @SuppressWarnings("WeakerAccess")
    public void showPhotoSelectorWithoutPermission(FragmentManager manager) {
        if (manager == null) return;
        if (mSheetAlertDialog == null) {
            mSheetAlertDialog = new SheetAlertDialog()
                    .setAlertTxt(null, new String[]{
                            "拍照", "从相册中选择"})
                    .setOnItemClickListener(PickPhotoHelper.this);
        }
        mSheetAlertDialog.show(manager);
    }

    @Override
    public void onAlertItemClick(SheetAlertDialog mAlert, int position, String itemMsg) {
        if (mSelectorCallback == null) return;
        if (position == 0) { //开启相机
            mSelectorCallback.callCamera(this);

        } else if (position == 1) { //开启相册
            mSelectorCallback.callAlbum(this);

        }
    }

    /**
     * 开启系统相机
     *
     * @param act       活动页
     * @param authority 辨识符
     */
    public void startCamera(Activity act, String authority) {
        Intent mCameraIntent = getCameraIntent(act, authority);
        if (mCameraIntent != null) {
            act.startActivityForResult(mCameraIntent, REQUESTCODE_CAMERA);
        }
    }

    /**
     * 开启系统相机
     *
     * @param fragment  片段
     * @param authority 辨识符
     */
    public void startCamera(Fragment fragment, String authority) {
        Intent mCameraIntent = getCameraIntent(fragment.getContext(), authority);
        if (mCameraIntent != null) {
            fragment.startActivityForResult(mCameraIntent, REQUESTCODE_CAMERA);
        }
    }

    // 获取启动相机意图
    private Intent getCameraIntent(Context c, String authority) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
            if (mPickCallback != null) {
                mPickCallback.onPickPhotoErr("未检测到内存卡");
            }
            return null;
        }
        Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 调用相机拍照后的照片存储的路径
        String displayName = "img" + System.currentTimeMillis() + ".jpeg";
        File image = FileUtils.getNewCacheFile(c, displayName, Environment.DIRECTORY_PICTURES);
        if (image == null) {
            if (mPickCallback != null) {
                mPickCallback.onPickPhotoErr("存储路径异常");
            }
            return null;
        }
        mCameraPhotoPath = image.getAbsolutePath();

        /* ============= 在安卓 7.0 以后, 请求文件 URI 需要进行安全转换 ================= */
        Uri uri = BitmapUtils.coverFile2Uri(c, image, authority);
        intent2.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        return intent2;
    }

//    /**
//     * 启动相册
//     *
//     * @param act         活动页
//     * @param isMultiMode 是否多选模式
//     * @param limitNum    限制选择数
//     */
//    public void startAlbum(Activity act, boolean isMultiMode, int limitNum) {
//        ImagePicker mImagePicker = ImagePicker.getInstance();
//        mImagePicker.setMultiMode(isMultiMode);//设置单选还是多选模式
//        if (isMultiMode) {
//            mImagePicker.setSelectLimit(limitNum);
//        }
//
//        Intent intent = new Intent(act, ImageGridActivity.class);
//        act.startActivityForResult(intent, 3033);
//    }

//    /**
//     * 启动相册
//     *
//     * @param fragment    片段
//     * @param isMultiMode 是否多选模式
//     * @param limitNum    限制选择数
//     */
//    public void startAlbum(Fragment fragment, boolean isMultiMode, int limitNum) {
//        ImagePicker mImagePicker = ImagePicker.getInstance();
//        mImagePicker.setMultiMode(isMultiMode);//设置单选还是多选模式
//        if (isMultiMode) {
//            mImagePicker.setSelectLimit(limitNum);
//        }
//
//        Intent intent = new Intent(fragment.getContext(), ImageGridActivity.class);
//        fragment.startActivityForResult(intent, 3033);
//    }

    /**
     * 启动相册
     *
     * @param act 活动
     */
    public void startAlbum(Activity act) {
        if (act != null && !act.isFinishing()) {
            mContext = act;
            Intent albumIntent = new Intent(Intent.ACTION_PICK);
            albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            act.startActivityForResult(albumIntent, REQUESTCODE_ALBUM);
        }
    }

    /**
     * 启动相册
     *
     * @param fragment 片段
     */
    public void startAlbum(Fragment fragment) {
        if (fragment != null) {
            mContext = fragment.getContext();
            Intent albumIntent = new Intent(Intent.ACTION_PICK);
            albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            fragment.startActivityForResult(albumIntent, REQUESTCODE_ALBUM);
        }
    }


    // 请使用这个接收回调更新
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mPickCallback == null) return;

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUESTCODE_CAMERA) { // 如果是系统相机回馈
                mPickCallback.onPickPhotoSuc(mCameraPhotoPath);

            } else if (requestCode == REQUESTCODE_ALBUM) { // 如果是系统相册回馈
                if (data != null) {
                    Uri mUri = data.getData();
                    if (mContext != null && mUri != null) {
                        String mFilePath = BitmapUtils.coverUri2FilePath(mContext, mUri);
                        if (!TextUtils.isEmpty(mFilePath)) {
                            mPickCallback.onPickPhotoSuc(mFilePath);
                        } else {
                            mPickCallback.onPickPhotoErr("没有数据");
                        }
                    } else {
                        mPickCallback.onPickPhotoErr("没有数据");
                    }
                } else {
                    mPickCallback.onPickPhotoErr("没有数据");
                }
            }

//        } else if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
//            if (data != null) {
//                @SuppressWarnings("unchecked")
//                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
//                Logger.e("获取到的 = " + images);
//                if (images != null && images.size() > 0) {
//                    String[] imgs = new String[images.size()];
//                    for (int index = 0; index < images.size(); index++) {
//                        imgs[index] = images.get(index).path;
//                    }
//                    mPickCallback.onPickPhotoSuc(imgs);
//                }
//            } else {
//                mPickCallback.onPickPhotoErr("没有数据");
//            }
        }
    }


    // =================================== 回调 ============================================//
    public interface SelectorCallback {
        void callCamera(PickPhotoHelper mHelper);

        void callAlbum(PickPhotoHelper mHelper);
    }

    public interface PickCallback {
        void onPickPhotoSuc(String... imgPath);

        void onPickPhotoErr(String err);
    }


    //----------------------------------相册初始化-----------------------------------------------//

    // 初始化相册选择器
//    public static void initImagePick() {
//        ImagePicker imagePicker = ImagePicker.getInstance();
//        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
//        imagePicker.setShowCamera(true);  //显示拍照按钮
//        imagePicker.setCrop(false);        //允许裁剪（单选才有效）
//        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
//        imagePicker.setSelectLimit(9);    //选中数量限制
//        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
//        imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
//        imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
//        imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
//        imagePicker.setOutPutY(1000);//保存文件的高度。单位像素
//
//    }
//
//    // 相册选择类
//    private static class GlideImageLoader implements ImageLoader {
//
//        @Override
//        public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
//            GlideHelper.displayImage(activity, new File(path), imageView);
//        }
//
//        @Override
//        public void displayImagePreview(Activity activity, String path, ImageView imageView, int width, int height) {
//            GlideHelper.displayImage(activity, new File(path), imageView);
//        }
//
//        @Override
//        public void clearMemoryCache() {
//            //这里是清除缓存的方法,根据需要自己实现
//        }
//
//    }

}
