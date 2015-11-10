package com.example.hanzh.gankio_han;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.Toast;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.example.hanzh.gankio_han.utils.ToastUtils;

import java.io.InputStream;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class VideoActivity extends AppCompatActivity {

    @Bind(R.id.video_toolbar)
    Toolbar toolbar;
    @Bind(R.id.videoWebView)
    WebView videoWebView;
    @Bind(R.id.video_number_progress_bar)
    NumberProgressBar mProgressbar;
    @Bind(R.id.video_frameLayout)
    FrameLayout frameLayout;

    private String mUrl;
    private String mDesc;

    private WebChromeClient.CustomViewCallback myCallBack = null;

    private View myView = null;

    private WebChromeClient chromeClient = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        ButterKnife.bind(this);
        mUrl = getIntent().getStringExtra("Url");
        mDesc = getIntent().getStringExtra("Desc");
        System.out.println("mUrl : " + mUrl + " ; mDesc : " + mDesc);
        initViewAction();
    }

    private void initViewAction() {
        toolbar.setTitle(mDesc);
        setSupportActionBar(toolbar);

        chromeClient = new ChromeClient();
        if (!mUrl.equals("")) {
            WebSettings settings = videoWebView.getSettings();
            settings.setJavaScriptEnabled(true);
            settings.setAllowFileAccess(true);
            settings.setDatabaseEnabled(true);
            settings.setDomStorageEnabled(true);
            settings.setSaveFormData(false);
            settings.setLoadWithOverviewMode(true);
            settings.setAppCacheEnabled(true);
            settings.setCacheMode(WebSettings.LOAD_DEFAULT);
            settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            settings.setSupportZoom(true);
            settings.setBuiltInZoomControls(true);
            settings.setUseWideViewPort(true);
            videoWebView.setWebChromeClient(chromeClient);
            videoWebView.setWebViewClient(new LoveClient());
            videoWebView.loadUrl(mUrl);
        }
    }

    @Override
    public void onBackPressed() {
        if (myView == null) {
            super.onBackPressed();
        } else {
            chromeClient.onHideCustomView();
        }
    }

    private class ChromeClient extends WebChromeClient implements MediaPlayer.OnCompletionListener {

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            if (myView != null) {
                callback.onCustomViewHidden();
                return;
            }
            //frameLayout.removeView(videoWebView);
            videoWebView.setVisibility(View.GONE);
            frameLayout.addView(view);
            myView = view;
            myCallBack = callback;
            videoWebView.setVisibility(View.VISIBLE);
            super.onShowCustomView(view, callback);
        }

        @Override
        public void onHideCustomView() {
            if (myView == null) {
                return;
            }
            frameLayout.removeView(myView);
            myView = null;
            //frameLayout.addView(videoWebView);
            videoWebView.setVisibility(View.GONE);
            myCallBack.onCustomViewHidden();
            videoWebView.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            // TODO Auto-generated method stub
            Log.d("ZR", consoleMessage.message() + " at " + consoleMessage.sourceId() + ":" + consoleMessage.lineNumber());
            return super.onConsoleMessage(consoleMessage);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            mProgressbar.setProgress(newProgress);
            if (newProgress == 100) {
                mProgressbar.setVisibility(View.GONE);
            } else {
                mProgressbar.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }

        @Override
        public void onCompletion(MediaPlayer mp) {
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

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            // 这些视频需要hack CSS才能达到全屏播放的效果
            if (url.contains("www.vmovier.com")) {
                injectCSS("vmovier.css");
            } else if (url.contains("video.weibo.com")) {
                injectCSS("weibo.css");
            } else if (url.contains("m.miaopai.com")) {
                injectCSS("miaopai.css");
            }
        }
    }

    // Inject CSS method: read style.css from assets folder
    // Append stylesheet to document head
    private void injectCSS(String filename) {
        try {
            InputStream inputStream = VideoActivity.this.getAssets().open(filename);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            String encoded = Base64.encodeToString(buffer, Base64.NO_WRAP);
            videoWebView.loadUrl("javascript:(function() {" +
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
        videoWebView.reload();
    }

    private void stopLoading() {
        videoWebView.stopLoading();
    }

    @OnClick(R.id.video_fab)
    void video_fab_click() {

        VideoActivity.this.finish();
    }

    @OnClick(R.id.video_fab_open)
    void video_fab_open_click(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.parse(mUrl);
        intent.setData(uri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            ToastUtils.showShort(VideoActivity.this, "打开网站失败");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            videoWebView.getClass().getMethod("onResume").invoke(videoWebView, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            videoWebView.getClass().getMethod("onPause").invoke(videoWebView, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (videoWebView != null) videoWebView.destroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_video, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.video_action_reload:
                refresh();
                return true;
            case R.id.video_action_share:
                showShare(mUrl, mDesc);
                return true;
            case R.id.video_action_stop:
                stopLoading();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showShare(String url, String title) {
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
        oks.setText(title + " : " + url + "\n通过" + this.getString(R.string.design) + "发布");
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
