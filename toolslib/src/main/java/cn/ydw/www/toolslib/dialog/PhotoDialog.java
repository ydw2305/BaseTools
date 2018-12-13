package cn.ydw.www.toolslib.dialog;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import cn.ydw.www.toolslib.R;
import cn.ydw.www.toolslib.base.BaseDialogFragment;
import cn.ydw.www.toolslib.helper.GlideHelper;
import cn.ydw.www.toolslib.utils.Logger;
import cn.ydw.www.toolslib.widget.ZoomPhotoView;

/**
 * ========================================
 *
 * @author 杨德望 create on 2018/7/14
 * 描述: 图片可放大的弹窗,
 * =========================================
 */
public class PhotoDialog extends BaseDialogFragment {

    private ObjImgLoader mPicImageLoader;
    private TextView mTvIndex;
    private int mCurrentNum = 0;

    // 外部直接调用的方法, 建议直接用这个, 一定要先设置加载器, 再显示,
    public static PhotoDialog create(ObjImgLoader picImageLoader) {
        return new PhotoDialog().setPicImageLoader(picImageLoader);
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_view_item_zoompic_show, container, false);
    }

    @Override
    public void initData() {
        ViewPager mViewPager = getRootView().findViewById(R.id.vp_photo_show_headview);
        mViewPager.setAdapter(new PhotoPageAdapter());

        mTvIndex = getRootView().findViewById(R.id.tv_photo_show_index);
        final ImageView ivDownload = getRootView().findViewById(R.id.iv_photo_show_download);
        ivDownload.setVisibility(View.GONE);
        mViewPager.addOnPageChangeListener(new PhotoPageChangeListener());

        if (mPicImageLoader != null) {
            int mSize = mPicImageLoader.getCount();
            if (mSize > 0) {
                mViewPager.setCurrentItem(mCurrentNum % mSize);
                if (mTvIndex != null) {
                    mTvIndex.setText(((mCurrentNum + 1) + "/" + mSize));
                }
            }
        }
    }

    // FIXME 设置图片加载器, 注意不要多次添加, 因为ViewPager不刷新被缓存的当前页面,
    // 而且页面展开的时候无法立即切换成对应的页面,
    public PhotoDialog setPicImageLoader(ObjImgLoader picImageLoader) {
        if (mPicImageLoader != null) {
            throw new IllegalArgumentException(" Don't call 'ObjImgLoader' again! " +
                    "It will cause the ViewPager not notify and not selected!");
        }
        mPicImageLoader = picImageLoader;
        return this;
    }
    // 设置展开的时候要显示哪个页面
    public PhotoDialog setSelNum(int currentNum) {
        this.mCurrentNum = currentNum;
         return this;
    }

    // ============================ 图片页面适配器 ===================================
    private class PhotoPageAdapter extends PagerAdapter implements View.OnClickListener,
            View.OnLongClickListener {
        private final LinkedList<ZoomPhotoView> viewCache = new LinkedList<>();

        @Override
        public int getCount() {
            if (mPicImageLoader == null) {
                return 0;
            } else {
                return mPicImageLoader.getCount();
            }
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull final ViewGroup container, int position) {
            ZoomPhotoView piv;
            if (viewCache.size() > 0) {
                piv = viewCache.remove();
                piv.reset();
            } else {
                piv = new ZoomPhotoView(act);
            }
            piv.setOnClickListener(this);
            piv.setOnLongClickListener(this);
            if (mPicImageLoader != null) {
                mPicImageLoader.picLoader(position, piv);
            }
            container.addView(piv);
            return piv;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            ZoomPhotoView piv = (ZoomPhotoView) object;
            container.removeView(piv);
            viewCache.add(piv);
        }

        @Override
        public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            super.setPrimaryItem(container, position, object);
        }

        @Override
        public void onClick(View v) {
            PhotoDialog.this.dismiss();
        }

        @Override
        public boolean onLongClick(View v) {
            return mPicImageLoader != null && mPicImageLoader.downLoader(mCurrentNum);
        }
    }

    // ========================== 页面切换监听 ==============================
    private class PhotoPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            if (mPicImageLoader != null) {
                mTvIndex.setText(((position + 1) + "/" + mPicImageLoader.getCount()));
            }
            mCurrentNum = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

    // ---------------- 加载字符串的图片, 加载器,--------------------
    public static class StringImgLoader extends ObjImgLoader<String> {

        public StringImgLoader(List<String> imgList) {
            super(imgList);
        }

        @Override
        public String analysisItem(String info) {
            return info;
        }

    }
    // ---------------- 加载文件的图片, 加载器, ---------------------
    public static class FileImgLoader extends ObjImgLoader<File> {

        public FileImgLoader(List<File> imgList) {
            super(imgList);
        }

        @Override
        public File analysisItem(File info) {
            return info;
        }

    }
    // ---------------- 加载Uri的图片, 加载器, ---------------------
    public static class UriImgLoader extends ObjImgLoader<Uri> {

        public UriImgLoader(List<Uri> imgList) {
            super(imgList);
        }

        @Override
        public Uri analysisItem(Uri info) {
            return info;
        }
    }

    // 加载不同类型的图片, 加载器
    public static abstract class ObjImgLoader<T> {
        private LinkedList<T> mImgList = new LinkedList<>();

        public ObjImgLoader(List<T> imgList) {
            if (imgList != null && imgList.size() > 0) {
                mImgList.addAll(imgList);
            }
        }
        private int getCount(){
            return mImgList.size();
        }

        // 解析单格的数据
        public abstract Object analysisItem(T info);

        public  void picLoader(int position, ZoomPhotoView piv){
            if (mImgList == null || mImgList.size() <= 0 || mImgList.size() <= position) {
                return;
            }
            Object mInfo = analysisItem(mImgList.get(position));
            if (mInfo != null) {
                try {
                    GlideHelper.displayImage(mInfo, piv);
                } catch (Exception e) {
                    Logger.e("解析图片异常", e);
                }
            }
        }
        // 下载器,
        @SuppressWarnings("unused")
        public boolean downLoader(int position){
            return false;
        }
    }
}
