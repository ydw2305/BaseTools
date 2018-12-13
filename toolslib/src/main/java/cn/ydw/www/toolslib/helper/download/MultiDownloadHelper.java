package cn.ydw.www.toolslib.helper.download;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import cn.ydw.www.toolslib.ToolConstants;
import cn.ydw.www.toolslib.utils.FileUtils;

/**
 * ========================================
 *
 * @author 杨德望
 * Create on 2018/12/3
 * 描述: 多下载器
 * =========================================
 */
public class MultiDownloadHelper {
    private static MultiDownloadHelper mHelper;

    private MultiDownloadHelper() {
    }

    public static MultiDownloadHelper get() {
        if (mHelper == null) {
            synchronized (MultiDownloadHelper.class) {
                if (mHelper == null) {
                    mHelper = new MultiDownloadHelper();
                }
            }
        }
        return mHelper;
    }

    ////////////////////////////////////////// init data ///////////////////////////////////////////

    private final Executor mTaskExecutor = Executors.newSingleThreadExecutor();// 任务池
    private final ReentrantLock mQueueLock = new ReentrantLock();// 重用锁
    private final Condition mProcessQueueCondition = mQueueLock.newCondition();
    private final Handler mHandler = new Handler(Looper.getMainLooper(), new DownloadResultCallback());

    private final Queue<String> mTaskQueue = new ConcurrentLinkedQueue<>(); // 呼叫下载的任务队列
    private final LinkedList<String> mDownloadTask = new LinkedList<>();// 正在下载的任务
    private final Map<String, String> cacheFileList = new LinkedHashMap<>();// 缓存的文件
    private final Set<DownloadCallback> mDownloadCallbacks = new LinkedHashSet<>();// 下载监听

    private boolean mTerminated = true; // 中断
    private int mMultiTaskNum = 3;//同时执行任务的数量
    private boolean mRun = false; // 是否正在下载
    private File mCacheDirectory;// 缓存文件夹

    ////////////////////////////////////////// throw method ///////////////////////////////////////////

    /**
     * 初始化
     * @param mContext 上下文
     */
    public MultiDownloadHelper init(Context mContext) {
        if (mTerminated) {
            mQueueLock.lock();
            // init data
            mTaskQueue.clear();
            mDownloadTask.clear();
            cacheFileList.clear();
            mDownloadCallbacks.clear();
            mTerminated = false;
            //初始化缓存文件夹
            mCacheDirectory = FileUtils.getCacheDirectory(mContext, null);
            execute();
            mQueueLock.unlock();
        }
        return this;
    }

    /**
     * 呼叫下载,
     *
     * @param mFileUrl 文件网址
     */
    public void postDownload(String mFileUrl) {
        if (mTerminated) throw new IllegalStateException("It's terminated,please call init()!");
        if (!TextUtils.isEmpty(mFileUrl)) {
            mQueueLock.lock();
            synchronized (mTaskQueue) {
                mTaskQueue.add(mFileUrl);
            }
            mProcessQueueCondition.signal();
            mQueueLock.unlock();
        }
    }

    /**
     * 添加下载监听
     *
     * @param callback 监听
     */
    public MultiDownloadHelper addDownloadListener(DownloadCallback callback) {
        if (mTerminated) throw new IllegalStateException("It's terminated,please call init()!");
        if (callback != null) {
            synchronized (mDownloadCallbacks) {
                mDownloadCallbacks.add(callback);
            }
        }
        return this;
    }
    /**
     * 移除下载监听
     *
     * @param callback 监听
     */
    public void removeDownloadListener(DownloadCallback callback) {
        if (callback != null) {
            synchronized (mDownloadCallbacks) {
                if (!mDownloadCallbacks.isEmpty()) {
                    mDownloadCallbacks.remove(callback);
                }
            }
        }
    }

    /**
     * 结束任务池运行
     */
    public void terminated(){
        mTerminated = true;
    }

    /**
     * 是否在执行下载动作
     * @return true 表示在下载
     */
    public boolean isRun() {
        return mRun;
    }

    ////////////////////////////////////////// init function ///////////////////////////////////////////

