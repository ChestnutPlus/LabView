package com.chestnut.ui.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2018/6/29 18:08
 *     desc  : 封装一些常用的UI套路函数
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */
public class ViewUtils {

    /**
     * 多种Matrix变化组合的时候使用
     */
    public static class MultipleMatrixSelfChangeBuilder {

        private Matrix matrix;
        private float posX,posY,offsetX,offsetY;

        private MultipleMatrixSelfChangeBuilder(Matrix matrix) {
            this.matrix = matrix;
        }

        public static MultipleMatrixSelfChangeBuilder get(Matrix matrix) {
            return new MultipleMatrixSelfChangeBuilder(matrix);
        }

        public MultipleMatrixSelfChangeBuilder start(Bitmap bitmap, float posX, float posY) {
            matrix.reset();
            offsetX = bitmap.getWidth() / 2;
            offsetY = bitmap.getHeight() / 2;
            matrix.postTranslate(-offsetX, -offsetY);
            this.posX = posX;
            this.posY = posY;
            return this;
        }

        public MultipleMatrixSelfChangeBuilder applySelfRotation(float rotation) {
            matrix.postRotate(rotation);
            return this;
        }

        public MultipleMatrixSelfChangeBuilder applySelfScale(float scaleX, float scaleY) {
            matrix.postScale(scaleX,scaleY);
            return this;
        }

        public void end() {
            matrix.postTranslate(posX + offsetX, posY + offsetY);
        }
    }

    /**
     * 将旋转应用到矩阵
     *  此旋转是基于Bitmap的中心旋转
     *
     * @param bitmap 位图对象
     * @param rotation 旋转度数
     * @param posX canvas绘制Bitmap的起点
     * @param posY canvas绘制Bitmap的起点
     * @param matrix matrix
     */
    public static void applySelfRotationToMatrix(Bitmap bitmap, float rotation, float posX, float posY, Matrix matrix) {
        float offsetX = bitmap.getWidth() / 2;
        float offsetY = bitmap.getHeight() / 2;
        matrix.postTranslate(-offsetX, -offsetY);
        matrix.postRotate(rotation);
        matrix.postTranslate(posX + offsetX, posY + offsetY);
    }

    /**
     * 将缩放应用到矩阵
     *  此缩放是基于Bitmap的中心旋转
     *
     * @param bitmap 位图对象
     * @param scaleX scaleX
     * @param scaleY scaleY
     * @param posX canvas绘制Bitmap的起点
     * @param posY canvas绘制Bitmap的起点
     * @param matrix matrix
     */
    public static void applySelfScaleToMatrix(Bitmap bitmap, float scaleX, float scaleY, float posX, float posY, Matrix matrix) {
        float offsetX = bitmap.getWidth() / 2;
        float offsetY = bitmap.getHeight() / 2;
        matrix.postTranslate(-offsetX, -offsetY);
        matrix.postScale(scaleX,scaleY);
        matrix.postTranslate(posX + offsetX, posY + offsetY);
    }

    /**
     * 因子映射
     * @param currentFactor 原因子的值
     * @param origStartFactor 原因子的上限
     * @param origEndFactor 原因子的下限
     * @param interpolatorFactors 新因子的插值，从数组0位置为新因子的初始值。
     * @return 新因子的值
     */
    public static float factorMapping(float currentFactor, float origStartFactor, float origEndFactor,
                                  float[] interpolatorFactors) {
        //现在的百分比
        float temp = (currentFactor - origStartFactor) / (origEndFactor - origStartFactor);
        //新的总大小
        float newFactorMax = 0;
        for (int i = 1; i < interpolatorFactors.length; i++) {
            newFactorMax += Math.abs(interpolatorFactors[i]-interpolatorFactors[i-1]);
        }

        //是否在第一个区间内
        float interpolatorTemp = 0;
        //比较每一个区间
        for (int i = 1; i < interpolatorFactors.length; i++) {
            float localTemp = Math.abs(interpolatorFactors[i]-interpolatorFactors[i-1])/newFactorMax;
            if (interpolatorTemp+localTemp>=temp) {
                //所占当前区间百分比
                float tempX = (temp - interpolatorTemp) * newFactorMax / Math.abs(interpolatorFactors[i]-interpolatorFactors[i-1]);
                return tempX * (interpolatorFactors[i]-interpolatorFactors[i-1]) + interpolatorFactors[i-1];
            }
            else {
                interpolatorTemp+=localTemp;
            }
        }
        return 0;
    }

    /**
     * 因子映射
     * @param currentFactor 原因子的值
     * @param origStartFactor 原因子的上限
     * @param origEndFactor 原因子的下限
     * @param startFactor 新因子的上限
     * @param interpolatorFactor 新因子的插值
     * @param endFactor 新因子的下限
     * @return 新因子的值
     */
    public static float factorMapping(float currentFactor, float origStartFactor, float origEndFactor,
                                      float startFactor, float interpolatorFactor, float endFactor) {
        //现在的百分比
        float temp = (currentFactor - origStartFactor) / (origEndFactor - origStartFactor);
        //新的总大小
        float newFactorMax = Math.abs(startFactor-interpolatorFactor) + Math.abs(endFactor-interpolatorFactor);
        //上限所占的百分比
        float startTemp = Math.abs(startFactor-interpolatorFactor) / newFactorMax;
        if (startTemp<temp) {
            //当前限所占百分比
            float tempX = Math.abs( (temp - startTemp) * newFactorMax / Math.abs(endFactor-interpolatorFactor));
            return tempX * (endFactor-interpolatorFactor) + interpolatorFactor;
        }
        else {
            //当前限所占百分比
            float tempX = Math.abs(temp * newFactorMax / Math.abs(startFactor-interpolatorFactor));
            return tempX * (interpolatorFactor-startFactor) + startFactor;
        }
    }

    public static float factorMapping(float currentFactor, float origStartFactor, float origEndFactor, float startFactor, float endFactor) {
        return (currentFactor - origStartFactor) * (endFactor - startFactor) / (origEndFactor - origStartFactor) + startFactor;
    }

    /**
     * 在平面中，一个点绕任意点旋转 radian 弧度
     *  后的点的坐标
     *
     * @param rX0 旋转基准点x
     * @param rY0 旋转基准点y
     * @param radian 旋转radian
     * @param x 旋转点x
     * @param y 旋转点y
     * @return 旋转的结果，x，y
     */
    public static float pointRotateGetX(float rX0, float rY0, float radian, float x, float y) {
        return (float) ((x - rX0) * Math.cos(radian) - (y - rY0) * Math.sin(radian) + rX0);
    }

    public static float pointRotateGetY(float rX0, float rY0, float radian, float x, float y) {
        return (float) ((x - rX0) * Math.sin(radian) + (y - rY0) * Math.cos(radian) + rY0);
    }

    /**
     * 释放Bitmap
     * @param bitmap bitmap
     */
    public static void releaseBitmap(Bitmap bitmap) {
        if (bitmap!=null && !bitmap.isRecycled())
            bitmap.recycle();
        bitmap = null;
    }
}
