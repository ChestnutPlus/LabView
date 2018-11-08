package com.chestnut.ui.widget.voiceAnim;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2018/7/5 16:04
 *     desc  :
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */
public class VoiceAnimPoint {
    public int centerX, centerY;
    public float maxHeight;
    public float threeHeight;
    public float halfHeight;
    public float oneHeight;

    public VoiceAnimPoint(int centerX, int centerY, float maxHeight, float threeHeight, float halfHeight, float oneHeight) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.maxHeight = maxHeight;
        this.threeHeight = threeHeight;
        this.halfHeight = halfHeight;
        this.oneHeight = oneHeight;
    }
}
