package com.hezongze.privatedemo.supportingScreens;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

public class MyRelativeLayout extends RelativeLayout {


    public MyRelativeLayout(Context context) {
        super(context);
    }

    public MyRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        float scaleX = UIUtils.getInstance(getContext()).getHorValue();
        float scaleY = UIUtils.getInstance(getContext()).getVerValue();

        //获取所有的子View
         int count = this.getChildCount();
         for (int i = 0;i< count;i++){
             View child = this.getChildAt(i);
             LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
             layoutParams.width = (int) (layoutParams.width*scaleX);
             layoutParams.height = (int) (layoutParams.height*scaleY);
             layoutParams.leftMargin = (int) (layoutParams.leftMargin*scaleX);
             layoutParams.rightMargin = (int) (layoutParams.rightMargin*scaleX);
             layoutParams.topMargin = (int) (layoutParams.topMargin*scaleY);
             layoutParams.bottomMargin = (int) (layoutParams.bottomMargin*scaleY);
         }
    }
}
