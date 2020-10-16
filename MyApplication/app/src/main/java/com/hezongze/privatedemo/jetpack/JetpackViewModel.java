package com.hezongze.privatedemo.jetpack;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

/**
 * activity中的视图model抽取出来
 */
public class JetpackViewModel extends AndroidViewModel {

    //LiveData 对数据的感应  数据发生改变，观察者设计模式  值改变==UI改变
    private MutableLiveData<String> phoneInfo;

    private Context context;

    //Google Toast Context
    //如果不需要context等内容变量，则直接继承ViewModel
    //如果需要则继承AndroidViewModel
    public JetpackViewModel(@NonNull Application application) {
        super(application);
        this.context = application.getApplicationContext();
    }

    //返回数据  暴露出去数据
    public  MutableLiveData<String> getPhoneInfo(){
        if(phoneInfo == null){//初始化放在这里防止横竖屏切换丢失数据
            phoneInfo = new MutableLiveData<>();
            phoneInfo.setValue("");//初始化数据
        }
        return  phoneInfo;
    }


    /**
     * 输入值
     * @param number
     */
    public void appendNumber(String number){
        phoneInfo.setValue(phoneInfo.getValue()+number);
    }
    /**
     * 删除值
     */
    public void backspaceNumber(){
        int length = phoneInfo.getValue().length();
        if(length > 0){
            phoneInfo.setValue(phoneInfo.getValue().substring(0,length -1));
        }
    }
    /**
     * 清空值
     */
    public void clearNumber(){
            phoneInfo.setValue("");
    }

    /**
     * 这个是打电话按钮
     */
    public void call(){
        Toast.makeText(context,"点击了打电话"+phoneInfo.getValue(),Toast.LENGTH_LONG).show();
    }
}
