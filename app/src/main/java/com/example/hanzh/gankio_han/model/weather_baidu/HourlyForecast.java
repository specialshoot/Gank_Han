package com.example.hanzh.gankio_han.model.weather_baidu;

/**
 * Created by 轾 on 2015/11/5.
 * http://apistore.baidu.com/apiworks/servicedetail/478.html
 */
public class HourlyForecast {

    private String date;
    private int hum;
    private int pop;
    private int pres;
    private int tmp;
    private Wind wind;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getHum() {
        return hum;
    }

    public void setHum(int hum) {
        this.hum = hum;
    }

    public int getPop() {
        return pop;
    }

    public void setPop(int pop) {
        this.pop = pop;
    }

    public int getPres() {
        return pres;
    }

    public void setPres(int pres) {
        this.pres = pres;
    }

    public int getTmp() {
        return tmp;
    }

    public void setTmp(int tmp) {
        this.tmp = tmp;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public class Wind{
        public int deg;
        public String dir;
        public String sc;
        public int spd;
    }

    @Override
    public String toString() {
        return "HourlyForecast{" +
                "date='" + date + '\'' +
                ", hum=" + hum +
                ", pop=" + pop +
                ", pres=" + pres +
                ", tmp=" + tmp +
                ", wind=" + wind +
                '}';
    }
}
