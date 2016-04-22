package com.lol.contacts.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by hh on 2016/4/21.
 */
public class ScreenAdapterUtils {

    public static float getScreenDesity(Context ctx){
        float density= 0;
        WindowManager manager = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = manager.getDefaultDisplay();

        DisplayMetrics metrics = new DisplayMetrics();
        defaultDisplay.getMetrics(metrics);

        density = metrics.density;
        return density;
    }
}
