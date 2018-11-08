package com.chestnut.ui.contract;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.chestnut.common.utils.BarUtils;
import com.chestnut.common.utils.ConvertUtils;
import com.chestnut.ui.R;
import com.chestnut.ui.utils.ScreenUtils;
import com.google.android.flexbox.FlexboxLayout;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2018/11/7 16:42
 *     desc  :
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */
public abstract class BaseShowActivity extends AppCompatActivity{

    protected Fragment currentFragment = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BarUtils.hideStatusBar(this);
    }

    protected void open(Fragment fragment) {
        if (currentFragment!=null) {
            if (currentFragment.getClass().getName().equals(fragment.getClass().getName()))
                return;
            close();
        }
        currentFragment = fragment;
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.layout, currentFragment, currentFragment.getClass().getName());
        transaction.commit();
    }

    protected void close() {
        if (currentFragment!=null) {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.remove(currentFragment);
            transaction.commit();
            currentFragment = null;
            ScreenUtils.setScreenOrientation(this,false);
        }
    }

    protected void addTvViewToLayout(String content, View.OnClickListener onClickListener) {
        FlexboxLayout flexboxLayout = (FlexboxLayout) findViewById(R.id.flx);
        TextView textView = new TextView(this);
        textView.setBackground(getResources().getDrawable(R.drawable.tv_bg_round));
        textView.setText(content);
        textView.setGravity(Gravity.CENTER);
        int padding = ConvertUtils.dp2px(this, 10);
        textView.setPadding(padding,padding,padding,padding);
        flexboxLayout.addView(textView);
        int margin = ConvertUtils.dp2px(this, 5);
        int marginTopBottom = ConvertUtils.dp2px(this, 5);
        FlexboxLayout.LayoutParams layoutParams = (FlexboxLayout.LayoutParams) textView.getLayoutParams();
        layoutParams.setMargins(margin, marginTopBottom, margin, marginTopBottom);
        textView.setClickable(true);
        textView.setFocusable(true);
        textView.setTag(content);
        textView.setOnClickListener(onClickListener);
    }
}
