package com.chestnut.ui.contract;

import android.view.View;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2018/10/17 9:53
 *     desc  :
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */
public interface BaseViewContract {
    void playAnim();
    void pauseAnim();
    void stopAnim();
    void setFactor(float factor);
    float getFactor();
    void release();

    abstract class AgentCallback {
        public void playAnim(View v) {}
        public void pauseAnim(View v) {}
        public void stopAnim(View v) {}
        public void setFactor(View v, float factor) {}
    }
}
