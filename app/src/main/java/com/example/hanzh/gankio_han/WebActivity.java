package com.example.hanzh.gankio_han;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.example.hanzh.gankio_han.utils.ToastUtils;

import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;

public class WebActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar webToolbar;
    private FloatingActionButton fab,web_fab_share;
    private WebView mWebView;
    private NumberProgressBar mProgressbar;
    private TextView web_toolbar_title;
    private String mUrl;
    private String mTitle;

    private FrameLayout frameLayout = null;

    private WebChromeClient.CustomViewCallback myCallBack = null;

    private View myView=null;

    private WebChromeClient chromeClient = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        mUrl = getIntent().getStringExtra("Url");
        mTitle = getIntent().getStringExtra("Desc");

        initView();
        initViewAction();
    }

    private void initView(){
        webToolbar = (Toolbar) findViewById(R.id.web_toolbar);
        fab = (FloatingActionButton) findViewById(R.id.web_fab);
        web_fab_share=(FloatingActionButton)findViewById(R.id.web_fab_share);
        mWebView=(WebView)findViewById(R.id.webView);
        mProgressbar=(NumberProgressBar)findViewById(R.id.number_progress_bar);
        web_toolbar_title=(TextView)findViewById(R.id.web_toolbar_title);
        frameLayout=(FrameLayout)findViewById(R.id.web_frameLayout);
    }

    private void initViewAction(){
        web_toolbar_title.setText(mTitle);
        setSupportActionBar(webToolbar);
        fab.setOnClickListener(this);
        web_fab_share.setOnClickListener(this);

        chromeClient=new ChromeClient();
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setDatabaseEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setSaveFormData(false);
        settings.setLoadWithOverviewMode(true);
        settings.setAppCacheEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setSupportZoom(true);  //支持缩放
        settings.setBuiltInZoomControls(true);  //显示缩放工具
        settings.setUseWideViewPort(true);
        mWebView.setWebChromeClient(chromeClient);
        mWebView.setWebViewClient(new LoveClient());
        mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mWebView.loadUrl(mUrl);
    }

    @Override
    public void onBackPressed() {
        if(myView == null){
            super.onBackPressed();
        }
        else{
            chromeClient.onHideCustomView();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.web_fab:
                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                }
                else {
                    //下面的操作是为了防止webVIew中ZoomButtonsController崩溃,有可能出现这种情况
//                    if(mWebView != null) {
//                        mWebView.getSettings().setBuiltInZoomControls(true);
//                        mWebView.setVisibility(View.GONE);
//                        long timeout = ViewConfiguration.getZoomControlsTimeout();//timeout ==3000
//                        Log.i("time==",timeout+"");
//                        new Timer().schedule(new TimerTask() {
//                            @Override
//                            public void run() {
//                                // TODO Auto-generated method stub
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        mWebView.destroy();//所有webview操作必须在同一线程
//                                    }
//                                });
//                            }
//                        }, timeout);
//                    }
                    ViewGroup view = (ViewGroup) getWindow().getDecorView();
                    view.removeAllViews();
                    WebActivity.this.finish();
                }
                break;
            case R.id.web_fab_share:
                showShare(mUrl,mTitle);
                break;
            default:
                break;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
    }

    private class ChromeClient extends WebChromeClient implements MediaPlayer.OnCompletionListener {


        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            if(myView != null){
                callback.onCustomViewHidden();
                return;
            }
            //frameLayout.removeView(videoWebView);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            mWebView.setVisibility(View.GONE);
            frameLayout.addView(view);
            myView = view;
            myCallBack = callback;
            mWebView.setVisibility(View.VISIBLE);
            super.onShowCustomView(view, callback);
        }

        @Override
        public void onHideCustomView() {
            if(myView == null){
                return;
            }
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            frameLayout.removeView(myView);
            myView = null;
            //frameLayout.addView(videoWebView);
            mWebView.setVisibility(View.GONE);
            myCallBack.onCustomViewHidden();
            mWebView.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            // TODO Auto-generated method stub
            Log.d("ZR", consoleMessage.message() + " at " + consoleMessage.sourceId() + ":" + consoleMessage.lineNumber());
            return super.onConsoleMessage(consoleMessage);
        }

        @Override public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            mProgressbar.setProgress(newProgress);
            if (newProgress == 100) { mProgressbar.setVisibility(View.GONE); }
            else { mProgressbar.setVisibility(View.VISIBLE); }
        }

        @Override public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }

        @Override public void onCompletion(MediaPlayer mp) {
            if (mp != null) {
                if (mp.isPlaying()) mp.stop();
                mp.reset();
                mp.release();
                mp = null;
            }
        }
    }

    private class LoveClient extends WebViewClient {

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url != null) view.loadUrl(url);
            return true;
        }

        @Override public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            // 这些视频需要hack CSS才能达到全屏播放的效果
            if (url.contains("www.vmovier.com")) {
                injectCSS("vmovier.css");
            }
            else if (url.contains("video.weibo.com")) {
                injectCSS("weibo.css");
            }
            else if (url.contains("m.miaopai.com")) {
                injectCSS("miaopai.css");
            }
        }
    }

    // Inject CSS method: read style.css from assets folder
    // Append stylesheet to document head
    private void injectCSS(String filename) {
        try {
            InputStream inputStream = WebActivity.this.getAssets().open(filename);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            String encoded = Base64.encodeToString(buffer, Base64.NO_WRAP);
            mWebView.loadUrl("javascript:(function() {" +
                    "var parent = document.getElementsByTagName('head').item(0);" +
                    "var style = document.createElement('style');" +
                    "style.type = 'text/css';" +
                    // Tell the browser to BASE64-decode the string into your script !!!
                    "style.innerHTML = window.atob('" + encoded + "');" +
                    "parent.appendChild(style)" +
                    "})()");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refresh() {
        mWebView.reload();
    }

    private void stopLoading() {
        mWebView.stopLoading();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) mWebView.destroy();
    }


    @Override protected void onPause() {
        if (mWebView != null) {
            mWebView.onPause();
            try {
                mWebView.getClass().getMethod("onPause").invoke(mWebView, (Object[]) null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onPause();
    }


    @Override protected void onResume() {
        super.onResume();
        if (mWebView != null) {
            mWebView.onResume();
            try {
                mWebView.getClass().getMethod("onResume").invoke(mWebView, (Object[]) null);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_web, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.web_action_reload:
                refresh();
                return true;
            case R.id.web_action_stop:
                stopLoading();
                return true;
            case R.id.web_action_openurl:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                Uri uri = Uri.parse(mUrl);
                intent.setData(uri);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
                else {
                    ToastUtils.showShort(WebActivity.this, "打开网站失败");
                }
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showShare(final String url,String title) {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(title);
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(url);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(title+" : "+url+"\n通过" + this.getString(R.string.design) + "发布");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(url);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("测试GankIO Design By Han");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(url);
        // 启动分享GUI
        oks.show(this);
    }
}
