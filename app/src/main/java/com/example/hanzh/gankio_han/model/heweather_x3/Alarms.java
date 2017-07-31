package com.example.hanzh.gankio_han.model.heweather_x3;

/**
 * Created by 轾 on 2017/1/6.
 * "alarms": [{  //灾害预警，若所在城市无预警则不显示该字段，仅限国内城市
 * "level": "黄色",  //预警等级
 * "stat": "预警中",  //预警状态
 * "title": "北京市怀柔区气象台发布暴雨黄色预警",  //预警信息标题
 * "txt": "目前怀柔北部山区局地降雨量已达35毫米，预计未来1至2小时降雨仍将持续，局地累计雨量将达到50到70毫米，山区请注意防范地质灾害。",  //预警信息详情
 * "type": "暴雨 "  //预警天气类型
 * }],
 */

public class Alarms {
    private String level;
    private String stat;
    private String title;
    private String txt;
    private String type;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Alarms{" +
                "level='" + level + '\'' +
                ", stat='" + stat + '\'' +
                ", title='" + title + '\'' +
                ", txt='" + txt + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
