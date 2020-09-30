package com.hezongze.privatedemo.uitest;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.view.animation.LinearInterpolator;

public class FishDrawable extends Drawable {


    private Path mPath;
    private Paint mPaint;

    //除鱼身体以外的所有区域透明度
    private final static int OTHER_ALPHA = 110;
    //鱼身透明度
    private final static int BODY_ALPHA = 110;
    //转弯更自然的重心
    private PointF middlePoint;

    public void setFishMainAngle(float fishMainAngle) {
        this.fishMainAngle = fishMainAngle;
    }

    //鱼的主角度
    private float fishMainAngle = 90;


    //鱼头的半径
    public final static float HEAD_RADIUS = 50;
    //鱼身长度
    private final static float BODY_LENGTH = 3.2f * HEAD_RADIUS;
    //---------------鱼鳍-------------------
    //寻找鱼鳍开始点的线长
    private final static float FIND_FINS_LENGTH = 0.9f * HEAD_RADIUS;
    //鱼鳍的长度
    private final static float FINS_LENGTH = 1.3f * HEAD_RADIUS;
    //---------------鱼尾-------------------
    //尾部大圆的半径
    private final static float BIG_CIRCLE_RADIUS = 0.7f * HEAD_RADIUS;
    //尾部中圆的半径
    private final static float MIDDLE_CIRCLE_RADIUS = 0.6f * BIG_CIRCLE_RADIUS;
    //尾部小圆的半径
    private final static float SMALL_CIRCLE_RADIUS = 0.6f * MIDDLE_CIRCLE_RADIUS;
    //--寻找尾部中圆圆心的线长
    private final static float FIND_MIDDLE_CIRCLE_LENGTH = BIG_CIRCLE_RADIUS + MIDDLE_CIRCLE_RADIUS;
    //--寻找尾部小圆圆心的线长
    private final static float FIND_SMALL_CIRCLE_LENGTH = MIDDLE_CIRCLE_RADIUS * (0.4f + 2.7f);
    //--寻找大三角底边中心点的线长
    private final static float FIND_TRIANGLE_LENGTH = MIDDLE_CIRCLE_RADIUS * 2.7f;

    private float currentBodyValue;

    private PointF  headPoint;

    public FishDrawable() {
        init();
    }


    public PointF getMiddlePoint() {
        return middlePoint;
    }

    public PointF getHeadPoint() {
        return headPoint;
    }
    //初始化属性动画
    ValueAnimator finsAnimator = ValueAnimator.ofFloat(1.8f, 0.3f);//旋转角度
    private void init() {
        mPath = new Path();//路径
        mPaint = new Paint();//画笔
        mPaint.setStyle(Paint.Style.FILL);//画笔类型 填充
        mPaint.setARGB(OTHER_ALPHA, 244, 92, 71);//设置颜色
        mPaint.setAntiAlias(true);//抗锯齿
        mPaint.setDither(true);//防抖动

        middlePoint = new PointF(4.19f * HEAD_RADIUS, 4.19f * HEAD_RADIUS);


        //初始化属性动画
        ValueAnimator valueAnimatorHeadAndBody = ValueAnimator.ofFloat(0, 360);//旋转角度
        valueAnimatorHeadAndBody.setDuration(1000);
        valueAnimatorHeadAndBody.setRepeatMode(ValueAnimator.RESTART);//代表从-1到1，然后从1到-1
        valueAnimatorHeadAndBody.setRepeatCount(ValueAnimator.INFINITE);//无限循环
        //设置插值器
        valueAnimatorHeadAndBody.setInterpolator(new LinearInterpolator());
        valueAnimatorHeadAndBody.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentBodyValue = (float) animation.getAnimatedValue();
                invalidateSelf();
            }
        });
        valueAnimatorHeadAndBody.start();


        finsAnimator.setDuration(500);
        finsAnimator.setRepeatMode(ValueAnimator.REVERSE);
        finsAnimator.setRepeatCount(ValueAnimator.INFINITE);//无限循环
        //设置插值器
        finsAnimator.setInterpolator(new LinearInterpolator());
        finsAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                if(isSwiming){
                    controlFins = (float) animation.getAnimatedValue();
                }else{
                    controlFins = 1.8f;
                }
