package com.example.hanzh.gankio_han.model.weather_baidu;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by 轾 on 2015/11/5.
 */
public class HeWeather {

    @SerializedName("HeWeather data service 3.0")
    private List<HeWeatherData> heWeatherDatas;

    public List<HeWeatherData> getHeWeatherDatas() {
        return heWeatherDatas;
    }

    public void setHeWeatherDatas(List<HeWeatherData> heWeatherDatas) {
        this.heWeatherDatas = heWeatherDatas;
    }

    @Override
    public String toString() {
        return "HeWeather{" +
                "heWeatherDatas=" + heWeatherDatas +
                '}';
    }
}
