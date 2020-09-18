package com.hezongze.privatedemo.rxJavaAndRetrofit;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hezongze.privatedemo.R;

public class RetrofitView extends AppCompatActivity implements View.OnClickListener {


    private Button getTestButton;
    private TextView responseTextView;

    RetrofitClient retrofitClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);
        initView();
        initData();
    }

    private void initData() {

        retrofitClient = new RetrofitClient();

    }

    private void initView() {
        getTestButton = findViewById(R.id.getTestButton);
        responseTextView = findViewById(R.id.responseTextView);
        getTestButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
              case R.id.getTestButton:
                  retrofitClient.getRectService().getManagerData(2);
                  break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
