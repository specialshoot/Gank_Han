package com.example.hanzh.gankio_han.model.heweather_x3;

/**
 * Created by 轾 on 2015/11/5.
 * http://apistore.baidu.com/apiworks/servicedetail/478.html
 */
public class DailyForecast {

    private String date;
    private Astro astro;
    private Cond cond;
    private int hum;
    private double pcpn;
    private int pop;
    private int pres;
    private Tmp tmp;
    private int vis;
    private Wind wind;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Astro getAstro() {
        return astro;
    }

    public void setAstro(Astro astro) {
        this.astro = astro;
    }

    public Cond getCond() {
        return cond;
    }

    public void setCond(Cond cond) {
        this.cond = cond;
    }

    public int getHum() {
        return hum;
    }

    public void setHum(int hum) {
        this.hum = hum;
    }

    public double getPcpn() {
        return pcpn;
    }

    public void setPcpn(double pcpn) {
        this.pcpn = pcpn;
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

    public Tmp getTmp() {
        return tmp;
    }

    public void setTmp(Tmp tmp) {
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

    public class Astro{
        public String sr;
        public String ss;
    }

    public class Cond{
        public int code_d;
        public int code_n;
        public String txt_d;
        public String txt_n;
    }

    public class Tmp{
        public String max;
        public String min;
    }

    public class Wind{
        public int deg;
        public String dir;
        public String sc;
        public int spd;
    }

    @Override
    public String toString() {
        return "DailyForecast{" +
                "date='" + date + '\'' +
                ", astro=" + astro +
                ", cond=" + cond +
                ", hum=" + hum +
                ", pcpn=" + pcpn +
                ", pop=" + pop +
                ", pres=" + pres +
                ", tmp=" + tmp +
                ", vis=" + vis +
                ", wind=" + wind +
                '}';
    }
}
