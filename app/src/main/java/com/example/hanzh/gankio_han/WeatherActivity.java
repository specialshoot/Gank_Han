package com.example.hanzh.gankio_han;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.example.hanzh.gankio_han.model.heweather_x3.HeWeather;
import com.example.hanzh.gankio_han.model.heweather_x3.HeWeatherData;
import com.example.hanzh.gankio_han.utils.NetUtils;
import com.example.hanzh.gankio_han.utils.SPCurrentCityUtils;
import com.example.hanzh.gankio_han.utils.SPUtils;
import com.example.hanzh.gankio_han.utils.ToastUtils;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WeatherActivity extends AppCompatActivity {

    @BindView(R.id.weather_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.weather_city)
    TextView cityTextView;
    @BindView(R.id.NowWeatherIcon)
    ImageView nowWeatherIcon;
    @BindView(R.id.NowWeather)
    TextView nowWeather;
    @BindView(R.id.NowWeatherCondition)
    TextView nowWeatherCondition;
    @BindView(R.id.lastUpdate)
    TextView lastUpdate;
    @BindView(R.id.weather_refresh)
    ImageView weather_refresh;

    //今日
    @BindView(R.id.today_weather_date)
    TextView today_weather_date;
    @BindView(R.id.today_weather_icon1)
    ImageView today_weather_icon1;
    @BindView(R.id.today_weather_icon2)
    ImageView today_weather_icon2;
    @BindView(R.id.today_weather_max)
    TextView today_weather_max;
    @BindView(R.id.today_weather_min)
    TextView today_weather_min;
    //明日
    @BindView(R.id.first_weather_date)
    TextView first_weather_date;
    @BindView(R.id.first_weather_icon1)
    ImageView first_weather_icon1;
    @BindView(R.id.first_weather_icon2)
    ImageView first_weather_icon2;
    @BindView(R.id.first_weather_max)
    TextView first_weather_max;
    @BindView(R.id.first_weather_min)
    TextView first_weather_min;
    //后天
    @BindView(R.id.second_weather_date)
    TextView second_weather_date;
    @BindView(R.id.second_weather_icon1)
    ImageView second_weather_icon1;
    @BindView(R.id.second_weather_icon2)
    ImageView second_weather_icon2;
    @BindView(R.id.second_weather_max)
    TextView second_weather_max;
    @BindView(R.id.second_weather_min)
    TextView second_weather_min;
    //大后天
    @BindView(R.id.third_weather_date)
    TextView third_weather_date;
    @BindView(R.id.third_weather_icon1)
    ImageView third_weather_icon1;
    @BindView(R.id.third_weather_icon2)
    ImageView third_weather_icon2;
    @BindView(R.id.third_weather_max)
    TextView third_weather_max;
    @BindView(R.id.third_weather_min)
    TextView third_weather_min;

    //大大后天
    @BindView(R.id.forth_weather_date)
    TextView forth_weather_date;
    @BindView(R.id.forth_weather_icon1)
    ImageView forth_weather_icon1;
    @BindView(R.id.forth_weather_icon2)
    ImageView forth_weather_icon2;
    @BindView(R.id.forth_weather_max)
    TextView forth_weather_max;
    @BindView(R.id.forth_weather_min)
    TextView forth_weather_min;

    //大大大后天
    @BindView(R.id.fifth_weather_date)
    TextView fifth_weather_date;
    @BindView(R.id.fifth_weather_icon1)
    ImageView fifth_weather_icon1;
    @BindView(R.id.fifth_weather_icon2)
    ImageView fifth_weather_icon2;
    @BindView(R.id.fifth_weather_max)
    TextView fifth_weather_max;
    @BindView(R.id.fifth_weather_min)
    TextView fifth_weather_min;

    @BindView(R.id.air_quality)
    TextView air_quality;
    @BindView(R.id.AQI_text)
    TextView AQI_text;
    @BindView(R.id.PM25_text)
    TextView PM25_text;
    @BindView(R.id.PM10_text)
    TextView PM10_text;
    @BindView(R.id.NO2_text)
    TextView NO2_text;

    Animation operatingAnim;
    //关于天气
    private static final String HeHttpUrl = "https://free-api.heweather.com/x3/";
    private String httpArg = "";
    private String jsonResult = "";
    private Handler weatherHandler = new Handler();  //天气handler
    Map<Integer, Integer> weatherIcon = new HashMap<Integer, Integer>();
    //关于定位
    private LocationClient mLocationClient = null;
    private BDLocationListener BaiduLocationListener = new MyLocationListener();
    public SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        ButterKnife.bind(this);

        //设置动画
        operatingAnim = AnimationUtils.loadAnimation(this, R.anim.refresh_rotate);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);

        setSupportActionBar(mToolbar);
        initViewAction();
    }

    private void initViewAction() {
        prefs = getSharedPreferences("current_city", MODE_PRIVATE);
        prefs.registerOnSharedPreferenceChangeListener(prefsListener);
        initMap();
        if (!SPUtils.get(WeatherActivity.this, "weatherjson", "").equals("")) {
            jsonResult = (String) SPUtils.get(WeatherActivity.this, "weatherjson", "");
            HeWeather weatherInfo = new Gson().fromJson(jsonResult, HeWeather.class);
            HeWeatherData data = weatherInfo.getHeWeatherDatas().get(0);
            handleData(data, false);
        }
        //百度地图定位
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(BaiduLocationListener);    //注册监听函数
        if (NetUtils.isConnected(WeatherActivity.this)) {
            String city = (String) SPCurrentCityUtils.get(this, ChooseCity.CURRENT_CITY_KEY, "");
            if (city.equals("")) {
                initLocation();
                ToastUtils.showShort(WeatherActivity.this, "正在定位中...");
                mLocationClient.start();//定位SDK start之后会默认发起一次定位请求，开发者无须判断isstart并主动调用request
            } else {
                inquireWeather(city);
            }
        } else {
            ToastUtils.showShort(WeatherActivity.this, "网络断开,请检查网络");
        }

    }

    private void initMap() {
        weatherIcon.put(100, R.drawable.weather100_white);
        weatherIcon.put(101, R.drawable.weather101_white);
        weatherIcon.put(102, R.drawable.weather102_white);
        weatherIcon.put(103, R.drawable.weather103_white);
        weatherIcon.put(104, R.drawable.weather104_white);
        weatherIcon.put(200, R.drawable.weather200_white);
        weatherIcon.put(201, R.drawable.weather201_white);
        weatherIcon.put(202, R.drawable.weather202_white);
        weatherIcon.put(203, R.drawable.weather203_white);
        weatherIcon.put(204, R.drawable.weather204_white);
        weatherIcon.put(205, R.drawable.weather205_white);
        weatherIcon.put(206, R.drawable.weather206_white);
        weatherIcon.put(207, R.drawable.weather207_white);
        weatherIcon.put(208, R.drawable.weather208_white);
        weatherIcon.put(209, R.drawable.weather209_white);
        weatherIcon.put(210, R.drawable.weather210_white);
        weatherIcon.put(211, R.drawable.weather211_white);
        weatherIcon.put(212, R.drawable.weather212_white);
        weatherIcon.put(213, R.drawable.weather213_white);
        weatherIcon.put(300, R.drawable.weather300_white);
        weatherIcon.put(301, R.drawable.weather301_white);
        weatherIcon.put(302, R.drawable.weather302_white);
        weatherIcon.put(303, R.drawable.weather303_white);
        weatherIcon.put(304, R.drawable.weather304_white);
        weatherIcon.put(305, R.drawable.weather305_white);
        weatherIcon.put(306, R.drawable.weather306_white);
        weatherIcon.put(307, R.drawable.weather307_white);
        weatherIcon.put(308, R.drawable.weather308_white);
        weatherIcon.put(309, R.drawable.weather309_white);
        weatherIcon.put(310, R.drawable.weather310_white);
        weatherIcon.put(311, R.drawable.weather311_white);
        weatherIcon.put(312, R.drawable.weather312_white);
        weatherIcon.put(313, R.drawable.weather313_white);
        weatherIcon.put(400, R.drawable.weather400_white);
        weatherIcon.put(401, R.drawable.weather401_white);
        weatherIcon.put(402, R.drawable.weather402_white);
        weatherIcon.put(403, R.drawable.weather403_white);
        weatherIcon.put(404, R.drawable.weather404_white);
        weatherIcon.put(405, R.drawable.weather405_white);
        weatherIcon.put(406, R.drawable.weather406_white);
        weatherIcon.put(407, R.drawable.weather407_white);
        weatherIcon.put(500, R.drawable.weather500_white);
        weatherIcon.put(501, R.drawable.weather501_white);
        weatherIcon.put(502, R.drawable.weather502_white);
        weatherIcon.put(503, R.drawable.weather503_white);
        weatherIcon.put(504, R.drawable.weather504_white);
        weatherIcon.put(507, R.drawable.weather507_white);
        weatherIcon.put(508, R.drawable.weather508_white);
        weatherIcon.put(900, R.drawable.weather900_white);
        weatherIcon.put(901, R.drawable.weather901_white);
        weatherIcon.put(999, R.drawable.weather999_white);
    }

    //初始化location
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("gcj02");//可选，默认gcj02，设置返回的定位结果坐标系，
        int span = 2000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setEnableSimulateGps(true);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        mLocationClient.setLocOption(option);
    }

    @OnClick(R.id.choose_city)
    void choose_city() {
        Intent intent = new Intent(WeatherActivity.this, ChooseCity.class);
        startActivity(intent);
    }

    //百度定位BDLocationListener
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());   //获取errorcode
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\ncity : ");
            sb.append(location.getCity());
            sb.append("\ncitycode : ");
            sb.append(location.getCityCode());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());// 单位度
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                sb.append("\naddr : ");
                String tempLocal = location.getAddrStr();
                sb.append(tempLocal);
                //localTextView.setText(tempLocal);
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());// 位置语义化信息
            List<Poi> list = location.getPoiList();// POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
            Log.d("BaiduLocationApiDem", sb.toString());
            //error code处理
            switch (location.getLocType()) {
                case 61:
                    ToastUtils.showShort(WeatherActivity.this, "GPS定位成功");
                    //locationButton.setText("开启定位");
                    if (location.getCity().equals("")) {
                        ToastUtils.showShort(WeatherActivity.this, "没有捕获到地理位置");
                        try {
                            weather_refresh.clearAnimation();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        inquireWeather(location.getCity());
                    }
                    break;
                case 62:
                    ToastUtils.showShort(WeatherActivity.this, "定位失败,请检查运营商网络或者wifi网络是否正常开启,尝试重新请求定位");
                    try {
                        weather_refresh.clearAnimation();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 63:
                    ToastUtils.showShort(WeatherActivity.this, "网络异常,没有成功向服务器发起请求,请确认当前测试手机网络是否通畅，尝试重新请求定位");
                    try {
                        weather_refresh.clearAnimation();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 161:
                    ToastUtils.showShort(WeatherActivity.this, "网络定位成功");
                    if (location.getCity().equals("")) {
                        ToastUtils.showShort(WeatherActivity.this, "没有捕获到地理位置");
                        try {
                            weather_refresh.clearAnimation();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        inquireWeather(location.getCity());
                    }
                    break;
                case 167:
                    Toast.makeText(WeatherActivity.this, "服务端定位失败,请您检查是否禁用获取位置信息权限,尝试重新请求定位", Toast.LENGTH_SHORT).show();
                    try {
                        weather_refresh.clearAnimation();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
            mLocationClient.stop();
            //在TextView上显示返回数据
            System.out.println("Location -> " + sb.toString());
        }
    }

    //查询天气
    private void inquireWeather(String city) {
        cityTextView.setText(city);
        if (city.charAt(city.length() - 1) == '市') {
            city = city.substring(0, city.length() - 1);
        }
        String encoderCity = "";
        try {
            encoderCity = URLEncoder.encode(city, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        httpArg = encoderCity;
        new Thread() {
            @Override
            public void run() {
                try {
                    jsonResult = request(httpArg);
                    weatherHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
//                                System.out.println(jsonResult);
                                try {
                                    HeWeather weatherInfo = new Gson().fromJson(jsonResult, HeWeather.class);
//                                    System.out.println("weatherInfo -> " + weatherInfo.toString());
                                    HeWeatherData data = weatherInfo.getHeWeatherDatas().get(0);
                                    if (data != null) {
                                        handleData(data, true);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    ToastUtils.showShort(WeatherActivity.this, "查询天气失败");
                                    if ((SPUtils.get(WeatherActivity.this, "weatherjson", "") != null && !(SPUtils.get(WeatherActivity.this, "weatherjson", "").equals("")))) {
                                        jsonResult = (String) SPUtils.get(WeatherActivity.this, "weatherjson", "");
                                        HeWeather weatherInfo = new Gson().fromJson(jsonResult, HeWeather.class);
                                        HeWeatherData data = weatherInfo.getHeWeatherDatas().get(0);
                                        handleData(data, false);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                ToastUtils.showShort(WeatherActivity.this, "查询天气失败");
                                if ((SPUtils.get(WeatherActivity.this, "weatherjson", "") != null && !SPUtils.get(WeatherActivity.this, "weatherjson", "").equals(""))) {
                                    jsonResult = (String) SPUtils.get(WeatherActivity.this, "weatherjson", "");
                                    HeWeather weatherInfo = new Gson().fromJson(jsonResult, HeWeather.class);
                                    HeWeatherData data = weatherInfo.getHeWeatherDatas().get(0);
                                    handleData(data, false);
                                }
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void handleData(HeWeatherData data, boolean update) {
        nowWeatherIcon.setImageResource(weatherIcon.get(data.getNow().getCond().code));
        nowWeather.setText(data.getNow().getFl() + "°");
        nowWeatherCondition.setText(data.getNow().getCond().txt);
        if (update) {
            ToastUtils.showShort(WeatherActivity.this, "天气更新成功");
            lastUpdate.setText(data.getBasic().getUpdate().loc);
            SPUtils.put(WeatherActivity.this, "weatherjson", jsonResult);
            SPUtils.put(WeatherActivity.this, "lastupdate", data.getBasic().getUpdate().loc);
        } else {
            if ((SPUtils.get(WeatherActivity.this, "lastupdate", "")) != null && !(SPUtils.get(WeatherActivity.this, "lastupdate", "").equals(""))) {
                lastUpdate.setText((String) SPUtils.get(WeatherActivity.this, "lastupdate", ""));
            } else {
                lastUpdate.setText("未知时间");
            }
        }
        //today
        today_weather_date.setText(data.getDaily_forecast().get(0).getDate());
        today_weather_icon1.setImageResource(weatherIcon.get(data.getDaily_forecast().get(0).getCond().code_d));
        today_weather_icon2.setImageResource(weatherIcon.get(data.getDaily_forecast().get(0).getCond().code_n));
        today_weather_max.setText(data.getDaily_forecast().get(0).getTmp().max);
        today_weather_min.setText(data.getDaily_forecast().get(0).getTmp().min);
        //first
        first_weather_date.setText(data.getDaily_forecast().get(1).getDate().substring(data.getDaily_forecast().get(1).getDate().indexOf("-") + 1, data.getDaily_forecast().get(1).getDate().length()));
        first_weather_icon1.setImageResource(weatherIcon.get(data.getDaily_forecast().get(1).getCond().code_d));
        first_weather_icon2.setImageResource(weatherIcon.get(data.getDaily_forecast().get(1).getCond().code_n));
        first_weather_max.setText(data.getDaily_forecast().get(1).getTmp().max);
        first_weather_min.setText(data.getDaily_forecast().get(1).getTmp().min);
        //second
        second_weather_date.setText(data.getDaily_forecast().get(2).getDate().substring(data.getDaily_forecast().get(2).getDate().indexOf("-") + 1, data.getDaily_forecast().get(2).getDate().length()));
        second_weather_icon1.setImageResource(weatherIcon.get(data.getDaily_forecast().get(2).getCond().code_d));
        second_weather_icon2.setImageResource(weatherIcon.get(data.getDaily_forecast().get(2).getCond().code_n));
        second_weather_max.setText(data.getDaily_forecast().get(2).getTmp().max);
        second_weather_min.setText(data.getDaily_forecast().get(2).getTmp().min);
        //third
        third_weather_date.setText(data.getDaily_forecast().get(3).getDate().substring(data.getDaily_forecast().get(3).getDate().indexOf("-") + 1, data.getDaily_forecast().get(3).getDate().length()));
        third_weather_icon1.setImageResource(weatherIcon.get(data.getDaily_forecast().get(3).getCond().code_d));
        third_weather_icon2.setImageResource(weatherIcon.get(data.getDaily_forecast().get(3).getCond().code_n));
        third_weather_max.setText(data.getDaily_forecast().get(3).getTmp().max);
        third_weather_min.setText(data.getDaily_forecast().get(3).getTmp().min);
        //forth
        forth_weather_date.setText(data.getDaily_forecast().get(4).getDate().substring(data.getDaily_forecast().get(4).getDate().indexOf("-") + 1, data.getDaily_forecast().get(4).getDate().length()));
        forth_weather_icon1.setImageResource(weatherIcon.get(data.getDaily_forecast().get(4).getCond().code_d));
        forth_weather_icon2.setImageResource(weatherIcon.get(data.getDaily_forecast().get(4).getCond().code_n));
        forth_weather_max.setText(data.getDaily_forecast().get(4).getTmp().max);
        forth_weather_min.setText(data.getDaily_forecast().get(4).getTmp().min);
        //fifth
        fifth_weather_date.setText(data.getDaily_forecast().get(5).getDate().substring(data.getDaily_forecast().get(5).getDate().indexOf("-") + 1, data.getDaily_forecast().get(5).getDate().length()));
        fifth_weather_icon1.setImageResource(weatherIcon.get(data.getDaily_forecast().get(5).getCond().code_d));
        fifth_weather_icon2.setImageResource(weatherIcon.get(data.getDaily_forecast().get(5).getCond().code_n));
        fifth_weather_max.setText(data.getDaily_forecast().get(5).getTmp().max);
        fifth_weather_min.setText(data.getDaily_forecast().get(5).getTmp().min);

        if (data.getAqi() != null) {
            AQI_text.setText(data.getAqi().getCity().aqi + "");
            PM25_text.setText(data.getAqi().getCity().pm25 + "");
            PM10_text.setText(data.getAqi().getCity().pm10 + "");
            NO2_text.setText(data.getAqi().getCity().no2 + "");
            if (data.getAqi().getCity().aqi <= 50) {
                air_quality.setText("优");
            } else if (data.getAqi().getCity().aqi <= 100) {
                air_quality.setText("良");
            } else if (data.getAqi().getCity().aqi <= 150) {
                air_quality.setText("轻度污染");
            } else if (data.getAqi().getCity().aqi <= 200) {
                air_quality.setText("中度污染");
            } else if (data.getAqi().getCity().aqi <= 300) {
                air_quality.setText("重度污染");
            } else {
                air_quality.setText("严重污染");
            }
        } else {
            AQI_text.setText("未知");
            PM25_text.setText("未知");
            PM10_text.setText("未知");
            NO2_text.setText("未知");
            air_quality.setText("未知");
        }
        try {
            weather_refresh.clearAnimation();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String request(String cityname) {
        String httpUrl = HeHttpUrl + "weather?city=" + cityname + "&key=a0559fb7368042b082aa9779d90b9530";
        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();
        try {
            URL url = new URL(httpUrl);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            result = sbf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @OnClick(R.id.weather_back)
    void weather_back() {
        finish();
    }

    @OnClick(R.id.weather_refresh)
    void weather_refresh() {
        if (operatingAnim != null) {
            weather_refresh.startAnimation(operatingAnim);
        }

        if (NetUtils.isConnected(WeatherActivity.this)) {
            String city = (String) SPCurrentCityUtils.get(this, ChooseCity.CURRENT_CITY_KEY, "");
            if (city.equals("")) {
                //百度地图定位
                if (mLocationClient.isStarted()) {
                    mLocationClient.stop();
                }
                mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
                mLocationClient.registerLocationListener(BaiduLocationListener);    //注册监听函数
                initLocation();
                ToastUtils.showShort(WeatherActivity.this, "正在定位中...");
                mLocationClient.start();//定位SDK start之后会默认发起一次定位请求，开发者无须判断isstart并主动调用request
            } else {
                inquireWeather(city);
            }
        } else {
            ToastUtils.showShort(WeatherActivity.this, "网络断开,请检查网络");
            try {
                weather_refresh.clearAnimation();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据view来生成bitmap图片，可用于截图功能
     */
    public Bitmap getViewBitmap() {
        ToastUtils.showShort(WeatherActivity.this, "正在保存截图");
        //获取状态栏高度
        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        WindowManager wm = this.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();

        View v = getWindow().getDecorView();
        v.clearFocus(); //
        v.setPressed(false); //
        // 能画缓存就返回false
        boolean willNotCache = v.willNotCacheDrawing();
        v.setWillNotCacheDrawing(false);
        int color = v.getDrawingCacheBackgroundColor();
        v.setDrawingCacheBackgroundColor(0);
        if (color != 0) {
            v.destroyDrawingCache();
        }
        v.buildDrawingCache();
        Bitmap cacheBitmap = v.getDrawingCache();
        if (cacheBitmap == null) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap, 0, statusBarHeight, width, height - statusBarHeight);
        // Restore the view
        v.destroyDrawingCache();
        v.setWillNotCacheDrawing(willNotCache);
        v.setDrawingCacheBackgroundColor(color);
        return bitmap;
    }

    /**
     * 保存Bitmap图片为本地文件
     */

    public boolean saveFile(Bitmap bitmap, String filename) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(filename);
            if (fileOutputStream != null) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                ToastUtils.showShort(WeatherActivity.this, "保存截图成功");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showShort(WeatherActivity.this, "保存截图失败");
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return true;
    }

    private SharedPreferences.OnSharedPreferenceChangeListener prefsListener = new SharedPreferences.OnSharedPreferenceChangeListener() {

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            System.out.println("进入onSharedPreferenceChanged");
            if (key.equals(ChooseCity.CURRENT_CITY_KEY)) {
                System.out.println("进入onSharedPreferenceChanged,Key=CURRENT_CITY_KEY");
                String city = (String) SPCurrentCityUtils.get(WeatherActivity.this, key, "");
                System.out.println("current city -> " + city);
                inquireWeather(city);
            }
        }
    };

    @Override
    protected void onResume() {
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).registerOnSharedPreferenceChangeListener(prefsListener);
        super.onResume();
    }

    @Override
    protected void onPause() {
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).unregisterOnSharedPreferenceChangeListener(prefsListener);
        super.onPause();
    }
}
