package com.chestnut.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.chestnut.ui.R;
import com.chestnut.ui.contract.BaseViewContract;
import com.chestnut.ui.utils.ScreenUtils;
import com.zhouyou.view.seekbar.SignSeekBar;

import java.lang.reflect.Constructor;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class DisplayFragment extends Fragment implements View.OnClickListener,View.OnLongClickListener,SignSeekBar.OnProgressChangedListener{

    private static Builder builder = null;
    private BaseViewContract baseViewContract;
    private ImageView imgControl;
    private SignSeekBar signSeekBar;
    private Disposable disposable;

    /**
     * 建造者模式
     * @return 建造者
     */
    public static Builder getBuilder() {
        return new Builder();
    }

    public static class Builder {

        private Class<? extends View> baseViewClass = null;
        private View diyView;
        private int diyViewHeight = FrameLayout.LayoutParams.MATCH_PARENT;
        private int diyViewWight = FrameLayout.LayoutParams.MATCH_PARENT;
        @LayoutRes
        private int diyViewLayoutRes = -1;
        private boolean isShowSeekBar = true;
        private boolean isShowControlBtn = true;
        private boolean isLandscape = false;//默认竖屏
        private boolean isAutoPlayAnim = false;
        private BaseViewContract.AgentCallback agentCallback;
        private int barMin = 0;
        private int barMax = 100;

        public Builder setAutoPlayAnim(boolean autoPlayAnim) {
            this.isAutoPlayAnim = autoPlayAnim;
            return this;
        }

        public Builder setLandscape(boolean landscape) {
            this.isLandscape = landscape;
            return this;
        }

        public Builder setBarSum(int barMin, int barMax) {
            this.barMin = barMin;
            this.barMax = barMax;
            return this;
        }

        public Builder setDiyViewLayoutRes(@LayoutRes int diyViewLayoutRes) {
            this.diyViewLayoutRes = diyViewLayoutRes;
            return this;
        }

        public Builder setBaseViewClass(Class<? extends View> baseViewClass) {
            this.baseViewClass = baseViewClass;
            return this;
        }

        public Builder setBaseViewClass(Class<? extends View> baseViewClass, int diyViewWight, int diyViewHeight) {
            this.baseViewClass = baseViewClass;
            this.diyViewWight = diyViewWight;
            this.diyViewHeight = diyViewHeight;
            return this;
        }

        public Builder setAgentCallback(BaseViewContract.AgentCallback agentCallback) {
            this.agentCallback = agentCallback;
            return this;
        }

        public Builder setShowControlBtn(boolean showControlBtn) {
            isShowControlBtn = showControlBtn;
            return this;
        }

        public Builder setShowSeekBar(boolean showSeekBar) {
            isShowSeekBar = showSeekBar;
            return this;
        }

        public Fragment newInstance() {
            DisplayFragment fragment = new DisplayFragment();
            DisplayFragment.builder = this;
            return fragment;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_display, container, false);
        //control btn
        imgControl = (ImageView) view.findViewById(R.id.img_view_control);
        imgControl.setOnClickListener(this);
        imgControl.setOnLongClickListener(this);

        //progress
        signSeekBar = (SignSeekBar) view.findViewById(R.id.sign_seek_bar);
        signSeekBar.setOnProgressChangedListener(this);

        //builder
        if (builder!=null) {
            if (!builder.isShowControlBtn) {
                imgControl.setVisibility(View.INVISIBLE);
            }
            if (!builder.isShowSeekBar) {
                signSeekBar.setVisibility(View.INVISIBLE);
            }
            //diy view
            if (builder.diyViewLayoutRes>0) {
                ViewStub viewStub = (ViewStub) view.findViewById(R.id.view_stub);
                viewStub.setLayoutResource(builder.diyViewLayoutRes);
                View diyView = viewStub.inflate();
                if (diyView instanceof BaseViewContract) {
                    baseViewContract = (BaseViewContract) diyView;
                }
                builder.diyView = diyView;
            }
            else if (builder.baseViewClass!=null) {
                try {
                    FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.layout);
                    Constructor<? extends View> constructor = builder.baseViewClass.getConstructor(Context.class, AttributeSet.class);
                    View diyView = constructor.newInstance(frameLayout.getContext(),null);
                    if (diyView instanceof BaseViewContract) {
                        baseViewContract = (BaseViewContract) diyView;
                    }
                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(builder.diyViewWight, builder.diyViewHeight);
                    layoutParams.gravity = Gravity.CENTER;
                    diyView.setLayoutParams(layoutParams);
                    frameLayout.addView(diyView, 0);
                    builder.diyView = diyView;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                throw new RuntimeException("Fragment not init");
            }
            ScreenUtils.setScreenOrientation(getActivity(), builder.isLandscape);
            if (builder.isAutoPlayAnim) {
                onClick(imgControl);
            }
            signSeekBar.getConfigBuilder()
                    .min(builder.barMin)
                    .max(builder.barMax)
                    .build();
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        builder = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_view_control:
                ImageView img = (ImageView) v;
                if (v.getTag()==null || (v.getTag() instanceof Boolean && (boolean) v.getTag())) {
                    img.setTag(false);
                    img.setImageResource(R.drawable.svg_pause);
                    if (baseViewContract !=null)
                        baseViewContract.playAnim();
                    if (builder!=null && builder.agentCallback!=null)
                        builder.agentCallback.playAnim(builder.diyView);
                    startGetProgress();
                }
                else {
                    stopUI();
                    if (baseViewContract !=null)
                        baseViewContract.pauseAnim();
                    if (builder!=null && builder.agentCallback!=null)
                        builder.agentCallback.pauseAnim(builder.diyView);
                    stopGetProgress();
                }
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.img_view_control:
                signSeekBar.setProgress(0);
                stopUI();
                if (baseViewContract !=null)
                    baseViewContract.stopAnim();
                if (builder!=null && builder.agentCallback!=null)
                    builder.agentCallback.stopAnim(builder.diyView);
                stopGetProgress();
                return true;
        }
        return false;
    }

    @Override
    public void onProgressChanged(SignSeekBar signSeekBar, int progress, float progressFloat, boolean fromUser) {
        if (fromUser) {
            if (baseViewContract !=null)
                baseViewContract.setFactor(progress);
            if (builder!=null && builder.agentCallback!=null)
                builder.agentCallback.setFactor(builder.diyView, progress);
        }
    }

    @Override
    public void getProgressOnActionUp(SignSeekBar signSeekBar, int progress, float progressFloat) {

    }

    @Override
    public void getProgressOnFinally(SignSeekBar signSeekBar, int progress, float progressFloat, boolean fromUser) {

    }

    private void stopUI() {
        imgControl.setTag(true);
        imgControl.setImageResource(R.drawable.svg_play);
    }

    private void startGetProgress() {
        stopGetProgress();
        disposable = Observable.interval(13, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    if (signSeekBar!=null) {
                        int progress = (int) baseViewContract.getFactor();
                        if (progress>=signSeekBar.getMax()) {
                            stopUI();
                            stopGetProgress();
                        }
                        signSeekBar.setProgress(progress);
                    }
                },throwable -> {});
    }

    private void stopGetProgress() {
        if (disposable!=null && !disposable.isDisposed())
            disposable.dispose();
        disposable = null;
    }
}
