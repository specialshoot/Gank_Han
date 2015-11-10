package com.example.hanzh.gankio_han.model;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

/**
 * Created by 轾 on 2015/11/8.
 */
@Table("provinces")public class provinces {

    @PrimaryKey(AssignType.AUTO_INCREMENT) @Column("_id") protected int id;
    @Column("name") private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "provinces{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
