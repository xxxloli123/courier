package com.android.slowlifecourier.database;

import com.android.slowlifecourier.objectmodle.ProvinceEntity;

/**
 * Created by sgrape on 2017/7/2.
 * e-mail: sgrape1153@gmail.com
 */

public interface ProvinceDao extends Dao<ProvinceEntity> {

    ProvinceEntity searchByName(String startPro);
}