    // 执行任务池
    private void execute() {
        mTaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                while (!mTerminated) {
                    mQueueLock.lock();

                    if (mTaskQueue.isEmpty() || mDownloadTask.size() >= mMultiTaskNum) {
                        try {
                            mProcessQueueCondition.await();// 任务池等待
                        } catch (InterruptedException e) {
                            throw new RuntimeException("InterruptedException", e);
                        }
                    }
                    mRun = true;
                    String fileUrl = mTaskQueue.poll();
                    if (mDownloadTask.size() <= 0 || !mDownloadTask.contains(fileUrl)) {
                        callDownload(fileUrl);
                    }

                    mQueueLock.unlock();
                    mRun = false;
                }
            }
        });
    }

    // 正式呼叫下载
    private void callDownload(String fileUrl) {
        if (TextUtils.isEmpty(fileUrl)) return;
        // 校验是否添加到缓存器
        if (cacheFileList.keySet().contains(fileUrl)) { // 如果下载过一次
            String localPath = cacheFileList.get(fileUrl);
            DownloadResultHelper.sendDownloadSuc(mHandler, fileUrl, localPath, true);
        } else {
            // 校验是否本地缓存了
            int mLastIndexOf = fileUrl.lastIndexOf("/");
            if (mLastIndexOf > 0) {
                // 获取文件名
                String displayName = fileUrl.substring(mLastIndexOf);
                // 要存储的目标文件
                File mDesFile = new File(mCacheDirectory, displayName);

                if (mDesFile.exists()) {
                    String localPath = mDesFile.getAbsolutePath();
                    cacheFileList.put(fileUrl, localPath);
                    DownloadResultHelper.sendDownloadSuc(mHandler, fileUrl, localPath, true);

                } else {
                    // Logger.e("正在下载 = " + fileUrl);
                    mDownloadTask.add(fileUrl);
                    DownloadResultHelper.sendDownloadWait(mHandler, fileUrl, 0, 0);
                    new MultiDownloader(mCacheDirectory, fileUrl, mHandler).execute();
                }
            }
        }
    }

    // 下载结果回馈
    private class DownloadResultCallback implements Handler.Callback {

        @Override
        public boolean handleMessage(Message msg) {
            Bundle mBundle = null;
            if (msg.obj != null && msg.obj instanceof Bundle) {
                mBundle = (Bundle) msg.obj;
            }

            if (msg.what == ToolConstants.DownloadState_Err) { // 下载任务执行失败
                if (mBundle != null) {
                    String fileUrl = mBundle.getString(ToolConstants.DownloadPathUrl);
                    updateCallback(fileUrl, null, false);
                }

            } else if (msg.what == ToolConstants.DownloadState_Wait) { // 下载任务执行进度
                if (mBundle != null) {
                    String fileUrl = mBundle.getString(ToolConstants.DownloadPathUrl);
                    float progress = mBundle.getFloat(ToolConstants.DownloadProgress);
                    float speed = mBundle.getFloat(ToolConstants.DownloadSpeed);
                    updateCallback(fileUrl, progress, speed);
                }

            } else if (msg.what == ToolConstants.DownloadState_Suc) { // 下载任务执行成功
                if (mBundle != null) {
                    String fileUrl = mBundle.getString(ToolConstants.DownloadPathUrl);
                    String localPath = mBundle.getString(ToolConstants.DownloadPathLocal);
                    boolean isLocal = mBundle.getBoolean(ToolConstants.DownloadIsLocal);
                    updateCallback(fileUrl, localPath, isLocal);
                }
            }
            return false;
        }
    }
    // 更新回调结果
    private void updateCallback(String fileUrl, String localPath, boolean isLocal) {
        if (mDownloadTask.size() > 0) {
            mQueueLock.lock();
            mDownloadTask.remove(fileUrl);
            cacheFileList.put(fileUrl, localPath);
            if (!mTaskQueue.isEmpty()) {
                mProcessQueueCondition.signal();
            }
            mQueueLock.unlock();
        }
        synchronized (mDownloadCallbacks) {
            for (DownloadCallback mCallback: mDownloadCallbacks) {
                if (mCallback != null) {
                    mCallback.onDownloadResult(fileUrl, localPath, isLocal);
                }
            }
        }
    }
    // 更新回调进度
    private void updateCallback(String fileUrl, float progress, float speed) {
        synchronized (mDownloadCallbacks) {
            for (DownloadCallback mCallback : mDownloadCallbacks) {
                if (mCallback != null) {
                    mCallback.onDownLoadProgress(fileUrl, progress, speed);
                }
            }
        }
    }
}
