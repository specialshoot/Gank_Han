package com.example.hanzh.gankio_han.model.weather_baidu;

/**
 * Created by 轾 on 2015/11/5.
 * http://apistore.baidu.com/apiworks/servicedetail/478.html
 */
public class NowWeather {

    private Cond cond;
    private int fl;
    private String hum;
    private double pcpn;
    private int pres;
    private int tmp;
    private int vis;
    private Wind wind;

    public Cond getCond() {
        return cond;
    }

    public void setCond(Cond cond) {
        this.cond = cond;
    }

    public int getFl() {
        return fl;
    }

    public void setFl(int fl) {
        this.fl = fl;
    }

    public String getHum() {
        return hum;
    }

    public void setHum(String hum) {
        this.hum = hum;
    }

    public double getPcpn() {
        return pcpn;
    }

    public void setPcpn(double pcpn) {
        this.pcpn = pcpn;
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

    public int getVis() {
        return vis;
    }

    public void setVis(int vis) {
        this.vis = vis;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public class Cond{
        public int code;
        public String txt;
    }

    public class Wind{
        public int deg;
        public String dir;
        public String sc;
        public int spd;
    }

    @Override
    public String toString() {
        return "NowWeather{" +
                "cond=" + cond +
                ", fl=" + fl +
                ", hum='" + hum + '\'' +
                ", pcpn=" + pcpn +
                ", pres=" + pres +
                ", tmp=" + tmp +
                ", vis=" + vis +
                ", wind=" + wind +
                '}';
    }
}
