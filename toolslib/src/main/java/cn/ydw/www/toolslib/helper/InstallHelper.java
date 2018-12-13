package cn.ydw.www.toolslib.helper;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import java.io.File;
import java.util.Locale;

/**
 * ========================================
 *
 * @author 杨德望 create on 2018/5/31
 * 描述: apk 自动安装辅助类
 * =========================================
 */

public final class InstallHelper {
    private Activity act;
    @SuppressLint("InlinedApi")
    private String[] installPermission = new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES};
    private final int GET_UNKNOWN_APP_SOURCES = 580, INSTALL_PACKAGES_REQUESTCODE = 590;
    private String apkPath, authority;

    public InstallHelper(Activity act) {
        this.act = act;
    }

    /**
     * 设置安装包路径, 和 7.0共享文件夹路径, 即清单文件里面的 authorities :
     * <provider
     * android:name="android.support.v4.content.FileProvider"
     * android:authorities="cn.ydw.www.toolslib"
     * android:exported="false"
     * android:grantUriPermissions="true">
     * <meta-data
     * android:name="android.support.FILE_PROVIDER_PATHS"
     * android:resource="@xml/filepaths" />
     * </provider>
     *
     * @param apkPath 安装包路径
     * @param authority 共享文件夹路径 {例如: cn.ydw.www.toolslib}
     */
    public InstallHelper setApkPath(String apkPath, String authority) {
        this.apkPath = apkPath;
        this.authority = authority;
        return this;
    }

    /**
     * 判断是否是8.0,8.0需要处理未知应用来源权限问题,否则直接安装
     */
    public void checkIsAndroidO() {
        if (Build.VERSION.SDK_INT >= 26) {
            boolean b = act.getPackageManager().canRequestPackageInstalls();
            if (b) {
                installApk();//安装应用的逻辑(写自己的就可以)
            } else {
                //请求安装未知应用来源的权限
                ActivityCompat.requestPermissions(act, installPermission
                        , INSTALL_PACKAGES_REQUESTCODE);
            }
        } else {
            installApk();
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull int[] grantResults) {
        if (requestCode == INSTALL_PACKAGES_REQUESTCODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                installApk();
            } else if (Build.VERSION.SDK_INT >= 26) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                act.startActivityForResult(intent, GET_UNKNOWN_APP_SOURCES);
            }
        }
    }

    public void onActivityResult(int requestCode) {
        if (requestCode == GET_UNKNOWN_APP_SOURCES) {
            checkIsAndroidO();
        }
    }

    /**
     * 安装APK
     */
    private void installApk() {
        if (TextUtils.isEmpty(apkPath)) {
            return;
        }
        File file = new File(apkPath);
        if ((!file.exists())) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);

        //判读版本是否在7.0以上
        if (Build.VERSION.SDK_INT >= 24) {
            //provider authorities
            Uri apkUri = FileProvider.getUriForFile(act, authority, file);
            //Granting Temporary Permissions to a URI
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        act.startActivity(intent);
    }


    //==============================附带额外的进度条弹窗==============================
    private ProgressDialog mDialog;
    private boolean needReset = true;

    public ProgressDialog initDownloadApkDialog() {
        mDialog = new ProgressDialog(act);
        mDialog.setTitle("提示");
        mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(true);
        return mDialog;
    }

    public void startDialog() {
        if (mDialog != null) {
            mDialog.setMessage("启动下载");
            if (!mDialog.isShowing()) {
                mDialog.show();
            }
        }
    }

    public ProgressDialog setDialogProgress(long total, long current, long speed) {
        if (mDialog != null) {
            if (needReset) {
                mDialog.setMax(Math.round(total));
                needReset = false;
            }
            mDialog.setProgress(Math.round(current));
            String mTxt;
            if (speed < 0) {
                mTxt = String.format(Locale.getDefault(), "进度: %s/%s",
                        formatRate(current), formatRate(total));
            } else {
                mTxt = String.format(Locale.getDefault(), "网速: %s/s 进度: %s/%s",
                        formatRate(speed), formatRate(current), formatRate(total));
            }
            mDialog.setMessage(mTxt);
        }
        return mDialog;
    }

    public void dismissDialog() {
        needReset = true;
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    private String formatRate(long value) {
        if (value < 1024) { // b
            return (value + "byte");
        } else if (value < 1048576) { //kb
            return String.format(Locale.getDefault(), "%.2fKB", value / 1024f);
        } else { //mb
            return String.format(Locale.getDefault(), "%.2fMB", value / 1048576f);
        }
    }
}
