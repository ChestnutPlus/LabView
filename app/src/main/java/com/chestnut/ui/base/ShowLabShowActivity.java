package com.chestnut.ui.base;

import android.content.Intent;
import android.os.Bundle;

import com.chestnut.codeSnippet.RatingBarActivity;
import com.chestnut.codeSnippet.TextViewActivity;
import com.chestnut.codeSnippet.ViewStubActivity;
import com.chestnut.source.asyncTask.TestAsyncTaskActivity;
import com.chestnut.source.handlerThread.HandlerThreadActivity;
import com.chestnut.source.intentService.TestIntentServiceActivity;
import com.chestnut.ui.R;
import com.chestnut.ui.contract.BaseShowActivity;
import com.chestnut.ui.widget.Loading.LeafLoadingActivity;
import com.chestnut.ui.widget.carSign.CarAdvancedSignView;
import com.chestnut.ui.widget.carSign.CarSignView;
import com.chestnut.ui.widget.carView.CarMarketView;
import com.chestnut.ui.widget.voiceAnim.VoiceAnimLottieActivity;
import com.chestnut.ui.widget.voiceAnim.VoiceAnimView;

public class ShowLabShowActivity extends BaseShowActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab);
        initView();
    }

    @Override
    public void onBackPressed() {
        if (currentFragment!=null)
            close();
        else
            super.onBackPressed();
    }

    /**
     * 在这里添加自定义View
     *      注意的是，目前都是同一个的控制风格
     *      若风格不同，可重写Fragment去做
     */
    private void initView() {
        addTvViewToLayout("叶子Loading动画",
                v -> startActivity(new Intent(this, LeafLoadingActivity.class)));
        addTvViewToLayout("扭曲动效",
                v -> open(DisplayFragment.getBuilder()
                        .setLandscape(true)
                        .setAutoPlayAnim(true)
                        .setBaseViewClass(CarMarketView.class)
                        .setShowSeekBar(false)
                        .newInstance()));
        addTvViewToLayout("波浪动效",
                v -> open(DisplayFragment.getBuilder()
                        .setLandscape(true)
                        .setAutoPlayAnim(true)
                        .setBaseViewClass(VoiceAnimView.class)
                        .setShowSeekBar(false)
                        .newInstance()));
        addTvViewToLayout("波浪动效-Lottie实现",
                v -> startActivity(new Intent(this, VoiceAnimLottieActivity.class)));
        addTvViewToLayout("指示牌弹出动画",
                v -> open(DisplayFragment.getBuilder()
                        .setLandscape(true)
                        .setBaseViewClass(CarSignView.class)
                        .newInstance()));
        addTvViewToLayout("指示牌弹出动画(高级版)",
                v -> open(DisplayFragment.getBuilder()
                        .setLandscape(true)
                        .setBaseViewClass(CarAdvancedSignView.class)
                        .newInstance()));
        addTvViewToLayout("IntentService", v -> {
            startActivity(new Intent(this, TestIntentServiceActivity.class));
        });
        addTvViewToLayout("AsyncTask", v -> {
            startActivity(new Intent(this, TestAsyncTaskActivity.class));
        });
        addTvViewToLayout("HandlerThread", v -> {
            startActivity(new Intent(this, HandlerThreadActivity.class));
        });
        addTvViewToLayout("TextView", v -> {
            startActivity(new Intent(this, TextViewActivity.class));
        });
        addTvViewToLayout("ViewStub", v -> {
            startActivity(new Intent(this, ViewStubActivity.class));
        });
        addTvViewToLayout("RatingBar", v -> {
            startActivity(new Intent(this, RatingBarActivity.class));
        });
    }
}
