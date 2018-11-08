package com.chestnut.ui.widget.carView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.chestnut.ui.R;
import com.chestnut.ui.contract.BaseViewContract;


/**
 * <pre>
 *     author: Chestnut
 *     blog  : http://www.jianshu.com/u/a0206b5f4526
 *     time  : 2018/7/30 10:05
 *     desc  :
 *     thanks To:
 *     dependent on:
 *     update log:
 * </pre>
 */
public class CarMarketView extends CarBaseView implements BaseViewContract{

    protected Bitmap bitmapCover, bitmapConnection;
    //将图像分成多少格
    private int carMarketConnectWidthPart = 2;
    private int carMarketConnectHeightPart = 2;
    private int carMarketCoverWidthPart = 2;
    private int carMarketCoverHeightPart = 2;
    //交点坐标的个数
    private int carMarketConnectCount = (carMarketConnectWidthPart + 1) * (carMarketConnectHeightPart + 1);
    private int carMarketCoverCount = (carMarketCoverWidthPart + 1) * (carMarketCoverHeightPart + 1);
    //用于保存COUNT的坐标
    //x0, y0, x1, y1......
    private float[] carMarketConnectNewsPos = new float[carMarketConnectCount * 2];
    private float[] carMarketConnectOrigPos = new float[carMarketConnectCount * 2];
    private float[] carMarketCoverNewsPos = new float[carMarketCoverCount * 2];
    private float[] carMarketCoverOrigPos = new float[carMarketCoverCount * 2];

    public CarMarketView(Context context) {
        super(context);
    }

    public CarMarketView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        bitmapCover = BitmapFactory.decodeResource(getResources(), R.drawable.car_market_cover);
        bitmapConnection = BitmapFactory.decodeResource(getResources(), R.drawable.car_market_connection);
        super.getBitmapMeshPoints(bitmapConnection, carMarketConnectHeightPart, carMarketConnectWidthPart, carMarketConnectNewsPos, carMarketConnectOrigPos);
        super.getBitmapMeshPoints(bitmapCover, carMarketCoverHeightPart, carMarketCoverWidthPart, carMarketCoverNewsPos, carMarketCoverOrigPos);
    }

    @Override
    protected Bitmap getCarWheel() {
        return BitmapFactory.decodeResource(getResources(), R.drawable.car_wheel_purple);
    }

    @Override
    protected Bitmap getCarBody() {
        return BitmapFactory.decodeResource(getResources(), R.drawable.car_market_main);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        //车篷连接处
        canvas.save();
        canvas.translate(getWidth()/2 - bitmapCarBodyWidth/2 + 57,getHeight()/2 - bitmapCarBodyHeight/2 - bitmapConnection.getHeight() + 27);
        super.changeCarBodyPoint(bitmapConnection,carMarketConnectCount,carMarketConnectWidthPart,carMarketConnectHeightPart,carMarketConnectNewsPos,carMarketConnectOrigPos, 0.3f, 0.08f);
        canvas.drawBitmapMesh(bitmapConnection, carMarketConnectWidthPart, carMarketConnectHeightPart, carMarketConnectNewsPos, 0, null, 0, paint);
        canvas.restore();

        //车厢主题
        super.onDraw(canvas);

        //车篷
        canvas.save();
        canvas.translate(getWidth()/2 - bitmapCarBodyWidth/2 + 30,getHeight()/2 - bitmapCarBodyHeight/2 - bitmapConnection.getHeight() - 21);
        super.changeCarBodyPoint(bitmapCover,carMarketCoverCount,carMarketCoverWidthPart,carMarketCoverHeightPart,carMarketCoverNewsPos,carMarketCoverOrigPos, 0.5f, 0.2f);
        canvas.drawBitmapMesh(bitmapCover, carMarketCoverWidthPart, carMarketCoverHeightPart, carMarketCoverNewsPos, 0, null, 0, paint);
        canvas.restore();
    }

    @Override
    public void playAnim() {
        super.startAnim();
    }

    @Override
    public void pauseAnim() {
        super.stopAnim();
    }

    @Override
    public void setFactor(float factor) {

    }

    @Override
    public float getFactor() {
        return 0;
    }

    @Override
    public void release() {

    }
}
