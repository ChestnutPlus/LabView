package com.chestnut.ui.widget.carSign;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.chestnut.common.utils.StringUtils;
import com.chestnut.ui.R;
import com.chestnut.ui.contract.BaseViewContract;
import com.chestnut.ui.utils.ViewUtils;

/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2018/8/14 23:37
 *     desc  :
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */

public class CarAdvancedSignView extends View implements BaseViewContract {

    private ObjectAnimator objectAnimator;

    private Bitmap bitmapTrunk;
    private float trunkX,trunkY,trunkRotationDegree;

    private Bitmap bitmapCover;
    private float coverX,coverY,coverRotationDegree;

    private String signStr;
    private Typeface typeface;
    private float signStrSize = 48f;
    private Path path = new Path();

    private RectF rectFSign = new RectF(0,0,240,290);
    private Paint paint = new Paint();
    private Matrix matrix = new Matrix();
    private PaintFlagsDrawFilter paintFlagsDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

    public CarAdvancedSignView(Context context) {
        this(context,null);
    }

    public CarAdvancedSignView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        bitmapCover = BitmapFactory.decodeResource(getResources(), R.drawable.car_sign_cover);
        bitmapTrunk = BitmapFactory.decodeResource(getResources(), R.drawable.car_sign_trunk);
        paint.setStyle(Paint.Style.STROKE);
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        initParams();
    }

    private void initParams() {
        trunkX = rectFSign.right / 2;
        trunkY = rectFSign.bottom - 35;
        trunkRotationDegree = 21;
        coverX = rectFSign.right / 2 - bitmapCover.getWidth() / 2;
        coverY = rectFSign.bottom;
        coverRotationDegree = -10;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.setDrawFilter(paintFlagsDrawFilter);

        //绘制动画区域
        paint.setColor(Color.GRAY);
        canvas.translate(100,20);
        canvas.drawRect(rectFSign,paint);

        matrix.reset();
        ViewUtils.applySelfRotationToMatrix(bitmapTrunk,trunkRotationDegree,trunkX,trunkY,matrix);
        canvas.drawBitmap(bitmapTrunk, matrix, paint);

        matrix.reset();
        ViewUtils.applySelfRotationToMatrix(bitmapCover,coverRotationDegree,coverX,coverY,matrix);
        canvas.drawBitmap(bitmapCover, matrix, paint);

        // sign str
        if (!StringUtils.isEmpty(signStr)) {
            // str 的绘制 path
            paint.setColor(Color.RED);
            path.reset();
            //左下点
            float x0 = rectFSign.right / 2 - bitmapCover.getWidth() / 2;
            float y0 = coverY + bitmapCover.getHeight();
            //旋转点
            float rX0 = x0 + bitmapCover.getWidth() / 2;
            float rY0 = y0 - bitmapCover.getHeight() / 2;
            //右下点
            float x1 = x0 + bitmapCover.getWidth();
            float y1 = y0;
            float radians = (float) Math.toRadians(coverRotationDegree);
            path.moveTo(ViewUtils.pointRotateGetX(rX0, rY0, radians, x0, y0), ViewUtils.pointRotateGetY(rX0, rY0, radians, x0, y0));
            path.lineTo(ViewUtils.pointRotateGetX(rX0, rY0, radians, x1, y1), ViewUtils.pointRotateGetY(rX0, rY0, radians, x1, y1));
            paint.setColor(Color.WHITE);
            paint.setTextSize(signStrSize);
            if (typeface != null)
                paint.setTypeface(typeface);
            float hOffset = (bitmapCover.getWidth() - paint.measureText(signStr)) / 2;
            canvas.drawTextOnPath(signStr, path, hOffset, -20, paint);
        }
    }

    public void setSignStr(String signStr) {
        this.signStr = signStr;
    }

    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
    }

    public void setSignStrSize(float signStrSize) {
        this.signStrSize = signStrSize;
    }

    public float getTrunkX() {
        return trunkX;
    }

    public void setTrunkX(float trunkX) {
        this.trunkX = trunkX;
    }

    public float getTrunkY() {
        return trunkY;
    }

    public void setTrunkY(float trunkY) {
        this.trunkY = trunkY;
    }

    public float getTrunkRotationDegree() {
        return trunkRotationDegree;
    }

    public void setTrunkRotationDegree(float trunkRotationDegree) {
        this.trunkRotationDegree = trunkRotationDegree;
    }

    public float getCoverX() {
        return coverX;
    }

    public void setCoverX(float coverX) {
        this.coverX = coverX;
    }

    public float getCoverY() {
        return coverY;
    }

    public void setCoverY(float coverY) {
        this.coverY = coverY;
    }

    public float getCoverRotationDegree() {
        return coverRotationDegree;
    }

    public void setCoverRotationDegree(float coverRotationDegree) {
        this.coverRotationDegree = coverRotationDegree;
    }

    @Override
    public void playAnim() {
        if (objectAnimator!=null) {
            if (objectAnimator.isPaused()) {
                if (objectAnimator.getAnimatedFraction()==1)
                    objectAnimator.start();
                else
                    objectAnimator.resume();
            }
            else {
                objectAnimator.start();
            }
        }
        else {
            initParams();

            PropertyValuesHolder trunkXPropertyValuesHolder =
                    PropertyValuesHolder.ofFloat("trunkX",trunkX, trunkX-bitmapTrunk.getWidth()/2);

            PropertyValuesHolder trunkYPropertyValuesHolder =
                    PropertyValuesHolder.ofFloat("trunkY",trunkY,5,120);

            PropertyValuesHolder trunkRotationDegreePropertyValuesHolder =
                    PropertyValuesHolder.ofFloat("trunkRotationDegree",trunkRotationDegree,0);

            PropertyValuesHolder coverYPropertyValuesHolder =
                    PropertyValuesHolder.ofFloat("coverY",coverY,80,108);

            PropertyValuesHolder coverRotationDegreePropertyValuesHolder =
                    PropertyValuesHolder.ofFloat("coverRotationDegree",coverRotationDegree,0);

            objectAnimator = ObjectAnimator.ofPropertyValuesHolder(this,
                    trunkXPropertyValuesHolder,trunkYPropertyValuesHolder,trunkRotationDegreePropertyValuesHolder,
                    coverYPropertyValuesHolder,coverRotationDegreePropertyValuesHolder);

            objectAnimator.setDuration(1500);
            objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            objectAnimator.addUpdateListener(valueAnimator -> CarAdvancedSignView.this.invalidate());
            objectAnimator.start();
        }
    }

    @Override
    public void pauseAnim() {
        if (objectAnimator!=null) {
            objectAnimator.pause();
            this.invalidate();
        }
    }

    @Override
    public void stopAnim() {
        if (objectAnimator!=null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                objectAnimator.setCurrentFraction(0);
            }
            objectAnimator.pause();
        }
    }

    @Override
    public void setFactor(float factor) {
        if (objectAnimator!=null)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                objectAnimator.setCurrentFraction(factor*0.01f);
            }
    }

    @Override
    public float getFactor() {
        return objectAnimator!=null ? objectAnimator.getAnimatedFraction()*100 : 0;
    }

    @Override
    public void release() {
        if (objectAnimator!=null) {
            objectAnimator.pause();
            objectAnimator.cancel();
            objectAnimator = null;
        }
    }
}
