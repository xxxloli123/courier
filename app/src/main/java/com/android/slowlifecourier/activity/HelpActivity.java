package com.android.slowlifecourier.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.slowlifecourier.R;
import com.interfaceconfig.Config;

public class HelpActivity extends AppCompatActivity {
    private WebView webView;
    public static final String URI = "uri";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        webView = (WebView) findViewById(R.id.webview);
        init();
    }

    @SuppressLint("JavascriptInterface")
    private void init() {
        String url = getIntent().getStringExtra(URI);
        WebSettings setting = webView.getSettings();
        setting.setJavaScriptEnabled(true);
        setting.setJavaScriptCanOpenWindowsAutomatically(false);
        setting.setAppCacheEnabled(true);
        setting.setUseWideViewPort(true);//关键点
        setting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        setting.setDisplayZoomControls(false);
        setting.setAllowFileAccess(false); // 允许访问文件
        setting.setBuiltInZoomControls(false); // 设置显示缩放按钮
        setting.setSupportZoom(false); // 支持缩放
        setting.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.addJavascriptInterface(new JavaScriptCallAndroid(), "Android");
//        webView.loadUrl("file:///android_asset/test.html");
        webView.loadUrl(Config.Url.getUrl(url));
//        webView.loadDataWithBaseURL(Config.LOCAL_HOST, null, "html/text", "utf-8",
//                Config.Url.getUrl("slowlife/app/appGeneral/ruleGZ.html"));
        webView.setWebViewClient(new WebViewClient() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                view.loadUrl(url);
                return true;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }


    class JavaScriptCallAndroid {
        @JavascriptInterface
        public void tel(String phone) {
            //用intent启动拨打电话
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
            startActivity(intent);
        }

        @JavascriptInterface
        public void goBack() {
            if (webView.canGoBack()) webView.goBack();
            else finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) webView.goBack();
            else finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
