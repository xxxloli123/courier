package com.android.slowlifecourier.util;


import android.util.Log;

import com.android.slowlifecourier.R;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by sgrape on 2017/6/26.
 * e-mail: sgrape1153@gmail.com
 */

public class LogoConfig {
    static final String TAG ="logoconfig";
    private static Map<String, Integer> logos = new HashMap<>();

    static {
        logos.put("YD", R.drawable.logo_yd);
        logos.put("ZTO", R.drawable.logo_zt);
        logos.put("YTO", R.drawable.logo_yt);
        logos.put("ZSHHD", R.drawable.logo_hd);
        logos.put("HHTT", R.drawable.logo_tt);
        logos.put("SF", R.drawable.logo_sf);
        logos.put("HTKY", R.drawable.logo_bs);
        logos.put("QFKD", R.drawable.logo_qf);
        logos.put("UC", R.drawable.logo_ys);
        logos.put("YZPY", R.drawable.logo_yz);
        logos.put("ZJS", R.drawable.logo_zjs);
    }

    public static int getLogoRes(String key) {
        Log.d(TAG, "getLogoRes: key"+key+"\t\t" +logos.containsKey(key));
        if (logos.containsKey(key))
            return logos.get(key);
        else return R.drawable.logo_hd;
    }
}
