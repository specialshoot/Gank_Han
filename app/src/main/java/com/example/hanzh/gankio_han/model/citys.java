package com.example.hanzh.gankio_han.model;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

/**
 * Created by 轾 on 2015/11/8.
 */
@Table("citys")public class citys {

    @PrimaryKey(AssignType.AUTO_INCREMENT) @Column("_id") protected long id;
    @Column("province_id") private int province_id;
    @Column("name") private String name;
    @Column("city_num") private String city_num;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getProvince_id() {
        return province_id;
    }

    public void setProvince_id(int province_id) {
        this.province_id = province_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity_num() {
        return city_num;
    }

    public void setCity_num(String city_num) {
        this.city_num = city_num;
    }

    @Override
    public String toString() {
        return "citys{" +
                "id=" + id +
                ", province_id=" + province_id +
                ", name='" + name + '\'' +
                ", city_num='" + city_num + '\'' +
                '}';
    }
}
