package com.hezongze.privatedemo.memory;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hezongze.privatedemo.MainActivity;
import com.hezongze.privatedemo.R;

import java.lang.ref.SoftReference;

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


        private SoftReference<MainActivity> mainActivitySoftReference;
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            handler.sendEmptyMessageDelayed(HAND_FLAG,1000);

        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(HAND_FLAG);
    }
}
