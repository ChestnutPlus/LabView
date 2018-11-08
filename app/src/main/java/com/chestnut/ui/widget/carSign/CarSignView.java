package com.chestnut.ui.widget.carSign;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
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
 *     time  : 2018/8/3 17:04
 *     desc  :
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */
public class CarSignView extends View implements BaseViewContract{

    //字体
    private String signStr;
    private Typeface typeface;
    private float signStrSize = 48f;

    private Bitmap bitmapCover;
    private Bitmap bitmapTrunk;

    private RectF rectFSign = new RectF(0,0,240,290);
    private Paint paint = new Paint();
    private Path path = new Path();
    private Matrix matrix = new Matrix();
    {
        paint.setStyle(Paint.Style.STROKE);
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
    }

    public CarSignView(Context context) {
        super(context);
    }

    public CarSignView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        bitmapCover = BitmapFactory.decodeResource(getResources(), R.drawable.car_sign_cover);
        bitmapTrunk = BitmapFactory.decodeResource(getResources(), R.drawable.car_sign_trunk);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制动画区域
        paint.setColor(Color.GRAY);
        canvas.translate(100,20);
        canvas.drawRect(rectFSign,paint);

        // bitmapTrunk
        //横向位移，bitmapTrunk width/2
        float xOffset = ViewUtils.factorMapping(factor, FACTOR_START, FACTOR_END, 0, bitmapTrunk.getWidth() / 2);

        //纵向位移，( rectFSign.bottom - 35 , 5 , 120 )
        float yOffset = ViewUtils.factorMapping(factor, FACTOR_START, FACTOR_END, rectFSign.bottom - 35, 5, 120);

        //旋转，21° - 0°，逆时针
        float rotationOffset = ViewUtils.factorMapping(factor, FACTOR_START, FACTOR_END, 21, 0);

        matrix.reset();
        ViewUtils.applySelfRotationToMatrix(bitmapTrunk, rotationOffset,rectFSign.right / 2 - xOffset, yOffset, matrix);
        canvas.drawBitmap(bitmapTrunk, matrix, paint);

        // bitmapCover
        //旋转，-10° - 0°，逆时针
        rotationOffset = ViewUtils.factorMapping(factor, FACTOR_START, FACTOR_END, -10, 0);

        //纵向位移，( rectFSign.bottom , 80 , 108 )
        yOffset = ViewUtils.factorMapping(factor, FACTOR_START, FACTOR_END, rectFSign.bottom, 80, 108);

        matrix.reset();
        ViewUtils.applySelfRotationToMatrix(bitmapCover, rotationOffset,rectFSign.right / 2 - bitmapCover.getWidth() / 2, yOffset, matrix);
        canvas.drawBitmap(bitmapCover, matrix, paint);

        // sign str
        if (!StringUtils.isEmpty(signStr)) {
            // str 的绘制 path
            paint.setColor(Color.RED);
            path.reset();
            //左下点
            float x0 = rectFSign.right / 2 - bitmapCover.getWidth() / 2;
            float y0 = yOffset + bitmapCover.getHeight();
            //旋转点
            float rX0 = x0 + bitmapCover.getWidth() / 2;
            float rY0 = y0 - bitmapCover.getHeight() / 2;
            //右下点
            float x1 = x0 + bitmapCover.getWidth();
            float y1 = y0;
            float radians = (float) Math.toRadians(rotationOffset);
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

    /*动画参数*/
    private ObjectAnimator objectAnimator;
    protected float factor = 0;
    protected final float FACTOR_END = 100;
    protected final float FACTOR_START = 0;

    @Override
    public void playAnim() {
        if (factor==100) {
            factor = 0;
            objectAnimator = null;
        }
        if (objectAnimator!=null) {
            objectAnimator.resume();
        }
        else {
            objectAnimator = ObjectAnimator.ofFloat(this, "factor", FACTOR_START, FACTOR_END);
            objectAnimator.setDuration(3000);
            objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
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
            objectAnimator.pause();
            objectAnimator.cancel();
            factor = 0;
            this.invalidate();
            objectAnimator = null;
        }
    }

    @Override
    public void setFactor(float factor) {
        this.factor = factor;
        invalidate();
    }

    @Override
    public float getFactor() {
        return factor;
    }

    @Override
    public void release() {

    }
}
