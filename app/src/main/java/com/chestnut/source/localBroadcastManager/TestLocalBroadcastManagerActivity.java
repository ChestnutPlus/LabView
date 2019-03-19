package com.chestnut.source.localBroadcastManager;

import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.chestnut.ui.R;
import com.chestnut.ui.contract.BaseShowActivity;

public class TestLocalBroadcastManagerActivity extends BaseShowActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_local_broadcast_manager);

        LocalBroadcastManager.getInstance(this);

    }
}
