package com.example.hanzh.gankio_han;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class HelloActivity extends Activity {

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // 隐去标题栏 （应用程序的名字）
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 全屏方法二 ：隐去状态栏部分 (电池等图标和一切修饰部分)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_hello);
        isFirst();
    }

    public void isFirst() {

        preferences = getSharedPreferences("count", MODE_PRIVATE);
        int count = preferences.getInt("count", 0);

//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }

        if (count == 0) {
            //延迟两秒后执行run方法中的页面跳转
            //全屏显示welcome画面
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    Intent intent = new Intent(HelloActivity.this, WhatsNew.class);
                    startActivity(intent);
                    HelloActivity.this.finish();
                }
            }, 1000);
        } else {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    Intent intent = new Intent(HelloActivity.this, MainActivity.class);
                    startActivity(intent);
                    HelloActivity.this.finish();
                }
            }, 1000);
        }
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("count", ++count);//存入数据
        editor.commit(); //提交修改
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }
}
