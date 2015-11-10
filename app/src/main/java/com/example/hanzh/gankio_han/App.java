package com.example.hanzh.gankio_han;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.litesuits.orm.LiteOrm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by 轾 on 2015/10/10.
 */
public class App extends Application {

    private String dirPath="/data/data/com.example.hanzh.gankio_han/databases";
    private static final String DB_NAME = "gank.db";
//    private static final String DB_WEATHER="db_weather.db";
    public static Context sContext;
    public static LiteOrm sDb;
//    public static LiteOrm sDb_Weather;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
//        File dir = new File(dirPath);
//        if(!dir.exists()) {
//            dir.mkdir();
//        }
//        //数据库文件
//        File file = new File(dir, "db_weather.db");
//        System.out.println("File -> "+file.toString());
//        try {
//            if(!file.exists()) {
//                file.createNewFile();
//            }
//            //加载需要导入的数据库
//            InputStream is = this.getApplicationContext().getResources().openRawResource(R.raw.db_weather);
//            FileOutputStream fos = new FileOutputStream(file);
//            byte[] buffere=new byte[is.available()];
//            is.read(buffere);
//            fos.write(buffere);
//            is.close();
//            fos.close();
//        }catch(FileNotFoundException e){
//            e.printStackTrace();
//        }catch(IOException e) {
//            e.printStackTrace();
//        }
        sDb = LiteOrm.newSingleInstance(this, DB_NAME);
//        sDb_Weather=LiteOrm.newCascadeInstance(this,file.toString());
        if (BuildConfig.DEBUG) {
            sDb.setDebugged(true);
//            sDb_Weather.setDebugged(true);
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
