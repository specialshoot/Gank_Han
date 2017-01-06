package com.example.hanzh.gankio_han;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.DatePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hanzh.gankio_han.adapter.GankListAdapter;
import com.example.hanzh.gankio_han.model.DailyData;
import com.example.hanzh.gankio_han.model.Gank;
import com.example.hanzh.gankio_han.utils.ToastUtils;
import com.example.hanzh.gankio_han.widget.VideoImageView;
import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class TodayActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    @BindView(R.id.today_rv_gank)
    RecyclerView mRecyclerView;
    //private ViewStub mEmptyViewStub, mVideoViewStub;
    @BindView(R.id.today_iv_video)
    VideoImageView mVideoImageView;
    @BindView(R.id.today_toolbar)
    Toolbar todayToolbar;

    private GankListAdapter mAdapter;
    private int year, month, day;
    //每日数据格式http://gank.avosapps.com/api/day/年/月/日
    private static final String mUrlHead = "http://gank.io/api/day/";
    private String mUrl;    //获取数据的Url
    private String mVideoPreviewUrl;    //Video的Url
    private String mPictureUrl;
    private List<Gank> mGankList;
    private int isSecond = 0; //DateTimerPicker两次
    private boolean kong = false; //判断当前页是否为空

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today);
        ButterKnife.bind(this);
        year = Calendar.getInstance().get(Calendar.YEAR);
        month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        mUrl = mUrlHead + year + "/" + month + "/" + day;
        System.out.println("mUrl : " + mUrl);
        mGankList = new ArrayList<>();
        mAdapter = new GankListAdapter(TodayActivity.this);
        initViewAction();
        getData(mUrl);
    }

    private void initViewAction() {
        todayToolbar.setTitle(year + "/" + month + "/" + day);
        setSupportActionBar(todayToolbar);
        initRecyclerView();
    }


    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(TodayActivity.this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @OnClick(R.id.today_fab)
    void today_fab_click() {
        TodayActivity.this.finish();
    }

    @OnClick(R.id.header_appbar)
    void header_appbar_click() {
        if (kong == false) {
            Intent intent = new Intent();
            intent.setClass(TodayActivity.this, VideoActivity.class);
            Bundle mBundle = new Bundle();
            mBundle.putString("Url", mGankList.get(0).getUrl());//压入数据
            mBundle.putString("Desc", mGankList.get(0).getDesc());
            intent.putExtras(mBundle);
            startActivity(intent);
        } else {
            ToastUtils.showShort(TodayActivity.this, "没有数据,请选择别日或刷新");
        }
    }

    private void getData(String url) {
        System.out.println("进入getData");
        mGankList.clear();
        mGankList = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        //enqueue开启一步线程访问网络
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                System.out.println("client onFailure");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    String body = response.body().string();
                    System.out.println("body:" + body);
                    DailyData tempdata = new DailyData();
                    tempdata = new Gson().fromJson(body, DailyData.class);
                    System.out.println("tempdata : " + tempdata.toString());
                    if (tempdata.isError() == true) {
                        kong = true;
                        System.out.println("isError() == true");
                    } else {
                        if (tempdata != null) {
                            if (tempdata.category.isEmpty()) {
                                kong = true;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtils.showShort(TodayActivity.this, "没有数据");
                                        mVideoImageView.setImageResource(R.mipmap.youknow404);
                                    }
                                });
                            } else {
                                kong = false;
                                addAllResults(tempdata.getResults());
                                if (tempdata.getResults().get妹纸List().size() > 0) {
                                    mPictureUrl = tempdata.getResults().get妹纸List().get(0).getUrl();
                                    startPreview(mPictureUrl);
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mAdapter.mGankList.clear();
                                        mAdapter.mGankList = mGankList;
                                        mRecyclerView.setAdapter(mAdapter);
                                        mAdapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        } else {
                            ToastUtils.showShort(TodayActivity.this, "获取数据失败,请刷新");
                        }
                    }
                }
            }
        });
    }

    private void showEmptyView() {
        System.out.println("进入showEmptyView");
        //mEmptyViewStub.inflate();
    }

    public void addAllResults(DailyData.Result results) {
        if (results.getAndroidList() != null) mGankList.addAll(results.getAndroidList());
        if (results.getiOSList() != null) mGankList.addAll(results.getiOSList());
        if (results.get拓展资源List() != null) mGankList.addAll(results.get拓展资源List());
        if (results.get瞎推荐List() != null) mGankList.addAll(results.get瞎推荐List());
        if (results.get妹纸List() != null) mGankList.addAll(results.get妹纸List());
        if (results.getAppList() != null) mGankList.addAll(results.getAppList());
        if (results.get休息视频List() != null) mGankList.addAll(0, results.get休息视频List());
    }

    //获取视频缩略图,暂时没有资源,先用妹子图
    private void startPreview(String preview) {
        mVideoPreviewUrl = preview;
        if (preview != null && mVideoImageView != null) {
            mVideoImageView.post(new Runnable() {
                @Override
                public void run() {
                    Glide.with(mVideoImageView.getContext())
                            .load(mVideoPreviewUrl)
                            .into(mVideoImageView);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_today, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.today_action_reload:
                getData(mUrl);
                break;
            case R.id.today_action_choose:
                DatePickerDialog dlg = new DatePickerDialog(this, this, year, month - 1, day);
                dlg.show();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onDateSet(DatePicker view, int yearOfToday, int monthOfYear, int dayOfMonth) {

        if (year == yearOfToday && month == monthOfYear + 1 && day == dayOfMonth) {
            System.out.println("时间选择没有改变");
        } else {
            ToastUtils.showShort(TodayActivity.this, "您选择日期:" + yearOfToday + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
            year = yearOfToday;
            month = monthOfYear + 1;
            day = dayOfMonth;
            todayToolbar.setTitle(year + "/" + month + "/" + day);
            mUrl = mUrlHead + year + "/" + month + "/" + day;
            getData(mUrl);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
