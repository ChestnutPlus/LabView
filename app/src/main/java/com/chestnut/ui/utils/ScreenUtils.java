package com.chestnut.ui.utils;

import android.app.Activity;
import android.content.pm.ActivityInfo;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2018/11/7 14:29
 *     desc  :
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */
public class ScreenUtils {

    public static void setScreenOrientation(Activity activity, boolean isLandscape) {
        if (!isLandscape && activity.getRequestedOrientation()== ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        if (isLandscape && activity.getRequestedOrientation()!= ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }
}
