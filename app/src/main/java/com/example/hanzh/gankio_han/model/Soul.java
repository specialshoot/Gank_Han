package com.example.hanzh.gankio_han.model;


import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.enums.AssignType;

import java.io.Serializable;

/**
 * Created by 轾 on 2015/10/10.
 */
public class Soul implements Serializable {

    @PrimaryKey(AssignType.AUTO_INCREMENT) @Column("_id") protected long id;
}
