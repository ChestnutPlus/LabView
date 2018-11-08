package com.chestnut.ui.widget.carView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2018/7/13 17:13
 *     desc  :
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */
public abstract class CarBaseView extends View{

    //将图像分成多少格
    private int carBodyWidthPart = 2;
    private int carBodyHeightPart = 2;
    private int carWheelWidthPart = 1;
    private int carWheelHeightPart = 1;
    //交点坐标的个数
    private int carBodyPartCount = (carBodyWidthPart + 1) * (carBodyHeightPart + 1);
    private int carWheelPartCount = (carWheelWidthPart + 1) * (carWheelHeightPart + 1);
    //用于保存COUNT的坐标
    //x0, y0, x1, y1......
    private float[] carBodyNewsPos = new float[carBodyPartCount * 2];
    private float[] carBodyOrigPos = new float[carBodyPartCount * 2];
    private float[] carWheelNewsPos = new float[carWheelPartCount * 2];
    private float[] carWheelOrigPos = new float[carWheelPartCount * 2];
    protected Bitmap bitmapCarBody;
    protected Bitmap bitmapWheel;
    protected int bitmapCarBodyWidth, bitmapCarBodyHeight;
    private PaintFlagsDrawFilter paintFlagsDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    protected Paint paint = new Paint();
    {
        paint.setStyle(Paint.Style.STROKE);
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);// 设置画笔的锯齿效果。 true是去除，大家一看效果就明白了
    }

    //中轴线-x
    protected int centralAxisX = 0;
    protected int changeFactor = 0;
    protected int delayChangeFactor = 25;
    protected final int FACTOR_MAX = 30;

    //anim
    private boolean positive = true;
    private boolean delayPositive = true;
    private boolean isStart = false;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (positive) 
                changeFactor+=5;
            else
                changeFactor-=5;
            if (delayPositive)
                delayChangeFactor+=5;
            else
                delayChangeFactor-=5;
            invalidate();
            if (changeFactor>=FACTOR_MAX) {
                positive = false;
            }
            if (changeFactor<=0) {
                positive = true;
            }
            if (delayChangeFactor>=FACTOR_MAX) {
                delayPositive = false;
            }
            if (delayChangeFactor<=0) {
                delayPositive = true;
            }
            CarBaseView.this.postDelayed(runnable, 60);
        }
    };

    public CarBaseView(Context context) {
        super(context);
    }

    public CarBaseView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        bitmapCarBody = getCarBody();
        bitmapWheel = getCarWheel();
        getBitmapMeshPoints(bitmapCarBody,carBodyHeightPart,carBodyWidthPart,carBodyNewsPos,carBodyOrigPos);
        getBitmapMeshPoints(bitmapWheel,carWheelHeightPart,carWheelWidthPart,carWheelNewsPos,carWheelOrigPos);
        //初始化坐标
        centralAxisX = bitmapCarBody.getWidth()/2;
        bitmapCarBodyHeight = bitmapCarBody.getHeight();
        bitmapCarBodyWidth = bitmapCarBody.getWidth();
    }

    /**
     * 每个车厢的部分UI不尽相同，所以子类要实现这个方法
     * @return bitmap
     */
    protected abstract Bitmap getCarWheel();
    protected abstract Bitmap getCarBody();

    /**
     * 车轮的x方向的偏移量
     * @return 偏移量
     */
    protected int getWheelXOffset() {
        return 0;
    }

    /**
     * 得到网格点集合
     * @param bitmap bitmap
     * @param heightPart 纵向分割的份数
     * @param widthPart 横向分割的份数
     * @param newPos 新点集合
     * @param origPos 原始点集合
     */
    protected void getBitmapMeshPoints(Bitmap bitmap, int heightPart, int widthPart, float[] newPos, float[] origPos) {
        //参考：https://www.jianshu.com/p/51d8dd99d27d
        //初始化网格点
        int index = 0;
        float bmWidth = bitmap.getWidth();
        float bmHeight = bitmap.getHeight();
        for (int i = 0; i < heightPart + 1; i++) {
            float fy = bmHeight * i / heightPart;
            for (int j = 0; j < widthPart + 1; j++) {
                float fx = bmWidth * j / widthPart;
                //X轴坐标 放在偶数位
                newPos[index * 2] = fx;
                origPos[index * 2] = newPos[index * 2];
                //Y轴坐标 放在奇数位
                newPos[index * 2 + 1] = fy;
                origPos[index * 2 + 1] = newPos[index * 2 + 1];
                index += 1;
            }
        }
    }

    /**
     * 改变车厢的Points
     * @param bitmap bitmap
     * @param posCount 点的个数
     * @param widthPart 横向分割的份数
     * @param heightPart 纵向分割的份数
     * @param newPos 新点集合
     * @param origPos 原始点集合
     * @param xFactor 变化因子
     * @param yFactor 变化因子
     */
    protected void changeCarBodyPoint(Bitmap bitmap, int posCount, int widthPart, int heightPart, float[] newPos, float[] origPos, float xFactor, float yFactor) {
        //遍历交点，修改
        for (int i = 0; i < posCount; i++) {
            int x = i*2;
            int y = x + 1;
            //x方向的拉伸，离x周轴线越近变化越小，离顶部（y=0）越近，变化也越小
            if (newPos[x]<centralAxisX) {
                newPos[x] = origPos[x] - changeFactor*Math.abs((widthPart+1)/2-i%(widthPart+1))*(origPos[y]/bitmap.getHeight()) * xFactor;
            }
            else if (newPos[x]>centralAxisX) {
                newPos[x] = origPos[x] + changeFactor*Math.abs((widthPart+1)/2-i%(widthPart+1))*(origPos[y]/bitmap.getHeight()) * xFactor;
            }
            //y方向的拉伸，离x周轴线越近变化越大，离顶部（y=0）越近，变化也越小
            newPos[y] = origPos[y] + changeFactor * (widthPart*heightPart/(heightPart+1)+1) * (1 - Math.abs(origPos[x]-centralAxisX)/bitmap.getWidth()) * yFactor;
        }
    }

    /**
     * 车轮的坐标变化
     * @param changeFactor 改变因子
     */
    private void changeWheelPoint(int changeFactor) {
        //改变的幅度由temp的大小决定
        float temp = bitmapWheel.getHeight()/15;
        changeFactor -= 15;
        float tempA = changeFactor * temp / FACTOR_MAX;
        //需要变化的坐标写死
        carWheelNewsPos[0 * 2] = carWheelOrigPos[0 * 2] - tempA;
        carWheelNewsPos[2 * 2] = carWheelOrigPos[2 * 2] - tempA;
        carWheelNewsPos[1 * 2] = carWheelOrigPos[1 * 2] + tempA;
        carWheelNewsPos[3 * 2] = carWheelOrigPos[3 * 2] + tempA;
    }

    /**
     * 绘制Mes网格点
     * @param canvas canvas
     * @param pos 点集
     * @param paint 画笔
     */
    protected void drawPoint(Canvas canvas, float[] pos, Paint paint) {
        for (int i = 0; i < pos.length/2; i++) {
            int x = i*2;
            int y = x + 1;
            canvas.drawPoint(pos[x], pos[y], paint);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.setDrawFilter(paintFlagsDrawFilter);

        //车厢主体
        canvas.save();
        changeCarBodyPoint(bitmapCarBody, carBodyPartCount, carBodyWidthPart, carBodyHeightPart, carBodyNewsPos, carBodyOrigPos, 0.4f, 0.1f);
        canvas.translate(getWidth()/2 - bitmapCarBodyWidth/2, getHeight()/2 - bitmapCarBodyHeight/2);
        canvas.drawBitmapMesh(bitmapCarBody, carBodyWidthPart, carBodyHeightPart, carBodyNewsPos, 0, null, 0, paint);
        canvas.restore();

        //车轮子，左边
        canvas.save();
        canvas.translate(getWidth()/2 - bitmapWheel.getWidth() + getWheelXOffset(), getHeight()/2 + bitmapCarBody.getHeight()/2 - 24);
        changeWheelPoint(changeFactor);
        canvas.drawBitmapMesh(bitmapWheel, carWheelWidthPart, carWheelHeightPart, carWheelNewsPos, 0, null, 0, paint);
        canvas.restore();

        //车轮子，右边
        canvas.save();
        canvas.translate(getWidth()/2 + 25 + getWheelXOffset(), getHeight()/2 + bitmapCarBody.getHeight()/2 - 24);
        changeWheelPoint(delayChangeFactor);
        canvas.drawBitmapMesh(bitmapWheel, carWheelWidthPart, carWheelHeightPart, carWheelNewsPos, 0, null, 0, paint);
        canvas.restore();
    }

    public void startAnim() {
        if (!isStart) {
            isStart = true;
            this.post(runnable);
        }
    }

    public void stopAnim() {
        if (isStart) {
            isStart = false;
            this.removeCallbacks(runnable);
            changeFactor = 0;
            delayChangeFactor = 25;
            this.invalidate();
        }
    }
}
