package com.chestnut.source.intentService;

import android.content.Intent;
import android.os.Bundle;

import com.chestnut.ui.R;
import com.chestnut.ui.contract.BaseShowActivity;

public class TestIntentServiceActivity extends BaseShowActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_intent_service);

        findViewById(R.id.btn_1).setOnClickListener(v -> {
            Intent intent = new Intent(this, TestIntentService.class);
            intent.setAction(TestIntentService.Action_Sleep);
            startService(intent);
        });

        findViewById(R.id.btn_2).setOnClickListener(v -> {
            Intent intent = new Intent(this, TestIntentService.class);
            intent.setAction(TestIntentService.Action_Soon);
            startService(intent);
        });

    }
}
