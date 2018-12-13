package cn.ydw.www.toolslib.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cn.ydw.www.toolslib.utils.Logger;

/**
 * =====================================
 * 作    者: 杨德望
 * 版    本：${VERSION_CODE}.
 * 创建日期：2018/4/10.
 * 描    述：
 * =====================================
 */
@SuppressWarnings({"WeakerAccess", "UnusedReturnValue"})
public class WebHelper implements View.OnLongClickListener {
    private Context context;
    private WebView mWebView;
    private OnWebLoadListener owll;
    private OnWebLoadListener2 owll2;

    public WebHelper(Context context) {
        this.context = context;
    }

    public WebHelper setOnWebLoadListener(OnWebLoadListener owll) {
        this.owll = owll;
        return this;
    }
    public WebHelper setOnWebLoadListener(OnWebLoadListener2 owll) {
        this.owll2 = owll;
        return this;
    }

    public WebHelper loadUrl(String url){
        if (mWebView != null) {
            mWebView.loadUrl(url);
        }
        return this;
    }

    /**
     * 初始化web控件
     * @param webView 控件
     * @return 辅助类
     */
    @SuppressLint({"SetJavaScriptEnabled"})
    public WebHelper initWebView(WebView webView){
        mWebView = webView;
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setSaveEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return loadUrlCanOpenApp(url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (owll != null) {
                        owll.onWebReceivedError(error.getErrorCode());
                    }
                    if (owll2 != null) {
                        owll2.onWebReceivedError(error.getErrorCode());
                    }
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    if (owll != null) {
                        owll.onWebReceivedError(errorCode);
                    }
                    if (owll2 != null) {
                        owll2.onWebReceivedError(errorCode);
                    }
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (owll2 != null) {
                    owll2.onWebPageFinished(view, url);
                }
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (owll != null) {
                    owll.onReceivedTitle(title);
                }
                if (owll2 != null) {
                    owll2.onReceivedTitle(title);
                }
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (owll != null) {
                    owll.onWebProgressChanged(newProgress);
                }
                if (owll2 != null) {
                    owll2.onWebProgressChanged(newProgress);
                }
            }
        });
        mWebView.setOnLongClickListener(this);
        return this;
    }

    @Override
    public boolean onLongClick(View v) {
        // 屏蔽长按事件
        return true;
    }

    @SuppressWarnings("SameParameterValue")
    @SuppressLint({"JavascriptInterface", "AddJavascriptInterface"})
    public WebHelper addJavascriptInterface(Object obj, String name){
        if (mWebView != null) {
            //webView与js 交互
            mWebView.addJavascriptInterface(obj, name);
        }
        return this;
    }

    /**
     * 销毁web控件
     */
    public void destroyWeb(){
        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, "",
                    "text/html", "utf-8", null);
            mWebView.clearHistory();
            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
    }


    //判断是打开app还是开启网页
    private boolean loadUrlCanOpenApp(String url) {
        if (TextUtils.isEmpty(url)) return false;
        try {
            if (!url.startsWith("http") && !url.startsWith("https") && !url.startsWith("ftp")) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                //判断app是否安装
                if (context.getPackageManager().queryIntentActivities(
                        intent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0) {
                    context.startActivity(intent);
                    return true;
                }
            }
        } catch (Exception e) {
            Logger.e("区分是APP还是网址异常", e);
        }
        return false;
    }

    public interface OnWebLoadListener {
        void onReceivedTitle(String title);
        void onWebProgressChanged(int newProgress);
        void onWebReceivedError(int errorCode);
    }
    public interface OnWebLoadListener2 {
        void onReceivedTitle(String title);
        void onWebProgressChanged(int newProgress);
        void onWebPageFinished(WebView view, String url);
        void onWebReceivedError(int errorCode);
    }
}
