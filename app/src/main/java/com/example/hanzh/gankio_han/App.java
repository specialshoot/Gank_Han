package com.example.hanzh.gankio_han;

import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.litesuits.orm.LiteOrm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by 轾 on 2015/10/10.
 */
public class App extends Application {

    private static final String PACKAGE_NAME = "com.example.hanzh.gankio_han";
    public static final String DB_PATH_WEATHER = "/data"
            + Environment.getDataDirectory().getAbsolutePath() + "/"
            + PACKAGE_NAME;  //在手机里存放数据库的位置
    private static final String DB_NAME = "gank.db";
    private static final String DB_WEATHER = "my_weather.db";
    private String dbfile = DB_PATH_WEATHER + "/databases/" + DB_WEATHER;
    private String databasesDir = DB_PATH_WEATHER + "/databases/";
    private final int BUFFER_SIZE = 400000;
    public static Context sContext;
    public static LiteOrm sDb, sDbWeather;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        if (sDb == null) {
            sDb = LiteOrm.newSingleInstance(this, DB_NAME);
        }
        sDb.setDebugged(true);

        //天气数据库
        System.out.println("dbfile -> " + dbfile);
        try {
            if (!(new File(dbfile).exists())) {//判断数据库文件是否存在，若不存在则执行导入，否则直接打开数据库
                System.out.println("不存在数据库,新建");
                InputStream is = this.getResources().openRawResource(
                        R.raw.my_weather); //欲导入的数据库
                File Dir = new File(databasesDir);
                if (!Dir.exists()) {
                    Dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(dbfile);
                byte[] buffer = new byte[BUFFER_SIZE];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
            }
            if (new File(dbfile).exists()) {
                if (sDbWeather == null) {
                    sDbWeather = LiteOrm.newSingleInstance(this, DB_WEATHER);
                }
                if (BuildConfig.DEBUG) {
                    sDbWeather.setDebugged(true);
                }
                System.out.println("数据库创建成功");
            } else {
                System.out.println("数据库创建失败,写入错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("数据库创建失败");
        }
        //极光推送初始化
        JPushInterface.setAlias(this, "love", new TagAliasCallback() {  //设置别名的方法
            @Override
            public void gotResult(int i, String s, Set<String> set) {
                Log.d("TAG", "set alias result is " + i);  //返回结果为0说明设置成功
            }
        });
        Set<String> sets = new HashSet<String>();
        sets.add("love");
        sets.add("yuanyuan");
        JPushInterface.setTags(this, sets, new TagAliasCallback() { //通过设置tag发送消息
            @Override
            public void gotResult(int i, String s, Set<String> set) {
                Log.d("TAG", "set tag result is " + i);  //返回结果为0说明设置成功
            }
        });
        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(getApplicationContext());
        builder.notificationDefaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS;  // 设置为铃声与震动都要
        JPushInterface.setPushNotificationBuilder(1, builder);
        //JPushInterface.setDebugMode(false);
        JPushInterface.init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
