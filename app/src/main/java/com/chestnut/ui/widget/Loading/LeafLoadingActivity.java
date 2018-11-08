package com.chestnut.ui.widget.Loading;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;
import android.widget.TextView;

import com.chestnut.ui.R;


public class LeafLoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaf_loading);
        LeafLoadingView leafLoadingView = (LeafLoadingView) findViewById(R.id.leaf);
        //progress
        TextView tvProgress = (TextView) findViewById(R.id.tv_progress);
        ((SeekBar)findViewById(R.id.seek_bar_progress)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                leafLoadingView.setProgress(i);
                tvProgress.setText("progress：{x}".replace("{x}", String.valueOf(i)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //fan-bg-margin
        TextView tvFanBgMargin = (TextView) findViewById(R.id.tv_fan_bg_margin);
        ((SeekBar)findViewById(R.id.seek_bar_fan_bg_margin)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                leafLoadingView.setFanBgMargin(i);
                tvFanBgMargin.setText("fan-bg-margin：{x}".replace("{x}", String.valueOf(i)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {


            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //fan-speed
        TextView tvSpeed = (TextView) findViewById(R.id.tv_fan_speed);
        ((SeekBar)findViewById(R.id.seek_bar_fan_speed)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                leafLoadingView.setFanSpeedFactor(i);
                tvSpeed.setText("fan-speed：{x}".replace("{x}", String.valueOf(i)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {


            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //leaf-size
        TextView tvLeafSize = (TextView) findViewById(R.id.tv_leaf_size);
        ((SeekBar)findViewById(R.id.seek_bar_leaf_size)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                leafLoadingView.setLeafSize(i);
                tvLeafSize.setText("leaf-size：{x}".replace("{x}", String.valueOf(i)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {


            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
