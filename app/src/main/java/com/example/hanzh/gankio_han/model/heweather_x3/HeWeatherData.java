package com.example.hanzh.gankio_han.model.heweather_x3;

import java.util.List;

/**
 * Created by 轾 on 2015/11/5.
 * http://apistore.baidu.com/apiworks/servicedetail/478.html
 */
public class HeWeatherData {

    private List<Alarms> alarms;
    private Aqi aqi;
    private Basic basic;
    private List<DailyForecast> daily_forecast;
    private List<HourlyForecast> hourly_forecast;
    private NowWeather now;
    private String status;
    private Suggestion suggestion;

    public List<Alarms> getAlarms() {
        return alarms;
    }

    public void setAlarms(List<Alarms> alarms) {
        this.alarms = alarms;
    }

    public Aqi getAqi() {
        return aqi;
    }

    public void setAqi(Aqi aqi) {
        this.aqi = aqi;
    }

    public Basic getBasic() {
        return basic;
    }

    public void setBasic(Basic basic) {
        this.basic = basic;
    }

    public List<DailyForecast> getDaily_forecast() {
        return daily_forecast;
    }

    public void setDaily_forecast(List<DailyForecast> daily_forecast) {
        this.daily_forecast = daily_forecast;
    }

    public List<HourlyForecast> getHourly_forecast() {
        return hourly_forecast;
    }

    public void setHourly_forecast(List<HourlyForecast> hourly_forecast) {
        this.hourly_forecast = hourly_forecast;
    }

    public NowWeather getNow() {
        return now;
    }

    public void setNow(NowWeather now) {
        this.now = now;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Suggestion getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(Suggestion suggestion) {
        this.suggestion = suggestion;
    }

    @Override
    public String toString() {
        return "HeWeatherData{" +
                "alarms=" + alarms +
                ", aqi=" + aqi +
                ", basic=" + basic +
                ", daily_forecast=" + daily_forecast +
                ", hourly_forecast=" + hourly_forecast +
                ", now=" + now +
                ", status='" + status + '\'' +
                ", suggestion=" + suggestion +
                '}';
    }
}
