package com.hezongze.privatedemo.uitest;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hezongze.privatedemo.MainActivity;
import com.hezongze.privatedemo.R;

import java.lang.ref.SoftReference;

public class UIActivity extends Activity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_drawable);
        ImageView imageView = findViewById(R.id.mImageview);
        imageView.setImageDrawable(new FishDrawable());
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
