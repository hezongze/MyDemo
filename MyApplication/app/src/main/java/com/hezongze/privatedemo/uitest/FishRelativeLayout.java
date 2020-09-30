package com.hezongze.privatedemo.uitest;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.RequiresApi;

public class FishRelativeLayout extends RelativeLayout {

    private Paint mPaint;
    private ImageView ivFish;
    private FishDrawable fishDrawable;


    private float touchX = 0;
    private float touchY = 0;
    private float ripple = 0;

    private int alpha = 110;
    private PointF fishRelativeMiddle;

    public FishRelativeLayout(Context context) {
        this(context, null);
    }

    public FishRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FishRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }

    private void init(Context context) {
        setWillNotDraw(false);
        mPaint = new Paint();//画笔
        mPaint.setStyle(Paint.Style.STROKE);//画笔类型 填充
        mPaint.setAntiAlias(true);//抗锯齿
        mPaint.setDither(true);//防抖动
        mPaint.setStrokeWidth(8);//设置宽度
//        mPaint.setARGB(alpha, 244, 92, 71);//设置颜色

        ivFish = new ImageView(context);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ivFish.setLayoutParams(layoutParams);

        fishDrawable = new FishDrawable();

        ivFish.setImageDrawable(fishDrawable);
        addView(ivFish);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setAlpha(alpha);
        canvas.drawCircle(touchX, touchY, ripple * 150, mPaint);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        touchX = event.getX();
        touchY = event.getY();

        makeTrail();
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "ripple", 0, 1f).setDuration(1000);
        objectAnimator.start();
        return super.onTouchEvent(event);
    }

    public void setRipple(float ripple) {
        alpha = (int) (150 * (1 - ripple));
        this.ripple = ripple;
        invalidate();
    }

    /**
     *
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void makeTrail() {


        fishDrawable.statSwiming(true);

        //鱼相对于自己imageView的坐标
        PointF fishRelativeMiddle = fishDrawable.getMiddlePoint();
        //鱼重心相对于整个屏幕的坐标 起始点
        PointF fishMiddle = new PointF(ivFish.getX() + fishRelativeMiddle.x, ivFish.getY() + fishRelativeMiddle.y);

        //鱼头中心相对于整个屏幕的坐标   第一个控制点
        PointF fishHead = new PointF(ivFish.getX() + fishDrawable.getHeadPoint().x, ivFish.getY() + fishDrawable.getHeadPoint().y);
        //点击的坐标   结束点
        PointF touch = new PointF(touchX, touchY);

        //AOB的角度
        float angle = includedAngle(fishMiddle,fishHead,touch);
        //鱼与X轴的夹角
        float delta = includedAngle(fishMiddle,new PointF(fishMiddle.x + 1,fishMiddle.y),fishHead);
        //控制鱼游动的贝塞尔曲线的点  第二个控制点
        PointF controlPoint = FishDrawable.calculaPoint(fishMiddle, FishDrawable.HEAD_RADIUS * 1.6f, angle / 2 + delta);

        Path path = new Path();
        path.moveTo(fishMiddle.x - fishRelativeMiddle.x,fishMiddle.y - fishRelativeMiddle.y);
        path.cubicTo(fishHead.x - fishRelativeMiddle.x,fishHead.y - fishRelativeMiddle.y,
                controlPoint.x - fishRelativeMiddle.x,controlPoint.y - fishRelativeMiddle.y,
                touchX - fishRelativeMiddle.x,touchY - fishRelativeMiddle.y);//三阶贝塞尔曲线
        ObjectAnimator animator = ObjectAnimator.ofFloat(ivFish,"x","y",path).setDuration(1500);

        final PathMeasure pathMeasure = new PathMeasure(path,false);
        final float[] tan = new float[2];
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animation.getAnimatedFraction();
                //求鱼头的切角，设置鱼头的角度
                pathMeasure.getPosTan(pathMeasure.getLength() * fraction ,null,tan);
                float angle = (float) Math.toDegrees(Math.atan2(-tan[1],tan[0]));
                fishDrawable.setFishMainAngle(angle);
                Log.i("xxx","animation--->"+ fraction);
                if(fraction == 1.0){
                    fishDrawable.statSwiming(false);
                }
            }
        });
        animator.start();
    }


    //向量夹角计算公式
    public static float includedAngle(PointF O, PointF A, PointF B) {
        //OA*OB = (AX-OX)*(BX-OX)+(Ay-Oy)*(By-Oy)
        float AOB = (A.x - O.x) * (B.x - O.x) + (A.y - O.y) * (B.y - O.y);
        //OA的长度
        float OALength = (float) Math.sqrt((A.x - O.x) * (A.x - O.x) + (A.y - O.y) * (A.y - O.y));
        //OB的长度
        float OBLength = (float) Math.sqrt((B.x - O.x) * (B.x - O.x) + (B.y - O.y) * (B.y - O.y));
        //cosAOB = (OA*OB)/(|OA|*|OB)
        float cosAOB = AOB / (OALength * OBLength);

        //toDegrees：将弧度转换为角度。 Math.acos:反余弦  angleAOB:计算得出AOB的角度大小
        float angleAOB = (float) Math.toDegrees(cosAOB);
        //判断方向  正左侧  负右侧 0线上，但是Android的坐标系Y轴是向下的，所以朝右颠倒一下
        //AB与X轴的夹角tan值 - OB与X轴的夹角的tan值  --> 角度是直角三角形里面的，肯定是0-90度，tan角度越大，值越大
        float direction = (A.y - B.y) / (A.x - B.x) - (O.y - B.y) / (O.x - B.x);
        if (direction == 0) {
            if (AOB >= 0) {
                return 0;
            } else {
                return 180;
            }
        } else {
            if (direction > 0) {//右侧顺时针为负
                return -angleAOB;
            } else {
                return angleAOB;
            }
        }


    }
}
