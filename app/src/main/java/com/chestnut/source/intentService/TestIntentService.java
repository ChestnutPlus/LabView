package com.chestnut.source.intentService;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.chestnut.common.utils.LogUtils;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2019/2/20 13:05
 *     desc  :
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */
public class TestIntentService extends IntentService {

    private final static String TAG = "TestIntentService";

    public final static String Action_Sleep = "Action_Sleep";
    public final static String Action_Soon = "Action_Soon";

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        LogUtils.i(TAG,"onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    public TestIntentService() {
        super(TAG);
    }

    public TestIntentService(String name) {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        LogUtils.i(TAG, intent==null ? "null intent" : "action:" + intent.getAction());
        if (intent!=null && intent.getAction()!=null) {
            switch (intent.getAction()) {
                case Action_Sleep:
                    try {
                        Thread.sleep(5*1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case Action_Soon:
                    break;
            }
        }
        LogUtils.i(TAG,"action, done");
    }

    @Override
    public void onDestroy() {
        LogUtils.i(TAG,"onDestroy");
        super.onDestroy();
    }
}
