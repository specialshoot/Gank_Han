package com.example.hanzh.gankio_han;

import android.content.Context;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.hanzh.gankio_han.utils.SPUtils;
import com.example.hanzh.gankio_han.utils.ToastUtils;
import com.mob.tools.FakeActivity;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * 中文注释
 * ShareSDK 官网地址 ： http://www.mob.com </br>
 * 1、这是用2.38版本的sharesdk，一定注意  </br>
 * 2、如果要咨询客服，请加企业QQ 4006852216 </br>
 * 3、咨询客服时，请把问题描述清楚，最好附带错误信息截图 </br>
 * 4、一般问题，集成文档中都有，请先看看集成文档；减少客服压力，多谢合作  ^_^</br></br></br>
 * <p>
 * The password of demokey.keystore is 123456
 * *ShareSDK Official Website ： http://www.mob.com </br>
 * 1、Be carefully, this sample use the version of 2.11 sharesdk  </br>
 * 2、If you want to ask for help，please add our QQ whose number is 4006852216 </br>
 * 3、Please describe detail of the question , if you have the picture of the bugs or the bugs' log ,that is better </br>
 * 4、Usually, the answers of some normal questions is exist in our user guard pdf, please read it more carefully,thanks  ^_^
 */
public class ThirdPartyLogin extends FakeActivity implements OnClickListener,
        Callback,
        PlatformActionListener,
        SignupPage.SuccessInterface {

    private static final int MSG_SMSSDK_CALLBACK = 1;
    private static final int MSG_AUTH_CANCEL = 2;
    private static final int MSG_AUTH_ERROR = 3;
    private static final int MSG_AUTH_COMPLETE = 4;

    private OnLoginListener signupListener;
    private Handler handler;

    /**
     * 设置授权回调，用于判断是否进入注册
     */
    public void setOnLoginListener(OnLoginListener l) {
        this.signupListener = l;
    }

    @Override
    public boolean isRegisterSuccess(boolean isSuccess) {
        if (isSuccess == true) {
            ToastUtils.showShort(activity, "注册成功");
            this.finish();
            return true;
        } else {
            System.out.println("false");
            ToastUtils.showShort(activity, "注册失败");
        }
        return false;
    }

    public void onCreate() {
        // 初始化ui
        handler = new Handler(this);
        activity.setContentView(R.layout.tpl_login_page);
        (activity.findViewById(R.id.tvWeixin)).setOnClickListener(this);
        (activity.findViewById(R.id.tvWeibo)).setOnClickListener(this);
        (activity.findViewById(R.id.tvQq)).setOnClickListener(this);
        (activity.findViewById(R.id.exitLogin)).setOnClickListener(this);
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tvWeixin:
                //微信登录
                //测试时，需要打包签名；sample测试时，用项目里面的demokey.keystore
                //打包签名apk,然后才能产生微信的登录
                Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                authorize(wechat);
                break;
            case R.id.tvWeibo:
                //新浪微博
                Platform sina = ShareSDK.getPlatform(SinaWeibo.NAME);
                authorize(sina);
                break;
            case R.id.tvQq:
                //QQ空间
                Platform qzone = ShareSDK.getPlatform(QZone.NAME);
                authorize(qzone);
                break;
            case R.id.exitLogin:
                SPUtils.put(this.activity, "usericon", "");
                SPUtils.put(this.activity, "username", "");
//					SPUtils.put(this.activity,"usergender","");
//                SPUtils.put(this.activity,"usernote","");
                finish();
                break;
            default:
                break;
        }
    }

    //执行授权,获取用户信息
    //文档：http://wiki.mob.com/Android_%E8%8E%B7%E5%8F%96%E7%94%A8%E6%88%B7%E8%B5%84%E6%96%99
    private void authorize(Platform plat) {

        plat.setPlatformActionListener(this);
        //关闭SSO授权
        plat.SSOSetting(true);
        plat.showUser(null);
    }

    public void onComplete(Platform platform, int action, HashMap<String, Object> res) {
        if (action == Platform.ACTION_USER_INFOR) {
            Message msg = new Message();
            msg.what = MSG_AUTH_COMPLETE;
            msg.obj = new Object[]{platform.getName(), res};
            handler.sendMessage(msg);
        }
    }

    public void onError(Platform platform, int action, Throwable t) {
        if (action == Platform.ACTION_USER_INFOR) {
            handler.sendEmptyMessage(MSG_AUTH_ERROR);
        }
        t.printStackTrace();
    }

    public void onCancel(Platform platform, int action) {
        if (action == Platform.ACTION_USER_INFOR) {
            handler.sendEmptyMessage(MSG_AUTH_CANCEL);
        }
    }

    @SuppressWarnings("unchecked")
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_AUTH_CANCEL: {
                //取消授权
                ToastUtils.showShort(activity, "授权操作已取消");
            }
            break;
            case MSG_AUTH_ERROR: {
                //授权失败
                ToastUtils.showShort(activity, "授权失败");
            }
            break;
            case MSG_AUTH_COMPLETE:
                //授权成功
                ToastUtils.showShort(activity, "授权成功,正在跳转");
                Object[] objs = (Object[]) msg.obj;
                String platform = (String) objs[0];
                HashMap<String, Object> res = (HashMap<String, Object>) objs[1];
                if (signupListener != null && signupListener.onSignin(platform, res)) {
                    System.out.println("进入if");
                    SignupPage signupPage = new SignupPage();
                    signupPage.setOnLoginListener(signupListener);
                    signupPage.setSignListener(this);
                    signupPage.setPlatform(platform);
                    signupPage.show(activity, null);
                }
                break;
            default:
                break;
        }
        return false;
    }

    public void show(Context context) {
        initSDK(context);
        super.show(context, null);
    }

    private void initSDK(Context context) {
        //初始化sharesdk,具体集成步骤请看文档：
        //http://wiki.mob.com/Android_%E5%BF%AB%E9%80%9F%E9%9B%86%E6%88%90%E6%8C%87%E5%8D%97
        ShareSDK.initSDK(context);
    }

}
