package com.example.hanzh.gankio_han.model.weather_baidu;

/**
 * Created by 轾 on 2015/11/5.
 * http://apistore.baidu.com/apiworks/servicedetail/478.html
 */
public class Aqi {

    private City city;

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public class City{
        public int aqi;
        public int co;
        public int no2;
        public int o3;
        public int pm10;
        public int pm25;
        public String qlty;
        public int so2;
    }

    @Override
    public String toString() {
        return "Aqi{" +
                "city=" + city +
                '}';
    }
}
