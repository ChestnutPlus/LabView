package com.chestnut.source.handlerThread;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.chestnut.ui.R;

public class HandlerThreadActivity extends AppCompatActivity {

    private HandlerThread handlerThread;
    private String TAG = "HandlerThreadTest";
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler_thread);
        Log.i(TAG,"onCreate");

        handlerThread = new HandlerThread(TAG) {
            @Override
            protected void onLooperPrepared() {
                super.onLooperPrepared();
                Log.i(TAG,"onLooperPrepared, "+Thread.currentThread().getName());
            }
        };
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0x01:
                        Log.i(TAG,"handleMessage, 0x01, start");
                        try {
                            Thread.sleep(10*1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Log.i(TAG,"handleMessage, 0x01, done");
                        break;
                    case 0x02:
                        Log.i(TAG,"handleMessage, 0x02, done");
                        break;
                }
            }
        };

        findViewById(R.id.btn_1).setOnClickListener(v -> {
            handler.sendEmptyMessage(0x01);
        });

        findViewById(R.id.btn_2).setOnClickListener(v -> {
            handler.sendEmptyMessageDelayed(0x02,10*1000);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"onDestroy");
//        handlerThread.quit();
        handlerThread.quitSafely();
    }
}
