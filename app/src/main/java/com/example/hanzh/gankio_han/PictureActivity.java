package com.example.hanzh.gankio_han;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hanzh.gankio_han.utils.SnackbarUtil;
import com.example.hanzh.gankio_han.utils.ToastUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class PictureActivity extends AppCompatActivity {

    private final static String TAG = "PictureActivity";
    private final static String ALBUM_PATH = Environment.getExternalStorageDirectory() + "/GankIO_Han/";
    @Bind(R.id.pic_container)
    View container;
    @Bind(R.id.showBigPicture)
    ImageView showBigPicture;
    @Bind(R.id.pictureToolbar)
    Toolbar picToolbar;
    @Bind(R.id.pic_toolbar_title)
    TextView pic_toolbar_title;

    private ProgressDialog mSaveDialog = null;  //保存图片Dialog
    private Bitmap mBitmap; //保存图片用的Bitmap
    private String mFileName;   //保存图片的filename
    private String mSaveMessage;    //保存图片成功或失败文字
    private String url;
    private String desc;
    private String picId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_activity);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        url = bundle.getString("Url");
        desc = bundle.getString("Desc");
        picId = bundle.getString("PicId");
        initViewAction();
    }

    private void initViewAction() {
        pic_toolbar_title.setText(desc);
        setSupportActionBar(picToolbar);
        Glide.with(PictureActivity.this).load(url).into(showBigPicture);
    }

    @OnClick(R.id.fabPicBack)
    void fabPicBackClick() {
        try {
            if (mSaveDialog != null) {
                mSaveDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        PictureActivity.this.finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            System.out.println("pic back");
            try {
                if (mSaveDialog != null) {
                    mSaveDialog.dismiss();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pic, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_save:
                mSaveDialog = ProgressDialog.show(PictureActivity.this, "保存图片", "图片正在保存中,请稍等...", true);
                new Thread(connectNet).start();
//                new Thread(saveFileRunnable).start();
                break;
            case R.id.action_share:
                showShare(url, desc);
                break;
            default:
                break;
        }
        return true;
    }

    /*
     * 连接网络
     * 由于在4.0中不允许在主线程中访问网络，所以需要在子线程中访问
     */
    private Runnable connectNet = new Runnable() {
        @Override
        public void run() {
            try {
                String filePath = url;
                mFileName = picId + ".jpg";

                //******** 直接从InputStream生成bitmap ***********/
                mBitmap = BitmapFactory.decodeStream(getImageStream(filePath));
                //********************************************************************/

                // 发送消息，通知handler在主线程中更新UI
                connectHanlder.sendEmptyMessage(0);
                Log.d(TAG, "set image ...");
            } catch (Exception e) {
                ToastUtils.showShort(PictureActivity.this, "无法链接网络！");
                e.printStackTrace();
            }
        }
    };

    private Handler connectHanlder = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG, "display image");
            // 更新UI，显示图片
            if (mBitmap != null) {
                new Thread(saveFileRunnable).start();
            }
        }
    };

    public InputStream getImageStream(String path) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5 * 1000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return conn.getInputStream();
        }
        return null;
    }

    private Runnable saveFileRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                //saveFile(mBitmap, mFileName);
                saveImageToGallery(mBitmap, mFileName);
                mSaveMessage = "图片保存成功！";
            } catch (Exception e) {
                mSaveMessage = "图片保存失败！";
                e.printStackTrace();
            }
            messageHandler.sendMessage(messageHandler.obtainMessage());
        }

    };

    private Handler messageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mSaveDialog.dismiss();
            Log.d(TAG, mSaveMessage);
            ToastUtils.showShort(PictureActivity.this, mSaveMessage);

            if (mSaveMessage.equals("图片保存成功！")) {
                SnackbarUtil.show(container, "文件目录 : " + ALBUM_PATH, 10000);
            }
        }
    };

    /**
     * 保存文件
     *
     * @param bm
     * @param fileName
     * @throws IOException
     */
    public void saveFile(Bitmap bm, String fileName) throws IOException {
        File dirFile = new File(ALBUM_PATH);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        File myCaptureFile = new File(ALBUM_PATH + fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        bos.flush();
        bos.close();
    }

    //保存并通知系统图库更新
    public void saveImageToGallery(Bitmap bmp, String fileName) {
        // 首先保存图片
        File appDir = new File(ALBUM_PATH);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        File file = new File(ALBUM_PATH, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(PictureActivity.this.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        String path = ALBUM_PATH + fileName;
        System.out.println("path : " + path);
        PictureActivity.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));
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
        oks.setImageUrl(url);
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
