package com.hezongze.privatedemo.supportingScreens;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.lang.reflect.Field;

public class UIUtils {


    private Context context;
    //可以将一些值放到配置文件中
    //需要反射获得状态烂的高度
    private static final String DIME_CLASS = "com.android.internal.R$dimen";

    //标准值
    private static final float STANDRD_WIDTH = 1080F;
    private static final float STANDRD_HEIGHT = 1920F;

    //实际宽高(机器的)
    private static  float displayMetricsWidth;
    private static  float displayMetricsHeight;
    
    private static UIUtils instance;

    public UIUtils(Context context) {

        this.context = context;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        if (displayMetricsWidth == 0.0F || displayMetricsHeight == 0.0F){
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
            //需要得到状态栏的高度。来让高度减掉
           int systemBarHeight =  getSystemBarHeight(context);
           //横屏和竖屏两种选择
            if(displayMetrics.widthPixels > displayMetrics.heightPixels){
                //横屏
                displayMetricsWidth =   (float)displayMetrics.heightPixels;
                displayMetricsHeight = (float)displayMetrics.widthPixels - systemBarHeight;
            }else{
                //竖屏
                displayMetricsWidth =   (float)displayMetrics.widthPixels;
                displayMetricsHeight = (float)displayMetrics.heightPixels - systemBarHeight;
            }
        }


    }

    public static UIUtils getInstance(Context context) {
        if (instance == null) {
            instance = new UIUtils(context);
        }
        return instance;
    }

    private int getSystemBarHeight(Context context){
        return getValue(context,DIME_CLASS,"system_bar_height",48);
    }

    //反射取值
    private int getValue(Context context, String dimeClass, String system_bar_height, int i) {
        try{
           Class<?> clz =  Class.forName(dimeClass);
           Object object = clz.newInstance();
           Field field = clz.getField(system_bar_height);
           int id = Integer.parseInt(field.get(object).toString());
           return  context.getResources().getDimensionPixelOffset(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return  i;
    }


    //得到缩放比
    public  float getHorValue(){
        //水平方向缩放比
        return  (float) displayMetricsWidth/STANDRD_WIDTH;
    }
    public  float getVerValue(){
        //竖直方向缩放比
        return  (float) displayMetricsHeight/(STANDRD_HEIGHT - getSystemBarHeight(context)); 
    }
}