//                invalidateSelf();
            }
        });

    }

    public void statSwiming(boolean isSwiming) {
        this.isSwiming = isSwiming;
        if(isSwiming){
            finsAnimator.start();
            triang = 5;
        }else{
            triang = 3;
        }
    }


    /**
     * 求各个点的坐标
     *
     * @param startPoint 起始点坐标
     * @param length     两点的距离
     * @param angle      两点连线与x轴的夹角
     * @return
     */
    public static PointF calculaPoint(PointF startPoint, float length, float angle) {
        float deltaX = (float) (Math.cos(Math.toRadians(angle)) * length);
        float deltaY = (float) (Math.sin(Math.toRadians(angle - 180)) * length);//获取Y轴坐标，因为坐标系原因，需要将角度取反
        return new PointF(startPoint.x + deltaX, startPoint.y + deltaY);
    }

    @Override
    public void draw(Canvas canvas) {
        float fishAngle = (float) (fishMainAngle + Math.sin(Math.toRadians(currentBodyValue)) * 10);
        //绘制鱼头
        headPoint = calculaPoint(middlePoint, BODY_LENGTH / 2, fishAngle);
        canvas.drawCircle(headPoint.x, headPoint.y, HEAD_RADIUS, mPaint);

        //绘制左鱼眼
        PointF leftFishEyePoint = calculaPoint(headPoint, HEAD_RADIUS / 2, fishAngle + 45);
        canvas.drawCircle(leftFishEyePoint.x, leftFishEyePoint.y, HEAD_RADIUS / 10, mPaint);
        //绘制右鱼眼
        PointF rightFishEyePoint = calculaPoint(headPoint, HEAD_RADIUS / 2, fishAngle - 45);
        canvas.drawCircle(rightFishEyePoint.x, rightFishEyePoint.y, HEAD_RADIUS / 10, mPaint);

        //绘制右鱼鳍
        PointF rightFinsPoint = calculaPoint(headPoint, FIND_FINS_LENGTH, fishAngle - 100);
        makeFins(canvas, rightFinsPoint, fishAngle, true);
        //绘制左鱼鳍
        PointF leftFinsPoint = calculaPoint(headPoint, FIND_FINS_LENGTH, fishAngle + 100);
        makeFins(canvas, leftFinsPoint, fishAngle, false);

        //绘制中间的鱼鳍
//        makeCenterFins(canvas,middlePoint,BODY_LENGTH/3,fishAngle);

        //画节肢1
        //找到身体底部的中心点
        PointF bodyBottomCenterPoint = calculaPoint(headPoint, BODY_LENGTH, fishAngle - 180);
        //画底部大圆加梯形
        PointF middleCenterPoint = makeSegment(canvas, bodyBottomCenterPoint, BIG_CIRCLE_RADIUS, MIDDLE_CIRCLE_RADIUS,
                FIND_MIDDLE_CIRCLE_LENGTH, fishAngle, true);
        //画底部小圆加梯形
        makeSegment(canvas, middleCenterPoint, MIDDLE_CIRCLE_RADIUS, SMALL_CIRCLE_RADIUS,
                FIND_SMALL_CIRCLE_LENGTH, fishAngle, false);
        //绘制底部大三角形
        makeTriangle(canvas, middleCenterPoint, FIND_TRIANGLE_LENGTH, BIG_CIRCLE_RADIUS, fishAngle);
        //绘制底部稍微小一点三角形
        makeTriangle(canvas, middleCenterPoint, FIND_TRIANGLE_LENGTH - 10, BIG_CIRCLE_RADIUS - 20, fishAngle);

        //绘制身体
        makeBody(canvas, headPoint, bodyBottomCenterPoint, fishAngle);
    }

    /**
     * 绘制中间的鱼鳍  根据中心点
     * @param canvas
     * @param middlePoint
     * @param v
     * @param fishAngle
     */
    private void makeCenterFins(Canvas canvas, PointF middlePoint, float v, float fishAngle) {

        PointF upperPoint = calculaPoint(middlePoint,BODY_LENGTH/3,fishAngle );
        PointF bottomPoint = calculaPoint(middlePoint,BODY_LENGTH/3,fishAngle - 180);
        mPath.reset();
        mPath.moveTo(upperPoint.x,upperPoint.y);
        mPath.lineTo(bottomPoint.x,bottomPoint.y);
        canvas.drawPath(mPath,mPaint);
    }

    private void makeBody(Canvas canvas, PointF headPoint, PointF bodyBottomCenterPoint, float fishAngle) {

        //身体头部左边点
        PointF headLeftPoint = calculaPoint(headPoint, HEAD_RADIUS, fishAngle + 90);
        //身体头部右边点
        PointF headRightPoint = calculaPoint(headPoint, HEAD_RADIUS, fishAngle - 90);

        //身体底部左边点
        PointF bottomleftRightPoint = calculaPoint(bodyBottomCenterPoint, BIG_CIRCLE_RADIUS, fishAngle + 90);
        //身体底部左边点
        PointF bottomRightPoint = calculaPoint(bodyBottomCenterPoint, BIG_CIRCLE_RADIUS, fishAngle - 90);
        //二阶贝塞尔曲线决定鱼的胖瘦
        PointF controlLeft = calculaPoint(headPoint, BODY_LENGTH * 0.56f, fishAngle + 130);
        PointF controlRight = calculaPoint(headPoint, BODY_LENGTH * 0.56f, fishAngle - 130);

        mPath.reset();
        mPath.moveTo(headLeftPoint.x, headLeftPoint.y);
        mPath.quadTo(controlLeft.x, controlLeft.y, bottomleftRightPoint.x, bottomleftRightPoint.y);
        mPath.lineTo(bottomRightPoint.x, bottomRightPoint.y);
        mPath.quadTo(controlRight.x, controlRight.y, headRightPoint.x, headRightPoint.y);
        mPaint.setAlpha(BODY_ALPHA);
        canvas.drawPath(mPath, mPaint);


    }

    private int triang = 3;

    /**
     * 绘制底部三角形
     *
     * @param canvas
     * @param startPoint
     * @param findCenterLength 三角形頂點到底邊的高
     * @param findEndLength    三角形底邊邊長
     * @param fishAngle
     */
    private void makeTriangle(Canvas canvas, PointF startPoint, float findCenterLength, float findEndLength, float fishAngle) {

        float triangle = (float) (fishMainAngle + Math.sin(Math.toRadians(currentBodyValue * triang)) * 30);
        //三角形底边中心
        PointF centerPoint = calculaPoint(startPoint, findCenterLength, triangle - 180);
        //三角形底边的两点
        PointF leftPoint = calculaPoint(centerPoint, findEndLength, triangle + 90);
        PointF rightPoint = calculaPoint(centerPoint, findEndLength, triangle - 90);

        mPath.reset();
        mPath.moveTo(startPoint.x, startPoint.y);
        mPath.lineTo(leftPoint.x, leftPoint.y);
        mPath.lineTo(rightPoint.x, rightPoint.y);
        mPath.lineTo(startPoint.x, startPoint.y);
        canvas.drawPath(mPath, mPaint);

    }


    /**
     * 绘制鱼尾
     *
     * @param canvas
     * @param bottomCenterPoint
     * @param bigRadius
     * @param smallRadius
     * @param findSmallCiclelength
     * @param fishAngle
     * @param hasBigCircle
     */
    private PointF makeSegment(Canvas canvas, PointF bottomCenterPoint,
                               float bigRadius, float smallRadius, float findSmallCiclelength,
                               float fishAngle, boolean hasBigCircle) {

        float segmentAngele;
        if (hasBigCircle) {
            segmentAngele = (float) (fishMainAngle + Math.cos(Math.toRadians(currentBodyValue * (triang -1))) * 20);
        } else {
            segmentAngele = (float) (fishMainAngle + Math.sin(Math.toRadians(currentBodyValue * triang)) * 30);
        }
        //梯形上底的终点
        PointF upperCenterPoint = calculaPoint(bottomCenterPoint, findSmallCiclelength, segmentAngele - 180);
        //梯形上的四个点
        PointF bottomLeftPoint = calculaPoint(bottomCenterPoint, bigRadius, segmentAngele + 90);
        PointF bottomRightPoint = calculaPoint(bottomCenterPoint, bigRadius, segmentAngele - 90);
        PointF upperLeftPoint = calculaPoint(upperCenterPoint, smallRadius, segmentAngele + 90);
        PointF upperRightPoint = calculaPoint(upperCenterPoint, smallRadius, segmentAngele - 90);
        if (hasBigCircle) {
            //画大圆
            canvas.drawCircle(bottomCenterPoint.x, bottomCenterPoint.y, bigRadius, mPaint);
        }
        //画小圆
        canvas.drawCircle(upperCenterPoint.x, upperCenterPoint.y, smallRadius, mPaint);
        //画梯形
        mPath.reset();
        mPath.moveTo(bottomLeftPoint.x, bottomLeftPoint.y);
        mPath.lineTo(upperLeftPoint.x, upperLeftPoint.y);
        mPath.lineTo(upperRightPoint.x, upperRightPoint.y);
        mPath.lineTo(bottomRightPoint.x, bottomRightPoint.y);
        canvas.drawPath(mPath, mPaint);

        return upperCenterPoint;

    }



    /**
     * 绘制鱼鳍
     *
     * @param canvas
     * @param startPoint
     * @param fishAngle
     */
    private void makeFins(Canvas canvas, PointF startPoint, float fishAngle, boolean isRightFins) {
        float controlAngle = 115;
        //鱼鳍终点的坐标
        PointF endPoint = calculaPoint(startPoint, FINS_LENGTH, fishAngle - 180);
        //控制点的坐标
        PointF controlPoint = calculaPoint(startPoint, controlFins * FINS_LENGTH,
                isRightFins ? fishAngle - controlAngle : fishAngle + controlAngle);


        mPath.reset();
        mPath.moveTo(startPoint.x, startPoint.y);
        mPath.quadTo(controlPoint.x, controlPoint.y, endPoint.x, endPoint.y);//二阶贝塞尔曲线,前面参数为控制点坐标，后面为结束点坐标
        canvas.drawPath(mPath, mPaint);


    }
    float controlFins = 1.8f;
    boolean isSwiming = false;

    /**
     * 设置透明度的方法
     *
     * @param alpha
     */
    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    /**
     * 设置了一个颜色过滤器，被绘制出来之前，被绘制的内容的每一个像素都会被颜色过滤器改变
     *
     * @param colorFilter
     */
    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    /**
     * 这个值可以根据setAlpha中设置的值进行调整，比如：Alpha == 0 时设置PixelFormat.TRANSPARENT
     * Alpha == 255 时设置PixelFormat.OPAQUE,在其他时候设置为TRANSLUCENT
     * PixelFormat.OPAQUE 即为完全不透明，遮盖在他下面的所有内容
     * PixelFormat.TRANSPARENT 透明 完全不显示任何东西
     * PixelFormat.TRANSLUCENT  只有绘制的地方才覆盖底下的内容
     *
     * @return
     */
    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public int getIntrinsicHeight() {
        return (int) (8.38f * HEAD_RADIUS);
    }

    @Override
    public int getIntrinsicWidth() {
        return (int) (8.38f * HEAD_RADIUS);
    }
}
