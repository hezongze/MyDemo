package com.hezongze.privatedemo.memory;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hezongze.privatedemo.R;

public class MemoryActivity extends Activity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_memory);
        //内存泄漏  handler
        handler.sendEmptyMessageDelayed(HAND_FLAG,1000);
    }

    private int HAND_FLAG = 666;

    private Handler handler = new Handler(){

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            handler.sendEmptyMessageDelayed(HAND_FLAG,1000);
        }
    };
}
