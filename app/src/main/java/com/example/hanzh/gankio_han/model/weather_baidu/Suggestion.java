package com.example.hanzh.gankio_han.model.weather_baidu;

/**
 * Created by 轾 on 2015/11/5.
 * http://apistore.baidu.com/apiworks/servicedetail/478.html
 */
public class Suggestion {

    private SuggestionItem comf;
    private SuggestionItem cw;
    private SuggestionItem drsg;
    private SuggestionItem flu;
    private SuggestionItem sport;
    private SuggestionItem trav;
    private SuggestionItem uv;

    public SuggestionItem getComf() {
        return comf;
    }

    public void setComf(SuggestionItem comf) {
        this.comf = comf;
    }

    public SuggestionItem getCw() {
        return cw;
    }

    public void setCw(SuggestionItem cw) {
        this.cw = cw;
    }

    public SuggestionItem getDrsg() {
        return drsg;
    }

    public void setDrsg(SuggestionItem drsg) {
        this.drsg = drsg;
    }

    public SuggestionItem getFlu() {
        return flu;
    }

    public void setFlu(SuggestionItem flu) {
        this.flu = flu;
    }

    public SuggestionItem getSport() {
        return sport;
    }

    public void setSport(SuggestionItem sport) {
        this.sport = sport;
    }

    public SuggestionItem getTrav() {
        return trav;
    }

    public void setTrav(SuggestionItem trav) {
        this.trav = trav;
    }

    public SuggestionItem getUv() {
        return uv;
    }

    public void setUv(SuggestionItem uv) {
        this.uv = uv;
    }

    public class SuggestionItem{
        public String brf;
        public String txt;
    }

    @Override
    public String toString() {
        return "Suggestion{" +
                "comf=" + comf +
                ", cw=" + cw +
                ", drsg=" + drsg +
                ", flu=" + flu +
                ", sport=" + sport +
                ", trav=" + trav +
                ", uv=" + uv +
                '}';
    }
}
