package com.hezongze.privatedemo.uitest;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class FlowLayout extends ViewGroup {



    private int lineWhithUsed;//每行使用的宽度
    private int lineHight;//行高

    //为什么是三个构造函数
    //这个函数是代码中直接new的
    public FlowLayout(Context context) {
        super(context);
    }

    //这个实现是在xml文件中使用的构造  反射
    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //如果自定义了style，就必须就得实现这个
    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    //度量布局
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //先度量所有的子view
        int childCount = getChildCount();
        for (int i = 0;i < childCount;i++){
            View childView = getChildAt(i);
            LayoutParams layoutParams = childView.getLayoutParams();
            int childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec,getPaddingLeft()+getPaddingRight(),layoutParams.width);
            int childHeighMeasureSpec = getChildMeasureSpec(heightMeasureSpec,getPaddingLeft()+getPaddingRight(),layoutParams.height);
            childView.measure(childWidthMeasureSpec,childHeighMeasureSpec);

            //获取子view的宽高
            int childMeasuredWidth = childView.getMeasuredWidth();
            int childMeasuredHight = childView.getMeasuredHeight();

            lineWhithUsed = lineWhithUsed + childMeasuredWidth;
        }
        //再度量保存自己的size

    }
}
