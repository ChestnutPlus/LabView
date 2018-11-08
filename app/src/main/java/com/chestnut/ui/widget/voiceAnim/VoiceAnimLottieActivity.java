package com.chestnut.ui.widget.voiceAnim;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

import com.chestnut.ui.R;

public class VoiceAnimLottieActivity extends AppCompatActivity {

    private LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_anim_lottie);
        lottieAnimationView = (LottieAnimationView) findViewById(R.id.animation_view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (lottieAnimationView!=null)
            lottieAnimationView.resumeAnimation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (lottieAnimationView!=null)
            lottieAnimationView.pauseAnimation();
    }
}
